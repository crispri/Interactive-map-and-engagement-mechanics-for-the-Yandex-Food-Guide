import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.feature1.R
import com.yandex.mapkit.mapview.MapView
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navToBack: () -> Unit,
    mapView: MapView
) {
    val bottomSheetState = rememberBottomSheetScaffoldState()
    val offsetState = remember { mutableFloatStateOf(-96f) }

    LaunchedEffect(bottomSheetState.bottomSheetState) {
        snapshotFlow { bottomSheetState.bottomSheetState.requireOffset() }
            .collect { offset ->
                offsetState.floatValue = offset
            }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 600.dp)
                        .background(Color.White)
                ) {
                    Carousel()
                    Spacer(modifier = Modifier.height(16.dp))
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
                MapScreen(mapView = mapView)
            }
        }

        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 36.dp)
        ) {
            FloatingActionButton(
                onClick = navToBack,
                containerColor = MaterialTheme.colorScheme.onSecondary,
                shape = CircleShape
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "go_back",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .offset(y = (-90).dp)
                .offset { IntOffset(0, offsetState.floatValue.roundToInt() - 16) }
        ) {
            CollectionCarousel()
        }
    }
}


@Composable
fun CollectionCarousel() {
    val items = (1..5).map { "Item $it" }

    LazyRow(
        modifier = Modifier
            .height(90.dp)
            .padding(horizontal = 6.dp) // Отступы в начале и в конце
            .background(Color.White)
    ) {
        itemsIndexed(items) { index, item ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(6.dp)) // Отступ между элементами
            }
            CardWithImageAndText(
                painterResource(id = com.example.core1.R.drawable.hardcode_picture_of_cafe),
                "Kalabasa",
                "Крутое место",
                {},
                {}
            )
        }
    }
}


@Composable
fun BottomSheetContent() {
    LazyColumn {
        val list = listOf(R.drawable.hardcode_picture_of_cafe, R.drawable.hardcode_picture_of_cafe, R.drawable.hardcode_picture_of_cafe, R.drawable.hardcode_picture_of_cafe, R.drawable.hardcode_picture_of_cafe)
        items(list) { item ->
            BigCard(item)
        }
    }
}


@Composable
fun Carousel() {
    val itemsList = listOf(
        "Музыка громче",
        "Завтраки",
        "Винотека",
        "Европейская",
        "Коктели",
        "Можно с собакой",
        "Веранда"
    )

    Row(
        /*        modifier = Modifier.padding(top = 8.dp)*/
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            colors = IconButtonColors(
                Color.LightGray,
                Color.Black,
                Color.LightGray,
                Color.LightGray
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(32.dp),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_slot),
                    contentDescription = "Фильтр"
                )
            }
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                /*                .height(50.dp)*/
                .background(Color.White)
        ) {
            items(itemsList) { item ->
                CategoryButtonCard(text = item) {
                }
            }
        }
    }
}

