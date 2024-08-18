package presintation.mapScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import model.MainScreenEvent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import model.NavigateToLocationEvent
import model.Recommendation
import model.Restaurant
import model.SaveInCollectionEvent
import custom_bottom_sheet.rememberBottomSheetState
import model.CollectionOfPlace
import model.Filter
import model.SelectFilter
import model.SelectItemFromBottomSheet
import model.UpdateItemsOnMap
import ui.BigCard
import ui.CardWithImageAndText
import ui.CategoryButtonCard
import ui.TextCard
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun FilterBottomScreen(
    send: (MainScreenEvent) -> Unit,
    uiState: MainUiState,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(1f)
                .navigationBarsPadding()
        ) {
            FilterResultCard()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(bottom = 8.dp)
        ) {
            item {
                MainFilters(send, uiState = uiState)
            }
            item {
                CategoryCard(
                    rowsCount = 2,
                    cardDescription = "Тип заведения",
                    cardItemsCount = arrayOf(2, 3),
                    cardItemsText = arrayOf(
                        arrayOf("Азиатский ресторан", "Бар-клуб"),
                        arrayOf("Бельгийский бар", "Бистро", "Винотека")
                    ),
                    send = send,
                    uiState = uiState
                )
            }

            item {
                CategoryCard(
                    rowsCount = 2,
                    cardDescription = "Блюда",
                    cardItemsCount = arrayOf(4, 4),
                    cardItemsText = arrayOf(
                        arrayOf("Десерты", "Бургер", "Суши", "Пиццы"),
                        arrayOf("Вок", "Суп", "Паста", "Шашлык")
                    ),
                    send = send,
                    uiState = uiState
                )
            }
            item {
                CategoryCard(
                    rowsCount = 2,
                    cardDescription = "Кухни",
                    cardItemsCount = arrayOf(3, 3),
                    cardItemsText = arrayOf(
                        arrayOf("Европа", "Грузия", "Итальянская"),
                        arrayOf("Русская", "Узбекская", "Восток")
                    ),
                    send = send,
                    uiState = uiState
                )
            }

            item {
                CategoryCard(
                    rowsCount = 2,
                    cardDescription = "Время работы",
                    cardItemsCount = arrayOf(2, 1),
                    cardItemsText = arrayOf(
                        arrayOf("Круглосуточно", "Открыто сейчас"),
                        arrayOf("Открыто в указанное время")
                    ),
                    send = send,
                    uiState = uiState
                )
            }

            item {
                CategoryCard(
                    rowsCount = 2,
                    cardDescription = "Доступность",
                    cardItemsCount = arrayOf(2, 1),
                    cardItemsText = arrayOf(
                        arrayOf("Пандус", "Парковка для инвалидов"),
                        arrayOf("Туалет для инвалидов")
                    ),
                    send = send,
                    uiState = uiState
                )
            }

            item {
                CategoryCard(
                    rowsCount = 1,
                    cardDescription = "Фуд-моллы",
                    cardItemsCount = arrayOf(3),
                    cardItemsText = arrayOf(arrayOf("Депо", "Ленинский", "Маросейка")),
                    send = send,
                    uiState = uiState
                )
            }
            item {
                SortedByCard()
            }
        }
    }
}


@Composable
fun SortedByCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .height(500.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Text(
            text = "Сортировать",
            modifier = Modifier.padding(top = 12.dp, start = 20.dp),
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "По предпочтениям",
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
                Text(
                    text = "Сначала то, что вам, вероятно, понравится",
                    modifier = Modifier.padding(),
                    color = Color.Gray,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                )
            }
            var isChecked by remember { mutableStateOf(false) }
            val color = if (isChecked) Color.Black else Color.LightGray

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { isChecked = !isChecked }
            ) {
                if (isChecked) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White
                    )
                }
            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 20.dp
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "По рейтингу",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
                Text(
                    text = "Сначала рестораны с высоким рейтингом",
                    color = Color.Gray,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                )
            }
            var isChecked by remember { mutableStateOf(false) }
            val color = if (isChecked) Color.Black else Color.LightGray

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { isChecked = !isChecked }
            ) {
                if (isChecked) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White
                    )
                }
            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "По расстоянию",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
                Text(
                    text = "Сначала ближайшие рестораны",
                    color = Color.Gray,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                )
            }
            var isChecked by remember { mutableStateOf(false) }
            val color = if (isChecked) Color.Black else Color.LightGray

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { isChecked = !isChecked }
            ) {
                if (isChecked) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun CategoryCard(
    rowsCount: Int,
    cardDescription: String,
    cardItemsCount: Array<Int>,
    cardItemsText: Array<Array<String>>,
    send: (MainScreenEvent) -> Unit,
    uiState: MainUiState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Text(
            text = cardDescription,
            modifier = Modifier.padding(top = 12.dp, start = 10.dp),
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 6.dp, start = 8.dp, end = 8.dp, bottom = 10.dp)
        ) {
            for (i in 0 until rowsCount) {
                CategoryRow(
                    itemsCount = cardItemsCount[i],
                    items = cardItemsText[i],
                    send,
                    uiState = uiState
                )
            }
        }
    }
}

@Composable
fun CategoryRow(
    itemsCount: Int,
    vararg items: String,
    send: (MainScreenEvent) -> Unit,
    uiState: MainUiState
) {
    Row(
        modifier = Modifier.padding(top = 6.dp)
    ) {
        for (i in 0 until itemsCount) {
            CategoryFilterButtonCard(uiState = uiState, text = items[i], send)
        }
    }
}


@Composable
fun MainFilters(
    send: (MainScreenEvent) -> Unit,
    uiState: MainUiState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.Transparent),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 8.dp, end = 8.dp, bottom = 10.dp)
        ) {
            CategoryRow(2, "Рядом со мной", "Навынос", send = send, uiState = uiState)
            CategoryRow(
                itemsCount = 3,
                "Выпить кофе",
                "Танцпол",
                "Бар",
                send = send,
                uiState = uiState
            )
            Row(
                modifier = Modifier.padding(top = 6.dp)
            ) {
                val painterFood = painterResource(id = R.drawable.ic_raiting)
                CategoryImageFilterButtonCard(
                    uiState = uiState,
                    text = "ULTIMA GUIDE",
                    send,
                    image = painterFood
                )
                val painterHigh = painterResource(id = R.drawable.food)
                CategoryImageFilterButtonCard(
                    uiState = uiState,
                    text = "Высокий рейтинг",
                    send,
                    image = painterHigh,
                )
            }
            CategoryRow(3, "Новое место", "Гриль", "Музыка", send = send, uiState = uiState)
            CategoryRow(
                itemsCount = 3,
                "Летняя веранда",
                "Панорманый вид",
                "Халал",
                send = send,
                uiState = uiState
            )
            CategoryRow(3, "Настольные игры", "На крыше", "Бильярд", send = send, uiState = uiState)
            CategoryRow(
                itemsCount = 3,
                "Детское меню",
                "Детская комната",
                "Завтрак",
                send = send,
                uiState = uiState
            )
            CategoryRow(
                itemsCount = 3,
                "Дог-френдли",
                "Бизнес-ланч",
                "Выпить кофе",
                "Свидание",
                send = send,
                uiState = uiState
            )
            CategoryRow(
                itemsCount = 2,
                "Для большой компании",
                "Семейный ужин с детьми",
                send = send,
                uiState = uiState
            )
        }
    }
}


@Preview
@Composable
fun FilterResultCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(6.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp), verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(end = 4.dp)
                        .clip(RoundedCornerShape(1)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = "Сбросить",
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.W400
                    )
                }
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(end = 4.dp)
                        .clip(RoundedCornerShape(10)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCE000)
                    )
                ) {
                    Text(
                        text = "Показать",
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.W400
                    )
                }
            }
        }
    }
}


@Composable
fun CategoryImageFilterButtonCard(
    uiState: MainUiState,
    text: String,
    send: (MainScreenEvent) -> Unit,
    image: Painter
) {
    var isSelected by remember { mutableStateOf(uiState.filterMap[text] ?: false) }

    TextButton(
        onClick = {
            isSelected = !isSelected
            var filter = Filter("", listOf(""), "", !isSelected)
            when (text) {
                "Высокий рейтинг" -> {
                    filter = Filter(
                        property = "rating",
                        value = listOf(4.5),
                        operator = "gt",
                        isSelected = !isSelected,
                    )
                    send(SelectFilter(isSelected, filter))
                }

                "Круглосуточно" -> {
                    filter = Filter(
                        property = "open_time",
                        value = listOf("00:00:00"),
                        operator = "eq",
                        isSelected = !isSelected,
                    )
                    send(SelectFilter(isSelected, filter))
                    filter = Filter(
                        property = "close_time",
                        value = listOf("00:00:00"),
                        operator = "eq",
                        isSelected = !isSelected,
                    )
                    send(SelectFilter(isSelected, filter))
                }

                "Открыто сейчас" -> {
                    val currentTime = System.currentTimeMillis()
                    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    val formattedTime = sdf.format(Date(currentTime))
                    filter = Filter(
                        property = "open_time",
                        value = listOf(formattedTime),
                        operator = "gt",
                        isSelected = !isSelected,
                    )
                    send(SelectFilter(isSelected, filter))
                    filter = Filter(
                        property = "close_time",
                        value = listOf(formattedTime),
                        operator = "gt",
                        isSelected = !isSelected,
                    )
                    send(SelectFilter(isSelected, filter))
                }

                else -> {
                    if (text == "Завтрак" || text == "ULTIMA GUIDE" || text == "Бар" || text == "Бизнес-ланч" || text == "Семейный ужин с детьми") {
                        filter = Filter(
                            property = "tags",
                            value = listOf(text),
                            operator = "in",
                            isSelected = !isSelected,
                        )
                        send(SelectFilter(isSelected, filter))
                    }
                }
            }
        },
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (isSelected) Color.Black else Color(0xFFE2E2E2),
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(38.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = image,
                contentDescription = "Your Icon",
                modifier = Modifier.size(24.dp),
                colorFilter = if (isSelected) ColorFilter.tint(Color.White) else ColorFilter.tint(
                    Color.Black
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            BasicText(
                text = text,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W500,
                    color = if (isSelected) Color.White else Color.Black
                )
            )
        }
    }
}


@Composable
fun CategoryFilterButtonCard(uiState: MainUiState, text: String, send: (MainScreenEvent) -> Unit) {

    val flag =
        if (uiState.filterMap[text] != null) uiState.filterMap[text]!!
        else false
    val isSelected = remember { mutableStateOf(uiState.filterMap[text] ?: false) }
    LaunchedEffect(flag) {
        isSelected.value = flag
    }



    Log.d("FLAG", "${isSelected.toString()}, ${text}")

    TextButton(
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (isSelected.value) Color.Black else Color(0xFFE2E2E2),
            contentColor = if (isSelected.value) Color.White else Color.White
        ),
        onClick = {
            isSelected.value = !isSelected.value
            if (text == "Завтрак" || text == "ULTIMA GUIDE" || text == "Бар" || text == "Бизнес-ланч" || text == "Семейный ужин с детьми") {

                val filter = Filter(
                    property = "tags",
                    value = listOf(text),
                    operator = "in",
                    isSelected = !isSelected.value
                )
                Log.d("UNTIL SEND isSelected $text", "$text ${isSelected.value}")
                Log.d("UNTIL SEND filterList", uiState.filterList.toString())
                Log.d(
                    "UNTIL SEND filterMap",
                    "${uiState.filterMap.keys.toString()}  ${uiState.filterMap.values.toString()}"
                )
                send(SelectFilter(isSelected.value, filter))

            }
            Log.d("isSelected $text", "$text ${isSelected.value}")
            Log.d("filterList", uiState.filterList.toString())
            Log.d(
                "filterMap",
                "${uiState.filterMap.keys.toString()}  ${uiState.filterMap.values.toString()}"
            )
        },
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(38.dp),
    ) {
        BasicText(
            text = text,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.W500,
                color = if (isSelected.value) Color.White else Color.Black
            )
        )
    }
}