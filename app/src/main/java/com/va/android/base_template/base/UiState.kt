package com.va.android.base_template.base

/**
 * Sealed class để represent UI state
 * - Idle: ban đầu
 * - Loading: đang load
 * - Success: load thành công kèm data
 * - Error: load thất bại kèm exception
 */
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val throwable: Throwable) : UiState<Nothing>()

    val isIdle: Boolean get() = this is Idle
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data
    fun errorOrNull(): Throwable? = (this as? Error)?.throwable

    inline fun <R> map(transform: (T) -> R): UiState<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(throwable)
            is Loading -> Loading
            is Idle -> Idle
        }
    }

    inline fun onSuccess(action: (T) -> Unit): UiState<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (Throwable) -> Unit): UiState<T> {
        if (this is Error) action(throwable)
        return this
    }

    inline fun onLoading(action: () -> Unit): UiState<T> {
        if (this is Loading) action()
        return this
    }

    inline fun onIdle(action: () -> Unit): UiState<T> {
        if (this is Idle) action()
        return this
    }
}