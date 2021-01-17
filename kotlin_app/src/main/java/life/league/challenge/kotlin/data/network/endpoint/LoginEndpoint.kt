package life.league.challenge.kotlin.data.network.endpoint

import life.league.challenge.kotlin.data.network.LeagueApi
import life.league.challenge.kotlin.data.network.model.LoginApiResponse
import retrofit2.Call
import javax.inject.Inject

/**
 * Login Api Endpoint abstraction
 */
class LoginEndpoint @Inject constructor(private val api: LeagueApi) : BaseEndpoint<LoginApiResponse>() {

    private var credentials: String? = null

    fun setCredentials(userName: String?, password: String?) {
        credentials = StringBuilder().append(userName).append(":").append(password).toString()
    }

    override fun getCall(): Call<LoginApiResponse> {
        return api.login(credentials)
    }
}