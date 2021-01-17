package life.league.challenge.kotlin.data.network

import android.util.Log
import life.league.challenge.kotlin.data.network.interceptor.AuthorisationInterceptor
import life.league.challenge.kotlin.data.network.model.LoginApiResponse
import life.league.challenge.kotlin.data.network.model.PostApiModel
import life.league.challenge.kotlin.data.network.model.UserApiModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Defines the League Api endpoints
 */
interface LeagueApi {

    companion object {
        private const val BASE_URL = "https://engineering.league.dev/challenge/api/"

        fun create(authInterceptor: AuthorisationInterceptor): LeagueApi {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(getLoggingInterceptor())
                    .build()

            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .build()
                    .create(LeagueApi::class.java)
        }

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("Guruve", message)
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @GET("login")
    fun login(@Header("Authorization") credentials: String?): Call<LoginApiResponse>

    @GET("posts")
    fun getPosts(): Call<List<PostApiModel>>

    @GET("users")
    fun getUsers(): Call<List<UserApiModel>>
}