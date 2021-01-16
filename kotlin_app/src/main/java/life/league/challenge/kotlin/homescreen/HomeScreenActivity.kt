package life.league.challenge.kotlin.homescreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.util.Either
import life.league.challenge.kotlin.data.network.endpoint.GetPostsEndpoint
import life.league.challenge.kotlin.data.network.endpoint.LoginEndpoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var loginEndpoint: LoginEndpoint

    @Inject
    lateinit var postsEndpoint: GetPostsEndpoint

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            makeApiCalls()
        }
    }

    private suspend fun makeApiCalls() {
        when (val response = loginEndpoint.execute()) {
            is Either.Failure -> {
            }
            is Either.Success -> fetchPosts(response.value.apiKey)
        }
    }

    private suspend fun fetchPosts(apiKey: String?) {
        when (val response = postsEndpoint.apply { accessToken = apiKey }.execute()) {
            is Either.Failure -> {
                response.error
            }
            is Either.Success -> {
                response.value
            }
        }
    }

}