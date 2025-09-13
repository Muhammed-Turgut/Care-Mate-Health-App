package com.muhammedturgut.caremate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.muhammedturgut.caremate.ui.aiChat.AIChatPageScreen
import com.muhammedturgut.caremate.ui.start.SplashScreen
import com.muhammedturgut.caremate.ui.start.login.LogInScreen
import com.muhammedturgut.caremate.ui.start.onboardingScreens.OnBoardingScreenFinish
import com.muhammedturgut.caremate.ui.start.onboardingScreens.OnBoardingScreenSecond
import com.muhammedturgut.caremate.ui.start.onboardingScreens.OnBoardingScreenStart
import com.muhammedturgut.caremate.ui.start.signUpScreens.SignUpScreen
import com.muhammedturgut.caremate.ui.start.signUpScreens.SignUpScreenInfo


@Composable
fun AppNavHost( maxWidth: Dp,
                maxHeight: Dp,
                isTablet:Boolean){

    val navControllerAppHost = rememberNavController()

    NavHost(navController = navControllerAppHost,
        startDestination = "SplashScreen"){

        composable("SplashScreen"){

            SplashScreen(navControllerAppHost=navControllerAppHost)

        }


        composable("LogInScreen"){

            LogInScreen(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,
                navControllerAppHost=navControllerAppHost)

        }

        composable("SignUpScreen"){
            SignUpScreen(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,
                navControllerAppHost=navControllerAppHost)
        }

        composable("SignUpScreenInfo"){
            SignUpScreenInfo(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,
                navControllerAppHost=navControllerAppHost
            )
        }

        composable("OnBoardingScreenStart"){
            OnBoardingScreenStart(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,
                navControllerAppHost=navControllerAppHost
            )
        }

        composable("OnBoardingScreenSecond"){
            OnBoardingScreenSecond(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,
                navControllerAppHost=navControllerAppHost
            )
        }

        composable("OnBoardingScreenFinish"){
            OnBoardingScreenFinish(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,
                navControllerAppHost=navControllerAppHost
            )
        }

        composable("AIChatPageScreen"){
            AIChatPageScreen(navControllerAppHost = navControllerAppHost)
        }


        composable("NavBarHostScreen"){

            NavBarHostScreen(
                navControllerAppHost=navControllerAppHost,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet)

        }

        }

}