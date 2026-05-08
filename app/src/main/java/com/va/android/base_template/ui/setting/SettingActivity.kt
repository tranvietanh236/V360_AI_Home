package com.va.android.base_template.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import com.va.android.base_template.R
import com.va.android.base_template.base.BaseActivity
import com.va.android.base_template.base.BaseActivityWithViewModel
import com.va.android.base_template.base.BaseViewModel
import com.va.android.base_template.databinding.ActivitySettingBinding
import com.va.android.base_template.ui.dialog.FeedbackDialog
import com.va.android.base_template.ui.dialog.RatingDialog
import com.va.android.base_template.ui.setting.language.LanguageSettingActivity
import com.va.android.base_template.utils.Constants
import com.va.android.base_template.utils.RateHelper.onRateApp
import com.va.android.base_template.utils.SystemUtilApp
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : BaseActivity<ActivitySettingBinding>() {


    override fun inflateViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initView() {
        action()
    }

    override fun initOnClickListener() {}


    override fun onResume() {
        super.onResume()
        SystemUtilApp.setLocale(this)
        if (SystemUtilApp.isRated(this)) {
            viewBinding.llRate.visibility = View.GONE
            viewBinding.lineRate.visibility = View.GONE
        }
    }

    private fun action() {
        viewBinding.llLanguage.setOnClickListener {
            val intent = Intent(this, LanguageSettingActivity::class.java)
            startActivity(intent)
        }
        viewBinding.llPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Constants.POLICY_URL.toUri())
            startActivity(intent)
        }
        viewBinding.llShare.setOnClickListener {
            share()
        }
        viewBinding.llRate.setOnClickListener {
            showRateDialog()
        }
        viewBinding.llFeedback.setOnClickListener {
            showFeedback()
        }


    }

    private fun showRateDialog() {
        val ratingDialogApp = RatingDialog(this@SettingActivity)
        ratingDialogApp.init(object : RatingDialog.OnPress {
            override fun send(s: Int) {
                SystemUtilApp.forceRated(this@SettingActivity)
            }

            override fun rating(s: Int) {
                SystemUtilApp.forceRated(this@SettingActivity)
                onRateApp(this@SettingActivity)
            }

            override fun cancel() {
                ratingDialogApp.dismiss()
            }

            override fun later() {
                ratingDialogApp.dismiss()
            }

            override fun gotIt() {
                ratingDialogApp.dismiss()
            }


        })
        ratingDialogApp.show()
        ratingDialogApp.setOnDismissListener {
            if (SystemUtilApp.isRated(this@SettingActivity)) {
                viewBinding.llRate.visibility = View.GONE
                viewBinding.lineRate.visibility = View.GONE
            }
        }
    }

    private fun showFeedback() {
        val dialogFB = FeedbackDialog(this)
        dialogFB.show()
    }

    private fun share() {
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intentShare.putExtra(
            Intent.EXTRA_TEXT,
            "${getString(R.string.app_name)}\nhttps://play.google.com/store/apps/details?id=${this.packageName}"
        )
        startActivity(Intent.createChooser(intentShare, "Share"))
    }
}