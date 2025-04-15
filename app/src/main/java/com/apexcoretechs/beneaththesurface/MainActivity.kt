package com.apexcoretechs.beneaththesurface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListScreen
import com.apexcoretechs.beneaththesurface.ui.theme.BeneathTheSurfaceTheme
import com.apexcoretechs.beneaththesurface.viewmodel.ExpandableListViewModel

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