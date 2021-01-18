package life.league.challenge.kotlin.ui.homescreen.feedscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_fragment_feed.*
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.ui.homescreen.FeedState
import life.league.challenge.kotlin.ui.homescreen.HomeScreenViewModel
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.model.FeedItem
import life.league.challenge.kotlin.ui.model.User
import life.league.challenge.kotlin.util.hide
import life.league.challenge.kotlin.util.show

/**
 * Fragments shows a feed of posts
 */
@AndroidEntryPoint
class FeedFragment : Fragment() {

    companion object {
        fun newInstance(): FeedFragment {
            return FeedFragment()
        }
    }

    private var hostCallback: HostCallback? = null
    private lateinit var viewModel: HomeScreenViewModel

    private val feedAdapter = FeedListAdapter(object : FeedClickListener {
        override fun onUserClicked(user: User) {
            viewModel.onUserClicked(user)
            hostCallback?.loadProfileScreen()
        }
    })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HostCallback) {
            this.hostCallback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        viewModel = ViewModelProvider(requireActivity()).get(HomeScreenViewModel::class.java)
        if (savedInstanceState == null) {
            viewModel.onFeedScreenReady()
        }
    }

    private fun setupViews() {
        feed_frag_recycler_view.apply {
            adapter = feedAdapter
        }
        feed_frag_error_view.setButtonClickListener {
            viewModel.reloadFeed()
        }
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getFeedState().observe(viewLifecycleOwner, Observer {
            onScreenStateUpdated(it)
        })
    }

    private fun onScreenStateUpdated(state: FeedState) {
        when (state) {
            is FeedState.Loading -> showLoading()
            is FeedState.Loaded -> showFeed(state.feed)
            is FeedState.Error -> {
                when (state.reason) {
                    FailureReason.NetworkIssue -> showNetworkError()
                    else -> showGenericError()
                }
            }
        }
    }

    private fun showLoading() {
        feed_frag_error_view.hide()
        feed_frag_recycler_view.hide()
        feed_frag_loading_view.show()
    }

    private fun showGenericError() {
        feed_frag_recycler_view.hide()
        feed_frag_loading_view.hide()
        feed_frag_error_view.apply {
            setMessage(false)
            show()
        }
    }

    private fun showNetworkError() {
        feed_frag_recycler_view.hide()
        feed_frag_loading_view.hide()
        feed_frag_error_view.apply {
            setMessage(true)
            show()
        }
    }

    private fun showFeed(feedItems: List<FeedItem>) {
        feed_frag_error_view.hide()
        feed_frag_loading_view.hide()
        feed_frag_recycler_view.show()
        feedAdapter.submitList(feedItems)
    }


    interface HostCallback {
        fun loadProfileScreen()
    }
}