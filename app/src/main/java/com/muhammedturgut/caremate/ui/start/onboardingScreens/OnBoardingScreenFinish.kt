package com.muhammedturgut.caremate.ui.start.onboardingScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold

@Composable
fun OnBoardingScreenFinish( maxWidth: Dp,
                            maxHeight: Dp,
                            isTablet:Boolean,
                            navControllerAppHost: NavController){

    val contentMaxWidth = if (maxWidth < 600.dp) maxWidth else 600.dp

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFFFFFF))){

        BackHandler {
            navControllerAppHost.navigate("OnBoardingScreenSecond"){
                popUpTo(navControllerAppHost.graph.id){
                    inclusive = true
                }
            }
        }

        ConstraintLayout(modifier = Modifier.fillMaxSize()){
            val (image,blueBlurEffect,greenBluerEffect, skip,title,text, btn, back, see) = createRefs()

            Image(painter = painterResource(R.drawable.green_ellipse_blur_efect),
                contentDescription = null,
                modifier = Modifier.constrainAs(greenBluerEffect) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                })


            Image(painter = painterResource(R.drawable.blue_ellipse_blur_efect),
                contentDescription = null,
                modifier = Modifier.constrainAs(blueBlurEffect) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

            Text(text="Skip",
                fontSize = 20.sp,
                fontFamily = PoppinSemiBold,
                color = Color(0xFFB7B7B7),
                modifier = Modifier
                    .clickable(onClick = {
                        navControllerAppHost.navigate("NavBarHostScreen"){
                            popUpTo(navControllerAppHost.graph.id){
                                inclusive = true
                            }
                        }
                    })
                    .constrainAs(skip){
                    end.linkTo(parent.end,  margin = 16.dp)
                    top.linkTo(parent.top, margin = 16.dp)
                }
            )



            Image(painter = painterResource(R.drawable.on_boarding_screen_third_image),
                contentDescription = null,
                modifier = Modifier.size(300.dp,370.dp)
                    .constrainAs(image){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 64.dp)
                    })



            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(see){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(image.bottom, margin = 12.dp)

                    }
            ) {

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEAEAEA))
                )

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEAEAEA))
                )

                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF70A056)) // yeşil
                )
            }

            Text(text ="Your Personal Health\nGuide",
                textAlign = TextAlign.Center,
                fontFamily = PoppinBold,
                color = Color.Black,
                fontSize = 28.sp,
                lineHeight = 28.sp,
                modifier = Modifier.constrainAs(title){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(see.bottom)
                }
            )


            Text(text ="CareMate keeps you informed\nand alerts you when you need to\nsee your doctor.",
                fontFamily = PoppinMedium,
                color = Color(0xFF70A056),
                fontSize = 18.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(text){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(title.bottom)
                }
            )


            Button(
                onClick = {
                    navControllerAppHost.navigate("NavBarHostScreen"){
                        popUpTo(navControllerAppHost.graph.id){
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .constrainAs(btn) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(text.bottom, margin = 20.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = min(maxWidth * 0.1f, 82.dp)) // responsive yatay padding
                    .heightIn(min = max(maxHeight * 0.06f, 48.dp), max = max(maxHeight * 0.08f, 32.dp)) // responsive yükseklik
                    .widthIn(max = contentMaxWidth)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FA5E3))
            ) {
                Text(
                    text = "Start",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = PoppinBold
                )
            }

            Text(text="back",
                fontFamily = PoppinSemiBold,
                color = Color(0xFFB7B7B7),
                fontSize = 18.sp,
                modifier = Modifier
                    .clickable(onClick = {
                        navControllerAppHost.navigate("OnBoardingScreenSecond"){
                            popUpTo(navControllerAppHost.graph.id){
                                inclusive = true
                            }
                        }
                    })
                    .constrainAs(back){

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(btn.bottom, margin = 18.dp)
                })

        }

    }

}
