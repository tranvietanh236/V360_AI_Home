package com.homedesign.interiordesign.aihome.utils

import android.R
import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Window.hideNavigation() {
    if (setFullScreenWallpaper()) return

    decorView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        val activityRoot = decorView
        activityRoot.getWindowVisibleDisplayFrame(rect)
        if (setFullScreenWallpaper()) return@addOnGlobalLayoutListener
    }
}

fun Window.hideStatusBar() {
    val flags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    decorView.systemUiVisibility = flags
    val decorView: View = decorView
    decorView.setOnSystemUiVisibilityChangeListener { visibility ->
        if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
            decorView.systemUiVisibility = flags
        }
    }
}

private fun Window.setFullScreenWallpaper(): Boolean {
    val windowInsetsController = ViewCompat.getWindowInsetsController(decorView) ?: return true
    // Đảm bảo layout không bị đẩy lên khi ẩn thanh điều hướng
    addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

    // Cho phép người dùng vuốt để hiện lại thanh điều hướng khi cần
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    // Ẩn Navigation Bar, nhưng vẫn hiển thị Status Bar
    windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

    return false
}

fun Activity.listenToKeyboardVisibility(
    onKeyboardShown: () -> Unit,
    onKeyboardHidden: () -> Unit,
) {
    val rootView = findViewById<View>(R.id.content)
    var isKeyboardVisible = false

    rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)

        val screenHeight = rootView.rootView.height
        val keyboardHeight = screenHeight - rect.bottom

        val isNowVisible = keyboardHeight > screenHeight * 0.15

        if (isNowVisible != isKeyboardVisible) {
            isKeyboardVisible = isNowVisible
            if (isKeyboardVisible) {
                onKeyboardShown()
            } else {
                onKeyboardHidden()
            }
        }
    }
}

fun View.listenToKeyboardVisibility(
    onKeyboardShown: () -> Unit,
    onKeyboardHidden: () -> Unit,
) {
    var isKeyboardVisible = false
    val rootView = rootView

    rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.height
        val keypadHeight = screenHeight - rect.bottom

        val isVisibleNow = keypadHeight > screenHeight * 0.15
        if (isVisibleNow != isKeyboardVisible) {
            isKeyboardVisible = isVisibleNow
            if (isVisibleNow) {
                onKeyboardShown()
            } else {
                onKeyboardHidden()
            }
        }
    }
}

@Suppress("DEPRECATION")
fun View.hideNavigation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = windowInsetsController
        controller?.hide(WindowInsets.Type.systemBars())
        controller?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    viewTreeObserver.addOnGlobalLayoutListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowInsetsController?.hide(WindowInsets.Type.systemBars())
        } else {
            systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }
}




