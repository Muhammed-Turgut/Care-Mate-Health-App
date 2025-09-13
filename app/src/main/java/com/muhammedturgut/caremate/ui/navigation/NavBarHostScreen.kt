package com.muhammedturgut.caremate.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.analysis.AnalyzePageScreen
import com.muhammedturgut.caremate.ui.diseases.DiseasesPageScreen
import com.muhammedturgut.caremate.ui.home.MainPageScreen
import com.muhammedturgut.caremate.ui.profile.ProfilePageScreen

@Composable
fun NavBarHostScreen(
    navControllerAppHost: NavController,
    maxWidth: Dp,
    maxHeight: Dp,
    isTablet: Boolean
) {
    var selected by remember { mutableStateOf("home") }
    val context = LocalContext.current
    val activity = context as? Activity

    BackHandler {



    }

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedItem = selected,
                onTapSelected = { selected = it },
                isTablet = isTablet
            )
        },
        contentWindowInsets = WindowInsets(0,0,0,0)

    ) { innerPadding ->
        // İçerik (BottomBar yüksekliği kadar padding ekleniyor)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 👈 Scaffold'un sağladığı boşluk
        ) {
            when (selected) {
                "chat" -> navControllerAppHost.navigate("AIChatPageScreen"){
                    popUpTo(navControllerAppHost.graph.id){
                        inclusive = true
                    }
                }

                "home" -> MainPageScreen(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    isTablet = isTablet
                )

                "health" -> DiseasesPageScreen()

                "analysis" -> AnalyzePageScreen(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    isTablet = isTablet
                )

                "profile" -> ProfilePageScreen()

                else -> MainPageScreen(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    isTablet = isTablet
                )
            }
        }
    }
}



@Composable
fun BottomBar(
    selectedItem: String,
    onTapSelected: (String) -> Unit,
    isTablet: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    start = if (isTablet) 24.dp else 16.dp,
                    end = if (isTablet) 24.dp else 16.dp,
                    top = if (isTablet) 4.dp else 4.dp,
                    bottom = if (isTablet) 4.dp else 4.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomItemList.forEach{ item ->
                BottomBarItem(
                    item = item,
                    isSelected = item.route.lowercase() == selectedItem.lowercase(),
                    isTablet = isTablet,
                    onClick = { onTapSelected(item.route.lowercase()) }
                )
            }

        }
    }
}

@Composable
private fun BottomBarItem(
    item: BottomBarItem,
    isSelected: Boolean,
    isTablet: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Generate butonu her zaman daha büyük olacak şekilde iconSize ayarla
    val iconSize = when (item.route.lowercase()) {
        "chat" -> if (isTablet) 64.dp else 52.dp
        else -> if (isTablet) 34.dp else 32.dp
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // Ripple efekti kapalı
                onClick = onClick // onClick burada olmalı
            )
            .padding(
                horizontal = if (isTablet) 16.dp else 12.dp,
                vertical = if (isTablet) 12.dp else 8.dp
            )
    ) {
        Image(
            painter = painterResource(
                id = if (isSelected) item.selectedIcon else item.defaultIcon
            ),
            contentDescription = item.route,
            modifier = Modifier.size(iconSize)
        )
    }

}


data class BottomBarItem(
    val route: String,
    val defaultIcon: Int,
    val selectedIcon: Int
)

val bottomItemList = listOf(

    BottomBarItem("home", R.drawable.home_default_icon, R.drawable.home_selected_icon),
    BottomBarItem("health", R.drawable.health_default_icon, R.drawable.health_selected_icon),
    BottomBarItem("chat", R.drawable.chat_icon, R.drawable.chat_icon),
    BottomBarItem("analysis", R.drawable.analysis_default_icon, R.drawable.analysis_selected_icon),
    BottomBarItem("profile", R.drawable.profile_default_icon, R.drawable.profile_selected_icon)
)