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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.data.local.room.RoomViewModel
import com.muhammedturgut.caremate.ui.analysis.AnalyzePageScreen
import com.muhammedturgut.caremate.ui.diseases.DiseasesPageScreen
import com.muhammedturgut.caremate.ui.home.MainPageScreen
import com.muhammedturgut.caremate.ui.profile.ProfilePageScreen
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold

@Composable
fun NavBarHostScreen(
    navControllerAppHost: NavController,
    maxWidth: Dp,
    maxHeight: Dp,
    isTablet: Boolean,
    roomViewModel: RoomViewModel = hiltViewModel()
) {
    var selected by remember { mutableStateOf("home") }
    val context = LocalContext.current
    val activity = context as? Activity

    BackHandler {



    }
    var show by remember { mutableStateOf(true) }

    if (show) {
        addDailyData(
            maxWidth, maxHeight, isTablet, roomViewModel = roomViewModel,
            show = {
                show = it
            })
    } else {

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
                    "chat" -> navControllerAppHost.navigate("AIChatPageScreen") {
                        popUpTo(navControllerAppHost.graph.id) {
                            inclusive = true
                        }
                    }

                    "home" -> MainPageScreen(
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                        isTablet = isTablet,
                        navControllerAppHost = navControllerAppHost
                    )

                    "calori" -> DiseasesPageScreen()

                    "analysis" -> AnalyzePageScreen(
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                        isTablet = isTablet,
                        navControllerAppHost = navControllerAppHost
                    )

                    "profile" -> ProfilePageScreen()

                    else -> MainPageScreen(
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                        isTablet = isTablet,
                        navControllerAppHost = navControllerAppHost
                    )
                }
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

    @Composable
    fun addDailyData(maxWidth: Dp,
                     maxHeight: Dp,
                     isTablet: Boolean,
                     show : (Boolean) -> Unit,
                     roomViewModel: RoomViewModel){

        var textWater by remember { mutableStateOf("") }
        var textStep by remember { mutableStateOf("") }
        var textHowYou by remember { mutableStateOf("")}
        var selectedSleepIndex by remember { mutableStateOf(-1) }

        // Tüm alanların dolu olup olmadığını kontrol et
        val allFieldsFilled = textWater.isNotBlank() &&
                textStep.isNotBlank() &&
                textHowYou.isNotBlank() &&
                selectedSleepIndex != -1

        Box(modifier = Modifier.fillMaxSize().background(Color.Transparent),
            contentAlignment = Alignment.BottomCenter){

            Column(modifier = Modifier
                .fillMaxWidth()
                .height(if (isTablet) 850.dp else 800.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .verticalScroll(rememberScrollState())){

                ConstraintLayout(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)){

                    val (sleep, water, step, title, cancel, howYou, btn) = createRefs()

                    Box(modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {
                            show(false)
                        })
                        .background(Color(0xFF70A056))
                        .constrainAs(cancel){
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(parent.top, margin = 16.dp)
                        },
                        contentAlignment = Alignment.Center){

                        Image(painter = painterResource(R.drawable.cancel_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp))
                    }

                    Text(text = "Your Daily Data",
                        fontFamily = PoppinBold,
                        fontSize = 20.sp,
                        color = Color(0xFF4FA5E3),
                        modifier = Modifier.constrainAs(title){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(cancel.bottom, margin = 16.dp)
                        })

                    // Water Section
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(water){
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(title.bottom, margin = 32.dp)
                        }){

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = min(maxWidth * 0.1f, 24.dp))
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start){

                                Image(painter = painterResource(R.drawable.glass_water_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp))

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(text = "Amount Of Water",
                                    fontFamily = PoppinSemiBold,
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = textWater,
                                onValueChange = { textWater = it },
                                placeholder = { Text("the amount of water you drink", color = Color(0xFF666666)) },
                                singleLine = true,
                                shape = RoundedCornerShape(min(maxWidth * 0.03f, 8.dp)),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedContainerColor = Color(0xFFF8F8F8),
                                    unfocusedContainerColor = Color(0xFFF8F8F8),
                                    focusedBorderColor = Color(0xFFFFFFFF),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    cursorColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp, max = 84.dp)
                            )
                        }
                    }

                    // Sleep Section
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(sleep){
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(water.bottom, margin = 24.dp)
                        }) {

                        val list = listOf("0", "3", "5", "8", "10", "12", "16+")

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.sleep_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "Hours of Sleep",
                                    fontFamily = PoppinSemiBold,
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp, max = 84.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                list.forEachIndexed { index, item ->
                                    val isSelected = index == selectedSleepIndex

                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) Color(0xFF70A056) else Color.LightGray.copy(
                                                    alpha = 0.3f
                                                )
                                            )
                                            .clickable {
                                                selectedSleepIndex = index
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item,
                                            color = if (isSelected) Color.White else Color.Black,
                                            fontSize = 16.sp,
                                            fontFamily = PoppinSemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Step Section
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(step){
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(sleep.bottom, margin = 24.dp)
                        }){

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = min(maxWidth * 0.1f, 24.dp))
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start){

                                Image(painter = painterResource(R.drawable.steep_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp))

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(text = "Number of steps taken daily",
                                    fontFamily = PoppinSemiBold,
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = textStep,
                                onValueChange = { textStep = it },
                                placeholder = { Text("Number of steps or exercise duration", color = Color(0xFF666666)) },
                                singleLine = true,
                                shape = RoundedCornerShape(min(maxWidth * 0.03f, 8.dp)),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedContainerColor = Color(0xFFF8F8F8),
                                    unfocusedContainerColor = Color(0xFFF8F8F8),
                                    focusedBorderColor = Color(0xFFFFFFFF),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    cursorColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp, max = 84.dp)
                            )
                        }
                    }

                    // How You Feel Section
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(howYou){
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(step.bottom, margin = 24.dp)
                        }){

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = min(maxWidth * 0.1f, 24.dp))
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start){

                                Text(text = "How do you feel today?",
                                    fontFamily = PoppinSemiBold,
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = textHowYou,
                                onValueChange = { textHowYou = it },
                                placeholder = { Text("I feel good today because yesterday I learned that a friend I love\nvery much is coming to the city where I am staying on holiday.", color = Color(0xFF666666)) },
                                singleLine = false,
                                maxLines = 4,
                                shape = RoundedCornerShape(min(maxWidth * 0.03f, 8.dp)),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedContainerColor = Color(0xFFF8F8F8),
                                    unfocusedContainerColor = Color(0xFFF8F8F8),
                                    focusedBorderColor = Color(0xFFFFFFFF),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    cursorColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp, max = 150.dp)
                            )
                        }
                    }

                    // Save Button
                    CustomDailyDataButton(
                        text = "Save Daily Data",
                        onClick = {
                            val sleepValue = if (selectedSleepIndex != -1) {
                                listOf("0", "3", "5", "8", "10", "12", "16+")[selectedSleepIndex]
                            } else "0"


                            roomViewModel.insertDailyUserData(textWater,textStep,textHowYou,sleepValue)

                        },
                        maxWidth = maxWidth,
                        enabled = allFieldsFilled,
                        modifier = Modifier.constrainAs(btn){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(howYou.bottom, margin = 32.dp)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun CustomDailyDataButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        maxWidth: Dp
    ) {
        Button(
            onClick = onClick,
            enabled = enabled && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (enabled) Color(0xFF70A056) else Color(0xFFB0B0B0),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color(0xFF9E9E9E)
            ),
            shape = RoundedCornerShape(min(maxWidth * 0.03f, 12.dp)),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = if (enabled) 4.dp else 0.dp,
                pressedElevation = 2.dp,
                disabledElevation = 0.dp
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 24.dp)
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Loading...",
                        fontFamily = PoppinSemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            } else {
                Text(
                    text = text,
                    fontFamily = PoppinSemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }


data class BottomBarItem(
    val route: String,
    val defaultIcon: Int,
    val selectedIcon: Int
)

val bottomItemList = listOf(

    BottomBarItem("home", R.drawable.home_default_icon, R.drawable.home_selected_icon),
    BottomBarItem("calori", R.drawable.calories_default_icon, R.drawable.calories_selected_icon),
    BottomBarItem("chat", R.drawable.chat_icon, R.drawable.chat_icon),
    BottomBarItem("analysis", R.drawable.analysis_default_icon, R.drawable.analysis_selected_icon),
    BottomBarItem("profile", R.drawable.profile_default_icon, R.drawable.profile_selected_icon)
)