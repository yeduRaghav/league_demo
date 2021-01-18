package life.league.challenge.kotlin.ui.homescreen.feedscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import life.league.challenge.kotlin.R
import life.league.challenge.kotlin.ui.model.FeedItem
import life.league.challenge.kotlin.ui.model.User
import life.league.challenge.kotlin.util.setThrottledClickListener

/**
 * Renders the feed.
 */
class FeedListAdapter(private val feedClickListener: FeedClickListener) : ListAdapter<FeedItem, FeedItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        return FeedItemViewHolder.get(parent)
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(getItem(position), feedClickListener)
    }

}


private class DiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        //todo: test me
        return (oldItem.post.postId == newItem.post.postId)
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return (oldItem.post.title == newItem.post.title) &&
                (oldItem.post.description == newItem.post.description)
        //todo: test me
        //note: more accurate logic can added in future.
    }

}

interface FeedClickListener {
    fun onUserClicked(user: User)
}

class FeedItemViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LAYOUT_ID = R.layout.layout_feed_item

        fun get(parent: ViewGroup): FeedItemViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(LAYOUT_ID, parent, false)
            return FeedItemViewHolder(itemView)
        }
    }

    private val avatar: ShapeableImageView = itemView.findViewById(R.id.feed_item_avatar)
    private val authorUserName: MaterialTextView = itemView.findViewById(R.id.feed_item_author_name)
    private val title: MaterialTextView = itemView.findViewById(R.id.feed_item_title)
    private val description: MaterialTextView = itemView.findViewById(R.id.feed_item_description)

    fun bind(feedItem: FeedItem, listener: FeedClickListener) {
        feedItem.user.also {
            authorUserName.text = it.userName
            Picasso.get()
                    .load(it.avatar)
                    .into(avatar)
            //todo: placeholder
            avatar.setThrottledClickListener { listener.onUserClicked(it) }
            authorUserName.setThrottledClickListener { listener.onUserClicked(it) }
        }

        feedItem.post.also {
            title.text = it.title
            description.text = it.description
        }

    }

}

