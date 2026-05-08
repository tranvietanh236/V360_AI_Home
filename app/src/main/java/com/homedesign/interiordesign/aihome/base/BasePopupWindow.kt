package com.homedesign.interiordesign.aihome.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import androidx.viewbinding.ViewBinding

abstract class BasePopupWindow<VB : ViewBinding>(
    private val context: Context,
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
        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        elevation = 10f

        // Gọi ngay các hàm setup để giữ tính đồng bộ với project
        initView()
        initClickListener()
    }

    // Buộc lớp con phải tách bạch logic UI
    protected abstract fun initView()
    protected abstract fun initClickListener()

    /**
     * Hàm show an toàn: Chống crash WindowLeaked
     */
    fun showAsDropDownSafely(anchor: View, xOff: Int = 0, yOff: Int = 0) {
        if (isActivityDying()) return
        try {
            showAsDropDown(anchor, xOff, yOff)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Hàm show an toàn ở tọa độ bất kỳ
     */
    fun showAtLocationSafely(parent: View, gravity: Int, x: Int, y: Int) {
        if (isActivityDying()) return
        try {
            showAtLocation(parent, gravity, x, y)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Hàm dismiss an toàn
     */
    fun dismissSafely() {
        if (isActivityDying()) return
        try {
            if (isShowing) dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Check xem Activity có đang bị hủy không trước khi thao tác với Window
    private fun isActivityDying(): Boolean {
        val activity = context as? Activity
        return activity == null || activity.isFinishing || activity.isDestroyed
    }
}
