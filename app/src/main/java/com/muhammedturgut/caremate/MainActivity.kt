package com.muhammedturgut.caremate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammedturgut.caremate.ui.navigation.AppNavHost


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold {paddingValues->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)){

                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

                        val maxWidth = maxWidth
                        val maxHeight =maxHeight
                        val isTablet = maxWidth>600.dp

                        AppNavHost(
                            maxWidth =maxWidth,
                            maxHeight=maxHeight,
                            isTablet=isTablet)

                    }
                }
            }

        }
    }
}

