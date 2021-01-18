package life.league.challenge.kotlin.ui.homescreen.profilescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_fragment_profile.*
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.ui.homescreen.HomeScreenViewModel

/**
 * Fragment displays a User profile
 */

class ProfileFragment : Fragment() {

    companion object {
        const val TAG = "Profile_Fragment"

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeScreenViewModel::class.java)
        setupProfileCard()
    }

    private fun setupProfileCard() {
        viewModel.getClickedPost().observe(viewLifecycleOwner, Observer {
            with(it) {
                profile_card_name.text = name
                profile_card_email.text = email
                profile_card_phone.text = phone
                profile_card_website.text = website
                Picasso.get()
                        .load(avatar)
                        .into(profile_card_avatar)
            }
        })
    }
}