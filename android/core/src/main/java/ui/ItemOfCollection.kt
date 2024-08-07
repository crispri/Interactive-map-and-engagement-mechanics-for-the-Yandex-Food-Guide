package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import model.Restaurant

@Composable
fun BigCard(restaurant: Restaurant) {
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .background(Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hardcode_picture_of_cafe),
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
                        painter = painterResource(id = R.drawable.ic_raiting),
                        modifier = Modifier.height(24.dp),
                        contentDescription = "Оценка"
                    )
                    Text(
                        text = restaurant.rating.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
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
                modifier = Modifier.padding(top = 8.dp)
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


@Composable
fun TextCard(text: String) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .background(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        BasicText(
            text = text,
            modifier = Modifier.padding(4.dp),
            style = androidx.compose.ui.text.TextStyle(fontSize = 11.sp)
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun BigCardPreview() {
//    BigCard()
//}