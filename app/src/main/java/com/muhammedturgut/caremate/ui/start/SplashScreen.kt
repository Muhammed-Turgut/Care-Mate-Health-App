package com.muhammedturgut.caremate.ui.start


import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.start.login.LoggedInViewModel
import com.muhammedturgut.caremate.ui.start.login.LoginCheckUiState
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold
import kotlinx.coroutines.delay
import javax.annotation.meta.When

@Composable
fun SplashScreen(
    navControllerAppHost: NavController,
    loggedInViewModel: LoggedInViewModel = hiltViewModel()
) {

    val isLoaded by remember { mutableStateOf(false) }
    val uiState by loggedInViewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when(val state = uiState){
            is LoginCheckUiState.Loading ->{

            }
            is LoginCheckUiState.Success ->{
                delay(3000)
                if (state.isLoggedIn){
                    navControllerAppHost.navigate("NavBarHostScreen"){
                        popUpTo(navControllerAppHost.graph.id){
                            inclusive = true
                        }
                    }
                }
                else{
                    navControllerAppHost.navigate("LogInScreen"){
                        popUpTo(navControllerAppHost.graph.id){
                            inclusive = true
                        }
                    }
                }
            }
            is LoginCheckUiState.Error ->{
                delay(3000)
                navControllerAppHost.navigate("LogInScreen"){
                    popUpTo(navControllerAppHost.graph.id){
                        inclusive = true
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {


        val context = LocalContext.current
        val activity = context as? Activity

        BackHandler {

            activity?.finish()

        }



        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (logo, text, image) = createRefs()


            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.constrainAs(logo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom, margin = 54.dp)

                }.size(232.dp, 90.dp)
            )

            Text(
                text = "For a healthier life",
                fontSize = 16.sp,
                fontFamily = PoppinSemiBold,
                color = Color(0xFF70A056),
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(logo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(image.top)
                })

            Image(
                painter = painterResource(R.drawable.image_splash_screen),
                contentDescription = null,
                modifier = Modifier.constrainAs(image) {

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                }.size(312.dp, 273.dp)
            )

        }
    }
}


