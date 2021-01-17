package life.league.challenge.kotlin.homescreen

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onResume() {
        super.onResume()
        loginUsecase.getLoginResult().observe(this, Observer { a ->
            when (a) {
                is Success -> {
                    TODO("")
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

}