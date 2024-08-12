package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.core.R

@Composable
fun IndicatorFavorite(onFavoriteClick: () -> Unit) {
    Badge(
        modifier = Modifier
            .size(24.dp)
            .clickable(onClick = onFavoriteClick),
        containerColor = Color.White,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .clip(RectangleShape),
                tint = Color.Black,
            )
        }
    )
}