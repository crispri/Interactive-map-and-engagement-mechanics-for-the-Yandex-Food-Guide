package presintation.mapScreen

import ImageCarousel
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import model.MainScreenEvent
import model.NavigateToLocationEvent
import model.Recommendation
import model.Restaurant
import model.SaveInCollectionEvent
import custom_bottom_sheet.rememberBottomSheetState
import model.CollectionOfPlace
import model.Filter
import model.SelectItemFromBottomSheet
import model.UpdateItemsOnMap
import ui.BigCard
import ui.CardWithImageAndText
import ui.CategoryButtonCard
import ui.TextCard
import java.text.DecimalFormat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navToRestaurant: (String) -> Unit,
    uiState: MainUiState,
    navToBack: () -> Unit,
    send: (MainScreenEvent) -> Unit,
    mapView: CustomMapView,
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

    val list = mutableStateOf(uiState.restaurantsOnMap)
    val isMapSelected = remember { mutableStateOf(false) }

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

    LaunchedEffect(uiState.selectedItemFromMapId) {
        if (uiState.selectedItemFromMapId != null) {
            sheetState.animateTo(SheetValue.PartiallyExpanded)
        } else {
            send(SelectItemFromBottomSheet(null))
            sheetState.animateTo(SheetValue.Hidden)
        }
    }

    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue == SheetValue.Hidden) {
            send(SelectItemFromBottomSheet(null))
        }
    }

    LaunchedEffect(uiState.selectedItemFromMapId) {
        val selectedId = uiState.selectedItemFromMapId
        if (selectedId != null) {
            val index = uiState.restaurantsOnMap.indexOfFirst { it.id == selectedId }
            if (index != -1) {
                list.value = listOf(uiState.restaurantsOnMap[index])
            } else {
                Log.e(
                    "selectedItemFromMapId",
                    "not found with id = $selectedId"
                )
                list.value = uiState.restaurantsOnMap
            }
        } else {
            list.value = uiState.restaurantsOnMap
        }
    }

    val currentIndex = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = lazyListState.firstVisibleItemScrollOffset, key2 = sheetState.currentValue) {
        val visibleIndex = lazyListState.firstVisibleItemIndex
        val visibleItemOffset = lazyListState.firstVisibleItemScrollOffset
        val itemHeightPx = itemHeight.value.value

        currentIndex.value = if (visibleItemOffset > itemHeightPx / 2) {
            visibleIndex + 1
        } else {
            visibleIndex
        }

        Log.d("lazyListState", "Current Index: ${currentIndex.value}")
        if (sheetState.currentValue == SheetValue.PartiallyExpanded
            && uiState.selectedItemFromMapId == null) {
            send(SelectItemFromBottomSheet(list.value[currentIndex.value].id))
            Log.e("lazyListState", "Selected Index: ${currentIndex.value} map = ${uiState.selectedItemFromMapId} bs = ${uiState.selectedItemFromBottomSheetId}")
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
                        itemsIndexed(list.value) { index, restaurant ->
                            isMapSelected.value = false
                            Card(
                                modifier = Modifier
                                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .clickable {
                                        Log.d("ClickOnCard", restaurant.id)
                                        navToRestaurant(restaurant.id)
                                    }
                                    .onGloballyPositioned { coordinates ->
                                        val heightInPx = coordinates.size.height
                                        itemHeight.value = with(density) { heightInPx.toDp() }
                                    }
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                Log.d("LongClickOnCard", restaurant.id)
                                                navToRestaurant(restaurant.id)
                                            },
                                            onPress = {
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
                                Column{
//                                    Image(
//                                        painter = painterResource(id = com.example.core.R.drawable.hardcode_picture_of_cafe),
//                                        contentDescription = "Фото места",
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .height(200.dp)
//                                            .clip(RoundedCornerShape(16.dp))
//                                    )

                                    ImageCarousel(

                                        imageUrls = restaurant.pictures
//                                        listOf(
//                                            "https://pixy.org/src/0/7688.jpg",
//                                            "https://avatanplus.com/files/resources/original/57a6de5284a4815663d4726f.jpg",
//                                        )
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 9.5.dp, start = 8.dp, end = 8.dp),
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
                                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                                    )

                                    Text(
                                        text = restaurant.description,
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 15.sp,
                                    )

                                    LazyRow(
                                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                    ) {
                                        items(restaurant.tags) { item ->
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
            CollectionCarousel(uiState.recommendations, uiState, send)
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
fun CollectionCarousel(
    recommendations: List<CollectionOfPlace>,
    uiState: MainUiState,
    send: (MainScreenEvent) -> Unit
) {
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
                text = item.name,
                description = item.description,
                {},
                {},
                onClick = {
                    val filterList = uiState.filterList
                    filterList.removeAll { it.property == "selection_id"}
                    if (isSelected){
                        selectedCardIndex = -1
                    } else {
                        selectedCardIndex = index
                        filterList.add(Filter("selection_id", listOf(item.id), "in"))
                    }

                    Log.d("okFilter", filterList.toString())
                    send(UpdateItemsOnMap(uiState.lowerLeft, uiState.topRight, filterList = filterList))
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


