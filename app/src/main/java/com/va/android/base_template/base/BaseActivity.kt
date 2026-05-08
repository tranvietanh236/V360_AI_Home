package com.va.android.base_template.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.va.android.base_template.utils.SystemUtilApp

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _viewBinding: VB? = null
    protected val viewBinding
        get() = _viewBinding ?: throw IllegalStateException("ViewBinding not initialized")

    protected open val enableDebugLog: Boolean = false

    private var isActivityCreated = false
    private var isActivityDestroyed = false

    abstract fun inflateViewBinding(): VB
    protected abstract fun initView()
    protected abstract fun initOnClickListener()

    protected open fun getToolbarView(): Toolbar? = null
    protected open fun setupTheme() {}
    protected open fun initAd() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()
        super.onCreate(savedInstanceState)

        debugLog("onCreate: Activity created")

        SystemUtilApp.setLocale(this)
        _viewBinding = inflateViewBinding()
        setContentView(viewBinding.root)

        setupWindow()
        setupToolbar()

        // Gọi các hàm khởi tạo UI
        initView()
        initOnClickListener()
        initAd()

        isActivityCreated = true
    }

    private fun setupWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun setupToolbar() {
        val toolbar = getToolbarView()
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    protected fun debugLog(message: String) {
        if (enableDebugLog) {
            Log.d(this::class.simpleName, message)
        }
    }

    // --- Lifecycle logs ---
    override fun onStart() {
        super.onStart()
        debugLog("onStart")
    }

    override fun onResume() {
        super.onResume()
        debugLog("onResume")
    }

    override fun onPause() {
        debugLog("onPause")
        super.onPause()
    }

    override fun onStop() {
        debugLog("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        debugLog("onDestroy")
        isActivityDestroyed = true
        _viewBinding = null
        super.onDestroy()
    }

    fun isActivityAlive(): Boolean = isActivityCreated && !isActivityDestroyed

    protected fun withViewBinding(block: (VB) -> Unit) {
        if (isActivityAlive() && _viewBinding != null) {
            block(viewBinding)
        }
    }
}