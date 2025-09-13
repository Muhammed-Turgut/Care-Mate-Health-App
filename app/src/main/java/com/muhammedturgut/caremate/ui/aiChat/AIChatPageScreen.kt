package com.muhammedturgut.caremate.ui.aiChat

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.theme.PoppinRegular
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold

@Composable
fun AIChatPageScreen(navControllerAppHost: NavController){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFAFAFA),
                        Color(0xFFFAFAFA),
                        Color(0xFF70A056),
                        Color(0xFF4FA5E3),
                        Color(0xFFFAFAFA),
                        Color(0xFFFAFAFA),
                    )
                )
            )
    ) {
        BackHandler {
            navControllerAppHost.navigate("NavBarHostScreen"){
                popUpTo(navControllerAppHost.graph.id){
                    inclusive = true
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top // SpaceBetween yerine Top
        ) {

            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Image(
                    painter = painterResource(R.drawable.arrow_back_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                        .clickable(onClick = {
                            navControllerAppHost.navigate("NavBarHostScreen"){
                                popUpTo(navControllerAppHost.graph.id){
                                    inclusive = true
                                }
                            }
                        })
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFF3F3F3))
                ) {

                    Text(
                        text = "Get Chatla",
                        fontFamily = PoppinSemiBold,
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF70A056),
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 4.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.plus_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 4.dp, bottom = 4.dp)
                            .size(20.dp)
                    )

                }

                Image(
                    painter = painterResource(R.drawable.choice_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

            }

            // Chat Messages Area - Expanding middle section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Bu kısım mevcut alanı doldurur
                    .padding(vertical = 16.dp)
            ) {
                // Burada chat mesajları gösterilecek
                // Örnek: LazyColumn ile mesaj listesi
            }

        }

        // Input Area - Bottom Fixed
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Alt kısımda sabit
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            var text by remember { mutableStateOf("") }

            Box(
                modifier = Modifier
                    .height(52.dp)
                    .width(320.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color(0xFFE4E3E3), CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.paperclip_icon),
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = text,
                        onValueChange = { it ->
                            text = it
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
                                    text = "head is aching",
                                    color = Color(0xFFC6C6C6),
                                    fontSize = 16.sp,
                                    fontFamily = PoppinRegular
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(R.drawable.send_icon),
                contentDescription = null,
                modifier = Modifier.size(54.dp)
            )

        }

    }
}


