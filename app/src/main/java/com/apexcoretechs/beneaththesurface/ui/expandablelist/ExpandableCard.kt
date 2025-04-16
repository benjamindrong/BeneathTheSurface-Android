package com.apexcoretechs.beneaththesurface.ui.expandablelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCardArrowClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.text} - ${item.year}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand/Collapse",
                modifier = Modifier.rotate(rotationAngle)
            )
        }

        AnimatedVisibility(visible = item.isExpanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                // Page titles as clickable, pageable items
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(item.children.size) { index ->
                        Text(
                            text = item.children[index],
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .clickable {
                                    // Handle page click, could navigate or show more content
                                }
                                .padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
