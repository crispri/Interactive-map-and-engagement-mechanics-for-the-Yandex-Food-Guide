package presintation.restaurantScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.feature.R
import presintation.mapScreen.Carousel
import ui.AboutPlaceCard
import ui.ImageCarousel
import ui.PlaceCard
import ui.PlaceWidgetCard
import ui.TopCard
import kotlin.math.roundToInt



//моки:)
val description = "Ресторан «William Bass» - это классический английский паб с камином и террасой, откуда открывается прекрасный вид на исторический центр Москвы. Здесь можно попробовать разнообразные сорта пива, в том числе «Гиннесс» и «Стаут», а также насладиться блюдами традиционной немецкой кухни, такими как рулька и штрудель. Посетители отмечают, что порции в ресторане большие, а цены демократичные."

val listImages = listOf(
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe,
    R.drawable.hardcode_picture_of_cafe
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(
    navToBack: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWeight = configuration.screenWidthDp.dp


    val bottomSheetState = rememberBottomSheetScaffoldState(
        rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true,
        confirmValueChange = { newState ->
            newState != SheetValue.Hidden
        })
    )

    val offsetState = remember { mutableFloatStateOf(200f) }
    var isRequired = false
    var sheetHeight = 500f
    var isTopCardVisible by remember { mutableStateOf(false) }

    LaunchedEffect(bottomSheetState.bottomSheetState) {
        snapshotFlow { bottomSheetState.bottomSheetState.requireOffset() }
            .collect { offset ->
                offsetState.floatValue = offset
                if (!isRequired){
                    sheetHeight = offsetState.floatValue
                    isRequired = true
                }
                isTopCardVisible = offsetState.floatValue < sheetHeight - 200f
            }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = screenHeight / 3 * 2)
                        .background(Color.White)
                        .padding(bottom = 120.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Carousel()
                    Spacer(modifier = Modifier.height(16.dp))
                    ImageCarousel(listImages = listImages)
                    Spacer(modifier = Modifier.height(4.dp))
                    AboutPlaceCard(true)
                    Spacer(modifier = Modifier.height(16.dp))
                    ImageCarousel(listImages = listImages)
                }
            },
            sheetContainerColor = Color.White,
            sheetPeekHeight = screenHeight / 3 * 2,
            sheetShadowElevation = 4.dp,
            sheetDragHandle = null,

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rest),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .size(width = screenWeight, height = screenHeight / 2)
                )
            }
        }

        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(start = 30.dp)
                .offset(y = (-170).dp)
                .offset { IntOffset(0, offsetState.floatValue.roundToInt() - 16) }
        ) {
            AnimatedVisibility(
                visible = offsetState.floatValue > sheetHeight - 200f,
                enter = fadeIn(),
                exit = fadeOut()
            ){
                PlaceWidgetCard()
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            PlaceCard()
        }


        AnimatedVisibility(
            visible = offsetState.floatValue > sheetHeight - 200f,
            enter = fadeIn(),
            exit = fadeOut()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                FloatingActionButton(
                    modifier = Modifier.size(40.dp),
                    containerColor = Color.White,
                    onClick = {navToBack()},
                    shape = CircleShape,
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = "go_back",
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                FloatingActionButton(
                    modifier = Modifier.size(40.dp),
                    containerColor = Color.White,
                    onClick = {},
                    shape = CircleShape,
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.baseline_ios_share_24),
                        contentDescription = "share",
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
            }
        }


        if (isTopCardVisible){
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
            ){
                AnimatedVisibility(
                    visible = offsetState.floatValue < sheetHeight - 200f,
                    enter = fadeIn(),
                    exit = fadeOut()
                ){
                    TopCard(navToBack = navToBack)
                }
            }
        }
    }
}