package presintation.homeScreen

import android.widget.FrameLayout.inflate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.feature.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import pins.CustomPinView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navToMap: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        Text(
                            text = "Ваше местоположение",
                            fontSize = 20.sp,
                            color = Color.Black,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                            contentDescription = "Стрелка",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = navToMap,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_map),
                                contentDescription = "Переход на экран карты"
                            )
                        })
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Image(
                    painter = painterResource(R.drawable.home_sc), //home_sc),
                    contentDescription = "Хардкод домашний экран",
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen({})
}