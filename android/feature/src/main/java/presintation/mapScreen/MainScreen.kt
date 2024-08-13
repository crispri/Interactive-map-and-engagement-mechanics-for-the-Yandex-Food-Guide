package presintation.mapScreen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import custom_bottom_sheet.BottomSheetState
import model.Event
import model.NavigateToLocationEvent
import model.Recommendation
import model.Restaurant
import model.SaveInCollectionEvent
import custom_bottom_sheet.rememberBottomSheetState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ui.BigCard
import ui.CardWithImageAndText
import ui.CategoryButtonCard
import ui.TextCard
import java.text.DecimalFormat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navToRestaurant: (id: String) -> Unit,
    uiState: MainUiState,
    navToBack: () -> Unit,
    send: (Event) -> Unit,
    mapView: MapView,
    curLocation: MutableState<Point?>
) {

    val offsetState = remember { mutableFloatStateOf(-96f) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val density = LocalDensity.current

    val isExpandedAtOffset = remember { mutableStateOf(false) }

    val itemHeight = remember { mutableStateOf(0.dp) }

    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        defineValues = {
            SheetValue.Hidden at height(100.dp)
            if (itemHeight.value == 0.dp) {
                SheetValue.PartiallyExpanded at offset(percent = 60)
            } else {
                SheetValue.PartiallyExpanded at height(itemHeight.value + 100.dp)
            }
            if (isExpandedAtOffset.value) {
                if (itemHeight.value == 0.dp) {
                    SheetValue.Expanded at offset(percent = 60)
                } else {
                    SheetValue.Expanded at height(itemHeight.value + 100.dp)
                }
            } else {
                SheetValue.Expanded at contentHeight
            }
        }
    )

    LaunchedEffect(isExpandedAtOffset.value) {
        sheetState.refreshValues()
    }

    LaunchedEffect(itemHeight.value) {
        sheetState.refreshValues()
    }

    val bottomSheetState = custom_bottom_sheet.rememberBottomSheetScaffoldState(
        sheetState = sheetState
    )

    val lazyListState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    LaunchedEffect(bottomSheetState.sheetState) {
        snapshotFlow { bottomSheetState.sheetState.requireOffset() }
            .collect { offset ->
                offsetState.floatValue = offset
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isExpandedAtOffset.value = false
                    }
                )
            },
    ) {
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
//                    BottomSheetContent(uiState.restaurantsOnMap, navToRestaurant)
                    LazyColumn(
                        state = lazyListState,
                        flingBehavior = snapBehavior,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        if (sheetState.currentValue != SheetValue.Expanded) {
                                            isExpandedAtOffset.value = true
                                            Log.d("tap111", "Палец поставлен на экран контент")
                                        }
                                    }
                                )
                            }) {
                        itemsIndexed(uiState.restaurantsOnMap) { index, restaurant ->

                            Card(
                                modifier = Modifier
                                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .clickable {
                                        Log.d("ClickOnCard", restaurant.id)
                                        navToRestaurant(restaurant.id) }
                                    .onGloballyPositioned { coordinates ->
                                        val heightInPx = coordinates.size.height
                                        itemHeight.value = with(density) { heightInPx.toDp() }
                                    }
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                Log.d("LongClickOnCard", restaurant.id)
                                                navToRestaurant(restaurant.id)
                                            },
                                            onPress = {
                                                if (isExpandedAtOffset.value) {
                                                    Log.d("ClickOnCard", restaurant.id)
                                                    navToRestaurant(restaurant.id)
                                                }
                                                if (sheetState.currentValue != SheetValue.Expanded) {
                                                    isExpandedAtOffset.value = true
                                                    Log.d(
                                                        "tap111",
                                                        "Палец поставлен на экран контент"
                                                    )

                                                }

                                            }
                                        )
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = com.example.core.R.drawable.hardcode_picture_of_cafe),
                                        contentDescription = "Фото места",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 9.5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = restaurant.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row {
                                            Icon(
                                                painter = painterResource(id = com.example.core.R.drawable.ic_raiting),
                                                modifier = Modifier.height(24.dp),
                                                contentDescription = "Оценка"
                                            )
                                            Text(
                                                text = DecimalFormat("#.#").format(restaurant.rating)
                                                    .toString(),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }

                                    Text(
                                        text = restaurant.address,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(top = 1.5.dp)
                                    )

                                    Text(
                                        text = restaurant.description,
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 8.dp),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 15.sp,
                                    )

                                    val itemsList = listOf(
                                        "Музыка громче",
                                        "Завтраки",
                                        "Винотека",
                                        "Европейская",
                                        "Коктели",
                                        "Можно с собакой",
                                        "Веранда"
                                    )
                                    LazyRow(
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        items(itemsList) { item ->
                                            TextCard(text = item)
                                        }
                                    }
                                }
                            }
                        }
                    }
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


@Composable
fun CollectionCarousel(recommendations: List<Recommendation>) {
    var selectedCardIndex by remember { mutableIntStateOf(-1) }
    val lazyListState = rememberLazyListState()

    val configuration = LocalConfiguration.current
    val screenWidthInt = configuration.screenWidthDp
    LaunchedEffect(selectedCardIndex) {
        if (selectedCardIndex != -1 && selectedCardIndex != 0) {
            lazyListState.animateScrollToItem(selectedCardIndex, -(screenWidthInt / 2))
        } else if (selectedCardIndex == 0) {
            lazyListState.animateScrollToItem(selectedCardIndex, 0)
        }
    }

    LazyRow(
        state = lazyListState,
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .background(Color.Transparent)
    ) {
        itemsIndexed(recommendations) { index, item ->
            val isSelected = index == selectedCardIndex
            val cardWidth = if (isSelected) (250.dp) else 216.dp
            val cardHeight = if (isSelected) 100.dp else 90.dp
            val yOffset = if (isSelected) (-10).dp else 0.dp

            if (index > 0) {
                Spacer(modifier = Modifier.width(6.dp))
            }

            CardWithImageAndText(
                painterResource(id = com.example.core.R.drawable.photo1),
                text = item.title,
                description = item.description,
                {},
                {},
                onClick = {
                    selectedCardIndex = if (isSelected) -1 else index
                },
                modifier = Modifier
                    .width(cardWidth)
                    .height(cardHeight)
                    .offset(y = yOffset)
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

    Row {
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


