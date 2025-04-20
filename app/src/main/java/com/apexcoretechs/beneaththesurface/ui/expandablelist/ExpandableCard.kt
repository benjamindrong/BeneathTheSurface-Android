package com.apexcoretechs.beneaththesurface.ui.expandablelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.apexcoretechs.beneaththesurface.model.ExpandableItem

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableCard(
    item: ExpandableItem,
    onCardArrowClick: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (item.isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "ChevronRotation"
    )

    val pagerState = rememberPagerState(pageCount = { item.pages.size })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCardArrowClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.title} ${if (item.year.isBlank()) "" else "(${item.year})"}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand/Collapse",
                modifier = Modifier.rotate(rotationAngle)
            )
        }

        // Expanded content
        AnimatedVisibility(visible = item.isExpanded) {
            Column {
                // Page indicator: "2 / 5"
                Text(
                    text = "${pagerState.currentPage + 1} / ${item.pages.size}",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )


                // Pager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(vertical = 8.dp)
                ) { pageIndex ->
                    val page = item.pages[pageIndex]

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(8.dp)
                    ) {
                        var showFullScreen by remember { mutableStateOf(false) }
                        var fullScreenImageUrl by remember { mutableStateOf("") }

                        page.thumbnail?.source?.let { imageUrl ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp) // Thumbnail size
                                    .clickable {
                                        // On click, set the image for fullscreen
                                        fullScreenImageUrl = page.originalimage?.source
                                            ?: imageUrl // Use originalImage or fallback
                                        showFullScreen = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = page.title ?: "Page thumbnail",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

// Fullscreen Dialog
                        if (showFullScreen) {
                            @Composable
                            fun imagePreviewDialog() {
                                Dialog(onDismissRequest = { showFullScreen = false }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.9f))
                                            .clickable { showFullScreen = false },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = fullScreenImageUrl,
                                            contentDescription = "Full image",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(24.dp)
                                        )
                                    }
                                }
                            }

                            imagePreviewDialog()
                        }


                        // Page title
                        Text(
                            text = page.title ?: "Untitled",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Page description/extract
                        Text(
                            text = page.extract ?: "No description available.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Dot indicators
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    repeat(item.pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .padding(horizontal = 4.dp)
                                .background(
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}
