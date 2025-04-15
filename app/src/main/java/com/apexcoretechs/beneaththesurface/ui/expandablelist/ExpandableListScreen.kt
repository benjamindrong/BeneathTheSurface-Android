package com.apexcoretechs.beneaththesurface.ui.expandablelist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apexcoretechs.beneaththesurface.viewmodel.ExpandableListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ExpandableListScreen(viewModel: ExpandableListViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        items(state.items.size) { index ->
            ExpandableCard(item = state.items[index]) {
                viewModel.onItemToggle(index)
            }
        }
    }
}

