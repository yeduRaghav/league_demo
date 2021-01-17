package life.league.challenge.kotlin.ui.homescreen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.model.FeedItem

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel by viewModels<HomeScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate()")
        setupViews()
        observeViewModel()
        viewModel.loadHomeScreen()
    }

    private fun setupViews() {
        setContentView(R.layout.activity_home_screen)

        //todo:
    }

    private fun observeViewModel() {
        viewModel.getScreenState().observe(this, Observer { onScreenStateUpdated(it) })
    }

    private fun onScreenStateUpdated(state: HomeScreenState) {
        when (state) {
            is HomeScreenState.Loading -> showLoading()
            is HomeScreenState.Loaded -> showFeed(state.feed)
            is HomeScreenState.Error -> {
                when (state.reason) {
                    FailureReason.NetworkIssue -> showNetworkError()
                    else -> showGenericError()
                }
            }
        }
    }

    private fun showLoading() {
        Toast.makeText(this, "LOADING", Toast.LENGTH_SHORT).show()
        //todo:
    }

    private fun showGenericError() {
        Toast.makeText(this, "GENERAL ERROR", Toast.LENGTH_SHORT).show()
        //todo:
    }

    private fun showNetworkError() {
        Toast.makeText(this, "NETWORK ERROR", Toast.LENGTH_SHORT).show()
        //todo:
    }


    private fun showFeed(feedItems: List<FeedItem>) {
        Toast.makeText(this, feedItems.size.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy()")
    }

}