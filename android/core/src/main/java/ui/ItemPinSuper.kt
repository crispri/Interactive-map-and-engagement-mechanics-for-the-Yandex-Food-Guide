package ui

import android.content.Context
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R

fun createViewSuperPinCard(context: Context): View {
    return ComposeView(context).apply {
        setContent {
            SuperPinCard(
                image = painterResource(id = R.drawable.open_kitchen_logo),
                title = "TitleTitleTitleTitle",
                raiting = "4,9",
                details = "кофе от 180Р.",
                isFavorite = true,
                onFavoriteClick = { /* Обработка клика */ }
            )
        }
    }
}

@Composable
fun SuperPinCard(
    image: Painter,
    title: String,
    raiting: String,
    details: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .defaultMinSize(minWidth = 84.dp, minHeight = 32.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
//        if (isFavorite) {
//            IconButton(
//                onClick = onFavoriteClick,
//                modifier = Modifier
//                    .size(24.dp)
//                    .align(Alignment.End)
//                    .padding(),
//                content = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_favorite),
//                        contentDescription = null,
//                        modifier = Modifier.size(12.dp)
//                    )
//                }
//            )
//        }
        Row(
            modifier = Modifier
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Row {
                    Text(
                        text = if (title.length > 19) {
                            title.take(19) + "..."
                        } else {
                            title
                        },
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_raiting),
                        contentDescription = "Рейтинг заведения",
                        Modifier
                            .padding(start = 4.dp)
                            .size(12.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = raiting,
                        fontWeight = FontWeight.Bold
                    )

                }
                Text(
                    text = details,
                )
            }

        }
    }
    if (isFavorite) {
        IndicatorFavorite(onFavoriteClick)
    }
}

@Preview
@Composable
fun PreviewSuperPinCard() {
    SuperPinCard(
        image = painterResource(id = R.drawable.open_kitchen_logo),
        title = "TitleTitleTitleTitle",
        raiting = "4,9",
        details = "кофе от 180Р.",
        isFavorite = true,
        onFavoriteClick = { /* Обработка клика */ }
    )
}