package com.va.android.base_template.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseBottomSheetWithSharedViewModel<VB : ViewBinding, VM : BaseViewModel> :
    BaseBottomSheetDialog<VB>() {

    protected abstract val viewModel: VM

    protected abstract fun observeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
        observeViewModel()
    }

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is UiState.Loading -> {
                            onLoading()
                        }
                        is UiState.Success<*> -> {
                            onSuccess(state.data)
                        }
                        is UiState.Error -> {
                            onError(state.throwable)
                        }
                        is UiState.Idle -> {
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
        throwable.printStackTrace()
    }

    protected open fun onIdle() {}
}