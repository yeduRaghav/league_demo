package life.league.challenge.kotlin.homescreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.data.network.endpoint.LoginEndpoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var loginEndpoint: LoginEndpoint

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onResume() {
        super.onResume()


    }

}
