package com.crystal.realengplayer

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.util.UnstableApi


// 출처 :  https://proandroiddev.com/learn-with-code-jetpack-compose-playing-media-part-3-3792bdfbe1ea
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun RubenGameVideoPlayer(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // create our player
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            this.prepare()
        }
    }

//    Column(modifier = modifier) {
    ConstraintLayout(modifier = modifier) {
        val (title, videoPlayer) = createRefs()

        // video title
        Text(
            text = "Current Title",
            color = Color.White,
            modifier =
            Modifier.padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // player view
        DisposableEffect(
            AndroidView(
                modifier =
                Modifier.testTag("VideoPlayer")
                    .constrainAs(videoPlayer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                ,
                factory = { context ->

                    // exo player view for our video player
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT,
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT
                            )
                    }
                }
            )
        ) {
            onDispose {
                // release player when no longer needed
                exoPlayer.release()
            }
        }
    }
}

private const val KEY = "VIDEO_VIEWER_KEY"

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun TestVideoViewer(
    exoPlayer: ExoPlayer,
    onFullscreenToggle: (Boolean) -> Unit
) {
    AndroidView(factory = { context ->
        PlayerView(context).apply {
//            setFullscreenButtonClickListener { isFullscreen -> onFullscreenToggle(isFullscreen) }  // old
            setControllerOnFullScreenModeChangedListener { isFullScreen -> onFullscreenToggle(isFullScreen)}
            showController()

            useController = true
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

            player = exoPlayer

            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    })

    DisposableEffect(key1 = KEY) {
        onDispose {
            exoPlayer.pause()
            exoPlayer.seekToDefaultPosition()
            exoPlayer.clearMediaItems()
        }
    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun SmallVideoPlayer(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
                MediaItem.fromUri(
                    "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                )
            )
            prepare()
            playWhenReady = true
        }
    }

    Box(modifier = modifier) {
        DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }

        AndroidView(
            factory = {
                PlayerView(context).apply {
//                StyledPlayerView(context).apply {
                    player = exoPlayer
                    layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                }
            }
        )
    }
}
