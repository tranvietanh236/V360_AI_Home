package com.va.android.base_template.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.va.android.base_template.R
import com.va.android.base_template.utils.hideNavigation

abstract class BaseBottomSheetDialog<VB : ViewBinding>(private val isCancel: Boolean) : BottomSheetDialogFragment() {
    lateinit var binding: VB
    protected abstract fun initView()
    protected abstract fun initClickListener()
    protected abstract fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setViewBinding(inflater, container)
        isCancelable = isCancel
        initView()
        initClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.hideNavigation()
    }
    override fun getTheme(): Int {
        return R.style.BaseBottomSheetDialog
    }

    override fun onDetach() {
        dialog?.dismiss()
        super.onDetach()
    }
}
