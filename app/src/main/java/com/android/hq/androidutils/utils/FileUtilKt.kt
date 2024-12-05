package com.android.hq.androidutils.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Author:hq on 2024/12/5 10:36
 *
 *  从 content：Uri中读取文件并保存在本地
 */
object FileUtilKt {
    private const val TAG = "FileUtilKt"
    fun uriToFile(context: Context, uri: Uri) {
        if (uri.scheme.equals("content")) {
            try {
                val resolver: ContentResolver = context.contentResolver
                val cursor = resolver.query(uri, null, null, null, null)
                var fileName:String = ""
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        fileName = cursor.getString(nameIndex)
                    }
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIndex >= 0) {
                        var size = cursor.getLong(sizeIndex)
                    }
                    val mimeTypeIndex = cursor.getColumnIndex("mime_type")
                    if (mimeTypeIndex >= 0) {
                        var mimeType = cursor.getString(mimeTypeIndex)
                    }
                    cursor.close()
                }

                val inputStream = resolver.openInputStream(uri)
                val bufferedInputStream = BufferedInputStream(inputStream)

                val file = File(context.cacheDir, fileName)
                Log.d(TAG, "start save File ${file.absolutePath}")
                var localPath = file.absolutePath
                val fileOutputStream = FileOutputStream(file)
                val bufferedOutputStream = BufferedOutputStream(fileOutputStream)
                val bufferSize = 10
                val byteArray = ByteArray(bufferSize)
                var rtBytes: Int = bufferedInputStream.read(byteArray)
                while (rtBytes in 1..bufferSize) {
                    bufferedOutputStream.write(byteArray, 0, rtBytes)
                    rtBytes = bufferedInputStream.read(byteArray)
                }
                bufferedOutputStream.close()
                bufferedInputStream.close()
                Log.d(TAG, "File ${file.absolutePath} is saved.")
            }catch (e: Exception) {
                Log.e(TAG, "uriToFile error: ", e)
            }
        }
    }
}