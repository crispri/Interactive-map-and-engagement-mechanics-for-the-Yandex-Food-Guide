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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R

@Composable
fun BigCard(id: Int) {
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
                painter = painterResource(id = id),
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
                    text = "Kalabasa",
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
                        text = "4.5",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "До 23:00 м. Тверская 12мин",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 1.5.dp)
            )

            Text(
                text = "Уютное атмосферное место. Сюда идут за десертами. Доброжелательное обслуживание",
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