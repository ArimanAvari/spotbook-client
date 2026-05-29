package com.spotbook.personalguide

import android.content.pm.ActivityInfo
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrientationInstrumentedTest {
    @Test
    fun mainScreenStaysVisibleAfterOrientationChanges() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            Thread.sleep(800)
            scenario.onActivity { activity ->
                val rootView = activity.window.decorView.rootView
                assertTrue(rootView.width > 0)
                assertTrue(rootView.height > 0)
            }

            scenario.onActivity { activity ->
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            Thread.sleep(800)
            scenario.onActivity { activity ->
                val rootView = activity.window.decorView.rootView
                assertTrue(rootView.width > 0)
                assertTrue(rootView.height > 0)
            }
        }
    }
}
