package com.apexcoretechs.beneaththesurface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import com.apexcoretechs.beneaththesurface.ui.onthisday.OnThisDayForm
import com.apexcoretechs.beneaththesurface.util.loadJsonFromAssets

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

    Column(modifier = Modifier.fillMaxSize()) {
        // Your existing form
        OnThisDayForm(
            onSubmit = { day, month ->
                viewModel.loadCombinedHistory(month, day)
            }
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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



