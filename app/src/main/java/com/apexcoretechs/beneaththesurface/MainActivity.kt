package com.apexcoretechs.beneaththesurface

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListViewModel.LoadStatus
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
    val aiStatus by viewModel.aiStatus.collectAsState()
    val historyStatus by viewModel.historyStatus.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top form
        OnThisDayForm(
            onSubmit = { day, month ->
                viewModel.loadCombinedHistory(month, day)
            }
        )

        StatusRow(
            aiStatus = aiStatus,
            historyStatus = historyStatus,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 4.dp)
        )

        // Video loader animation
        VideoLoadingIndicator(
            isLoading = isLoading,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            items = uiState.items
        )

        // List of expandable cards
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
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = androidx.media3.common.MediaItem.Builder()
                .setUri("android.resource://${context.packageName}/${R.raw.loading_animation}")
                .build()
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
    }

    var showLoading by remember { mutableStateOf(false) }

    // Case 1: Initial empty state → Show paused frame
    LaunchedEffect(isLoading, items) {
        if (items.isEmpty() && !isLoading) {
            player.playWhenReady = false
            player.setPlaybackSpeed(1.0f)
            player.seekTo(0)
            showLoading = true
        }
    }

    // Case 2: Actively loading with no data → Play video, ramp speed
    LaunchedEffect(isLoading, items) {
        if (isLoading && items.isEmpty()) {
            player.seekTo(0)
            player.setPlaybackSpeed(1.0f)
            player.playWhenReady = true
            showLoading = true

            // Ramp speed after 0.1s
            delay(100)
            player.setPlaybackSpeed(2.0f)
        }
    }

    // Case 3: Data has arrived → Hide video
    LaunchedEffect(isLoading, items) {
        if (!isLoading && items.isNotEmpty()) {
            player.playWhenReady = false
            showLoading = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    if (showLoading) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    useController = false
                    this.player = player
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

@Composable
fun StatusIndicator(label: String, status: LoadStatus) {
    androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.material3.Text(
            text = label,
            modifier = Modifier.padding(end = 4.dp)
        )
        when (status) {
            LoadStatus.LOADING -> androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
            LoadStatus.SUCCESS -> androidx.compose.material3.Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Check,
                contentDescription = "Success",
                tint = androidx.compose.material3.MaterialTheme.colorScheme.primary
            )
            LoadStatus.FAILED -> androidx.compose.material3.Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Close,
                contentDescription = "Failed",
                tint = androidx.compose.material3.MaterialTheme.colorScheme.error
            )
            else -> androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(16.dp))
        }
    }
}


@Composable
fun StatusRow(
    aiStatus: LoadStatus,
    historyStatus: LoadStatus,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatusIndicator(label = "AI", status = aiStatus)
        Spacer(modifier = Modifier.size(16.dp))
        StatusIndicator(label = "History", status = historyStatus)
    }
}
