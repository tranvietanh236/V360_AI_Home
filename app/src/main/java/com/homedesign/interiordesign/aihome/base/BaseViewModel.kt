package com.homedesign.interiordesign.aihome.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected val _uiState = MutableLiveData<UiState<Any>>(UiState.Idle)
    val uiState: LiveData<UiState<Any>> = _uiState

    protected open val enableDebugLog: Boolean = false

    protected fun launch(
        showLoading: Boolean = true,
        onError: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                debugLog("Exception: ${exception.message}")
                onError?.invoke(exception) ?: postError(exception)
            }
        ) {
            if (showLoading) _uiState.value = UiState.Loading
            try {
                block()
            } catch (e: Exception) {
                debugLog("Caught exception: ${e.message}")
                onError?.invoke(e) ?: postError(e)
            }
        }
    }

    protected fun postSuccess(data: Any) {
        debugLog("Success: $data")
        _uiState.value = UiState.Success(data)
    }

    protected fun postError(e: Throwable) {
        debugLog("Error: ${e.message}")
        _uiState.value = UiState.Error(e)
    }

    protected fun postIdle() {
        _uiState.value = UiState.Idle
    }

    protected fun postLoading() {
        _uiState.value = UiState.Loading
    }


    protected fun debugLog(message: String) {
        if (enableDebugLog) {
            Log.d(this::class.simpleName, message)
        }
    }
}
