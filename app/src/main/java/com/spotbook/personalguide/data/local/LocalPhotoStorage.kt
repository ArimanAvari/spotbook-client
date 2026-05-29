package com.spotbook.personalguide.data.local

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File

object LocalPhotoStorage {
    fun savePhoto(context: Context, uri: Uri): String {
        val directory = File(context.filesDir, "place_photos").apply {
            mkdirs()
        }
        val extension = context.contentResolver.getType(uri)
            ?.let { type -> MimeTypeMap.getSingleton().getExtensionFromMimeType(type) }
            ?.takeIf { it.isNotBlank() }
            ?: "jpg"
        val target = File(directory, "place_${System.currentTimeMillis()}.$extension")

        context.contentResolver.openInputStream(uri)?.use { input ->
            target.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: error("Не удалось открыть выбранное фото")

        return target.absolutePath
    }

    fun saveServerPhoto(context: Context, sourcePath: String, bytes: ByteArray): String {
        val directory = File(context.filesDir, "place_photos").apply {
            mkdirs()
        }
        val extension = sourcePath
            .substringBefore('?')
            .substringAfterLast('.', "jpg")
            .takeIf { it.isNotBlank() && it.length <= 8 }
            ?: "jpg"
        val target = File(directory, "server_${sourcePath.hashCode().toUInt()}.$extension")

        target.writeBytes(bytes)
        return target.absolutePath
    }
}
