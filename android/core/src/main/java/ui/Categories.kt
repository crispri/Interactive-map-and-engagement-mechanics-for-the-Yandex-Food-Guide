package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R


@Composable
fun CategoriesButton() {
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
                /*                .padding(vertical = 4.dp)*/
                .clip(RoundedCornerShape(16.dp))
                .height(32.dp),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_slot),
                    contentDescription = "Фильтр"
                )
            }
        )

        LazyRow {
            items(itemsList) { item ->
                CategoryButtonCard(text = item) {}
            }
        }
    }
}

@Composable
fun CategoryButtonCard(text: String, clickOnCategory: () -> Unit) {
    TextButton(
        onClick = clickOnCategory,
        colors = ButtonColors(Color.LightGray, Color.Black, Color.LightGray, Color.LightGray),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(32.dp),
    ) {
        BasicText(
            text = text,
            style = androidx.compose.ui.text.TextStyle(fontSize = 11.sp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CategoriesButtonPreview() {
    CategoriesButton()
}