import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.core.R
import model.MainScreenEvent
import model.SaveInCollectionEvent

/**
 * A Composable function that displays
 * an image carousel with indicators and a button for saving items to a collection.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    imageUrls: List<String>,
    restaurantId: String,
    inCollection: Boolean,
    send: (MainScreenEvent) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val imageUrl = imageUrls[page]

            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
// Индикаторы в правом углу
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            IconButton(onClick = {
                send(
                    SaveInCollectionEvent(
                        "91c0a3ee-76b3-4fac-a0f6-247dd1cf5f75",
                        restaurantId
                    )
                )
            }, content = {
                Icon(
                    painter = if (!inCollection) painterResource(id = R.drawable.ic_favorite) else painterResource(
                        id = R.drawable.ic_favorite_on
                    ),
                    contentDescription = "Кнопка добавления в личные подборки",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White,
                )
            })
        }
        // Индикаторы в правом углу
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            for (index in imageUrls.indices) {
                val color = if (index == pagerState.currentPage) Color.White else Color.Gray
                Box(
                    modifier = Modifier
                        .size(if (index == pagerState.currentPage) 20.dp else 18.dp)
                        .padding(4.dp)
                        .background(color, shape = CircleShape)
                )
            }
        }
    }
}


@Composable
fun ImageCard(imageUrls: List<String>) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            ImageCarousel(imageUrls = imageUrls, "", true, {})
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewImageCard() {
    val sampleImages = remember {
        listOf(
            "https://pixy.org/src/0/7688.jpg",
            "https://avatanplus.com/files/resources/original/57a6de5284a4815663d4726f.jpg",
        )
    }

    MaterialTheme {
        ImageCard(imageUrls = sampleImages)
    }
}
