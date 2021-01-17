package life.league.challenge.kotlin.ui.homescreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.R
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }


    @Inject
    lateinit var loginUsecase: LoginUsecase

    @Inject
    lateinit var feedUsecase: FeedUsecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onResume() {
        super.onResume()
        loginUsecase.getLoginResult().observe(this, Observer { a ->
            when (a) {
                is LoginState.Success -> {
                    getUsers()
                }
                is LoginState.Failure -> {
                }
            }
        })

        feedUsecase.getLoadState().observe(this, Observer { feed ->
            when (feed) {
                is FeedLoadState.Success -> {
                    feed.feed
                }
                is FeedLoadState.Failure -> {
                    when (feed.reason) {
                        FailureReason.InvalidCredentials -> TODO()
                        FailureReason.BadResponseData -> TODO()
                        FailureReason.NetworkIssue -> TODO()
                        FailureReason.GenericIssue -> TODO()
                    }
                }
            }
        })

        GlobalScope.launch {
            loginUsecase.setScope(this)
            loginUsecase.login("guru", "appan")
        }
    }

    private fun getUsers() {
        GlobalScope.launch {
            feedUsecase.setScope(this)
            feedUsecase.loadFeed()

        }
    }

}