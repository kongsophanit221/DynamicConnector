package soteca.com.genisysandroid.framwork.networking

/**
 * Created by SovannMeasna on 3/26/18.
 */

interface Errors {
    val error: Exception
}

enum class DynamicsError : Errors {
    nilDataResponse() {
        override val error: Exception
            get() = Exception("nilDataResponse")
    },
    invalidDataResponse {
        override val error: Exception
            get() = Exception("invalidDataResponse")
    }
}

enum class AuthenticationError : Errors {
    missingCredential {
        override val error: Exception
            get() = Exception("missingCredential")
    },
    invalidCredential {
        override val error: Exception
            get() = Exception("invalidCredential")
    },
    invalidSecurityToken {
        override val error: Exception
            get() = Exception("invalidSecurityToken")
    },
    failedDeviceTokenAcquisition {
        override val error: Exception
            get() = Exception("failedDeviceTokenAcquisition")
    },
}

