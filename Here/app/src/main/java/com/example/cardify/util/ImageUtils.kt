package com.example.cardify.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

object ImageUtils {
    @Throws(IOException::class)
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @Throws(IOException::class)
    fun resourceToBase64(context: Context, resourceId: Int): String {
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        return bitmapToBase64(bitmap)
    }
}
