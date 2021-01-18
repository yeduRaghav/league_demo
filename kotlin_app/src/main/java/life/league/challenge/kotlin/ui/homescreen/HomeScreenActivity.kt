package life.league.challenge.kotlin.ui.homescreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import life.league.challenge.kotlin.R

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeScreenViewModel>()
    private lateinit var feedFragment: FeedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        if (savedInstanceState == null) {
            feedFragment = FeedFragment.newInstance()
            loadFeedFragment()
        }
    }

    private fun loadFeedFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.home_screen_frag_container, feedFragment)
                .commit()
    }

    private fun addUserDetailFragment() {
        //todo:
    }

}