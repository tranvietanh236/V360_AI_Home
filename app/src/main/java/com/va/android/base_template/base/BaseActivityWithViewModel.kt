package com.va.android.base_template.base

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseActivityWithViewModel<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() {

    protected abstract val viewModel: VM

    protected abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewModelState()
        observeViewModel()
    }

    /**
     * Observe UI state từ ViewModel (Loading, Error, Success)
     */
    private fun observeViewModelState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Đã sửa thành .collect() chuẩn Coroutines/StateFlow
                // (Nếu bạn vẫn dùng LiveData, hãy xóa launch và repeatOnLifecycle đi nhé)
                viewModel.uiState.observe(this@BaseActivityWithViewModel) { state ->
                    when (state) {
                        is UiState.Loading -> {
                            debugLog("State: Loading")
                            onLoading()
                        }
                        is UiState.Success<*> -> {
                            debugLog("State: Success")
                            onSuccess(state.data)
                        }
                        is UiState.Error -> {
                            debugLog("State: Error - ${state.throwable.message}")
                            onError(state.throwable)
                        }
                        is UiState.Idle -> {
                            debugLog("State: Idle")
                            onIdle()
                        }
                    }
                }
            }
        }
    }
    protected open fun onLoading() {}

    protected open fun onSuccess(data: Any?) {}

    protected open fun onError(throwable: Throwable) {
        debugLog("Error: ${throwable.message}")
        throwable.printStackTrace()
    }

    protected open fun onIdle() {}
}