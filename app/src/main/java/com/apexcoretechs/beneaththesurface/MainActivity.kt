package com.apexcoretechs.beneaththesurface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableCard
import com.apexcoretechs.beneaththesurface.ui.theme.BeneathTheSurfaceTheme
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListViewModel
import androidx.compose.runtime.getValue
import com.apexcoretechs.beneaththesurface.ui.onthisday.OnThisDayForm
import com.apexcoretechs.beneaththesurface.util.loadJsonFromAssets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeneathTheSurfaceTheme {
                ExpandableListScreen()
            }
        }
    }
}

@Composable
fun ExpandableListScreen(viewModel: ExpandableListViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 📅 Form at the top
        item {
            OnThisDayForm { day, month ->
                viewModel.loadOnThisDayData(month, day)
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
        }

        // 🔽 Expandable cards
        items(state.items.size) { index ->
            ExpandableCard(item = state.items[index]) {
                viewModel.onItemToggle(index)
            }
        }
    }
}



