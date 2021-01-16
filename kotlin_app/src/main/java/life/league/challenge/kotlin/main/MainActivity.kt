package life.league.challenge.kotlin.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.api.Either
import life.league.challenge.kotlin.api.Service


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

//        Service.login("Hello", "World") { result ->
//            when (result) {
//                is Either.Success -> Log.v(TAG, result.response?.apiKey ?: "")
//                is Either.Failure -> Log.e(TAG, result.errorResponse)
//            }
//        }
    }

}
