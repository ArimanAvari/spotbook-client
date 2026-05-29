package com.spotbook.personalguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spotbook.personalguide.presentation.navigation.AppNavGraph
import com.spotbook.personalguide.presentation.theme.SpotBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpotBookTheme {
                AppNavGraph()
            }
        }
    }
}

