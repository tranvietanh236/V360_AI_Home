package com.va.android.base_template.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected val _uiState = MutableLiveData<UiState<Any>>(UiState.Idle)
    val uiState: LiveData<UiState<Any>> = _uiState

    protected fun launch(
        showLoading: Boolean = true,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            if (showLoading) _uiState.value = UiState.Loading
            block()
        }
    }

    protected fun postSuccess(data: Any) {
        _uiState.value = UiState.Success(data)
    }

    protected fun postError(e: Throwable) {
        _uiState.value = UiState.Error(e)
    }
}
