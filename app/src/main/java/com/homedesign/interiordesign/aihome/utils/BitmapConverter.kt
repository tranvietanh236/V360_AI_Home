package com.homedesign.interiordesign.aihome.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.core.graphics.scale
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object BitmapConverter {
    @TypeConverter
    fun fromBitmapList(list: List<Bitmap>?): String? {
        if (list == null) return null
        val byteArrayList = list.map { bitmap ->
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }
        return Gson().toJson(byteArrayList)
    }

    @TypeConverter
    fun toBitmapList(json: String?): List<Bitmap>? {
        if (json == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = Gson().fromJson(json, type)
        return stringList.map { encoded ->
            val bytes = Base64.decode(encoded, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
    fun bitmapsToBase64List(bitmaps: List<Bitmap>): List<String> {
        return bitmaps.map { bitmap ->
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
        val fileName = "img_${System.currentTimeMillis()}.png"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file.absolutePath
    }

    fun saveBitmapsToFiles(context: Context, bitmaps: List<Bitmap>?): List<String> {
        val paths = mutableListOf<String>()
        bitmaps?.forEachIndexed { index, bitmap ->
            val fileName = "img_${System.currentTimeMillis()}_$index.png"
            val file = File(context.filesDir, fileName)
            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                paths.add(file.absolutePath)
                Log.d("SaveBitmap", "Đã lưu: ${file.absolutePath}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return paths
    }

    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int = 400, maxHeight: Int = 400): Bitmap {
        val ratio = minOf(
            maxWidth.toFloat() / bitmap.width,
            maxHeight.toFloat() / bitmap.height
        )
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return bitmap.scale(width, height)
    }

    // 2. Helper function
    fun bitmapToBase64(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }


    fun convertHardwareBitmapToMutable(bitmap: Bitmap): Bitmap {
        if (bitmap.config == Bitmap.Config.HARDWARE) {
            val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            return mutableBitmap
        }
        return bitmap
    }

    @SuppressLint("ExifInterface")
    fun uriToBitmap(uri: Uri, context: Context): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            val exif = context.contentResolver.openInputStream(uri)?.use { ExifInterface(it) }
            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
            }

            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
