package com.crystal.realengplayer

import android.content.pm.ActivityInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View.inflate
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.media3.common.MediaMetadata
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.R
import androidx.media3.ui.PlayerView
import com.crystal.realengplayer.util.formatMinSec
import java.util.concurrent.TimeUnit
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.ui.AspectRatioFrameLayout
import com.crystal.realengplayer.data.TEST_MP4_URI_STRING
import com.crystal.realengplayer.databinding.ExoPlayerAbbrBinding
import com.crystal.realengplayer.ui.theme.RealEngPlayerTheme
import kotlinx.coroutines.coroutineScope
import java.security.AccessController.getContext

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun CustomExoPlayer(modifier: Modifier = Modifier,
                    uri: String,
                    startOffset: Long = 0L,
                    title: () -> String = { "My Video" }
) {

    val context = LocalContext.current


    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .apply {
                setSeekBackIncrementMs(PLAYER_SEEK_BACK_INCREMENT)
                setSeekForwardIncrementMs(PLAYER_SEEK_FORWARD_INCREMENT)
            }
            .build()
            .apply {
                setMediaItem(
                    MediaItem.Builder()
                        .apply {
                            setUri( uri)
                            setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setDisplayTitle( title() )
                                    .build()
                            )
                        }
                        .build()
                )
                prepare()

                playWhenReady = true
                seekTo(startOffset)
            }
    }


    var shouldShowControls by remember { mutableStateOf(true) }

    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }

    var totalDuration by remember { mutableStateOf(0L) }

    var currentTime by remember { mutableStateOf(0L) }

    var bufferedPercentage by remember { mutableStateOf(0) }

    var playbackState by remember { mutableStateOf(exoPlayer.playbackState) }
    var isFullModeOn by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        DisposableEffect(key1 = Unit) {
            val listener =
                object : Player.Listener {
                    override fun onEvents(
                        player: Player,
                        events: Player.Events
                    ) {
                        super.onEvents(player, events)
                        totalDuration = player.duration.coerceAtLeast(0L)
                        currentTime = player.currentPosition.coerceAtLeast(0L)
                        bufferedPercentage = player.bufferedPercentage
                        isPlaying = player.isPlaying
                        playbackState = player.playbackState
                    }
                }

            exoPlayer.addListener(listener)



            onDispose {
                exoPlayer.removeListener(listener)
                exoPlayer.release()
                exoPlayer.clearMediaItems()  //??
            }
        }

//        AndroidViewBinding(ExoPlayerAbbrBinding::inflate) {
//            Log.d("TAG", "${this.exoPlayer.background}")
//            PlayerView(context).apply {
//                player = exoPlayer
//                useController = false
//                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT   // for video??
//                layoutParams =
//                    FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                    )
//                defaultArtwork = ContextCompat.getDrawable(getContext(),
//                    com.crystal.realengplayer.R.drawable.ic_music_note);
//            }
//        }

//        AndroidView(
//            { context ->
//                LayoutInflater.from(context)
//                    .inflate(com.crystal.realengplayer.R.layout.exo_player_abbr, null)
//                    .apply {
//                        // Nested Scroll Interop will be Enabled when
//                        // nested scroll is enabled for the root view
//                        ViewCompat.setNestedScrollingEnabled(this, true)
//                    }
//            }
//        )

        AndroidView(
            modifier =
            Modifier
                .clickable {
                    shouldShowControls = shouldShowControls.not()
                }
                .aspectRatio(16f / 9f)
//                .align(alignment = Alignment.Center)
//                .fillMaxSize()

            , factory = {
                PlayerView(context).apply {

//                    var mediaController = MediaController(this)


                    defaultArtwork = ContextCompat.getDrawable(getContext(),
                        com.crystal.realengplayer.R.drawable.ic_music_note);
                    player = exoPlayer
                    useController = false
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
//                    layoutParams =
//                        FrameLayout.LayoutParams(
////                            ViewGroup.LayoutParams.MATCH_PARENT,
////                            ViewGroup.LayoutParams.MATCH_PARENT
//                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                        )

                    // my code
                    setControllerOnFullScreenModeChangedListener {
                        if (isFullModeOn) {
                            Log.d("TAG", "full mode on")
                        } else  {
                            Log.d("TAG", "full mode off")
                        }
                    }

                    // stackoverflow code
                    // https://stackoverflow.com/questions/72102097/jetpack-compose-exoplayer-full-screen
//                    setFullscreenButtonClickListener { isFullScreen ->
//                        with(context) {
//                            if (isFullScreen) {
//                                setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
//                            } else {
//                                setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                            }
//                        }
//                    }
                }
            }
        )

        PlayerControls(
            modifier = Modifier.fillMaxSize(),
            isVisible = { shouldShowControls },
            isPlaying = { isPlaying },
            title = { exoPlayer.mediaMetadata.displayTitle.toString() },
            playbackState = { playbackState },
            onReplayClick = { exoPlayer.seekBack() },
            onForwardClick = { exoPlayer.seekForward() },
            onPauseToggle = {
                when {
                    exoPlayer.isPlaying -> {
                        // pause the video
                        exoPlayer.pause()
                    }
                    exoPlayer.isPlaying.not() &&
                            playbackState == STATE_ENDED -> {
                        exoPlayer.seekTo(0)
                        exoPlayer.playWhenReady = true
                    }
                    else -> {
                        // play the video
                        // it's already paused
                        exoPlayer.play()
                    }
                }
                isPlaying = isPlaying.not()
            },
            totalDuration = { totalDuration },
            currentTime = { currentTime },
            bufferedPercentage = { bufferedPercentage },
            onSeekChanged = { timeMs: Float ->
                exoPlayer.seekTo(timeMs.toLong())
            },
            isFullModeOn = isFullModeOn,
            onFullModeToggle = { isFullModeOn = it
                               },
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    title: () -> String,
    onReplayClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferedPercentage: () -> Int,
    playbackState: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
    isFullModeOn: Boolean = false,
    onFullModeToggle: (Boolean) -> Unit = {}
) {

    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.6f))) {
            TopControl(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(),
                title = title,
                isFullModeOn = isFullModeOn,
                onFullModeToggle = onFullModeToggle,
            )

            Column(modifier = Modifier.align(alignment = Alignment.BottomCenter).fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CenterControls(
                    modifier = Modifier.fillMaxWidth(),
                    isPlaying = isPlaying,
                    onReplayClick = onReplayClick,
                    onForwardClick = onForwardClick,
                    onPauseToggle = onPauseToggle,
                    playbackState = playbackState
                )

                BottomControls(
                    modifier =
                    Modifier
                        .fillMaxWidth().padding(top = 8.dp)
                        .animateEnterExit(
                            enter =
                            slideInVertically(
                                initialOffsetY = { fullHeight: Int ->
                                    fullHeight
                                }
                            ),
                            exit =
                            slideOutVertically(
                                targetOffsetY = { fullHeight: Int ->
                                    fullHeight
                                }
                            )
                        ),
                    totalDuration = totalDuration,
                    currentTime = currentTime,
                    bufferedPercentage = bufferedPercentage,
                    onSeekChanged = onSeekChanged,
                )
            }
        }
    }
}

@Composable
private fun TopControl(modifier: Modifier = Modifier,
                       title: () -> String,
                       isFullModeOn: Boolean = false,
                       onFullModeToggle: (Boolean) -> Unit = {},
) {
    val videoTitle = remember(title()) { title() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            modifier = Modifier.padding(16.dp),
            text = videoTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.inversePrimary
        )

        IconButton(
            enabled = true,
            modifier = Modifier
                .size(48.dp)
                .padding(top = 4.dp, end = 16.dp),
            onClick = {
                Log.d("TAG", "$isFullModeOn")
                onFullModeToggle(!isFullModeOn)
            }

        ) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(
                    id =
                    if (!isFullModeOn) com.crystal.realengplayer.R.drawable.ic_fullscreen
                    else com.crystal.realengplayer.R.drawable.ic_fullscreen_exit
                ),
                contentDescription = "Enter/Exit fullscreen"
            )
        }
    }
}

@Composable
private fun CenterControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    playbackState: () -> Int,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onForwardClick: () -> Unit
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }

    val playerState = remember(playbackState()) { playbackState() }

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(modifier = Modifier.size(40.dp), onClick = onReplayClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = com.crystal.realengplayer.R.drawable.ic_replay_5),
                contentDescription = "Replay 5 seconds"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onPauseToggle) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter =
                when {
                    isVideoPlaying -> {
                        painterResource(id = R.drawable.exo_icon_pause)
                    }
                    isVideoPlaying.not() && playerState == STATE_ENDED -> {
                        painterResource(id = R.drawable.exo_icon_play)
                    }
                    else -> {
                        painterResource(id = R.drawable.exo_icon_play)
                    }
                },
                contentDescription = "Play/Pause"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onForwardClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = com.crystal.realengplayer.R.drawable.ic_forward_10),
                contentDescription = "Forward 10 seconds"
            )
        }
    }
}

@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferedPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
) {

    val duration = remember(totalDuration()) { totalDuration() }

    val currentTime = remember(currentTime()) { currentTime() }

    val buffer = remember(bufferedPercentage()) { bufferedPercentage() }

    Column(modifier = modifier.padding(bottom = 36.dp)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)) {
            Slider(
                value = buffer.toFloat(),
                enabled = false,
                onValueChange = { /*do nothing*/},
                valueRange = 0f..100f,
                colors =
                SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Gray
                )
            )

            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = currentTime.toFloat(),
                onValueChange = onSeekChanged,
                valueRange = 0f..duration.toFloat(),
                colors =
                SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTickColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = currentTime.formatMinSec(), fontSize = 20.sp,
                color = MaterialTheme.colorScheme.inversePrimary
            )

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = duration.formatMinSec(), fontSize = 20.sp,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }


    }
}


fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}

private const val PLAYER_SEEK_BACK_INCREMENT = 5 * 1000L // 5 seconds
private const val PLAYER_SEEK_FORWARD_INCREMENT = 10 * 1000L // 10 seconds


@Preview(showBackground = true)
@Preview(showBackground = false)
@Composable
fun TopControlPreview() {
    RealEngPlayerTheme() {
        TopControl(title = { "Title"})
    }
}

@Preview(showBackground = true)
@Preview(showBackground = false)
@Composable
fun BottomControlPreview() {
    RealEngPlayerTheme() {
        var seekChanged = 0f

        BottomControls(
            totalDuration = { 10000L },
            currentTime = {  2000L },
            bufferedPercentage = { 30 },
            onSeekChanged = { seekChanged = it }
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = false)
@Composable
fun CustomExoPlayerPreview() {
    RealEngPlayerTheme() {
        CustomExoPlayer(uri = TEST_MP4_URI_STRING)
    }
}