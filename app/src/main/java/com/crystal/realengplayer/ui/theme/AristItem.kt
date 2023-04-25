package com.crystal.realengplayer.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ArtistItem() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image( // cast the Image import to match this name // ArtistImage
            painter = painterResource(id = com.crystal.realengplayer.R.drawable.ic_music_note),
            modifier = Modifier
                .padding(all = 8.dp)
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = "artist image"
        )
        Text(
            text = "Juice Wrld"
        )
    }
}

@Composable
fun MusicItem() {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = com.crystal.realengplayer.R.drawable.ic_baseline_audiotrack_24),
            contentDescription = "audio icon"
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Text(text = "Song title")
            Text(
                text = "Song artist",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RealEngPlayerTheme {
        ArtistItem()
        MusicItem()
    }
}