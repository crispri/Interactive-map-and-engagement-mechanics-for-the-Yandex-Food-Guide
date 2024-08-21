package ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R

@Composable
fun CardWithImageAndText(
    imagePainter: Painter,
    text: String,
    description: String,
    size: Int,
    onInfoClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBlackText: Boolean = false,
    isUltima: Boolean = false,
    isSelected: Boolean = false,
) {

    val alpha = if (isBlackText) 0f else 0.3f

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .animateContentSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            if (isUltima) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Фон",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
            } else {
                Image(
                    painter = imagePainter,
                    contentDescription = "Фон",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = alpha))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                if (isSelected && !isBlackText) {
                    Text(
                        text = "Сохранено $size мест",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                    )
                }
                Text(
                    text = text,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isBlackText) Color.Black else Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 18.dp, end = 18.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp,
                )
                if (isBlackText) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isBlackText) Color.Black else Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 18.dp, end = 18.dp),
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                    )
                }
            }
            if (!isUltima && isSelected) {
                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info),
                            contentDescription = "Информация",
                            tint = Color.White,
                        )
                    }
                )

                IconButton(onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Добавить в избранное",
                            tint = Color.White,
                            modifier = Modifier
                                .size(16.dp),
                        )
                    })
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun CardWithImageAndTextPreview() {
    CardWithImageAndText(
        painterResource(id = R.drawable.hardcode_picture_of_cafe),
        "Kalabasa ",
        "Куда сходить чтобы вкусно поесть даже очень вкусно прям вау",
        15,
        {},
        {},
        onClick = {}
    )
}