package life.league.challenge.kotlin.data.network.endpoint.base

import androidx.annotation.VisibleForTesting
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Defines localized error responses and their associated data.
 */
sealed class ApiError(open val message: String? = null) {

    data class GenericError(val code: Int? = null, val throwable: Throwable? = null, override val message: String?) : ApiError(message)
    data class Unreachable(override val message: String?) : ApiError(message)

    sealed class ClientError(override val message: String?) : ApiError(message) {
        data class BadRequest(override val message: String?) : ClientError(message)
        data class Unauthorised(override val message: String?) : ClientError(message)
        data class Forbidden(override val message: String?) : ClientError(message)
        data class NotFound(override val message: String?) : ClientError(message)
        data class Timeout(override val message: String?) : ClientError(message)
        data class IAmATeapot(override val message: String?) : ClientError(message)
    }

    sealed class ServerError(override val message: String?) : ApiError(message) {
        data class InternalServerError(override val message: String?) : ServerError(message)
        data class ServiceUnavailable(override val message: String?) : ServerError(message)
    }
}

@VisibleForTesting
object StatusCode {
    const val BAD_REQUEST = 400
    const val UNAUTHORISED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val TIMEOUT = 408
    const val I_AM_A_TEAPOT = 418
    const val INTERNAL_SERVER_ERROR = 500
    const val SERVICE_UNAVAILABLE = 503
}


/**
 * @return ApiError object based on Http response code.
 * @param code the response code from api.
 * @param message message returned from api
 * */
fun getApiError(code: Int, message: String?): ApiError {
    return when (code) {
        StatusCode.BAD_REQUEST -> ApiError.ClientError.BadRequest(message)
        StatusCode.UNAUTHORISED -> ApiError.ClientError.Unauthorised(message)
        StatusCode.FORBIDDEN -> ApiError.ClientError.Forbidden(message)
        StatusCode.NOT_FOUND -> ApiError.ClientError.NotFound(message)
        StatusCode.TIMEOUT -> ApiError.ClientError.Timeout(message)
        StatusCode.I_AM_A_TEAPOT -> ApiError.ClientError.IAmATeapot(message)
        StatusCode.INTERNAL_SERVER_ERROR -> ApiError.ServerError.InternalServerError(message)
        StatusCode.SERVICE_UNAVAILABLE -> ApiError.ServerError.ServiceUnavailable(message)
        else -> ApiError.GenericError(code = code, message = message)
    }
}

/**
 *  @return ApiError based on throwable
 * */
fun Throwable.toApiError(): ApiError {
    return when (this) {
        is ConnectException,
        is UnknownHostException -> ApiError.Unreachable(message)
        else -> ApiError.GenericError(message = message)
    }
}