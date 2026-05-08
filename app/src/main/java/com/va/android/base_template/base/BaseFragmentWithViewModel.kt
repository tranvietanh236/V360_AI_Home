package com.va.android.base_template.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseFragmentWithViewModel<VB : ViewBinding, VM: BaseViewModel> : BaseFragment<VB>() {
    protected abstract val viewModel: VM
    protected abstract fun observeViewModel()

    protected fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is UiState.Loading -> {
                            debugLog("State: Loading")
                            onLoading()
                        }

                        is UiState.Success -> {
                            debugLog("State: Success - ${state.data}")
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
    protected open fun onSuccess(data: Any) {}
    protected open fun onError(throwable: Throwable) {
        throwable.printStackTrace()
    }
    protected open fun onIdle() {}
}

