package com.va.android.base_template.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.va.android.base_template.ui.no_internet.NoInternetActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// stat an hien view
var View.visible: Boolean
    get() = isVisible
    set(value) {
        visibility = when (value) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

// check internet and tap
fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListener() {
        override fun onTap(v: View?) {
            try {
                if (!CheckInternet.haveNetworkConnection(context)) {
                    context.findActivity()?.let {
                        val intent = Intent(it, NoInternetActivity::class.java)
                        it.startActivity(intent)
                        it.overridePendingTransition(0, 0)
                    }
                } else {
                    action(v)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    })
}

fun View.visibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.INVISIBLE
}

abstract class DebounceClickListener(time: Long = 300) : Debounced(time),
    View.OnClickListener {
    final override fun onClick(v: View) {
        if (!debounce()) {
            onClickImpl(v)
        }
    }

    abstract fun onClickImpl(v: View)
}

open class Debounced(val debounceTime: Long) {
    private var lastClickTimestamp: Long = 0

    protected fun debounce(): Boolean {
        val currentTimestamp = SystemClock.elapsedRealtime()
        val shouldDebounce = currentTimestamp - lastClickTimestamp < debounceTime
        if (!shouldDebounce) {
            lastClickTimestamp = currentTimestamp
        }
        return shouldDebounce
    }
}


fun Int.dp(view: View): Int {
    return (this * view.resources.displayMetrics.density).toInt()
}

@SuppressLint("DefaultLocale")
fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

@SuppressLint("DefaultLocale")
fun Long.formatFileSize(): String {
    val kb = this / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    val tb = gb / 1024.0
    return when {
        tb >= 1 -> String.format("%.1f TB", tb)
        gb >= 1 -> String.format("%.1f GB", gb)
        mb >= 1 -> String.format("%.1f MB", mb)
        kb >= 1 -> String.format("%.1f KB", kb)
        else -> "$this B"
    }
}

@SuppressLint("DefaultLocale")
fun Long.formatDate(): String {
    return try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this // Convert seconds to milliseconds

        val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
        val year = calendar.get(Calendar.YEAR)
        val month = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
        val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
        val minute = String.format("%02d", calendar.get(Calendar.MINUTE))

        Log.d("TAG", "formatDate: $dayOfWeek $year $month $day - $hour:$minute")
        "$dayOfWeek $year $month $day - $hour:$minute"
    } catch (e: Exception) {
        "Unknown date"
    }
}


fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

fun ImageView.loadImage(
    uri: Uri?,
    placeholder: Int? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
) {
    val glideRequest = Glide.with(this.context)
        .load(uri)
//        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)

    placeholder?.let {
        glideRequest.placeholder(it)
    }
    glideRequest
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean,
            ): Boolean {
                onError?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                onSuccess?.invoke()
                return false
            }
        }).into(this)
}

fun ImageView.loadImage(
    url: String,
    onSuccess: ((Drawable) -> Unit)? = null,
    onError: ((Exception?) -> Unit)? = null,
) {
    Glide.with(this.context)
        .load(url)
        //.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                onError?.invoke(e)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                resource?.let { onSuccess?.invoke(it) }
                return false
            }
        }).into(this)
}

fun getBitmapSynchronous(context: Context, url: String?): Bitmap? {
    return try {
        Glide.with(context).asBitmap().load(url).submit().get()
    } catch (e: Exception) {
        null
    }
}

fun getSyncToBitmapWithRadius(
    context: Context,
    url: String,
    width: Int,
    height: Int,
    radiusDp: Float = 0f
): Bitmap? {
    return try {
        val density = context.resources.displayMetrics.density
        val radiusPx = radiusDp * density

        val maxSize = 512
        val targetWidth = minOf(width, maxSize)
        val targetHeight = minOf(height, maxSize)

        // Load với RGB_565 (giảm 50% bộ nhớ)
        val bitmap = Glide.with(context)
            .asBitmap()
            .load(url)
            .override(targetWidth, targetHeight)
            .encodeQuality(75)
            .format(DecodeFormat.PREFER_RGB_565)
            .submit()
            .get()

        // Apply rounded corners (hàm đã tự recycle bitmap gốc)
        if (radiusPx > 0) {
            bitmap.roundCorners(radiusPx)
        } else {
            bitmap
        }
    } catch (e: Exception) {
        Log.e("TAG", "Error loading bitmap with radius from $url", e)
        null
    }
}

fun Bitmap.roundCorners(radiusPx: Float): Bitmap {
    // Giữ nguyên config của bitmap gốc (RGB_565)
    val output = createBitmap(width, height, this.config ?: Bitmap.Config.RGB_565)

    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

    canvas.drawRoundRect(rect, radiusPx, radiusPx, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, 0f, 0f, paint)

    return output
}

fun Bitmap.optimizeBitmapForWallpaper(): Bitmap {
    val maxSize = 2048

    if (this.width <= maxSize && this.height <= maxSize) {
        return this
    }

    val ratio = minOf(
        maxSize.toFloat() / this.width,
        maxSize.toFloat() / this.height
    )

    val scaledWidth = (this.width * ratio).toInt()
    val scaledHeight = (this.height * ratio).toInt()

    return this.scale(scaledWidth, scaledHeight)
}


fun Bitmap.scaleToWidgetSize(context: Context, widgetSizeDp: Int): Bitmap {
    val density = context.resources.displayMetrics.density
    val sizePx = (widgetSizeDp * density).toInt()
    return if (this.width != sizePx || this.height != sizePx) {
        this.scale(sizePx, sizePx)
    } else {
        this
    }
}


fun Bitmap.scaleToWidgetSize(context: Context, widthDp: Int, heightDp: Int): Bitmap {
    val density = context.resources.displayMetrics.density
    val widthPx = (widthDp * density).toInt()
    val heightPx = (heightDp * density).toInt()
    return if (this.width != widthPx || this.height != heightPx) {
        this.scale(widthPx, heightPx)
    } else {
        this
    }
}

fun isAppInstalled(context: Context, packageName: String?): Boolean {
    return try {
        context.packageManager.getApplicationInfo(packageName.orEmpty(), 0)
        true
    } catch (e: Exception) {
        Log.d("IconDetailViewModel", "App not installed: $packageName")
        false
    }
}

fun AppCompatActivity.enableBackToExitApp() {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finishAffinity()
        }
    })
}

inline fun <reified T> Context.getListFromPrefs(key: String): List<T> {
    val encodedString = SystemUtilApp.getString(this, key) ?: return emptyList()

    val jsonString = encodedString.decodeHtmlEntities()

    val gson = Gson()
    val type = object : TypeToken<List<T>>() {}.type
    return gson.fromJson(jsonString, type) ?: emptyList()
}

// Helper function để decode HTML entities
fun String.decodeHtmlEntities(): String {
    return this
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&#39;", "'")
        .replace("&apos;", "'")
}

fun View.setPaddingDp(dp: Int) {
    val scale = context.resources.displayMetrics.density
    val px = (dp * scale + 0.5f).toInt()
    setPadding(px, px, px, px)
}

fun RecyclerView.isAtBottom(threshold: Int = 100): Boolean {
    val layoutManager = layoutManager as? LinearLayoutManager ?: return false
    val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
    val totalItemCount = adapter?.itemCount ?: 0

    if (totalItemCount == 0) return true

    return lastVisiblePosition >= totalItemCount - 2
}


fun Activity.hideKeyboard() {
    val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}