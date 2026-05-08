package com.va.android.base_template.data.remote.apiservice

import com.va.android.base_template.data.remote.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppApi {
    @GET("api/v3/data/{baseId}/{tableId}/records")
    suspend fun getDataAll(
        @Path("baseId") baseId: String,
        @Path("tableId") tableId: String,
        @Query("fields") fields: String,
        @Query("where") where: String,
        @Query("limit") limit: Int
    ): ApiResponse
}