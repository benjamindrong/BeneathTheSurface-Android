package com.apexcoretechs.beneaththesurface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListScreen
import com.apexcoretechs.beneaththesurface.ui.theme.BeneathTheSurfaceTheme

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