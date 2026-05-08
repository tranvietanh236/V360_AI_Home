package com.homedesign.interiordesign.aihome.base

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

abstract class BaseRepository {

    /**
     * Hàm bọc an toàn mọi lời gọi API.
     * Trả về Result.Success hoặc Result.Error của riêng bạn.
     */
    protected suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ): Result<T> {
        return withContext(dispatcher) {
            try {
                // Gọi API và bọc data vào Success
                val response = apiCall.invoke()
                Result.Success(response)
            } catch (e: Exception) {
                // Xử lý lỗi và bọc vào Error
                handleException(e)
            }
        }
    }

    /**
     * Tách riêng hàm bắt lỗi để code dễ đọc và dễ bảo trì
     */
    private fun <T> handleException(e: Exception): Result<T> {
        return when (e) {
            is CancellationException -> {
                // LƯU Ý TỐI QUAN TRỌNG: Phải ném lại lỗi này để Coroutine có thể tự huỷ an toàn
                throw e
            }
            is IOException -> {
                // Lỗi mạng (Mất internet, Timeout, DNS...)
                Result.Error(Exception("Lỗi kết nối mạng. Vui lòng kiểm tra lại!"))
            }
            is HttpException -> {
                // Lỗi từ Server trả về (400, 401, 404, 500...)
                val code = e.code()
                val errorBody = e.response()?.errorBody()?.string()

                // Tip: Có thể custom thêm parse JSON errorBody ở đây
                Result.Error(Exception("Lỗi hệ thống ($code): $errorBody"))
            }
            else -> {
                // Các lỗi lặt vặt khác (Parse JSON sai, NullPointer...)
                Result.Error(Exception(e.message ?: "Đã có lỗi không xác định xảy ra"))
            }
        }
    }
}