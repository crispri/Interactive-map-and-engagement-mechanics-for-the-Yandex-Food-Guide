package ui

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


fun createViewNormalPinCard(context: Context): View {
    val composeView = ComposeView(context).apply {
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

    // Установите слушатель для ожидания завершения размещения
    composeView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            // Удалите слушатель после первого вызова
            composeView.viewTreeObserver.removeOnGlobalLayoutListener(this)

            // Теперь можно безопасно использовать composeView
            // Например, вы можете создать Bitmap или выполнить другие действия
        }
    })
    return composeView

}


@Composable
fun NormalPinCard(
    title: String,
    raiting: String,
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
        Row(
            modifier = Modifier
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(
                    text = if (title.length > 12) {
                        title.take(12) + "..."
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
        }
    }
    if (isFavorite) {
        IndicatorFavorite(onFavoriteClick)
    }
}


@Preview
@Composable
fun PreviewNormalPinCard() {
    NormalPinCard(
        title = "Name Name Name",
        raiting = "4,9",
        isFavorite = true,
        onFavoriteClick = { /* Обработка клика */ }
    )
}