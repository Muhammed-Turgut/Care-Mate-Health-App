package com.muhammedturgut.caremate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.muhammedturgut.caremate.ui.aiChat.AIChatPageScreen
import com.muhammedturgut.caremate.ui.analysis.AnalysisViewModel
import com.muhammedturgut.caremate.ui.analysis.PositionAnalysisScreen
import com.muhammedturgut.caremate.ui.analysis.PostureCameraScreen
import com.muhammedturgut.caremate.ui.start.SplashScreen
import com.muhammedturgut.caremate.ui.start.login.LogInScreen
import com.muhammedturgut.caremate.ui.start.onboardingScreens.OnBoardingScreenFinish
import com.muhammedturgut.caremate.ui.start.onboardingScreens.OnBoardingScreenSecond
import com.muhammedturgut.caremate.ui.start.onboardingScreens.OnBoardingScreenStart
import com.muhammedturgut.caremate.ui.start.signUpScreens.SignUpScreen


@Composable
fun AppNavHost( maxWidth: Dp,
                maxHeight: Dp,
                isTablet:Boolean){

    val navControllerAppHost = rememberNavController()

    // POSTÜR ANALİZ İÇİN PAYLAŞILAN VIEWMODEL - NavHost seviyesinde oluştur
    val sharedAnalysisViewModel: AnalysisViewModel = hiltViewModel()

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
                navControllerAppHost=navControllerAppHost,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet,)
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

        // POSTÜR KAMERA EKRANI - Paylaşılan ViewModel kullan
        composable("PostureCameraScreen"){
            PostureCameraScreen(
                analysisViewModel = sharedAnalysisViewModel, // Paylaşılan ViewModel
                navControllerAppHost = navControllerAppHost,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet = isTablet,
            )
        }

        // POSTÜR ANALİZ SONUÇ EKRANI - Aynı ViewModel instance'ı kullan
        composable("PostureAnalysisResultScreen"){
            PositionAnalysisScreen(
                analysisViewModel = sharedAnalysisViewModel, // Aynı ViewModel
                navControllerAppHost = navControllerAppHost
            )
        }

        composable("NavBarHostScreen"){
            NavBarHostScreen(
                navControllerAppHost=navControllerAppHost,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet=isTablet)
        }

        // Eski route - geriye dönük uyumluluk için (eğer başka yerlerden kullanılıyorsa)
        composable("PositionAnalysisScreen/{image}"){ backStackEntry ->
            val image = backStackEntry.arguments?.getString("image")
            PositionAnalysisScreen(navControllerAppHost=navControllerAppHost)
        }
    }
}