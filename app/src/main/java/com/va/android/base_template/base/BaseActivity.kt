package com.va.android.base_template.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.va.android.base_template.utils.SystemUtilApp

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    private var _viewBinding: VB? = null
    protected val viewBinding
        get() = _viewBinding ?: throw IllegalStateException("ViewBinding not initialized")
    protected abstract val viewModel: VM

    abstract fun inflateViewBinding(): VB

    protected open fun getToolbarView(): View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtilApp.setLocale(this)
        _viewBinding = inflateViewBinding()
        setContentView(viewBinding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideNavigationBar()
        initView()
        initOnClickListener()
        observeViewModel()
        observeViewModelState()
        initAd()
    }

    protected open fun hideNavigationBar() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    protected open fun initAd() {}
    protected abstract fun initView()
    protected abstract fun initOnClickListener()
    protected abstract fun observeViewModel()
    private fun observeViewModelState() {}

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}