package com.muhammedturgut.caremate.ui.start.signUpScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold
import com.muhammedturgut.caremate.viewModel.UserViewModel
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun SignUpScreen(
    navControllerAppHost: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    maxWidth: Dp,
    maxHeight: Dp,
    isTablet: Boolean
) {

    var email by remember { mutableStateOf("") }
    var nameAndSureName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    val context = LocalContext.current
    var shouldRegister by remember { mutableStateOf(false) }
    var userId by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var screen by remember { mutableStateOf(1) }
    var hasNavigated by remember { mutableStateOf(false) } // Navigation kontrolü için

    Box(modifier = Modifier.fillMaxSize()) {

        val uiState by registerViewModel.uiState.collectAsState()

        // Kayıt işlemini kontrol et - Güvenli null check
        LaunchedEffect(shouldRegister) {
            if (shouldRegister) {
                Log.d("SignUpDebug", "shouldRegister: $shouldRegister")
                Log.d("SignUpDebug", "nameAndSureName: '$nameAndSureName'")
                Log.d("SignUpDebug", "email: '$email'")
                Log.d("SignUpDebug", "password: '$password'")
                Log.d("SignUpDebug", "job: '$job'")
                Log.d("SignUpDebug", "dateOfBirth: '$dateOfBirth'")
                Log.d("SignUpDebug", "gender: '$gender'")

                // Tüm alanların dolu olup olmadığını kontrol et
                if (nameAndSureName.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    job.isNotBlank() &&
                    dateOfBirth.isNotBlank() &&
                    gender.isNotBlank()
                ) {
                    try {
                        userId = UUID.randomUUID().toString().replace("-", "")
                        Log.d("SignUpDebug", "Kayıt işlemi başlatılıyor - userId: $userId")
                        registerViewModel.register(
                            email = email,
                            password = password,
                            userId = userId,
                            userName = nameAndSureName
                        )
                        shouldRegister = false // Tekrar tetiklenmemesi için
                        showMessage = true
                    } catch (e: Exception) {
                        Log.e("SignUpDebug", "Kayıt işlemi sırasında hata: ${e.message}")
                        shouldRegister = false
                        showMessage = false
                    }
                } else {
                    Log.d("SignUpDebug", "Kayıt işlemi başlatılamıyor - eksik alanlar var")
                    shouldRegister = false


                    Toast.makeText(context, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // UI State değişikliklerini dinle
        LaunchedEffect(uiState, hasNavigated) {
            when (uiState) {
                is RegisterUiState.Success -> {
                    if (!hasNavigated) {
                        try {
                            hasNavigated = true
                            userViewModel.createUserData(
                                userId = userId,
                                email = email,
                                userName = nameAndSureName
                            )

                            screen = 3
                            delay(2000)

                            navControllerAppHost.navigate("OnBoardingScreenStart") {
                                popUpTo(navControllerAppHost.graph.id) {
                                    inclusive = true
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("SignUpDebug", "Navigation hatası: ${e.message}")
                            hasNavigated = false
                        }
                    }
                }

                is RegisterUiState.Error -> {
                    if (showMessage) {
                        Toast.makeText(
                            context,
                            (uiState as RegisterUiState.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                        showMessage = false
                    }
                }

                else -> Unit
            }
        }

        // Mesaj gösterimi - Güvenli kontrol
        when {
            showMessage && uiState is RegisterUiState.Loading -> {
                PrivateMessage("Registration in progress...", R.drawable.check_circle_icon)
            }
            showMessage && uiState is RegisterUiState.Error -> {
                PrivateMessage("Registration failed", R.drawable.error_icon)
            }
        }

        // Screen navigation
        when (screen) {
            1 -> SignUpStartScreen(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet = isTablet,
                navControllerAppHost = navControllerAppHost,
                name = { nameAndSureName = it },
                password = { password = it },
                email = { email = it },
                screen = { screen = it }
            )

            2 -> SignUpScreenInfo(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet = isTablet,
                navControllerAppHost = navControllerAppHost,
                job = { job = it },
                gender = { gender = it },
                dateOfBirth = { dateOfBirth = it },
                registrationCheck = { shouldRegister = it },
                screen = { screen = it }
            )

            3->LoadingScreen()

            else -> SignUpStartScreen(
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                isTablet = isTablet,
                navControllerAppHost = navControllerAppHost,
                name = { nameAndSureName = it },
                password = { password = it },
                email = { email = it },
                screen = { screen = it }
            )
        }
    }
}

@Composable
private fun PrivateMessage(text: String, image: Int) {
    Box(
        modifier = Modifier
            .size(280.dp, 44.dp)
            .border(0.5.dp, Color(0xFFA2A2A2), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp, 24.dp)
            )

            Text(
                text = text,
                fontSize = 14.sp,
                fontFamily = PoppinSemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation(
            modifier = Modifier.size(150.dp) // boyut ayarlayabilirsin
        )
    }
}


@Composable
fun LoadingAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Sonsuz döngü için
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}