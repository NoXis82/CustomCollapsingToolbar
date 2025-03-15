package ru.noxis.customcollapsingtoolbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import ru.noxis.customcollapsingtoolbar.custom_toolbar.CollapsingTitle
import ru.noxis.customcollapsingtoolbar.custom_toolbar.CustomToolbar
import ru.noxis.customcollapsingtoolbar.custom_toolbar.rememberToolbarScrollBehavior
import ru.noxis.customcollapsingtoolbar.ui.theme.CustomCollapsingToolbarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomCollapsingToolbarTheme {

//                CustomToolbarDemo()
                val scrollBehavior = rememberToolbarScrollBehavior()
//
                Scaffold(
                    modifier = Modifier
                        .statusBarsPadding()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        CustomToolbar(
                            scrollBehavior = scrollBehavior,
                            collapsingTitle = CollapsingTitle(
                                titleText = "Toolbar tile",
                                expandedTextStyle = MaterialTheme.typography.headlineLarge
                            ),
                            collapsedElevation = 4.dp,
                        )
                    }

                ) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        scrollableItemsForSample()
                    }
                }
            }
        }
    }
}

private fun LazyListScope.scrollableItemsForSample() {
    for (i in 0..100) {
        item("scroll_test_$i") {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "Item for scroll testing #$i"
            )
        }
    }
}
