package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
    onInfoClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .width(216.dp)
                .height(90.dp)

        ) {

            Image(
                painter = imagePainter,
                contentDescription = "Фон",
                modifier = Modifier.wrapContentSize(unbounded = true, align = Alignment.Center)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp,
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally).padding(start = 18.dp, end = 18.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp,
                )
            }

            IconButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .align(Alignment.BottomStart),
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
                    .align(Alignment.BottomEnd),
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


@Preview(showBackground = true)
@Composable
fun CardWithImageAndTextPreview() {
    CardWithImageAndText(
        painterResource(id = R.drawable.hardcode_picture_of_cafe),
        "Kalabasa ",
        "Куда сходить чтобы вкусно поесть даже очень вкусно прям вау",
        {},
        {}
    )
}