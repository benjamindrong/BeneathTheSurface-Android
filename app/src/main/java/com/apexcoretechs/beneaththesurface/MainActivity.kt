package com.apexcoretechs.beneaththesurface

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.apexcoretechs.beneaththesurface.model.ExpandableItem
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableCard
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListViewModel
import com.apexcoretechs.beneaththesurface.ui.onthisday.OnThisDayForm
import com.apexcoretechs.beneaththesurface.ui.theme.BeneathTheSurfaceTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeneathTheSurfaceTheme {
                CombinedHistoryScreen()
            }
        }
    }
}

@Composable
fun CombinedHistoryScreen(viewModel: ExpandableListViewModel = viewModel()) {
    val uiState by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isWaiting by viewModel.isWaiting.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Your existing form
        OnThisDayForm(
            onSubmit = { day, month ->
                viewModel.loadCombinedHistory(month, day)
            }
        )

//        if (isWaiting)
//            VideoLoadingIndicator(
//                isLoading = isLoading,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(16.dp)
//            )
        VideoLoadingIndicator(
            isLoading = isLoading,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            items = uiState.items
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            itemsIndexed(uiState.items) { index, item ->
                ExpandableCard(
                    item = item,
                    onCardArrowClick = { viewModel.onItemToggle(index) }
                )
            }
        }
    }
}

@Composable
fun VideoLoadingIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    items: List<ExpandableItem>
) {
    val context = LocalContext.current
    val playbackSpeed = 2.0f
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = androidx.media3.common.MediaItem.Builder()
                .setUri("android.resource://${context.packageName}/${R.raw.loading_animation}")
                .build()
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_OFF
            prepare()
            setPlaybackSpeed(playbackSpeed)
        }
    }

    var showLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Phase 1: Show player paused if items are empty and loading hasn’t started yet
    LaunchedEffect(items, isLoading) {
        if (items.isEmpty() && !isLoading) {
            showLoading = true
            player.playWhenReady = false
            player.seekTo(0) // Reset to start
        }
    }

    // Phase 2: Start playing when loading begins
    LaunchedEffect(isLoading) {
        if (isLoading) {
            showLoading = true
            player.playWhenReady = false // Start as paused
            player.seekTo(0) // Reset the player to the start
            player.prepare()

            // Wait until player is in the ready state before playing
            while (player.playbackState != Player.STATE_READY) {
                delay(50) // Check periodically if the player is ready
            }

            // Once ready, start playback
            player.playWhenReady = true
        }
    }

    // Phase 3: After loading ends, wait for animation duration and hide
    LaunchedEffect(isLoading) {
        if (!isLoading && items.isNotEmpty()) {
            // Wait for the player to reach STATE_ENDED
            while (player.playbackState != Player.STATE_ENDED) {
                delay(50) // Wait until playback is finished
            }
            player.playWhenReady = false
            showLoading = false
        }
    }

    // Display the player view when showLoading is true
    if (showLoading) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    this.player = player
                    useController = false
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            },
            modifier = modifier
        )
    }
}
