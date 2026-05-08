package com.va.android.base_template.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiResponse(
    @SerializedName("records")
    val records: List<RecordItem>
) : Parcelable

@Parcelize
data class RecordItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("fields")
    val fields: FieldsData
) : Parcelable

@Parcelize
data class FieldsData(
    @SerializedName("CreatedAt")
    val createdAt: String? = null,
    @SerializedName("UpdatedAt")
    val updatedAt: String? = null,
    @SerializedName("app_id")
    val appId: String? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("data")
    val data: String? = null,
    @SerializedName("name")
    val name: String? = null
) : Parcelable
