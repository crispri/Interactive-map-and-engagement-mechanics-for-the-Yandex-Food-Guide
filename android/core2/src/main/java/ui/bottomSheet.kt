package ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetScaffoldState()

    val offsetState = remember { mutableFloatStateOf(500f) }

    LaunchedEffect(bottomSheetState.bottomSheetState) {
        snapshotFlow { bottomSheetState.bottomSheetState.requireOffset() }
            .collect { offset ->
                offsetState.floatValue = offset
                Log.d("tag", offsetState.floatValue.toString())
            }
    }

    Column {
        CollectionCarousel()
        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 600.dp)
                        .background(Color.White)
                ) {
                    Carousel()  // Add Carousel here
                    Spacer(modifier = Modifier.height(16.dp)) // Add some space between Carousel and LazyColumn
                    BottomSheetContent()
                }
            },
            sheetPeekHeight = 80.dp,
            sheetContainerColor = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text("Main Content")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CollectionCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .offset(y = offsetState.floatValue.dp)
                    .align(Alignment.TopCenter)
            )
        }
    }

}

@Composable
fun BottomSheetContent() {
    LazyColumn {
        items((1..20).toList()) { item ->
            Text(
                text = "Item $item",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = Color.Black
            )
        }
    }
}

@Composable
fun Carousel() {
    val items = (1..4).map { "Item $it" }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White)
    ) {
        items(items) { item ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(120.dp, 50.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item, color = Color.White)
            }
        }
    }
}

@Composable
fun CollectionCarousel(modifier: Modifier = Modifier) {
    val items = (1..5).map { "Item $it" }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.White)
    ) {
        items(items) { item ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(160.dp, 90.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item, color = Color.White)
            }
        }
    }
}