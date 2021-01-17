package life.league.challenge.kotlin.homescreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.data.network.endpoint.GetUsersEndpoint
import life.league.challenge.kotlin.util.Either
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    @Inject
    lateinit var loginUsecase: LoginUsecase

    @Inject
    lateinit var userEndpoint: GetUsersEndpoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onResume() {
        super.onResume()
        loginUsecase.getLoginResult().observe(this, Observer { a ->
            when (a) {
                is Success -> {
                    getUsers()
                }
                is Failure -> {
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
            val response = userEndpoint.execute()
            when (response) {
                is Either.Success -> {

                }
                is Either.Failure -> {

                }
            }
        }
    }

}