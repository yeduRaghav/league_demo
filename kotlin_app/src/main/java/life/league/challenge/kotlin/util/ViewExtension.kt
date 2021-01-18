package life.league.challenge.kotlin.util

import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

/**
 * Extension functions for the View classes for convenience
 */

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}


/**
 * Click listener for Views with debounce.
 * */
fun View.setThrottledClickListener(delayInMillis: Long = 500L, runWhenClicked: SimpleCallback) {
    setOnClickListener {
        this.isClickable = false
        this.postDelayed({ this.isClickable = true }, delayInMillis)
        runWhenClicked()
    }
}

/**
 * Removes underline from textviews
 * Source: https://stackoverflow.com/a/61082747/4127441
 * */
fun TextView.removeLinksUnderline() {
    val spannable = SpannableString(text)
    for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
        spannable.setSpan(object : URLSpan(u.url) {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, spannable.getSpanStart(u), spannable.getSpanEnd(u), 0)
    }
    text = spannable
}