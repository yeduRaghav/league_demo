package life.league.challenge.kotlin.ui.homescreen

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

/**
 * Renders the feed.
 */
class FeedListAdapter : ListAdapter<FeedItem, FeedItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        return FeedItemViewHolder.get(parent)
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(getItem(position))
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
        //note: more accurate logic for checking content integrity can added in future.
    }

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
    private val authorName: MaterialTextView = itemView.findViewById(R.id.feed_item_author_name)
    private val title: MaterialTextView = itemView.findViewById(R.id.feed_item_title)
    private val description: MaterialTextView = itemView.findViewById(R.id.feed_item_description)

    fun bind(feedItem: FeedItem) {
        feedItem.user.also {
            authorName.text = it.name
            Picasso.get()
                    .load(it.avatar)
                    .into(avatar)
            //todo: placeholder
        }

        //todo: click
        feedItem.post.also {
            title.text = it.title
            description.text = it.description
        }

    }

}

