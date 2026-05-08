package com.va.android.base_template.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T, VB : ViewBinding>(
    val binding: VB
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Hàm bắt buộc phải override ở lớp con.
     * Dùng để bind TOÀN BỘ dữ liệu lên UI (gọi khi item mới xuất hiện).
     */
    abstract fun onBindData(item: T)

    /**
     * Hàm open (không bắt buộc override).
     * Dùng để bind MỘT PHẦN dữ liệu (Partial Update) dựa vào payloads.
     */
    open fun onBindDataWithPayload(item: T, payloads: List<Any>) {
        onBindData(item)
    }
}