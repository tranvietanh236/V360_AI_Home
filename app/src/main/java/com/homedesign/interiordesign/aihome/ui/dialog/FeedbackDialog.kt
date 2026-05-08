package com.homedesign.interiordesign.aihome.ui.dialog

import android.content.Context
import com.homedesign.interiordesign.aihome.base.BaseDialog
import com.homedesign.interiordesign.aihome.databinding.DialogFeedbackBinding

class FeedbackDialog(context: Context) : BaseDialog<DialogFeedbackBinding>(context) {
    override fun inflateViewBinding(): DialogFeedbackBinding {
        return DialogFeedbackBinding.inflate(layoutInflater)
    }

    override fun initView() {
        viewBinding.btnCancel.setOnClickListener { dismiss() }
        viewBinding.btnSubmit.setOnClickListener { dismiss() }
    }

    override fun initOnClickListener() {
    }

    override fun observeViewModel() {
    }
}