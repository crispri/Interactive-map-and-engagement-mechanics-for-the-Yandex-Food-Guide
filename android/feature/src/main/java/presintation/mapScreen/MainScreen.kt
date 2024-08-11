package presintation.mapScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import model.Event
import model.NavigateToLocationEvent
import model.Recommendation
import model.Restaurant
import model.SaveInCollectionEvent
import custom_bottom_sheet.rememberBottomSheetState
import ui.BigCard
import ui.CardWithImageAndText
import ui.CategoryButtonCard
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navToRestaurant: () -> Unit,
    uiState: MainUiState,
    navToBack: () -> Unit,
    send: (Event) -> Unit,
    mapView: MapView,
    curLocation: MutableState<Point?>
) {

    val offsetState = remember { mutableFloatStateOf(-96f) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val offsetInDp = with(LocalDensity.current) {
        offsetState.floatValue.toDp()
    }

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        defineValues = {
            SheetValue.Hidden at height(100.dp)
            SheetValue.PartiallyExpanded at offset(percent = 60)
            SheetValue.Expanded at contentHeight
        }
    )

    val bottomSheetState = custom_bottom_sheet.rememberBottomSheetScaffoldState(
        sheetState = sheetState
    )

    LaunchedEffect(bottomSheetState.sheetState) {
        snapshotFlow { bottomSheetState.sheetState.requireOffset() }
            .collect { offset ->
                offsetState.floatValue = offset
            }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        custom_bottom_sheet.BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = screenHeight - 240.dp, min = 80.dp)
                        .background(Color.White)
                ) {
                    Carousel()
                    Spacer(modifier = Modifier.height(16.dp))
                    BottomSheetContent(uiState.isLoading, uiState.restaurantsOnMap, navToRestaurant)
                }
            },
            sheetContainerColor = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                MapScreen(uiState, send, mapView, curLocation)
            }
        }

        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 46.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                onClick = { navToBack() },
                shape = CircleShape,
            ) {
                Image(
                    modifier = Modifier.size(28.dp, 28.dp),
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "go_back",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }

            Spacer(modifier = Modifier.weight(0.4f))

            Box(
                modifier = Modifier.weight(2.2f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Ваше местоположение",
                            fontSize = 14.sp,
                            color = Color.Black,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                        )
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                            contentDescription = "Стрелка",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                    Text(
                        text = uiState.currentAddress,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.4f))

            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    send(SaveInCollectionEvent(""))
                },
                shape = CircleShape,
            ) {
                Image(
                    modifier = Modifier.size(28.dp, 28.dp),
                    painter = painterResource(R.drawable.baseline_bookmark_border_24),
                    contentDescription = "go_back",
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .offset(y = (-100).dp)
                .offset { IntOffset(0, offsetState.floatValue.roundToInt()) }
        ) {
            CollectionCarousel(uiState.recommendations)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-160).dp)
                .offset { IntOffset(0, offsetState.floatValue.roundToInt()) }
        ) {
            AnimatedVisibility(
                visible = offsetState.floatValue > 800.0f,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                ) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        onClick = {
                            send(NavigateToLocationEvent())
                        },
                        shape = CircleShape,
                    ) {
                        Image(
                            modifier = Modifier.size(28.dp, 28.dp),
                            painter = painterResource(R.drawable.ic_navigate_to_location),
                            contentDescription = "go_to_current_location",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}


/*fun MainScreen(
    navToRestaurant: () -> Unit,
    uiState: MainUiState,
    navToBack: () -> Unit,
    send: (Event) -> Unit,
    mapView: MapView
) {

    val offsetState = remember { mutableFloatStateOf(-96f) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        defineValues = {
            SheetValue.Hidden at height(100.dp)
            SheetValue.PartiallyExpanded at offset(percent = 60)
            SheetValue.Expanded at contentHeight
        }
    )

    val bottomSheetState = custom_bottom_sheet.rememberBottomSheetScaffoldState(
        sheetState = sheetState
    )

    LaunchedEffect(bottomSheetState.sheetState) {
        snapshotFlow { bottomSheetState.sheetState.requireOffset() }
            .collect { offset ->
                offsetState.floatValue = offset
            }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        custom_bottom_sheet.BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = screenHeight - 240.dp, min = 80.dp)
                        .background(Color.White)
                ) {
                    Carousel()
                    Spacer(modifier = Modifier.height(16.dp))
                    BottomSheetContent(uiState.restaurantsOnMap, navToRestaurant)
                }
            },
            sheetContainerColor = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                MapScreen(uiState, send, mapView, curLocation)
            }
        }

        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 46.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                onClick = { navToBack() },
                shape = CircleShape,
            ) {
                Image(
                    modifier = Modifier.size(28.dp, 28.dp),
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "go_back",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }

            Spacer(modifier = Modifier.weight(0.4f))

            Box(
                modifier = Modifier.weight(2.2f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Ваше местоположение",
                            fontSize = 14.sp,
                            color = Color.Black,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                        )
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                            contentDescription = "Стрелка",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                    Text(
                        text = uiState.currentAddress,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.4f))

            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    send(SaveInCollectionEvent(""))
                },
                shape = CircleShape,
            ) {
                Image(
                    modifier = Modifier.size(28.dp, 28.dp),
                    painter = painterResource(R.drawable.baseline_bookmark_border_24),
                    contentDescription = "go_back",
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .offset(y = (-100).dp)
                .offset { IntOffset(0, offsetState.floatValue.roundToInt()) }
        )
        {
            CollectionCarousel(uiState.recommendations)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-160).dp)
                .offset { IntOffset(0, offsetState.floatValue.roundToInt()) })
        {
            AnimatedVisibility(
                visible = offsetState.floatValue > 950.0f,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                ) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        onClick = {
                            send(NavigateToLocationEvent())
                        },
                        shape = CircleShape,
                    ) {
                        Image(
                            modifier = Modifier.size(28.dp, 28.dp),
                            painter = painterResource(R.drawable.ic_navigate_to_location),
                            contentDescription = "go_to_current_location",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}*/



















@Composable
fun CollectionCarousel(recommendations: List<Recommendation>) {

    LazyRow(
        modifier = Modifier
            .height(90.dp)
            .padding(horizontal = 6.dp)
            .background(Color.Transparent)
    ) {
        itemsIndexed(recommendations) { index, item ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(6.dp))
            }
            CardWithImageAndText(
                painterResource(id = com.example.core.R.drawable.hardcode_picture_of_cafe),
                text = item.title,
                description = item.description,
                {},
                {}
            )
        }
    }
}


@Composable
fun BottomSheetContent(
    isLoading: Boolean,
    restaurants: List<Restaurant>,
    navToRestaurant: () -> Unit,
) {
    if (isLoading) {
        CircularProgressBar()
    } else {
        LazyColumn {
            items(restaurants) { item ->
                BigCard(item, navToRestaurant)
            }
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
                /* .height(50.dp)*/
                .background(Color.White)
        ) {
            items(itemsList) { item ->
                CategoryButtonCard(text = item) {
                }
            }
        }
    }
}

