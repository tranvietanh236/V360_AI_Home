package com.va.android.base_template.base

abstract class BaseRepository {

    protected suspend fun <T> safeCall(
        call: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(call())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}