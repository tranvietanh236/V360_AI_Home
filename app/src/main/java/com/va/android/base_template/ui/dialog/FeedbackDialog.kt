package com.va.android.base_template.ui.dialog

import android.content.Context
import com.va.android.base_template.base.BaseDialog
import com.va.android.base_template.databinding.DialogFeedbackBinding

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