package life.league.challenge.kotlin.ui.homescreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home_screen.*
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.model.FeedItem
import life.league.challenge.kotlin.util.hide
import life.league.challenge.kotlin.util.show

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel by viewModels<HomeScreenViewModel>()
    private val feedAdapter = FeedListAdapter(object : FeedClickListener {
        override fun onAuthorClicked(feedItem: FeedItem) {
            viewModel.postAuthorClicked(feedItem)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        observeViewModel()
        if (savedInstanceState == null) {
            viewModel.loadHomeScreen()
        }
    }

    private fun setupViews() {
        setContentView(R.layout.activity_home_screen)
        home_screen_recycler_view.apply {
            adapter = feedAdapter
        }
        home_screen_error_view.setButtonClickListener {
            viewModel.loadHomeScreen()
        }
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
        home_screen_error_view.hide()
        home_screen_recycler_view.hide()
        home_screen_loading_view.show()
    }

    private fun showGenericError() {
        home_screen_recycler_view.hide()
        home_screen_loading_view.hide()
        home_screen_error_view.apply {
            setMessage(false)
            show()
        }
    }

    private fun showNetworkError() {
        home_screen_recycler_view.hide()
        home_screen_loading_view.hide()
        home_screen_error_view.apply {
            setMessage(true)
            show()
        }
    }

    private fun showFeed(feedItems: List<FeedItem>) {
        home_screen_error_view.hide()
        home_screen_loading_view.hide()
        home_screen_recycler_view.show()
        feedAdapter.submitList(feedItems)
    }

}