package life.league.challenge.kotlin.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_feed_fragment.*
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.model.FeedItem
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

    private val viewModel by viewModels<HomeScreenViewModel>()

    private val feedAdapter = FeedListAdapter(object : FeedClickListener {
        override fun onAuthorClicked(feedItem: FeedItem) {
            viewModel.postAuthorClicked(feedItem)
        }
    })


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_feed_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feed_frag_recycler_view.apply {
            adapter = feedAdapter
        }
        feed_frag_error_view.setButtonClickListener {
            viewModel.reloadFeed()
        }
        if(savedInstanceState == null) {
            viewModel.onFeedScreenReady()
        }
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getScreenState().observe(viewLifecycleOwner, Observer {
            onScreenStateUpdated(it)
        })
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


}