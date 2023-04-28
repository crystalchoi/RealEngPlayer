package com.crystal.realengplayer.ui.screen

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.crystal.realengplayer.CustomExoPlayer
import com.crystal.realengplayer.R
import com.crystal.realengplayer.data.TEST_MP3_URI_STRING
import com.crystal.realengplayer.data.TEST_MP4_URI_STRING
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val menuItems = listOf("Item #1", "Item #2")
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        bottomBar = {
            MainBottomBar(navController)
        },
        snackbarHost = {}

    ) {

        NavHost(
            navController = navController, startDestination = "home",
            modifier = Modifier.padding(it)
        ) {
            composable("home") {
                Home(navController)
            }
            composable("starMarked") {
                StarMarked(navController)
            }
            composable("settings") {
                Settings(navController)
            }

        }
    }
}


@Composable
fun MainBottomBar(navController: NavHostController) {
    BottomNavigation(modifier = Modifier.heightIn(min = 64.dp, max = 128.dp),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        RealEngPlayerScreen.screens.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                },
                label = {
                    Text(text = stringResource(id = screen.label)
//                        , color = Color.White
                        , color = MaterialTheme.colorScheme.primary
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(id = screen.label),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .size(24.dp)
                    )
                },
                alwaysShowLabel = false,
            )
        }
    }
}



sealed class RealEngPlayerScreen(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {
    companion object {
        val screens = listOf(
            Home,
            StarMarked,
            Settings
        )

        const val route_home = "home"
        const val route_starMarked = "starMarked"
        const val route_settings = "settings"
    }

    private object Home : RealEngPlayerScreen(
        route_home,
        R.string.home,
        R.drawable.ic_home
    )

    private object StarMarked : RealEngPlayerScreen(
        route_starMarked,
        R.string.starMarked,
        R.drawable.ic_star
    )
    private object Settings : RealEngPlayerScreen(
        route_settings,
        R.string.settings,
        R.drawable.ic_settings
    )
}
