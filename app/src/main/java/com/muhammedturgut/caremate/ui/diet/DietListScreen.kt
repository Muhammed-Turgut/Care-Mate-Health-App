package com.muhammedturgut.caremate.ui.diet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.data.local.entity.DietItem
import com.muhammedturgut.caremate.data.local.room.RoomViewModel
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold

@Composable
fun DietListScreen(roomViewModel: RoomViewModel = hiltViewModel(),
                   navControllerAppHost: NavController){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFAFAFA))){

        val dietItems by roomViewModel.dietItems.collectAsState()


        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

            BackHandler {
                navControllerAppHost.navigate("NavBarHostScreen"){
                    popUpTo(navControllerAppHost.graph.id){

                        inclusive = true

                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(Color(0xFF70A056))
                        .padding(horizontal = 16.dp, vertical = 8.dp), // dış padding
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Sol ok butonu
                    Box(
                        modifier = Modifier
                            .size(32.dp) // daha büyük bir daire
                            .clip(CircleShape)
                            .clickable(onClick = {
                                navControllerAppHost.navigate("NavBarHostScreen"){
                                    popUpTo(navControllerAppHost.graph.id){

                                        inclusive = true

                                    }
                                }
                            })
                            .background(Color.White),
                        contentAlignment = Alignment.Center // ikonu ortala
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chevron_left_icon),
                            tint = Color(0xFF70A056),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp) // ikon boyutu dairesinden küçük
                        )
                    }

                    // Başlık
                    Text(
                        text = "Today",
                        fontFamily = PoppinSemiBold,
                        fontSize = 24.sp,
                        color = Color.White
                    )

                    // Sağ kapatma butonu
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable(onClick = {
                                navControllerAppHost.navigate("NavBarHostScreen"){
                                    popUpTo(navControllerAppHost.graph.id){

                                        inclusive = true

                                    }
                                }
                            }
                            ).background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cancel_icon),
                            tint = Color(0xFF70A056),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "1200",
                            fontFamily = PoppinBold,
                            fontSize = 20.sp,
                            color = Color(0xFFF5A857)
                        )

                        Text(
                            text = "Tamamlanan",
                            fontFamily = PoppinMedium,
                            fontSize = 10.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    MobileSemiCircleProgressBar(0.45f)

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "2800",
                            fontFamily = PoppinBold,
                            fontSize = 20.sp,
                            color = Color(0xFFF5A857)
                        )

                        Text(
                            text = "Hedef",
                            fontFamily = PoppinMedium,
                            fontSize = 10.sp,
                            color = Color(0xFF666666)
                        )
                    }

                }

                Spacer(modifier = Modifier.height(56.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(R.drawable.chevron_left_icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "Sebzeli Balık",
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp,
                            color = Color.Black,
                        )

                        Text(
                            "1250 kcal",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        painter = painterResource(R.drawable.chevron_right_icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                }

                Spacer(modifier = Modifier.height(24.dp))


                Row(
                    modifier = Modifier
                        .padding(horizontal = 48.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(R.drawable.sunrise_icon),
                            contentDescription = null,
                            modifier = Modifier.size(42.dp)
                        )

                        Text(
                            text = "Morning",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFFF18E35)
                        )

                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(R.drawable.sun_icon),
                            contentDescription = null,
                            modifier = Modifier.size(42.dp)
                        )

                        Text(
                            text = "afternoon",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFFE7E7E7)
                        )

                    }


                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.sleep_icon),
                            contentDescription = null,
                            tint = Color(0xFFE7E7E7),
                            modifier = Modifier.size(42.dp)
                        )

                        Text(
                            text = "Evening",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFFE7E7E7)
                        )

                    }

                }

                Spacer(modifier = Modifier.height(24.dp))


            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)){

                items(dietItems){ item->
                    DietListRow(item)
                    Spacer(modifier = Modifier.height(16.dp))

                }

            }



        }

    }

}





@Composable
private fun DietListRow(dietItems: DietItem){

    Column(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        Row(modifier = Modifier.fillMaxWidth()
            .clip(CircleShape)
            .background(Color(0xFF70A056))
            .padding(horizontal = 16.dp, vertical = 4.dp)){

            Text(text = "${dietItems.day}",
                fontSize = 16.sp,
                fontFamily = PoppinSemiBold,
                color = Color.White,)
        }


        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE4E4E4),RoundedCornerShape(12.dp))
            .background(Color.White),
            contentAlignment = Alignment.Center){

            Column(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)){

                Text(text = "Morning",
                    fontFamily = PoppinBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start){

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){

                        Text(text = "Calorie",
                            fontFamily = PoppinBold,
                            fontSize = 12.sp,
                            color = Color(0xFF7B7878)
                        )

                        Text(text = "${dietItems.breakfastCalorie}",
                            fontFamily = PoppinBold,
                            fontSize = 20.sp,
                            color = Color(0xFFF18E35)
                        )

                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start){

                        Text(text = "${dietItems.breakfastOneFood}",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFFA4A4A4)
                        )

                        Text(text = "${dietItems.breakfastTwoFood}",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFFA4A4A4)
                        )
                    }

                }

            }

        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE4E4E4),RoundedCornerShape(12.dp))
            .background(Color.White),
            contentAlignment = Alignment.Center){

            Column(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)){

                Text(text = "Launch",
                    fontFamily = PoppinBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start){

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){

                        Text(text = "Calorie",
                            fontFamily = PoppinBold,
                            fontSize = 12.sp,
                            color = Color(0xFF7B7878)
                        )

                        Text(text = "${dietItems.lunchCalorie}",
                            fontFamily = PoppinBold,
                            fontSize = 20.sp,
                            color = Color(0xFFF18E35)
                        )

                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start){

                        Text(text = "${dietItems.lunchOneFood}",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFFA4A4A4)
                        )

                        Text(text = "${dietItems.lunchTwoFood}",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFFA4A4A4)
                        )
                    }

                }

            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE4E4E4),RoundedCornerShape(12.dp))
            .background(Color.White),
            contentAlignment = Alignment.Center){

            Column(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)){

                Text(text = "Evening Meal",
                    fontFamily = PoppinBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start){

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){

                        Text(text = "Calorie",
                            fontFamily = PoppinBold,
                            fontSize = 12.sp,
                            color = Color(0xFF7B7878)
                        )

                        Text(text = "${dietItems.eveningMealCalorie}",
                            fontFamily = PoppinBold,
                            fontSize = 20.sp,
                            color = Color(0xFFF18E35)
                        )

                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start){

                        Text(text = "${dietItems.eveningMealOneFood}",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFFA4A4A4)
                        )

                        Text(text = "${dietItems.eveningMealTwoFood}",
                            fontFamily = PoppinSemiBold,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFFA4A4A4)
                        )
                    }

                }

            }

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
            Column(modifier = Modifier.offset(y = (48).dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Image(painter = painterResource(R.drawable.fish_food_image),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Show(){

    val dietItems = DietItem(1,"Monday","250","Yulaf","adem sütü","350","Köfte","Makrna","560","Ispanak","Bulgur Pilavı")
    DietListRow(dietItems)

}