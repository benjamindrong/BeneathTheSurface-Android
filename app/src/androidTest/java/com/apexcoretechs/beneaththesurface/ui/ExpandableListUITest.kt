package com.apexcoretechs.beneaththesurface.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.apexcoretechs.beneaththesurface.ExpandableListScreen
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpandableListUITest {

    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testExpandableList() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val json = loadJsonFromAssets(context, "on_this_day_test.json")

        val viewModel = ExpandableListViewModel().apply {
            loadFromJson(json)
        }

        composeTestRule.setContent {
            ExpandableListScreen(viewModel = viewModel)
        }

        composeTestRule.onRoot().printToLog("UI_TREE")

        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("easter").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("easter").assertExists()
    }

}

