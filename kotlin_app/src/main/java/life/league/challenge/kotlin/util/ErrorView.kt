package life.league.challenge.kotlin.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import life.league.challenge.kotlin.R


/**
 *  A simple view that can be used for error cases, shows a message and a button.
 */
class ErrorView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_view_error, this, true)
    }

    private val button = findViewById<Button>(R.id.error_view_button)
    private val messageView = findViewById<TextView>(R.id.error_view_message_text_view)

    fun setButtonClickListener(callback: SimpleCallback) {
        button.setThrottledClickListener {
            callback()
        }
    }

    /**
     * This is a quick hack, ideally it should be done through prams or just letting user set the res for string
     * */
    fun setMessage(isNetworkError: Boolean) {
        val text = if (isNetworkError) {
            R.string.error_view_message_network_issue
        } else {
            R.string.error_view_message_network_issue
        }

        messageView.text = context.resources.getText(text)
    }


}