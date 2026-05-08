package com.va.android.base_template.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.va.android.base_template.ui.dialog.SettingSystemDialog
import kotlin.collections.all
import kotlin.reflect.KMutableProperty0

object PermissionHelper {
    // Check permissions
    fun isCameraPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Kiểm tra quyền audio
    fun isAudioPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
    // Request permissions
    fun requestAudioPermission(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(arrayOf(Manifest.permission.RECORD_AUDIO))
    }

    fun requestCameraPermission(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    fun requestNotificationPermission(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
    }
    //-------------------------------Permission---------------------//

    private fun openSetting(context: Context) {
        try {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts(Constants.PACKAGE, context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSettingsDialogNew(context: Context) {
        val dialog = SettingSystemDialog(context)
        dialog.onCLickConfirm {
            openSetting(context)
        }
        dialog.show()
    }

    fun handlePermission(
        context: Context,
        permissions: Map<String, Boolean>,
        deniedCount: KMutableProperty0<Int>,
        action: () -> Unit
    ) {
        if (permissions.values.all { it }) {
            deniedCount.set(0)
        } else {
            deniedCount.set(deniedCount.get() + 1)
            if (deniedCount.get() > 2) {
                showSettingsDialogNew(context)
            }
        }
        action.invoke()
    }
}
