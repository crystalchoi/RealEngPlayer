package com.crystal.realengplayer

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.crystal.realengplayer.data.RealEngEpisodeManager
import com.crystal.realengplayer.data.TEST_MP4_URI_STRING
import com.crystal.realengplayer.ui.theme.RealEngPlayerTheme
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.crystal.realengplayer.data.TEST_MP3_URI_STRING

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealEngPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    VideoPlayer(uri = RealEngEpisodeManager.getUri(1))
                    Log.d("TAG", TEST_MP3_URI_STRING)
                    CustomExoPlayer(uri = TEST_MP4_URI_STRING)
                }
            }
        }
//        hideSystemUI()
    }


//    fun hideSystemUI() {
//
//        //Hides the ugly action bar at the top
//        actionBar?.hide()
//
//        //Hide the status bars
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        } else {
//            window.insetsController?.apply {
//                hide(WindowInsets.Type.statusBars())
//                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        }
//    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RealEngPlayerTheme {
//        Greeting("Android")
        CustomExoPlayer(uri = TEST_MP3_URI_STRING)
    }
}