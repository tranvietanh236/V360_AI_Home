package com.va.android.base_template.ui.dialog

import android.content.Context
import com.va.android.base_template.base.BaseDialog
import com.va.android.base_template.databinding.DialogSettingSystemBinding

class SettingSystemDialog (context: Context) :
    BaseDialog<DialogSettingSystemBinding>(context) {
    private var onClickConfirm: (() -> Unit)? = null

    fun onCLickConfirm(onClick: () -> Unit) {
        this.onClickConfirm = onClick
    }

    override fun inflateViewBinding(): DialogSettingSystemBinding {
        return DialogSettingSystemBinding.inflate(layoutInflater)
    }

    override fun initView() {}
    override fun initOnClickListener() {
        viewBinding.tvCancel.setOnClickListener {
            dismiss()
        }
        viewBinding.tvConfirm.setOnClickListener {
            onClickConfirm?.invoke()
            dismiss()
        }
    }

    override fun observeViewModel() {}
}