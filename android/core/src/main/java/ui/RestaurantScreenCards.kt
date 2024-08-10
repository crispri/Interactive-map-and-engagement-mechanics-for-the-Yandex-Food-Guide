package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R

@Composable
fun ImageCarousel(listImages: List<Int>){
    val size = 240f
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ImageCard(id = listImages[0], size, false)
        }
        items(listImages.drop(1).chunked(2)) { pair ->
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pair.forEach { imageUrl ->
                    ImageCard(id = imageUrl, sizeY = (size - 10) / 2, true)
                }
            }
        }
    }
}



@Preview
@Composable
fun OpenKitchenCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.open_kitchen_logo),
                contentDescription = "Открытая кухня логотип",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp)
            ){
                Text(
                    text = "Редакция о месте",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Открытая кухня",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Image(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                        contentDescription = "smth",
                        colorFilter = ColorFilter.tint(Color.DarkGray)
                    )

                }

            }
        }
    }
}

@Composable
@Preview
fun YandexGPTCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.gpt_logo),
                contentDescription = "Открытая кухня логотип",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp)
                    .background(Color.Transparent)
            ){
                Text(
                    text = "YandexGPT о месте",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Создано нейросетью на основе отзывов",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Image(
                        modifier = Modifier.size(10.dp),
                        painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                        contentDescription = "smth",
                        colorFilter = ColorFilter.tint(Color.DarkGray)
                    )

                }

            }
        }
    }
}


@Composable
fun ImageCard(id: Int, sizeY: Float, cardSmall: Boolean) {
    val sizeX = (if (cardSmall) sizeY else (sizeY * 4 / 3))
    Box(
        modifier = Modifier
            .width(sizeX.dp)
            .height(sizeY.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        Image(
            painter = painterResource(id = id),
            contentDescription = "Фото места",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

val description = "Ресторан «William Bass» - это классический английский паб с камином и террасой, откуда открывается прекрасный вид на исторический центр Москвы. Здесь можно попробовать разнообразные сорта пива, в том числе «Гиннесс» и «Стаут», а также насладиться блюдами традиционной немецкой кухни, такими как рулька и штрудель. Посетители отмечают, что порции в ресторане большие, а цены демократичные."

@Preview
@Composable
fun AboutPlaceCard(isGPT: Boolean = true,
                   text:String = description
){
    val backgroundCard = if (isGPT) Color(0xFFF6F1FF) else Color.White
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(backgroundCard)
    ){
        Column {
            if (isGPT){
                YandexGPTCard()
            }
            else{
                OpenKitchenCard()
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 6.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
        ){
            Text(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                text = text,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400,
                style = TextStyle.Default,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview
@Composable
fun PlaceWidgetCard(name: String = "Ян Примус", note: Float = 4.7f, comments: Int = 2022){
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = name,
            fontSize = 36.sp,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            modifier = Modifier.padding(top = 8.dp)
        ){
            Row (
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                FeedbackCard(note = note, comments = comments)
                Column (
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    FavouriteCard()
                    ToWebCard()
                }
            }

        }
    }
}

@Preview
@Composable
fun FavouriteCard(){
    Card(
        modifier = Modifier
            .size(50.dp, 50.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                contentDescription = "Фото места",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.DarkGray),
                modifier = Modifier.size(30.dp, 30.dp)
            )
        }
    }
}


@Preview
@Composable
fun ToWebCard(){
    Card(
        modifier = Modifier
            .size(50.dp, 50.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_language_24),
                contentDescription = "Фото места",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.DarkGray),
                modifier = Modifier.size(30.dp, 30.dp)
            )
        }
    }
}

@Preview
@Composable
fun FeedbackCard(note: Float = 4.7f, comments: Int = 2022) {
    Card(
        modifier = Modifier
            .size(80.dp, 110.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                alignment = Alignment.Center,
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = "Фото места",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(24.dp, 24.dp)
            )
            Text(
                text = note.toString(),
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400,
                style = TextStyle.Default,
                lineHeight = 20.sp
            )
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = comments.toString(),
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400,
                style = TextStyle.Default,
                lineHeight = 20.sp
            )
            Text(
                text = "оценки",
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400,
                style = TextStyle.Default,
                lineHeight = 20.sp
            )
        }

    }
}

@Preview
@Composable
fun PlaceCard(isOpenTime: String = "Закрыто до 11:00", isOpen: Boolean = false, distance: Int = 704, time: Int = 3) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(6.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_location_on_24),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Row{
                    TextWithDotSeparator(isOpenTime, isOpen, distance, time)
                }
                Image(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(25.dp)
                )
            }
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
                        text = "Маршрут",
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
                        .clip(RoundedCornerShape(1)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = "Позвонить",
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
fun TextWithDotSeparator(isOpenTime: String = "Закрыто до 11:00", isOpen: Boolean = true, distance: Int = 704, time: Int = 3) {
    val text = buildAnnotatedString {
        if (isOpen){
            withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                append(isOpenTime)
            }
        }
        else{
            withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Normal)) {
                append(isOpenTime)
            }
        }
        append("  ")
        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
            append("•")
        }
        append("  ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
            append("$distance м")
        }
        append("  ")
        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
            append("•")
        }
        append("  ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
            append("$time мин на машине")
        }
    }

    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )
}


@Composable
fun TopCard(name: String = "Ян Примус", navToBack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .shadow(6.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                modifier = Modifier.size(25.dp).clickable { navToBack() }
            )
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold
            )
            Image(
                painter = painterResource(id = R.drawable.baseline_ios_share_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(25.dp)
            )
        }
    }
}