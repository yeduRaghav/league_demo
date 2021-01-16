package life.league.challenge.kotlin.data.network.endpoint

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import life.league.challenge.kotlin.api.Either
import retrofit2.Call
import retrofit2.Response

/**
 * Parent class for all endpoints.
 * Handles network response and exceptions and return localized response.
 */
abstract class BaseEndpoint<R> {

    /**
     * The current call instance
     * */
    private lateinit var currentCall: Call<R>

    /**
     * Returns a new Retrofit Call
     * */
    protected abstract fun getCall(): Call<R>


    /**
     * Invokes the actual api call and returns a localised response.
     * Ignore the warning.
     * https://discuss.kotlinlang.org/t/warning-inappropriate-blocking-method-call-with-coroutines-how-to-fix/16903
     * */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun execute(): Either<ApiError, R> {
        cancelCurrentCall()
        currentCall = getCall()
        return withContext(Dispatchers.IO) {
            try {
                currentCall.execute().localise()
            } catch (throwable: Throwable) {
                Either.failure(throwable.toApiError())
            }
        }
    }

    /**
     * Cancels any ongoing call.
     * */
    private fun cancelCurrentCall() {
        if (::currentCall.isInitialized) {
            try {
                currentCall.cancel()
            } catch (exception: IllegalStateException) {
                Log.e("BaseEndpoint", "cancelCurrentCall: " + exception.localizedMessage)
            }
        }
    }

    /**
     * @return Either the response body or appropriate ApiError
     * */
    private fun Response<R>.localise(): Either<ApiError, R> {
        return body()?.let {
            Either.success(it)
        } ?: Either.failure(getApiError(code(), message()))
    }

}