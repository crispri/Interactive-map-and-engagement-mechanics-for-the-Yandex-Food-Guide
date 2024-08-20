package presintation.homeScreen

import ImageCarousel
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import model.MainScreenEvent
import model.Restaurant
import model.UpdateItemsOnMap
import presintation.mapScreen.CircularProgressBar
import presintation.mapScreen.MainUiState
import ui.BigCard
import ui.CategoryButtonCard
import ui.TextCard
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    send: (MainScreenEvent) -> Unit,
    uiState: MainUiState,
    navToMap: () -> Unit
) {

    val list = mutableStateOf(uiState.restaurantsOnMap)

    LaunchedEffect(Unit) {
        send(UpdateItemsOnMap(Point(55.0, 37.0), Point(55.737, 37.597), filterList = emptyList(), 0.0, 0.0))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .zIndex(1f)
                .padding(bottom = 8.dp, top = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Переход на экран карты"
                        )
                    })
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Ваше местоположение",
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                    contentDescription = "Стрелка",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {navToMap()},
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_map),
                            contentDescription = "Переход на экран карты"
                        )
                    })
            }
            SearchRow()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            item{
                Spacer(modifier = Modifier.height(6.dp))
                MapFrame()
                Spacer(modifier = Modifier.height(6.dp))
            }
            item {
                HomeScreenImageCarousel()
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                WhereToGoCarousel()
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                ChooseAccordingToYourTaste(list = list.value, send = send)
            }
        }
    }

}


@Preview
@Composable
fun MapFrame() {
    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.map_frame),
            contentDescription = "Ваше изображение",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}



@Composable
fun ChooseAccordingToYourTaste(list: List<Restaurant>, send: (MainScreenEvent) -> Unit) {
    Column(
        modifier = Modifier.padding(top = 15.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = "Выбрать на свой вкус",
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        HomeScreenFilterCarousel()
        Spacer(modifier = Modifier.height(8.dp))
        RestaurantCardsInHomeScreen(list = list, send = send)
    }
}



@Composable
fun RestaurantCard(restaurant: Restaurant, send: (MainScreenEvent) -> Unit){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column{
            ImageCarousel(
                imageUrls = restaurant.pictures,
                restaurantId = restaurant.id,
                inCollection = restaurant.inCollection,
                send = send,
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


@Composable
fun RestaurantCardsInHomeScreen(list: List<Restaurant>, send: (MainScreenEvent) -> Unit) {
    Column {
        list.forEach { restaurant ->
            RestaurantCard(restaurant = restaurant, send = send)
        }
    }
}

@Composable
fun HomeScreenFilterCarousel() {

    val itemsList = listOf(
        "ULTIMA GUIDE",
        "Рядом со мной",
        "Завтраки",
        "Винотека",
        "Европейская",
        "Коктели",
        "Можно с собакой",
        "Веранда"
    )
    Row{
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            item{
                IconButton(
                    onClick = {},
                    colors = IconButtonColors(
                        Color(0xFFE2E2E2),
                        Color.Black,
                        Color(0xFFE2E2E2),
                        Color(0xFFE2E2E2)
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .height(38.dp),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_slot),
                            contentDescription = "Фильтр"
                        )
                    }
                )
            }
            items(itemsList) { item ->
                if (item == "ULTIMA GUIDE"){
                    HomeFilterImageButton(text = item, image = R.drawable.food)
                }
                else{
                    CategoryButtonCard(text = item, clickOnCategory = {})
                }
            }
        }
    }
}

@Composable
fun HomeFilterImageButton(text: String, image: Int) {

    TextButton(
        onClick = {},
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color(0xFFE2E2E2),
            contentColor = Color.Black
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
                painter = painterResource(id = image),
                contentDescription = "Your Icon",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
            Spacer(modifier = Modifier.width(4.dp))
            BasicText(
                text = text,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
            )
        }
    }
}





@Composable
fun SearchRow(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 2.dp, start = 15.dp, end = 15.dp),
        colors = CardDefaults.cardColors(Color(0xFFE2E2E2))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Your Icon",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
            Spacer(modifier = Modifier.width(6.dp))
            BasicText(
                text = "Найти в городе",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenImageCarousel(){
    val list = arrayOf(R.drawable.top_50_ultima, R.drawable.for_you_home, R.drawable.top_50_ultima, R.drawable.for_you_home)
    LazyRow(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
            .background(Color.Transparent)
    ) {
        itemsIndexed(list) { index, item ->

            if (index > 0) {
                Spacer(modifier = Modifier.width(6.dp))
            }
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                Image(
                    painter = painterResource(id = item),
                    contentDescription = "Фото места",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

val description = arrayOf("Фрески, шампань и русская печь: 5 ресторанов на Трёхгорной мануфактуре",
    "Топ-10 мест Москвы от Арины Журавлёвой",
    "15 dog-friendly ресторанов и кафе Москвы",
    "Латиноамериканская кухня в Москве: 6 ресторанов, где искать вкусы " +
            "другого континента",
    "Лавандовые поля, фермы, виноградники: 6 место для агротуризма в России")

@Preview
@Composable
fun WhereToGoCarousel(){
    val list = arrayOf(R.drawable.manufacture, R.drawable.arina, R.drawable.dog_friendly, R.drawable.latina, R.drawable.lavanda)

    Column(
        modifier = Modifier.padding(top = 16.dp, start = 10.dp, end = 10.dp)
    ){
        Text(
            text = "Куда сходить",
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp)
        LazyRow(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(top = 10.dp)
        ) {
            itemsIndexed(list) { index, item ->

                if (index > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
                ImageInHomeCarousel(description[index], list[index])
            }
        }
    }
}


@Composable
fun ImageInHomeCarousel(text: String, image: Int){
    Box(
        modifier = Modifier
            .width(205.dp)
            .height(270.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 2.dp),
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                lineHeight = 15.sp

            )
            HomeWithDotText()
        }
        Image(
            painter = painterResource(id = image),
            contentDescription = "Фото места",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }

}


@Preview
@Composable
fun HomeWithDotText() {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.W400, color = Color.White)) {
            append("Куда сходить")
        }
        append(" ")
        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Normal)) {
            append("•")
        }
        append(" ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.W400, color = Color.White)) {
            append("Места")
        }
        append("  ")
    }

    Text(
        modifier = Modifier,
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 8.sp
    )
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





