package com.crystal.realengplayer.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.crystal.realengplayer.CustomExoPlayer
import com.crystal.realengplayer.data.TEST_MP3_URI_STRING
import com.crystal.realengplayer.data.TEST_MP4_URI_STRING
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val Samples = listOf(
    "basic" to "Basic",
    "settings" to "Settings",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
//                    VideoPlayer(uri = RealEngEpisodeManager.getUri(1))
        Log.d("TAG", TEST_MP3_URI_STRING)
        CustomExoPlayer(uri = TEST_MP4_URI_STRING)
    }


//    Scaffold(
//        topBar = {
////            TopAppBar(
////                title = { Text("Sample") }
////            )
//        },
//        modifier = Modifier
//            .fillMaxSize()
//            .systemBarsPadding(),
//    ) { padding ->
//        LazyColumn(
//            contentPadding = padding,
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//        ) {
//            items(Samples) { sample ->
//                val (route, name) = sample
//                Card(
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text(
//                        text = name,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                navController.navigate(route)
//                            }
//                            .padding(horizontal = 0.dp, vertical = 15.dp),
//                        fontSize = 18.sp,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//

//    }
}


@Composable
fun HideSystemUi()
{
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.isSystemBarsVisible = false
            //  portrait일떄, SystemBar 위치에 영상이 없으니, 아예 까맣게 보임.
            //  lanscape일때, SystemBar hidden 정상.
    }
}