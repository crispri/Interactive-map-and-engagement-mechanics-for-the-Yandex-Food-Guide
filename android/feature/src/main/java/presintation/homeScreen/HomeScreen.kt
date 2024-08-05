package presintation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.feature1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navToMap: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
//                    Text(
//                        text = "Автозаводская 23Б, к2",
//                        color = Color.Gray,
//                        fontSize = 13.sp,
//                        // modifier = Modifier.align(Alignment.CenterHorizontally)
//                    )
                    Text(
                        text =
                        "Ваше местоположение >",
                    )
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
            Image(
                painter = painterResource(R.drawable.home_sc),
                contentDescription = "Хардкод домашний экран",
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )

        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen({})
}