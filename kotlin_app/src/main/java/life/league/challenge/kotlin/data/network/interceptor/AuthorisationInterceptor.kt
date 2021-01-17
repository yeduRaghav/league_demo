package life.league.challenge.kotlin.data.network.interceptor

import life.league.challenge.kotlin.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val HEADER_ACCESS_TOKEN = "x-access-token"

/**
 * Interceptor adds Authorisation headers to requests
 * */
class AuthorisationInterceptor @Inject constructor(
        private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
                .addHeader(HEADER_ACCESS_TOKEN, sessionManager.getApiKey().orEmpty())
                .build()
        return chain.proceed(newRequest)
    }
}