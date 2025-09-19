package com.muhammedturgut.caremate.ui.home


import android.R.attr.maxWidth
import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.data.local.room.RoomViewModel
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinRegular
import kotlinx.coroutines.delay

@Composable
fun MainPageScreen(maxWidth: Dp,
                   maxHeight: Dp,
                   isTablet: Boolean,
                   navControllerAppHost: NavController,
                   roomViewModel: RoomViewModel = hiltViewModel()
){

    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFAFAFA))){


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .verticalScroll(scrollState)
            ) {

                HeaderBar()
                Spacer(modifier = Modifier.height(12.dp))
                SearchBar()

                Spacer(modifier = Modifier.height(12.dp))

                ReliefArea()

                Spacer(modifier = Modifier.height(12.dp))

                AIChat(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    isTablet = isTablet,
                    navControllerAppHost = navControllerAppHost
                )

                Spacer(modifier = Modifier.height(12.dp))

                PossibleDiseases(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    isTablet = isTablet
                )

                Spacer(modifier = Modifier.height(12.dp))

                NaturalTreatmentMethods()

        }

    }
}

@Composable
private fun HeaderBar(){
    Box(modifier = Modifier.fillMaxWidth()){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){

            Image(painter = painterResource(R.drawable.profile_photo_icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp))

            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)){

                Text(text="Welcome back!",
                    fontFamily = PoppinMedium,
                    fontSize = 14.sp,
                    color= Color(0xFF868686),
                    lineHeight = 14.sp
                )

                Text(text="Muhammed Turgut "+"\uD83D\uDC4B",
                    fontFamily = PoppinSemiBold,
                    fontSize = 16.sp,
                    color= Color.Black,
                    lineHeight = 16.sp
                )

            }

            Box(modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White)
                ,contentAlignment = Alignment.Center){

                Image(painter = painterResource(R.drawable.notification_icon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp))
            }

        }

    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search illness",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFCBCBCB), RoundedCornerShape(16.dp))
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.search_icon),
                contentDescription = "Search Icon",
                tint = Color(0xFF888888)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = text,
                onValueChange = { it->
                    text = it
                    onSearch(it)
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily.SansSerif // yerine PoppinSemiBold koyabilirsin
                ),
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Text(
                            text = hint,
                            color = Color(0xFFC6C6C6),
                            fontSize = 16.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun ReliefArea() {
    val reliefAreaImages = listOf(
        R.drawable.recommendations_sleep,
        R.drawable.recommendations_water,
        R.drawable.recommendations_stress
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { reliefAreaImages.size }
    )

    // 6 saniyede bir otomatik değişim
    LaunchedEffect(Unit) {
        while (true) {
            delay(6000) // 6 saniye bekle
            val nextPage = (pagerState.currentPage + 1) % reliefAreaImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center) {

        Text(text = "Recommendations",
            fontFamily = PoppinSemiBold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // Sabit yükseklik
            contentAlignment = Alignment.BottomCenter
        ) {

            // Image Carousel
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .width(380.dp)
                    .height(100.dp)
            ) { page ->
                Image(
                    painter = painterResource(reliefAreaImages[page]),
                    contentDescription = null,
                    modifier = Modifier
                        .width(380.dp)
                        .height(100.dp),
                    contentScale = ContentScale.Fit // Resmin oranını korur
                )
            }


            // Indicators
            Row(
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(reliefAreaImages.size) { index ->
                    val isSelected = pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .animateContentSize(
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing
                                )
                            )
                    )
                }
            }
        }
    }
}


@Composable
private fun AIChat(
    maxWidth: Dp,
    maxHeight: Dp,
    isTablet: Boolean,
    navControllerAppHost: NavController
) {
    val contentMaxWidth = if (maxWidth < 600.dp) maxWidth else 600.dp
    val imageSize = if (isTablet) 150.dp else 120.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,

    ) {
        Text(
            text = "AI Chat",
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontFamily = PoppinSemiBold,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFE7E4E4),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier

                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Is there a problem?",
                        fontSize = if (isTablet) 22.sp else 20.sp,
                        fontFamily = PoppinBold,
                        color = Color(0xFF4FA5E3)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Talk to AI about potential\nrisks",
                        fontSize = if (isTablet) 16.sp else 14.sp,
                        textAlign = TextAlign.Start,
                        fontFamily = PoppinSemiBold,
                        color = Color(0xFF6A859B),
                        lineHeight = (if (isTablet) 20.sp else 18.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            navControllerAppHost.navigate("AIChatPageScreen"){
                                popUpTo(navControllerAppHost.graph.id){
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier
                            .widthIn(
                                min = 200.dp,
                                max = min(contentMaxWidth * 0.6f, 200.dp)
                            )
                            .height(32.dp), // Sabit 32dp yükseklik
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF70A056)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 0.dp // Vertical padding'i sıfırla
                        )
                    ) {
                        Text(
                            text = "Start Chat",
                            color = Color.White,
                            fontSize = if (isTablet) 14.sp else 12.sp,
                            fontFamily = PoppinSemiBold
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.ai_chat_image),
                    contentDescription = "AI Chat illustration",
                    modifier = Modifier
                        .size(imageSize)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun PossibleDiseases(maxWidth: Dp,
                             maxHeight: Dp,
                             isTablet: Boolean){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,

        ) {
        Text(
            text = "PossibleDiseases",
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontFamily = PoppinSemiBold,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
        )

        LazyRow(modifier = Modifier.fillMaxWidth()){

            items(diseasesList) { iteam ->

                DiseasesRow(maxWidth = maxWidth,
                    maxHeight=maxHeight,
                    isTablet =isTablet,
                    item = iteam)

                Spacer(modifier = Modifier.width(8.dp))

            }
        }


    }
}

@Composable
fun DiseasesRow(maxWidth: Dp,
                maxHeight: Dp,
                isTablet: Boolean,
                item: DiseasesItem){

    val contentMaxWidth = if (maxWidth < 600.dp) maxWidth else 600.dp
    val imageSize = if (isTablet) 150.dp else 120.dp

    Box(modifier = Modifier
        .size(208.dp,116.dp)
        .background(Color.White)
        .clip(RoundedCornerShape(8.dp))
        .border(1.dp, Color(0xFFE7E4E4),RoundedCornerShape(8.dp))){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {

            Box(modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFF7AC7FF))
                .border(1.dp, Color(0xFFE7E4E4),RoundedCornerShape(30.dp))){

                Text(text = "${item.name}",
                    fontSize = 8.sp,
                    lineHeight = 8.sp,
                    color= Color.White,
                    fontFamily = PoppinSemiBold,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp , top = 2.dp, bottom = 2.dp))
            }

             Spacer(modifier = Modifier.height(4.dp))

            Text(text = "${item.illness}",
                 fontSize = 10.sp,
                 fontFamily = PoppinSemiBold,
                 color= Color.Black)

            Text(text = "${item.explanation}",
                fontSize = 8.sp,
                lineHeight = 8.sp,
                fontFamily = PoppinRegular,
                color= Color(0xFF4D4D4D)
            )
            Spacer(modifier = Modifier.height(4.dp))

            CustomProgressBar(item.progressBar)

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    // navControllerAppHost.navigate("NavBarHost")
                },
                modifier = Modifier
                    .widthIn(
                        min = 180.dp,
                        max = min(contentMaxWidth * 0.6f, 200.dp)
                    )
                    .height(20.dp), // Sabit 32dp yükseklik
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4FA5E3)
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 0.dp // Vertical padding'i sıfırla
                )
            ) {
                Text(
                    text = "Precautions that can be taken",
                    color = Color.White,
                    lineHeight = 8.sp,
                    fontSize = if (isTablet) 14.sp else 8.sp,
                    fontFamily = PoppinRegular
                )
            }


        }

    }
}

@Composable
fun CustomProgressBar(
    progress: Float, // 0.0f ile 1.0f arasında
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE7E4E4),
    progressColor: Color = Color(0xFF4FA5E3),
    height: Dp = 8.dp,
    cornerRadius: Dp = 4.dp,
    animationDuration: Int = 1000,
    showPercentage: Boolean = true
) {
    // Animasyonlu progress değeri
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "progress_animation"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f) // Kalan alanı kapla
                .height(height)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .background(
                        color = progressColor,
                        shape = RoundedCornerShape(cornerRadius)
                    )
            )
        }

        if (showPercentage) {
            Text(
                text = "%${(progress * 100).toInt()}",
                fontSize = 8.sp,
                fontFamily = PoppinSemiBold,
                color = Color.Black
            )
        }
    }
}


@Composable
private fun NaturalTreatmentMethods(){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,

        ) {
        Text(
            text = "Natural Treatment Methods",
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontFamily = PoppinSemiBold,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(138.dp)
            .border(1.dp,Color(0xFFE7E4E4),RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(brush = Brush.horizontalGradient(

                colors = listOf(Color(0xFFFFFFFF), Color(0xFF83BF4F)))

            )){

            Row(modifier = Modifier
                .fillMaxSize()){

                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment =Alignment.Start) {

                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFF18E35)),
                        contentAlignment = Alignment.Center){

                        Text(text = "Herbal Treatments",
                            fontSize = 10.sp,
                            fontFamily = PoppinSemiBold,
                            color= Color.White,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp))


                    }

                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF70A056)),
                        contentAlignment = Alignment.Center){

                        Text(text = "Acupuncture and Massage",
                            fontSize = 10.sp,
                            fontFamily = PoppinSemiBold,
                            color= Color.White,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp))


                    }


                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFF4631E)),
                        contentAlignment = Alignment.Center){

                        Text(text = "Aromaterapi",
                            fontSize = 10.sp,
                            fontFamily = PoppinSemiBold,
                            color= Color.White,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp))


                    }

                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF4FA5E3)),
                        contentAlignment = Alignment.Center){

                        Text(text = "Meditation and Breathing Techniques",
                            fontSize = 10.sp,
                            fontFamily = PoppinSemiBold,
                            color= Color.White,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp))


                    }

                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF309898)),
                        contentAlignment = Alignment.Center){

                        Text(text = "Yoga and Exercise",
                            fontSize = 10.sp,
                            fontFamily = PoppinSemiBold,
                            color= Color.White,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp))


                    }


                }

                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (image1, image2, image3) = createRefs()

                    // Üst-ortada
                    Image(
                        painter = painterResource(R.drawable.aromatherapy_svgrepo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .constrainAs(image1) {
                                top.linkTo(parent.top, margin = 12.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    // Sağ-altta
                    Image(
                        painter = painterResource(R.drawable.yoga_svgrepo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .constrainAs(image2) {
                                end.linkTo(parent.end, margin = 12.dp)
                                top.linkTo(image1.bottom)
                                bottom.linkTo(parent.bottom, margin = 32.dp)
                            }
                    )

                    // Sol-altta
                    Image(
                        painter = painterResource(R.drawable.massage_svgrepo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .constrainAs(image3) {
                                start.linkTo(parent.start, margin = 12.dp)
                                bottom.linkTo(parent.bottom, margin = 12.dp)
                                end.linkTo(image2.start)
                            }
                    )
                }


            }

        }


    }

}


data class DiseasesItem(val name:String, val illness:String, val explanation:String, val progressBar: Float)

val diseasesList = listOf<DiseasesItem>(
    DiseasesItem(
    name = "Orthopedic disease",
    illness = "Herniated disc",
    explanation = "Slow down the progression of the hernia with special exercises",
    progressBar = 0.4f),

    DiseasesItem(
        name = "Orthopedic disease",
        illness = "Herniated disc",
        explanation = "Slow down the progression of the hernia with special exercises",
        progressBar = 0.4f),

    DiseasesItem(
        name = "Orthopedic disease",
        illness = "Herniated disc",
        explanation = "Slow down the progression of the hernia with special exercises",
        progressBar = 0.4f),

    DiseasesItem(
        name = "Orthopedic disease",
        illness = "Herniated disc",
        explanation = "Slow down the progression of the hernia with special exercises",
        progressBar = 0.4f)
)



@SuppressLint("UnusedBoxWithConstraintsScope")
@Preview(showBackground = true)
@Composable
private fun Show(){
    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        val maxWidth = maxWidth
        val maxHeight = maxHeight
        val isTablet = maxWidth >600.dp

       // addDailyData(maxWidth,maxHeight,isTablet)
    }

}