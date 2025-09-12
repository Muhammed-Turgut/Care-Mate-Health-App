package com.muhammedturgut.caremate.ui.start


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold

@Composable
fun SplashScreen(){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){

        ConstraintLayout(modifier = Modifier.fillMaxSize()){

            val (logo,text,image) = createRefs()


            Image(painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.constrainAs(logo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom, margin = 54.dp)

                }.size(232.dp,90.dp))

            Text(text="For a healthier life",
                fontSize = 16.sp,
                fontFamily = PoppinSemiBold,
                color = Color(0xFF70A056),
                modifier = Modifier.constrainAs(text){
                    top.linkTo(logo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(image.top)
                })

            Image(painter = painterResource(R.drawable.image_splash_screen),
                contentDescription = null,
                modifier = Modifier.constrainAs(image) {

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                }.size(312.dp,273.dp))

        }
    }

}


@Preview(showBackground = true)
@Composable
private fun Show(){
    SplashScreen()
}