package com.homedesign.interiordesign.aihome.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.homedesign.interiordesign.aihome.R
import com.homedesign.interiordesign.aihome.utils.hideNavigation

abstract class BaseBottomSheetDialog<VB : ViewBinding>(
    private val isCancelableDialog: Boolean = true
) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("ViewBinding is null. Cannot access view after onDestroyView.")

    // Abstract functions
    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    protected abstract fun initView()
    protected abstract fun initClickListener()

    override fun getTheme(): Int {
        return R.style.BaseBottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        isCancelable = isCancelableDialog
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.hideNavigation()
        initView()
        initClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
