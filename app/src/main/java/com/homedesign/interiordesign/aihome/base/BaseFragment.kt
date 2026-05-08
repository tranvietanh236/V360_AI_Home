package com.homedesign.interiordesign.aihome.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected open val enableDebugLog: Boolean = false

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    abstract fun initView()
    abstract fun initListener()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initListener()
    }

    protected fun debugLog(message: String) {
        if (enableDebugLog) {
            android.util.Log.d(this::class.simpleName, message)
        }
    }

    protected fun withBinding(block: (VB) -> Unit) {
        _binding?.let { block(it) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}