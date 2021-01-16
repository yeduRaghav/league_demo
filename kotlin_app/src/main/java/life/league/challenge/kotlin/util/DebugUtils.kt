package life.league.challenge.kotlin.util

import android.util.Log
import life.league.challenge.kotlin.BuildConfig

/**
 * Functions to help debug
 */


fun logThread(title: String) {
    if (!BuildConfig.DEBUG) return
    Log.d("appan", "$title ==> ${Thread.currentThread().name}")
}