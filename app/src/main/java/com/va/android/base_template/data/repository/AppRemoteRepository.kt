package com.va.android.base_template.data.repository

import com.va.android.base_template.data.remote.apiservice.AppApi

class AppRemoteRepository(private val appApi: AppApi){
    suspend fun fetchData(
        baseId: String,
        tableId: String,
        fields: String,
        where: String,
        limit: Int = 1
    ): String {

        val response = appApi.getDataAll(
            baseId = baseId,
            tableId = tableId,
            fields = fields,
            where = where,
            limit = limit
        )
        return response.records.firstOrNull()?.fields?.data
            ?: throw kotlin.IllegalStateException("No data found")
    }
}