package com.va.android.base_template.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T, VB : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<T, VB>>(diffCallback) {

    override fun onBindViewHolder(holder: BaseViewHolder<T, VB>, position: Int) {
        holder.onBindData(getItem(position))
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<T, VB>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            // Nếu không có payload, bind lại toàn bộ data như bình thường
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.onBindDataWithPayload(getItem(position), payloads)
        }
    }
}