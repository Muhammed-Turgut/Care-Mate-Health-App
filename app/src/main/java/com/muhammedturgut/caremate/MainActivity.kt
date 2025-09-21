package com.muhammedturgut.caremate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.muhammedturgut.caremate.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                Scaffold {paddingValues->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF8F8F8))
                        .padding(paddingValues)){

                        //Google gamini ai = AIzaSyABnWyx8V4HkqOYnXaG3xyG-cDDQhgsy70 api key
                        //Google Gemini ai Chat Bot = AIzaSyBdMF6Bw7fwKCSBX8VV-oZdz8F_MUD1kzE



                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

                            val maxWidth = maxWidth
                            val maxHeight =maxHeight
                            val isTablet = maxWidth>600.dp
                            val navController = rememberNavController()

                           // PositionAnalysisScreen(navController)

                            AppNavHost(
                                maxWidth =maxWidth,
                                maxHeight=maxHeight,
                                isTablet=isTablet)

                        }



                    }
                }

            }
        }catch (e: Exception){
            Log.d("MainActivity",e.message.toString())
        }

    }
}

