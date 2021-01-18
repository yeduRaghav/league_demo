package life.league.challenge.kotlin.ui.homescreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.ui.homescreen.feedscreen.FeedFragment
import life.league.challenge.kotlin.ui.homescreen.profilescreen.ProfileFragment

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity(), FeedFragment.HostCallback {

    private val viewModel by viewModels<HomeScreenViewModel>()
    private val fragmentContainer = R.id.home_screen_frag_container

    private lateinit var feedFragment: FeedFragment
    private lateinit var profileFragment: ProfileFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        if (savedInstanceState == null) {
            loadFeedFragment()
        }
    }

    override fun loadProfileScreen() {
        loadProfileFragment()
    }

    private fun loadFeedFragment() {
        if (!::feedFragment.isInitialized) {
            feedFragment = FeedFragment.newInstance()
        }
        supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer, feedFragment)
                .commit()

    }

    private fun loadProfileFragment() {
        if (!::profileFragment.isInitialized) {
            profileFragment = ProfileFragment()
        }

        supportFragmentManager
                .beginTransaction()
                .addToBackStack(ProfileFragment.TAG)
                .add(fragmentContainer, profileFragment)
                .commit()
    }


}