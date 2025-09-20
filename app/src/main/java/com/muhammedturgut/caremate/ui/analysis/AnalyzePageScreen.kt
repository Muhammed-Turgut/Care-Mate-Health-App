package com.muhammedturgut.caremate.ui.analysis


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
import com.muhammedturgut.caremate.data.local.room.RoomViewModel
import com.muhammedturgut.caremate.ui.start.login.LogInScreen
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinRegular
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold


@Composable
fun AnalyzePageScreen(maxWidth: Dp,
                      maxHeight: Dp,
                      isTablet:Boolean,
                      navControllerAppHost: NavController,
                      roomViewModel: RoomViewModel = hiltViewModel()

){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFAFAFA))) {

        val scrollState = rememberScrollState()
        val dailyUserData by roomViewModel.dailyUserData.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {

            Text(
                text = "Diseases",
                fontSize = 24.sp,
                fontFamily = PoppinBold,
                color = Color(0xFF70A056)
            )

            Spacer(modifier = Modifier.height(12.dp))


            dailyUserData?.let { daliy ->

                HealthAnalysis(dailyUserData = daliy)

            }





            Spacer(modifier = Modifier.height(12.dp))

            SearchBar()

            Spacer(modifier = Modifier.height(12.dp))

            AnalyzedDiseasesRow(maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,
                navControllerAppHost = navControllerAppHost)

        }
    }
}

@Composable
private fun HealthAnalysis(dailyUserData: DailyUserData){

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, Color(0xFFF3F1F1), RoundedCornerShape(16.dp))
        .background(Color.White)){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Health Analysis",
                fontFamily = PoppinSemiBold,
                fontSize = 20.sp,
                lineHeight = 20.sp,
                color = Color(0xFF70A056),
                modifier = Modifier.padding(top = 8.dp)
            )

            MobileSemiCircleProgressBar(45.5f)
            Spacer(modifier = Modifier.height(24.dp))
            AnalysisResults(dailyUserData)

        }

    }

}

@Composable
private fun MobileSemiCircleProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 16.dp,
    size: Dp = 200.dp,
    backgroundColor: Color = Color(0xFFE0E0E0),
    progressColor: Color = Color(0xFF70A056),
    showPercentage: Boolean = true,
    textSize: TextUnit = 18.sp
) {
    Box(
        modifier = modifier
            .size(width = size, height = size * 0.6f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.value * density
            val canvasHeight = (size.value * 0.6f) * density
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (canvasWidth - strokeWidthPx) / 2
            val center = Offset(canvasWidth / 2, canvasHeight)

            // Arka plan yarım daire (180 derece)
            drawArc(
                color = backgroundColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidthPx,
                    cap = StrokeCap.Round
                ),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )

            // Progress yarım daire
            if (progress > 0) {
                drawArc(
                    color = progressColor,
                    startAngle = 180f,
                    sweepAngle = 180f * (progress / 100f),
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidthPx,
                        cap = StrokeCap.Round
                    ),
                    size = Size(radius * 2, radius * 2),
                    topLeft = Offset(center.x - radius, center.y - radius)
                )
            }
        }

        // Merkez yüzde metni
        if (showPercentage) {
            Column(modifier = Modifier.offset(y = (16).dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "risk",
                    fontFamily = PoppinSemiBold,
                    fontSize = 24.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier= Modifier.height(2.dp))

                Text(
                    text = "${progress.toInt()}",
                    fontSize = 36.sp,
                    lineHeight = 36.sp,
                    fontFamily = PoppinBold,
                    color = Color(0xFFFF6969),
                )
            }

        }
    }
}

@Composable
private fun AnalysisResults(dailyUserData: DailyUserData){
    Row (modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){

            Image(painter = painterResource(R.drawable.health_stress_icon),
                contentDescription = null,
                modifier = Modifier.size(36.dp))

            Spacer(modifier = Modifier.width(4.dp))

            Column {
                Text(text = "Stress",
                    fontFamily = PoppinSemiBold,
                    color = Color(0xFFC8C8C8),
                    lineHeight = 12.sp,
                    fontSize = 12.sp)

                Text(text = ""+"/100",
                    fontFamily = PoppinBold,
                    color = Color.Black,
                    lineHeight = 12.sp,
                    fontSize = 12.sp)
            }

        }

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){

            Image(painter = painterResource(R.drawable.sleep_icon),
                contentDescription = null,
                modifier = Modifier.size(36.dp))

            Spacer(modifier = Modifier.width(4.dp))

            Column {
                Text(text = "Sleep",
                    fontFamily = PoppinSemiBold,
                    color = Color(0xFFC8C8C8),
                    lineHeight = 12.sp,
                    fontSize = 12.sp)

                Text(text = "${dailyUserData.todaySleepDuration}"+" Hour",
                    fontFamily = PoppinBold,
                    color = Color.Black,
                    lineHeight = 12.sp,
                    fontSize = 12.sp)
            }

        }

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){

            Image(painter = painterResource(R.drawable.navigate_icon),
                contentDescription = null,
                modifier = Modifier.size(36.dp))

            Spacer(modifier = Modifier.width(4.dp))

            Column {
                Text(text = "Step",
                    fontFamily = PoppinSemiBold,
                    color = Color(0xFFC8C8C8),
                    lineHeight = 12.sp,
                    fontSize = 12.sp)

                Text(text = "${dailyUserData.numberOfStepsTakenDaily}",
                    fontFamily = PoppinBold,
                    color = Color.Black,
                    lineHeight = 12.sp,
                    fontSize = 12.sp)
            }

        }

    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "search test",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth()
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
private fun AnalyzedDiseasesRow(maxWidth: Dp,
                                maxHeight: Dp,
                                isTablet:Boolean,
                                navControllerAppHost: NavController){

    val contentMaxWidth = if (maxWidth < 600.dp) maxWidth else 600.dp

    Box(modifier = Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, Color(0xFFE7E4E4),RoundedCornerShape(16.dp))
        .height(212.dp)
        .background(Color.White)){

        Row (modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){

            Image(painter = painterResource(R.drawable.postur_analiz_image),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(122.dp,196.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Column(horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(end = 8.dp)) {

                Text(text = "Posture analysis",
                    fontFamily = PoppinSemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF4FA5E3)
                )

                Text(text = "Some imbalances were detected in her posture.",
                    fontFamily = PoppinMedium,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    color = Color.Black
                )


                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    color = Color(0xFFA0A0A0),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){

                    Box(modifier = Modifier
                        .size(6.dp,6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF70A056)))

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(text = "Head slightly tilted forward",
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontFamily = PoppinRegular,
                        color = Color.Black)

                }

                Spacer(modifier = Modifier.width(4.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){

                    Box(modifier = Modifier
                        .size(6.dp,6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF70A056)))

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(text = "Right shoulder is lower than left",
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontFamily = PoppinRegular,
                        color = Color.Black
                    )

                }

                Spacer(modifier = Modifier.width(4.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){

                    Box(modifier = Modifier
                        .size(6.dp,6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF70A056)))

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Lumbar curve (lordosis) is more than normal",
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontFamily = PoppinRegular,
                        color = Color.Black)

                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {

                        navControllerAppHost.navigate("PostureAnalysisResultScreen"){
                            popUpTo(navControllerAppHost.graph.id){
                                inclusive = true
                            }
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4FA5E3)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 0.dp
                    )
                ) {
                    Text(
                        text = "Exercise recommendations",
                        color = Color.White,
                        lineHeight = 10.sp,
                        fontSize = if (isTablet) 14.sp else 8.sp,
                        fontFamily = PoppinSemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        navControllerAppHost.navigate("PostureCameraScreen"){
                            popUpTo(navControllerAppHost.graph.id){
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color(0xFF4FA5E3)),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 0.dp
                    )
                ) {
                    Text(
                        text = "reanalyze",
                        color = Color(0xFF4FA5E3),
                        lineHeight = 10.sp,
                        fontSize = if (isTablet) 14.sp else 8.sp,
                        fontFamily = PoppinSemiBold
                    )
                }

            }

        }

    }
}
