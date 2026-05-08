package com.va.android.base_template.utils

import android.os.SystemClock
import android.view.View

abstract class TapListener : View.OnClickListener {
    private var now = 0L

    override fun onClick(view: View) {
        now = SystemClock.elapsedRealtime()
        if (now - lastClickMillis > DEBOUNCE) {
            onTap(view)
            lastClickMillis = now
        }
    }

    abstract fun onTap(v: View?)

    companion object {
        private const val DEBOUNCE = 500L
        private var lastClickMillis: Long = 0
    }
}