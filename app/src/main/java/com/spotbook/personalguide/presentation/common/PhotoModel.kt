package com.spotbook.personalguide.presentation.common

import android.net.Uri
import com.spotbook.personalguide.BuildConfig
import java.io.File

fun photoModel(photoPath: String?): Any? {
    val path = photoPath?.takeIf { it.isNotBlank() } ?: return null
    return when {
        path.startsWith("http://") || path.startsWith("https://") -> path
        path.startsWith("content://") -> Uri.parse(path)
        path.startsWith("/") -> File(path)
        path.startsWith("uploads/") -> "${BuildConfig.BACKEND_BASE_URL.trimEnd('/')}/$path"
        else -> path
    }
}
