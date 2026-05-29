package com.spotbook.personalguide.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdaptivePane(
    modifier: Modifier = Modifier,
    wideFraction: Float = 0.72f,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val paneModifier = if (maxWidth >= 700.dp) {
            Modifier.fillMaxWidth(wideFraction.coerceIn(0.4f, 1f))
        } else {
            Modifier.fillMaxWidth()
        }

        Box(
            modifier = paneModifier,
            content = content
        )
    }
}
