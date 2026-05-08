package com.va.android.base_template.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding

abstract class BasePopupWindow<VB : ViewBinding>(
    context: Context,
    bindingInflater: (LayoutInflater) -> VB,
    popupWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    popupHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) : PopupWindow(context) {

    protected val binding: VB

    init {
        val inflater = LayoutInflater.from(context)
        binding = bindingInflater(inflater)

        contentView = binding.root
        width = popupWidth
        height = popupHeight

        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
