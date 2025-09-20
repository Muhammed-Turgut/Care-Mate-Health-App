package com.muhammedturgut.caremate.ui.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.data.remote.GeminiAiViewModel
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinRegular
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold
import androidx.compose.runtime.getValue


@Composable
fun ProfilePageScreen(geminiAiViewModel : GeminiAiViewModel = hiltViewModel()){



    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFAFAFA))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            ProfileAboutCard()

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(text = "Settings",
                    color = Color(0xFF70A056),
                    fontSize = 20.sp,
                    fontFamily = PoppinBold,
                    modifier = Modifier.padding(start = 8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF3A90CE)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){


                    Image(painter = painterResource(R.drawable.edit_profile_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
                            .size(32.dp)
                            .clickable(onClick = {

                            }))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Edit profile name",
                        fontSize = 16.sp,
                        fontFamily = PoppinMedium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    Image(painter = painterResource(R.drawable.arrow_right_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .size(20.dp))

                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF3A90CE)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){


                    Image(painter = painterResource(R.drawable.drugs_profile_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
                            .size(32.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Drugs",
                        fontSize = 16.sp,
                        fontFamily = PoppinMedium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    Image(painter = painterResource(R.drawable.arrow_right_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .size(20.dp))

                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF3A90CE)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){


                    Image(painter = painterResource(R.drawable.password_profile_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
                            .size(32.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Change password",
                        fontSize = 16.sp,
                        fontFamily = PoppinMedium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    Image(painter = painterResource(R.drawable.arrow_right_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .size(20.dp))

                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF3A90CE)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){


                    Image(painter = painterResource(R.drawable.feedback_profile_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
                            .size(32.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Feedback",
                        fontSize = 16.sp,
                        fontFamily = PoppinMedium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    Image(painter = painterResource(R.drawable.arrow_right_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .size(20.dp))

                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF3A90CE)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){


                    Image(painter = painterResource(R.drawable.sign_out_profile_iconsvg),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 16.dp, bottom = 16.dp)
                            .size(32.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Log out",
                        fontSize = 16.sp,
                        fontFamily = PoppinMedium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    Image(painter = painterResource(R.drawable.arrow_right_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .size(20.dp))

                }
            }
        }
    }
}

@Composable

private fun ProfileAboutCard(){
    Box(modifier = Modifier.fillMaxWidth()
        .height(276.dp)
        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF4FA5E3), Color(0xFF2278B6))
            )
        )){

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            Text(text = "Profile",
                fontSize = 24.sp,
                fontFamily = PoppinBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Muhammed Turgut",
                fontSize = 24.sp,
                lineHeight = 24.sp,
                fontFamily = PoppinBold,
                color = Color.White
            )

            Text(text = "muhammedtur20@gmail.com",
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontFamily = PoppinRegular,
                color = Color(0xFF006AB7)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Image(painter = painterResource(R.drawable.profile_photo_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(84.dp)
                    .border(1.dp, Color.White, CircleShape))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween){

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(R.drawable.age_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(text = "Age",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = PoppinBold)

                        Text(text = "20 Years",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = PoppinSemiBold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically){

                    Image(painter = painterResource(R.drawable.check_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(text = "Final Check",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = PoppinBold)

                        Text(text = "06/11/2024",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = PoppinSemiBold)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically){

                    Image(painter = painterResource(R.drawable.health_profile_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(text = "Health",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = PoppinBold)

                        Text(text = "56/100",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = PoppinSemiBold)
                    }
                }



            }


        }

    }
}


@Preview(showBackground = true)
@Composable
private fun Show(){
    ProfilePageScreen()
}