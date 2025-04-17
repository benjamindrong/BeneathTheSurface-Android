import com.apexcoretechs.beneaththesurface.model.OnThisDayData
import com.apexcoretechs.beneaththesurface.viewmodel.ExpandableListViewModel
import com.apexcoretechs.beneaththesurface.viewmodel.loadTestJson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ExpandableListViewModelTest {

    private val sampleJson = """
        {
            "selected": [
                {
                    "text": "Some historic event",
                    "year": 1776,
                    "pages": [
                        {
                            "title": "Declaration of Independence",
                            "extract": "The Declaration of Independence was adopted..."
                        }
                    ]
                }
            ]
        }
    """.trimIndent()

    @Test
    fun `loadFromJson populates state correctly`() = runTest {
        val viewModel = ExpandableListViewModel()

        viewModel.loadFromJson(sampleJson)

        val state = viewModel.state.value
        assertEquals(1, state.items.size)
        assertEquals("Some historic event", state.items[0].title)
        assertFalse(state.items[0].isExpanded)
    }

    @Test
    fun `test JSON deserialization into OnThisDayData`() {
        val json = loadTestJson("on_this_day_test.json")
        val parsed = Json.decodeFromString<OnThisDayData>(json)

        assertNotNull(parsed)
        assertTrue(parsed.selected.isNotEmpty())
        assertNotNull(parsed.selected.first().text)
    }

    @Test
    fun `onItemToggle updates isExpanded`() = runTest {
        val viewModel = ExpandableListViewModel()
        viewModel.loadFromJson(sampleJson)

        // Before toggle
        val beforeToggle = viewModel.state.value.items[0]
        assertFalse(beforeToggle.isExpanded)

        // Toggle
        viewModel.onItemToggle(0)

        val afterToggle = viewModel.state.value.items[0]
        assertTrue(afterToggle.isExpanded)
    }
}
