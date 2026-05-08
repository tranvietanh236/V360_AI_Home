package com.homedesign.interiordesign.aihome.ui.dialog

import android.content.Context
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RatingBar
import com.homedesign.interiordesign.aihome.base.BaseDialog
import com.homedesign.interiordesign.aihome.databinding.DialogRateBinding

class RatingDialog(context: Context) :
    BaseDialog<DialogRateBinding>(context) {
    private var onPress: OnPress? = null
    private var s = 5

    override fun inflateViewBinding(): DialogRateBinding {
        return DialogRateBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val attributes = window!!.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.let {
            it.attributes = attributes
            it.setSoftInputMode(16)
        }
        viewBinding.ratingBar.rating = 5.0f
        onclick()
        changeRating()
    }

    override fun initOnClickListener() {
    }

    override fun observeViewModel() {
    }

    interface OnPress {
        fun send(s: Int)

        fun rating(s: Int)

        fun cancel()

        fun later()

        fun gotIt()
    }

    fun init(onPress: OnPress?) {
        this.onPress = onPress
    }

    private fun changeRating() {
        viewBinding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _: RatingBar?, _: Float, _: Boolean ->
                val getRating = viewBinding.ratingBar.rating.toString()
                s = when (getRating) {
                    "1.0" -> 1
                    "2.0" -> 2
                    "3.0" -> 3
                    "4.0" -> 4
                    "5.0" -> 5
                    else -> 0
                }
            }
    }

    private fun onclick() {
        viewBinding.btnSubmit.setOnClickListener {
            Log.d("TAG23", "onclick: ${viewBinding.ratingBar.rating}")
            if (viewBinding.ratingBar.rating <= 4.0) {
                onPress.let {
                    it?.send(s)
                }
            } else {
                onPress.let {
                    it?.rating(s)
                }
            }
            viewBinding.tvTitle.text = "xxxxxxxxxxxxx"
            viewBinding.tvContent.text = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"

            viewBinding.btnGotit.visibility =View.VISIBLE
            viewBinding.ratingBar.visibility =View.GONE
            viewBinding.btnCancel.visibility =View.GONE
        }

        viewBinding.btnCancel.setOnClickListener {
            onPress.let {
                it?.later()
            }
        }

        viewBinding.btnGotit.setOnClickListener {
            onPress.let {
                it?.gotIt()
            }
        }
    }
}