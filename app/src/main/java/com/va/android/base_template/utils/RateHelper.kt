package com.va.android.base_template.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import com.google.android.play.core.review.ReviewManagerFactory
import androidx.core.net.toUri

object RateHelper {
    private var TAG = "RateHelper"
    fun onRateApp(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                SystemUtilApp.forceRated(activity)
                val reviewInfo = task.result

                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnSuccessListener {
                    rateAppOnStore(activity)
                }
            } else {
                // fallback nếu request thất bại
                rateAppOnStore(activity)
            }
        }
    }


    fun rateAppOnStore(activity: Activity) {
        val packageName = activity.packageName
        val uri = "market://details?id=$packageName".toUri()
        val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
        }

        try {
            activity.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "ActivityNotFoundException: $e:")
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                )
            )
        }
    }
}
