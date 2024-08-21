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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R


@Composable
fun CategoriesButton() {
    val itemsList = listOf(
        "Музыка громче",
        "Завтрак",
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
                Color(0xFFE2E2E2),
                Color.Black,
                Color.LightGray,
                Color.LightGray
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
        colors = ButtonColors(Color(0xFFE2E2E2), Color.Black, Color.LightGray, Color.LightGray),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(38.dp),
    ) {
        BasicText(
            text = text,
            style = androidx.compose.ui.text.TextStyle(fontSize = 11.sp, fontWeight = FontWeight.W400)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CategoriesButtonPreview() {
    CategoriesButton()
}