package com.apexcoretechs.beneaththesurface.ui.expandablelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apexcoretechs.beneaththesurface.model.ExpandableItem

@Composable
fun ExpandableCard(item: ExpandableItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = item.title, style = MaterialTheme.typography.headlineSmall)
        AnimatedVisibility(visible = item.isExpanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                item.children.forEach {
                    Text(text = it, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}
