package soteca.com.genisysandroid.framwork.connector

import android.content.Context
import android.util.Log
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import soteca.com.genisysandroid.framwork.authenticator.Authenticator
import soteca.com.genisysandroid.framwork.authenticator.DynamicAuthenticator
import soteca.com.genisysandroid.framwork.helper.SharedPreferenceHelper
import soteca.com.genisysandroid.framwork.model.*
import soteca.com.genisysandroid.framwork.model.decoder.*
import soteca.com.genisysandroid.framwork.model.encoder.EnvelopeEncoder
import soteca.com.genisysandroid.framwork.model.encoder.body.*
import soteca.com.genisysandroid.framwork.model.encoder.header.HeaderEncoder
import soteca.com.genisysandroid.framwork.networking.AuthenticationError
import soteca.com.genisysandroid.framwork.networking.Errors
import soteca.com.genisysandroid.framwork.networking.RequestTask


/**
 * Created by SovannMeasna on 4/12/18.
 */
class DynamicsConnector {

    companion object {
        private var shared: DynamicsConnector? = null

        fun default(context: Context): DynamicsConnector {
            if (shared == null) {
                shared = DynamicsConnector(context, DynamicAuthenticator(context))
            }
            return shared!!
        }
    }

    val crmUrl: String
        get() = authenticator!!.crmUrl

    private val TAG = "tDynamic"
    private var authenticator: Authenticator? = null
    private var context: Context
    private var allEntities: ArrayList<EntityMetadata>? = null

    constructor(context: Context, authenticator: Authenticator) {
        this.context = context
        this.authenticator = authenticator
    }

    private fun preExecutionCheck(): Triple<String, String, String> = runBlocking {

        if (authenticator == null) {
            throw AuthenticationError.invalidSecurityToken.error
        }
        async { authenticator!!.authenticate() }.await() //securityContent
    }

    /**
     * Dynamic connector with retrieve function
     */

    fun authenticate(configuration: DynamicsConfiguration, done: (DynamicsUser?, Errors?) -> Unit) {

        authenticator!!.setConfiguration(configuration)

        try {
            authenticator!!.authenticate()

            this.whoAmI { identifier, errors ->

                if (identifier != null) {

                    val userIdentify = identifier

                    this.retrieveUserPrivileges(userIdentify.userId, { rolePrivileges, error ->

                        if (error == null) {
                            val user = DynamicsUser(context)
                            user.userIdentify = userIdentify
                            user.userRolePrivileges = rolePrivileges

                            val userIdentifyDataHashMap = userIdentify.toHashMap()
                            val userIdentifyData = userIdentify.toJSONObject(userIdentifyDataHashMap)
                            var userRoleJSONObjects = ArrayList<String>()

                            rolePrivileges!!.forEach {
                                userRoleJSONObjects.add(it.toJSONObject().toString())
                            }

                            val userInformation = hashMapOf("USER_IDENTIFY" to userIdentifyData, "USER_ROLEPRIVILEGE" to userRoleJSONObjects)
                            SharedPreferenceHelper.getInstance(context).setUserInformation(userInformation)

                            done(user, error)
                        } else {
                            Log.e(TAG, "Error $error")
                        }
                    })
                }
            }

        } catch (ex: Exception) {
            Log.d(TAG, "ex: " + ex.localizedMessage)
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

    }

    fun retrieveTimestamp(done: (Int?, Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }
        val envelopEncoder = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent), BodyEncoder(ExecuteActionEncoder(RequestEncoder.timeStamp())))
        val request = DynamicConnectorRequest(crmUrl, envelopEncoder)
        val timeStampDecoder = TimeStampDecoder(request)

        RequestTask<TimeStampDecoder>(timeStampDecoder, { timeStampResponse, responseError ->

            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.retrieveTimestamp(done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }
            Log.d(TAG, timeStampResponse!!.timeStamp!!.toString())
            done(timeStampResponse.timeStamp, null)
        }).execute()
    }

    fun retrieveUserPrivileges(userId: String, done: (rolePrivileges: ArrayList<RolePrivilege>?, error: Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent), BodyEncoder(ExecuteActionEncoder(RequestEncoder.userPrivileges(userId))))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val rolePrivilegeDecoder = RolePrivilegeDecoder(request)

        RequestTask<RolePrivilegeDecoder>(rolePrivilegeDecoder, { rolePrivilegeResponse, responseError ->
            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.retrieveUserPrivileges(userId, done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }

            Log.d(TAG, "size: " + rolePrivilegeResponse!!.rolePrivileges!!.size)
            done(rolePrivilegeResponse.rolePrivileges, null)
        }).execute()
    }

    fun retrieveAllEntities(done: (ArrayList<EntityMetadata>?, Errors?) -> Unit) {
        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent), BodyEncoder(ExecuteActionEncoder(RequestEncoder.allEntities())))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val entityMetadataListDecoder = EntityMetadataListDecoder(request)

        RequestTask<EntityMetadataListDecoder>(entityMetadataListDecoder, { entityMetadataListResponse, responseError ->
            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.retrieveAllEntities(done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }

            Log.d(TAG, "size: " + entityMetadataListResponse!!.entityMetadatas!!.size)
            done(entityMetadataListResponse.entityMetadatas, null)
        }).execute()
    }

    fun retrieveEntity(metadataId: String, done: (EntityMetadata?, Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            return retrieveEntity(metadataId, done)
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent), BodyEncoder(ExecuteActionEncoder(RequestEncoder.entities(metadataId))))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val entityMetadataDecoder = EntityMetadataDecoder(request)

        RequestTask<EntityMetadataDecoder>(entityMetadataDecoder, { entityMetadataDecoder, responseError ->
            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.retrieveEntity(metadataId, done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }

            Log.d(TAG, "size: " + entityMetadataDecoder!!.entityMetadata)
            done(entityMetadataDecoder.entityMetadata, null)
        }).execute()
    }

    fun whoAmI(done: (identifier: MyIdentifier?, Errors?) -> Unit) {
        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent, false), BodyEncoder(ExecuteActionEncoder(RequestEncoder.whoAmI())))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val myIdentifierDecoder = MyIdentifierDecoder(request)

        RequestTask<MyIdentifierDecoder>(myIdentifierDecoder, { myIdentifierResponse, responseError ->
            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.whoAmI(done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }
            Log.d(TAG, myIdentifierResponse!!.myIdentifier!!.toString())
            done(myIdentifierResponse.myIdentifier, null)
        }).execute()
    }

    fun retrieveMultiple(fetchExpression: FetchExpression, done: (EntityCollection?, Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            authenticator!!.clearSecurityToken()
            this@DynamicsConnector.retrieveMultiple(fetchExpression, done)
            done(null, AuthenticationError.invalidSecurityToken)

            return
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent), BodyEncoder(ExecuteActionEncoder(RequestEncoder.multipleRetreive(fetchExpression))))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val retrieveMultipleDecoder = RetrieveMultipleDecoder(request)

        RequestTask<RetrieveMultipleDecoder>(retrieveMultipleDecoder, { retrieveMultipleResponse, responseError ->

            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.retrieveMultiple(fetchExpression, done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }
            Log.d(TAG, retrieveMultipleResponse!!.results!!.entityName)
            done(retrieveMultipleResponse.results, null)
        }).execute()
    }

    fun create(entity: EntityCollection.Entity, done: (String?, Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        val createdEntity = EntityCollection.Entity(entity.attribute,
                "00000000-0000-0000-0000-000000000000", entity.logicalName)

        val envelop = EnvelopeEncoder(HeaderEncoder.create(crmUrl, securityContent), BodyEncoder(Create(createdEntity)))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val createDecoder = CreateDecoder(request)

        RequestTask<CreateDecoder>(createDecoder, { responseDecoder, responseError ->

            if (responseError != null) {
                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.create(entity, done)
                } else {
                    done(null, responseError.error)
                }
                return@RequestTask
            }
            done(responseDecoder!!.createResult, null)
        }).execute()
    }

    fun update(entity: EntityCollection.Entity, done: (Boolean?, Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.update(crmUrl, securityContent), BodyEncoder(Update(entity = entity)))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val stringDecoder = StringDecoder(request)

        RequestTask<StringDecoder>(stringDecoder, { responseDecoder, responseError ->

            if (responseError != null) {
                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.update(entity, done)
                } else {
                    done(null, responseError.error)
                }
                return@RequestTask
            }

            done(!(responseDecoder!!.text!!.contains("<ErrorCode>-2147220960</ErrorCode>")
                    || responseDecoder.text!!.contains("is missing prvAssignOrder privilege")
                    || responseDecoder.text!!.contains("because it is read-only")
                    || responseDecoder.text!!.contains("<ErrorCode>-2147220946</ErrorCode>")), null)
        }).execute()
    }

    fun delete(entityName: String, entityId: String, done: (Boolean?, Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.delete(crmUrl, securityContent), BodyEncoder(Delete(entityName, entityId)))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val stringDecoder = StringDecoder(request)

        RequestTask<StringDecoder>(stringDecoder, { responseDecoder, responseError ->

            if (responseError != null) {
                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.delete(entityName, entityId, done)
                } else {
                    done(null, responseError.error)
                }
                return@RequestTask
            }

            done(responseDecoder!!.text!!.contains("<DeleteResponse"), null)

        }).execute()
    }

    fun executeMultiple(actionRequests: ArrayList<ActionRequest>, done: (ArrayList<ExecuteMultipleResponseItem>?, Errors?) -> Unit) {

        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent), BodyEncoder(ExecuteActionEncoder(RequestEncoder.multipleExecute(actionRequests))))
        val request = DynamicConnectorRequest(crmUrl, envelop)
        val executeMultipleDecoder = ExecuteMultipleDecoder(request)

        RequestTask<ExecuteMultipleDecoder>(executeMultipleDecoder, { executeMultipleDecoder, responseError ->

            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.executeMultiple(actionRequests, done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }
            Log.d(TAG, "size: " + executeMultipleDecoder!!.responseItems?.size)
            done(executeMultipleDecoder!!.responseItems, null)

        }).execute()
    }

    // Not yet test, currently done setState XML, requestState Fullfill same iOS
    fun setState(entityType: String, entityId: String, stateCode: String, statusCode: String, stateCodeName: String, done: (Boolean?, Errors?) -> Unit) {
        val securityContent: Triple<String, String, String>

        try {
            securityContent = preExecutionCheck()
        } catch (e: Exception) {
            done(null, AuthenticationError.invalidSecurityToken)
            return
        }

        var responseName = "ResponseName>SetState<"

        val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent, false), BodyEncoder(ExecuteActionEncoder(RequestEncoder.setState(entityType, entityId, stateCode, statusCode))))
        var request = DynamicConnectorRequest(crmUrl, envelop)

        if (entityType == "salesorder" && stateCodeName == "Fulfilled" || stateCodeName == "Canceled") {

            var stateRequest: String? = null

            if (stateCodeName == "Fulfilled") {
                stateRequest = "FulfillSalesOrder"
                responseName = "ResponseName>FulfillSalesOrder<"
            }
            if (stateCodeName == "Canceled") {
                stateRequest = "CancelSalesOrder"
                responseName = "ResponseName>CancelSalesOrder<"
            }

            request = stateRequest.let {
                val envelop = EnvelopeEncoder(HeaderEncoder.execute(crmUrl, securityContent, false), BodyEncoder(ExecuteActionEncoder(RequestEncoder.stateRequest(entityType, entityId, statusCode))))
                DynamicConnectorRequest(crmUrl, envelop)
            }
        }

        val stringDecoder = StringDecoder(request)

        RequestTask<StringDecoder>(stringDecoder, { stringResponse, responseError ->
            if (responseError != null) {

                if (responseError.error == AuthenticationError.invalidSecurityToken) {
                    authenticator!!.clearSecurityToken()
                    this@DynamicsConnector.setState(entityType, entityId, stateCode, statusCode, stateCodeName, done)
                } else {
                    done(null, responseError.error)
                }

                return@RequestTask
            }
            Log.d(TAG, stringResponse!!.text)
            done(false, null)
        }).execute()
    }

}

