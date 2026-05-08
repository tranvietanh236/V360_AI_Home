package com.va.android.base_template.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.va.android.base_template.R
import com.va.android.base_template.utils.hideNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


abstract class BaseDialog<VB : ViewBinding>(context: Context) :
    Dialog(context, R.style.full_screen_dialog), LifecycleOwner {

    private var _viewBinding: VB? = null

    // Safe viewBinding access - returns null instead of throwing
    protected val viewBinding: VB
        get() = _viewBinding
            ?: throw kotlin.IllegalStateException("ViewBinding not initialized. Dialog may be dismissed.")

    // Safe nullable viewBinding for cleanup/dismiss scenarios
    protected val viewBindingOrNull: VB?
        get() = _viewBinding

    private val lifecycleRegistry = LifecycleRegistry(this)

    // Coroutine scope tied to dialog lifecycle - auto cancels on dismiss
    private val dialogScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    // Thread-safe dismiss flag
    @Volatile
    private var isDismissed = false

    abstract fun inflateViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.hideNavigation()
        // Prevent re-initialization if already destroyed
        if (isDismissed || lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
            return
        }

        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        try {
            _viewBinding = inflateViewBinding()
            setContentView(viewBinding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            initView()
            initOnClickListener()
            observeViewModel()
        } catch (e: Exception) {
            // Handle initialization errors gracefully
            e.printStackTrace()
            dismissSafely()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isDismissed && lifecycleRegistry.currentState != Lifecycle.State.DESTROYED) {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    override fun onStop() {
        // Only move to CREATED if not already DESTROYED
        if (!isDismissed && lifecycleRegistry.currentState != Lifecycle.State.DESTROYED) {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }
        super.onStop()
    }

    override fun dismiss() {
        dismissSafely()
    }

    // Synchronized dismiss to prevent race conditions
    @Synchronized
    private fun dismissSafely() {
        // Prevent multiple dismiss calls
        if (isDismissed) {
            return
        }

        isDismissed = true

        try {
            // Cancel all coroutines tied to this dialog
            dialogScope.cancel()

            // Properly transition through lifecycle states
            when (lifecycleRegistry.currentState) {
                Lifecycle.State.RESUMED,
                Lifecycle.State.STARTED -> {
                    lifecycleRegistry.currentState = Lifecycle.State.CREATED
                }

                else -> {
                    // Already in CREATED or lower, safe to destroy
                }
            }

            // Move to destroyed state
            if (lifecycleRegistry.currentState != Lifecycle.State.DESTROYED) {
                lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
            }

            // Clean up view binding
            _viewBinding = null

        } catch (e: Exception) {
            // Catch any lifecycle transition errors
            e.printStackTrace()
        } finally {
            // Always call super.dismiss()
            try {
                if (isShowing) {
                    super.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Safe UI ready that checks dialog state
     */
    fun uiReady(actionChangeUI: () -> Unit) {
        if (isDismissed || lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
            return
        }

        try {
            if (isShowing) {
                actionChangeUI()
            } else {
                setOnShowListener {
                    if (!isDismissed) {
                        actionChangeUI()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Safe coroutine launch tied to dialog lifecycle
     * Automatically cancels when dialog is dismissed
     */
    protected fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
        if (!isDismissed && lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            dialogScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    block()
                }
            }
        }
    }

    protected abstract fun initView()

    protected abstract fun initOnClickListener()

    protected abstract fun observeViewModel()
}