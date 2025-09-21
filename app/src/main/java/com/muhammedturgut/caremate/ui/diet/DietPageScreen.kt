package com.muhammedturgut.caremate.ui.diet

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.data.local.entity.DietItem
import com.muhammedturgut.caremate.data.local.room.RoomViewModel
import com.muhammedturgut.caremate.data.remote.GeminiAiViewModel
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinExtraBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinRegular
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold

@Composable
fun DietPageScreen(maxWidth: Dp,
                   geminiAiViewModel: GeminiAiViewModel = hiltViewModel(),
                   roomViewModel: RoomViewModel = hiltViewModel(),
                   dietViewModel: DietViewModel = hiltViewModel(),
                   navControllerAppHost: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        BackHandler {

        }
        val scrollState = rememberScrollState()
        val dietResult by geminiAiViewModel.dietText.collectAsState()
        var showDialog by remember { mutableStateOf(false) }
        val context = LocalContext.current

        val aiResponse by geminiAiViewModel.dietText.collectAsState()
        val currentDayDiet by roomViewModel.currentDayDiet.collectAsState()
        val getCurrentMealTimeInformation by dietViewModel.getCurrentMealTimeInformation.collectAsState()

        // dailyDietList'i derived state olarak tanımlayın
        val dailyDietList by remember(currentDayDiet) {
            derivedStateOf {
                if (currentDayDiet != null) {
                    listOf(
                        MealData(currentDayDiet!!.breakfastCalorie, currentDayDiet!!.breakfastOneFood, currentDayDiet!!.breakfastTwoFood),
                        MealData(currentDayDiet!!.lunchCalorie, currentDayDiet!!.lunchOneFood, currentDayDiet!!.lunchTwoFood),
                        MealData(currentDayDiet!!.eveningMealCalorie, currentDayDiet!!.eveningMealOneFood, currentDayDiet!!.eveningMealTwoFood)
                    )
                } else {
                    listOf(
                        MealData("0", "", ""),
                        MealData("0", "", ""),
                        MealData("0", "", "")
                    )
                }
            }
        }

        // totalCalorie'yi derived state olarak hesaplayın
        val totalCalorie by remember(dailyDietList) {
            derivedStateOf {
                dailyDietList.sumOf { it.calorie.toIntOrNull() ?: 0 }
            }
        }

        val currentMealTime by dietViewModel.getCurrentMealTimeInformation.collectAsState()
        var currentImageIndex by remember { mutableIntStateOf(0) }

        val foodImage = listOf(
            ImageFoodList("Kahvaltı",
                R.drawable.yulaf,
                R.drawable.kuru_kayisi),

            ImageFoodList("Öğle Yemeği",
                R.drawable.thanksgiving_turkey,
                R.drawable.potato_salad),

            ImageFoodList("Akşam Yemeği",
                R.drawable.fish_food_image,
                R.drawable.carrots)
        )

        // Şu anki öğün zamanına göre hangi meal'in seçili olacağını belirle
        val currentMealIndex = when (currentMealTime) {
            "Kahvaltı" -> 0
            "Öğle Yemeği" -> 1
            "Akşam Yemeği" -> 2
            else -> 0
        }

        LaunchedEffect(aiResponse) {
            if (aiResponse.isNotEmpty() && aiResponse != "Henüz diyet oluşturulmadı") {
                parseDietPlanAndSave(aiResponse, roomViewModel)
                Toast.makeText(context, "Diyet planı kaydedildi!", Toast.LENGTH_SHORT).show()
            }
        }

        if (showDialog) {
            DietPlanDialog(
                onDismiss = { showDialog = false },
                onConfirm = { dietPlanData ->
                    // Handle the diet plan data
                    showDialog = false
                    // Process dietPlanData here
                },
                geminiAiViewModel = geminiAiViewModel,
                roomViewModel = roomViewModel,
                context = context
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFFE7E4E4), CircleShape)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Calorie tracking",
                        fontFamily = PoppinBold,
                        fontSize = 20.sp,
                        color = Color(0xFF70A056),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(top = 12.dp, bottom = 12.dp, start = 16.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.food_add_icon),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )

                    Text(
                        text = "Add Food",
                        fontFamily = PoppinMedium,
                        fontSize = 12.sp,
                        color = Color(0xFF70A056),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE7E4E4), RoundedCornerShape(16.dp))
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "",
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = "Today",
                            fontFamily = PoppinSemiBold,
                            color = Color.Black,
                            fontSize = 24.sp
                        )

                        Image(
                            painter = painterResource(R.drawable.view_list_icon),
                            contentDescription = "View List Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = {
                                    navControllerAppHost.navigate("DietListScreen") {
                                        popUpTo(navControllerAppHost.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                })
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$getCurrentMealTimeInformation",
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val currentMeal = foodImage[currentMealIndex]
                    val currentMealImages = listOf(currentMeal.foodImageOne, currentMeal.foodImageTwo)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Sol ok - önceki resim
                        Image(
                            painter = painterResource(R.drawable.chevron_left_icon),
                            contentDescription = "Önceki Resim",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    currentImageIndex = if (currentImageIndex > 0) {
                                        currentImageIndex - 1
                                    } else {
                                        currentMealImages.size - 1 // Son resme git
                                    }
                                }
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        // Şu anki resim
                        Image(
                            painter = painterResource(currentMealImages[currentImageIndex]),
                            contentDescription = null,
                            modifier = Modifier.size(96.dp)
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        // Sağ ok - sonraki resim
                        Image(
                            painter = painterResource(R.drawable.chevron_right_icon),
                            contentDescription = "Sonraki Resim",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    currentImageIndex = if (currentImageIndex < currentMealImages.size - 1) {
                                        currentImageIndex + 1
                                    } else {
                                        0 // İlk resme dön
                                    }
                                }
                        )
                    }

                    // Öğün zamanı değiştiğinde resim indeksini resetle
                    LaunchedEffect(currentMealTime) {
                        currentImageIndex = 0
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Güvenli null kontrolü ile
                    Text(
                        text = currentDayDiet?.lunchTwoFood ?: "",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic
                    )

                    Text(
                        text = "${currentDayDiet?.lunchCalorie ?: "0"} Kcal",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = PoppinSemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Canvas(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                        val start = Offset(x = size.width * 0.1f, y = size.height * 0.5f)
                        val end = Offset(x = size.width * 0.9f, y = size.height * 0.5f)

                        drawLine(
                            color = Color(0xFFC6C6C6),
                            start = start,
                            end = end,
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(R.drawable.dieat_food_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 12.dp, start = 12.dp, bottom = 12.dp)
                                .size(152.dp, 116.dp)
                        )

                        Column(
                            modifier = Modifier.padding(
                                top = 12.dp,
                                start = 12.dp,
                                bottom = 12.dp
                            )
                        ) {
                            Text(
                                text = "Create a diet list\nwith AI",
                                fontFamily = PoppinExtraBold,
                                fontSize = 20.sp,
                                color = Color(0xFFE67386)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Add foods with correct\ncalories using AI.",
                                fontFamily = PoppinMedium,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }

                    CustomDailyDataButton(
                        text = "Create a diet list",
                        onClick = {
                            showDialog = true
                        },
                        maxWidth = maxWidth
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.48f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE7E4E4), RoundedCornerShape(12.dp))
                            .background(Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 8.dp, bottom = 8.dp)
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE67386).copy(0.45f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.steep_icon),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)) {
                                Text(
                                    text = "Step to walk",
                                    fontFamily = PoppinBold,
                                    fontSize = 14.sp
                                )

                                Text(
                                    text = "5000",
                                    fontFamily = PoppinRegular,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE7E4E4), RoundedCornerShape(12.dp))
                            .background(Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 8.dp, bottom = 8.dp)
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF70A056).copy(0.45f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.glass_water_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)) {
                                Text(
                                    text = "Drink Water",
                                    fontFamily = PoppinBold,
                                    fontSize = 14.sp
                                )

                                Text(
                                    text = "12 glass",
                                    fontFamily = PoppinRegular,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE7E4E4), RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.calories_default_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = Color(0xFFF5A857)
                                    )

                                    Text(
                                        text = "Calorie",
                                        fontFamily = PoppinBold,
                                        fontSize = 24.sp,
                                        color = Color(0xFFE67386)
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "1250",
                                        fontSize = 32.sp,
                                        lineHeight = 32.sp,
                                        fontFamily = PoppinBold,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "kcal",
                                        fontSize = 20.sp,
                                        fontFamily = PoppinMedium,
                                        color = Color(0xFF828282)
                                    )
                                }
                            }

                            Text(
                                text = "target $totalCalorie kcal",
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontFamily = PoppinMedium
                            )
                        }

                        WeeklyCalorieChart()
                    }
                }
            }
        }
    }
}

data class DailyCalorieData(
    val day: String,
    val consumed: Int,
    val target: Int
)

@Composable
fun WeeklyCalorieChart(
    weeklyData: List<DailyCalorieData> = getDefaultWeeklyData(),
    modifier: Modifier = Modifier
) {
    // En yüksek değeri bul - güvenli şekilde
    val maxValue = remember {
        weeklyData.maxOfOrNull { maxOf(it.consumed, it.target) }?.toFloat() ?: 2000f
    }

    // Veri boş mu kontrol et
    if (weeklyData.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No data available", color = Color.Gray)
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Grafik
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceEvenly, // Eşit aralık için
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyData.forEach { data ->
                    DayCalorieColumn(
                        data = data,
                        maxValue = maxValue,
                        modifier = Modifier.weight(1f) // Eşit genişlik için
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
private fun DayCalorieColumn(
    data: DailyCalorieData,
    maxValue: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Çubuklar
        Row(
            modifier = Modifier.height(150.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Tüketilen çubuğu
            CalorieBar(
                value = data.consumed,
                maxValue = maxValue,
                color = Color(0xFFFD3354),
                modifier = Modifier.width(8.dp)
            )

            // Hedef çubuğu
            CalorieBar(
                value = data.target,
                maxValue = maxValue,
                color = Color(0xFF70A056),
                modifier = Modifier.width(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Gün adı
        Text(
            text = data.day,
            fontSize = 10.sp,
            fontFamily = PoppinMedium,
            color = Color(0xFF666363)
        )
    }
}

@Composable
private fun CalorieBar(
    value: Int,
    maxValue: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val height = if (maxValue > 0) ((value / maxValue) * 150).dp else 0.dp

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(color)
    )
}

// Örnek veri
private fun getDefaultWeeklyData(): List<DailyCalorieData> {
    return listOf(
        DailyCalorieData("Mon", 1800, 2000),
        DailyCalorieData("Tue", 2200, 2000),
        DailyCalorieData("Wed", 1650, 2000),
        DailyCalorieData("Thu", 1950, 2000),
        DailyCalorieData("Fri", 2400, 2000),
        DailyCalorieData("Sat", 2100, 2000),
        DailyCalorieData("Sun", 1750, 2000)
    )
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
            containerColor = if (enabled) Color(0xFF4FA5E3) else Color(0xFFB0B0B0),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFE0E0E0),
            disabledContentColor = Color(0xFF9E9E9E)
        ),
        shape = RoundedCornerShape(min(maxWidth * 0.03f, 8.dp)),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (enabled) 4.dp else 0.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .padding(horizontal = 8.dp)
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
                fontFamily = PoppinBold,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}



@Composable
fun DietPlanDialog(
    onDismiss: () -> Unit,
    onConfirm: (DietPlanData) -> Unit,
    modifier: Modifier = Modifier,
    geminiAiViewModel: GeminiAiViewModel,
    roomViewModel: RoomViewModel,
    context: Context
) {
    var goal by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var dietaryPreference by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var currentStep by remember { mutableIntStateOf(0) }

    val steps = listOf(R.drawable.goal_icon, R.drawable.runing_icon, R.drawable.allergy_food_icon, R.drawable.nutrition_meal_icon, R.drawable.notes_icon)
    val stepTitles = listOf("Hedefin", "Aktiviten", "Alerjilerin", "Tercihlerin", "Notların")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
        ) {
            // Animated gradient background
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4FA5E3),
                                Color(0xFF70A056),
                                Color(0xFF9C27B0)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    ),
                color = Color.Transparent,
                tonalElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier.padding(2.dp)
                ) {
                    // Inner surface with rounded corners
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp)),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .animateContentSize()
                        ) {
                            // Header with close button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Diyet Planın",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF2D3748)
                                        )
                                    )
                                    Text(
                                        text = "Kişisel beslenme rehberin",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color(0xFF718096)
                                        )
                                    )
                                }

                                Surface(
                                    onClick = onDismiss,
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFF7FAFC),
                                    shadowElevation = 4.dp
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Kapat",
                                            tint = Color(0xFF4A5568),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Progress indicators
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                steps.forEachIndexed { index, emoji ->
                                    StepIndicator(
                                        iconRes = emoji,
                                        isActive = index <= currentStep,
                                        isCompleted = index < currentStep
                                    )
                                    if (index < steps.size - 1) {
                                        StepConnector(isActive = index < currentStep)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Current step content
                            AnimatedContent(
                                targetState = currentStep,
                                transitionSpec = {
                                    slideInHorizontally(
                                        initialOffsetX = { if (targetState > initialState) 300 else -300 }
                                    ) + fadeIn() togetherWith
                                            slideOutHorizontally(
                                                targetOffsetX = { if (targetState > initialState) -300 else 300 }
                                            ) + fadeOut()
                                },
                                label = "step_animation"
                            ) { step ->
                                when (step) {
                                    0 -> StepContent(
                                        title = "Hedefin Nedir?",
                                        subtitle = "Beslenme yolculuğunda varmak istediğin yer",
                                        value = goal,
                                        onValueChange = { goal = it },
                                        placeholder = "Kilo vermek, kas yapmak, sağlıklı yaşam...",
                                        examples = listOf("💪 Kas yapmak", "⚖️ Kilo vermek", "🌱 Sağlıklı yaşam")
                                    )
                                    1 -> StepContent(
                                        title = "🏃‍♀️ Ne Kadar Aktifsin?",
                                        subtitle = "Günlük hareket seviyeni öğrenelim",
                                        value = activity,
                                        onValueChange = { activity = it },
                                        placeholder = "Sedanter, hafif aktif, çok aktif...",
                                        examples = listOf("🪑 Sedanter", "🚶 Hafif aktif", "🏋️ Çok aktif")
                                    )
                                    2 -> StepContent(
                                        title = "Alerjilerin Var mı?",
                                        subtitle = "Dikkat etmemiz gereken besinler",
                                        value = allergies,
                                        onValueChange = { allergies = it },
                                        placeholder = "Fıstık, süt, gluten... (Yoksa boş bırak)",
                                        examples = listOf("🥜 Fıstık", "🥛 Süt ürünleri", "🌾 Gluten")
                                    )
                                    3 -> StepContent(
                                        title = "Beslenme Tarzın?",
                                        subtitle = "Hangi beslenme şeklini tercih ediyorsun",
                                        value = dietaryPreference,
                                        onValueChange = { dietaryPreference = it },
                                        placeholder = "Vejetaryen, vegan, mediterran...",
                                        examples = listOf("🌱 Vejetaryen", "🥑 Vegan", "🍅 Mediterran")
                                    )
                                    4 -> StepContent(
                                        title = "Ek Notların",
                                        subtitle = "Bilmemiz gereken başka bir şey var mı?",
                                        value = notes,
                                        onValueChange = { notes = it },
                                        placeholder = "İsteğin ya da özel durumların...",
                                        examples = listOf("İlaç kullanımı", "🤱 Hamilelik", "🏥 Sağlık durumu"),
                                        isMultiline = true
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Navigation buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (currentStep > 0) {
                                    OutlinedButton(
                                        onClick = { currentStep-- },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color(0xFF4A5568)
                                        ),
                                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Geri")
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (currentStep < steps.size - 1) {
                                            currentStep++
                                        } else {
                                            val dietPlanData = DietPlanData(
                                                goal = goal.trim(),
                                                activity = activity.trim(),
                                                allergies = allergies.trim(),
                                                dietaryPreference = dietaryPreference.trim(),
                                                notes = notes.trim()
                                            )

                                            if (dietPlanData.goal.isEmpty() && dietPlanData.activity.isEmpty()) {
                                                Toast.makeText(context, "Lütfen en az hedef ve aktivite seviyenizi belirtin", Toast.LENGTH_SHORT).show()
                                            } else {
                                                roomViewModel.deleteAllDietItems()

                                                val dietText = buildString {
                                                    append("27 yaşında, 165 cm boyunda, 62 kg, kadın kullanıcı için:\n")
                                                    append("Hedef: ${dietPlanData.goal.ifEmpty { "Sağlıklı beslenme" }}\n")
                                                    append("Aktivite seviyesi: ${dietPlanData.activity.ifEmpty { "Orta seviye" }}\n")
                                                    if (dietPlanData.dietaryPreference.isNotEmpty()) {
                                                        append("Beslenme tercihi: ${dietPlanData.dietaryPreference}\n")
                                                    }
                                                    if (dietPlanData.allergies.isNotEmpty()) {
                                                        append("Alerjiler/kısıtlamalar: ${dietPlanData.allergies}\n")
                                                    }
                                                    if (dietPlanData.notes.isNotEmpty()) {
                                                        append("Özel notlar: ${dietPlanData.notes}\n")
                                                    }
                                                    append("\nLütfen günlük 7 gün için 3 ana öğün (kahvaltı, öğle yemeği, akşam yemeği) hazırla. ")
                                                    append("Her öğün için maksimum 2 yiyecek öner ve her öğünün kalorisini belirt. ")
                                                    append("Format şu şekilde olsun:\n\n")
                                                    append("**Pazartesi:**\n")
                                                    append("* **Kahvaltı (Yaklaşık 400 kalori):** Yiyecek1 + Yiyecek2\n")
                                                    append("* **Öğle Yemeği (Yaklaşık 500 kalori):** Yiyecek1 + Yiyecek2\n")
                                                    append("* **Akşam Yemeği (Yaklaşık 450 kalori):** Yiyecek1 + Yiyecek2\n\n")
                                                    append("Bu formatı tüm 7 gün için tekrarla.")
                                                }

                                                Log.d("DietDialog", "Gönderilen prompt: $dietText")
                                                geminiAiViewModel.generateDiet(dietText)
                                                onDismiss()
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(if (currentStep > 0) 1f else 1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (currentStep == steps.size - 1)
                                            Color(0xFF70A056) else Color(0xFF4FA5E3)
                                    )
                                ) {
                                    Text(
                                        if (currentStep == steps.size - 1) "Planı Oluştur"
                                        else "Devam Et"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = if (currentStep == steps.size - 1)
                                            Icons.Default.CheckCircle else Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(
    iconRes: Int,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        color = when {
            isCompleted -> Color(0xFF70A056)
            isActive -> Color(0xFF4BA9E4)
            else -> Color(0xFFF7FAFC)
        },
        shadowElevation = if (isActive || isCompleted) 8.dp else 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
@Composable
private fun StepConnector(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(24.dp)
            .height(4.dp)
            .padding(vertical = 22.dp)
            .background(
                color = if (isActive) Color(0xFF48BB78) else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(2.dp)
            )
    )
}

@Composable
private fun StepContent(
    title: String,
    subtitle: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    examples: List<String>,
    isMultiline: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF718096)
                )
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF8F9FA),
            shadowElevation = 2.dp
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Color(0xFFA0AEC0)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = if (isMultiline) 4 else 1,
                singleLine = !isMultiline
            )
        }

        // Example chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(examples) { example ->
                AssistChip(
                    onClick = { onValueChange(example.substringAfter(" ")) },
                    label = {
                        Text(
                            text = example,
                            fontSize = 12.sp,
                            color = Color(0xFF4A5568)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFFF0F4F8),
                        labelColor = Color(0xFF4A5568)
                    )
                )
            }
        }
    }
}

// Data class remains the same
data class DietPlanData(
    val goal: String,
    val activity: String,
    val allergies: String,
    val dietaryPreference: String,
    val notes: String
)

// Düzeltilmiş ve iyileştirilmiş parse fonksiyonları
fun parseDietPlanAndSave(aiResponse: String, roomViewModel: RoomViewModel) {
    try {
        Log.d("DietParser", "=== PARSE İŞLEMİ BAŞLADI ===")
        Log.d("DietParser", "AI Response uzunluğu: ${aiResponse.length}")
        Log.d("DietParser", "AI Response ilk 1000 karakter: ${aiResponse.take(1000)}")

        val days = listOf("Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar")
        var successCount = 0

        days.forEach { dayName ->
            Log.d("DietParser", "\n--- $dayName İÇİN PARSE İŞLEMİ ---")
            val dayData = extractDayData(aiResponse, dayName)
            if (dayData != null) {
                Log.d("DietParser", "✅ $dayName başarıyla parse edildi")
                Log.d("DietParser", "Kahvaltı: ${dayData.breakfastCalorie} kcal - ${dayData.breakfastOneFood} + ${dayData.breakfastTwoFood}")
                Log.d("DietParser", "Öğle: ${dayData.lunchCalorie} kcal - ${dayData.lunchOneFood} + ${dayData.lunchTwoFood}")
                Log.d("DietParser", "Akşam: ${dayData.eveningMealCalorie} kcal - ${dayData.eveningMealOneFood} + ${dayData.eveningMealTwoFood}")

                roomViewModel.dietListInsert(
                    dayData.day,
                    dayData.breakfastCalorie,
                    dayData.breakfastOneFood,
                    dayData.breakfastTwoFood,
                    dayData.lunchCalorie,
                    dayData.lunchOneFood,
                    dayData.lunchTwoFood,
                    dayData.eveningMealCalorie,
                    dayData.eveningMealOneFood,
                    dayData.eveningMealTwoFood
                )
                successCount++
            } else {
                Log.w("DietParser", "❌ $dayName parse edilemedi")
            }
        }

        Log.d("DietParser", "=== PARSE İŞLEMİ TAMAMLANDI ===")
        Log.d("DietParser", "Toplam başarılı parse: $successCount/7")
    } catch (e: Exception) {
        Log.e("DietParser", "Parse sırasında hata: ${e.message}")
        e.printStackTrace()
    }
}

private fun extractDayData(response: String, dayName: String): DietItem? {
    try {
        Log.d("DietParser", "🔍 $dayName için arama başlıyor...")

        // Günün başlangıcını bul - daha esnek pattern
        val dayPatterns = listOf(
            "\\*\\*$dayName\\s*:\\s*\\*\\*".toRegex(RegexOption.IGNORE_CASE),
            "\\*\\*$dayName\\s*\\*\\*".toRegex(RegexOption.IGNORE_CASE),
            "$dayName\\s*:".toRegex(RegexOption.IGNORE_CASE)
        )

        var dayStart = -1
        for (pattern in dayPatterns) {
            val match = pattern.find(response)
            if (match != null) {
                dayStart = match.range.first
                Log.d("DietParser", "✅ $dayName bulundu pozisyon: $dayStart")
                break
            }
        }

        if (dayStart == -1) {
            Log.w("DietParser", "❌ $dayName için başlangıç bulunamadı")
            return null
        }

        // Bir sonraki günün başlangıcını bul
        val allDays = listOf("Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar")
        var nextDayStart = response.length

        for (nextDay in allDays) {
            if (nextDay != dayName) {
                for (pattern in dayPatterns) {
                    val nextDayPattern = pattern.pattern.replace(dayName, nextDay).toRegex(RegexOption.IGNORE_CASE)
                    val nextMatch = nextDayPattern.find(response, dayStart + 1)
                    if (nextMatch != null && nextMatch.range.first > dayStart && nextMatch.range.first < nextDayStart) {
                        nextDayStart = nextMatch.range.first
                    }
                }
            }
        }

        val dayContent = response.substring(dayStart, nextDayStart)
        Log.d("DietParser", "$dayName içerik uzunluğu: ${dayContent.length}")
        Log.d("DietParser", "$dayName içerik:\n${dayContent}")

        // Öğünleri parse et
        val breakfast = parseMealFromContent(dayContent, "Kahvaltı")
        val lunch = parseMealFromContent(dayContent, "Öğle")
        val dinner = parseMealFromContent(dayContent, "Akşam")

        Log.d("DietParser", """
            $dayName Final Parse Sonuçları:
            Kahvaltı: ${breakfast.calorie}kcal - "${breakfast.firstFood}" + "${breakfast.secondFood}"
            Öğle: ${lunch.calorie}kcal - "${lunch.firstFood}" + "${lunch.secondFood}"
            Akşam: ${dinner.calorie}kcal - "${dinner.firstFood}" + "${dinner.secondFood}"
        """.trimIndent())

        // Verilerin geçerli olup olmadığını kontrol et
        if (breakfast.calorie == "0" && lunch.calorie == "0" && dinner.calorie == "0") {
            Log.w("DietParser", "$dayName için hiçbir geçerli öğün bulunamadı")
            return null
        }

        return DietItem(
            id = 0,
            day = dayName,
            breakfastCalorie = breakfast.calorie,
            breakfastOneFood = breakfast.firstFood,
            breakfastTwoFood = breakfast.secondFood,
            lunchCalorie = lunch.calorie,
            lunchOneFood = lunch.firstFood,
            lunchTwoFood = lunch.secondFood,
            eveningMealCalorie = dinner.calorie,
            eveningMealOneFood = dinner.firstFood,
            eveningMealTwoFood = dinner.secondFood
        )
    } catch (e: Exception) {
        Log.e("DietParser", "$dayName parse edilirken hata: ${e.message}")
        e.printStackTrace()
        return null
    }
}

private fun parseMealFromContent(dayContent: String, mealName: String): MealData {
    try {
        Log.d("DietParser", "🍽️ $mealName parse ediliyor...")

        // Farklı formatlar için pattern listesi
        val patterns = listOf(
            // * **Kahvaltı (Yaklaşık 400 kalori):** yiyecek1 + yiyecek2
            "\\*\\s*\\*\\*$mealName.*?\\((.*?)\\d+(.*?)kalori.*?\\).*?:\\*\\*\\s*(.+?)(?=\\n\\*|\\n\\n|$)".toRegex(
                setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
            ),
            // **Kahvaltı:** (400 kalori) yiyecek1 + yiyecek2
            "\\*\\*$mealName.*?:\\*\\*.*?\\((.*?)\\d+(.*?)kalori.*?\\)\\s*(.+?)(?=\\n\\*|\\n\\n|$)".toRegex(
                setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
            ),
            // Kahvaltı: 400 kalori - yiyecek1 + yiyecek2
            "$mealName.*?:\\s*(\\d+)\\s*kalori.*?[:-]\\s*(.+?)(?=\\n|$)".toRegex(
                setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
            )
        )

        var calorie = "0"
        var foodContent = ""

        for (pattern in patterns) {
            val match = pattern.find(dayContent)
            if (match != null) {
                Log.d("DietParser", "✅ $mealName için pattern eşleşti")
                Log.d("DietParser", "Match groups: ${match.groupValues}")

                // Kaloriyi bul
                for (i in 1 until match.groupValues.size) {
                    val group = match.groupValues[i]
                    val calorieMatch = "\\d+".toRegex().find(group)
                    if (calorieMatch != null) {
                        calorie = calorieMatch.value
                        Log.d("DietParser", "Kalori bulundu: $calorie")
                        break
                    }
                }

                // Yiyecek içeriğini al
                foodContent = match.groupValues.last().trim()
                Log.d("DietParser", "Yiyecek içeriği: $foodContent")
                break
            }
        }

        if (calorie == "0" || foodContent.isEmpty()) {
            Log.w("DietParser", "❌ $mealName için kalori veya yiyecek bulunamadı")
            return MealData("0", "", "")
        }

        val foods = extractFoodsFromContent(foodContent)
        val result = MealData(
            calorie = calorie,
            firstFood = foods.getOrNull(0) ?: "",
            secondFood = foods.getOrNull(1) ?: ""
        )

        Log.d("DietParser", "$mealName sonuç: $result")
        return result

    } catch (e: Exception) {
        Log.e("DietParser", "$mealName parse edilirken hata: ${e.message}")
        e.printStackTrace()
        return MealData("0", "", "")
    }
}

private fun extractFoodsFromContent(foodContent: String): List<String> {
    try {
        Log.d("DietParser", "🥘 Yiyecek ayırma: '$foodContent'")

        // İçeriği temizle
        var cleanContent = foodContent
            .replace(Regex("\\([^)]*\\)"), "") // Parantez içlerini kaldır
            .replace(Regex("\\d+\\s*(adet|kase|porsiyon|dilim|avuç|çorba kaşığı|orta boy|küçük|büyük|gr|gram|ml)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("yaklaşık|toplam", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s+"), " ")
            .trim()

        Log.d("DietParser", "Temizlenmiş: '$cleanContent'")

        // Farklı ayırıcıları dene
        val separators = listOf("+", "&", "ve", ",", "-", ":")
        var foods = listOf<String>()

        for (separator in separators) {
            if (cleanContent.contains(separator, ignoreCase = true)) {
                foods = cleanContent.split(separator, ignoreCase = true)
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && it.length > 1 }
                Log.d("DietParser", "Ayırıcı '$separator' ile bölündü: $foods")
                break
            }
        }

        // Hiçbir ayırıcı yoksa tüm içeriği tek yiyecek olarak al
        if (foods.isEmpty() && cleanContent.isNotEmpty()) {
            foods = listOf(cleanContent)
        }

        // Final temizlik ve filtreleme
        val finalFoods = foods
            .take(2) // Sadece ilk 2 yiyeceği al
            .map { food ->
                food.replace(Regex("^\\d+\\.?\\s*"), "") // Başındaki numaraları kaldır
                    .replace(Regex("kalori.*$", RegexOption.IGNORE_CASE), "")
                    .trim()
            }
            .filter { it.isNotEmpty() && it.length > 2 }

        Log.d("DietParser", "Final yiyecekler: $finalFoods")
        return finalFoods

    } catch (e: Exception) {
        Log.e("DietParser", "Yiyecek ayırma hatası: ${e.message}")
        e.printStackTrace()
        return emptyList()
    }
}

data class DailyDietItem(
    val  breakfastCalorie: String,
    val  breakfastOneFood: String,
    val  breakfastTwoFood: String,

    val  lunchCalorie: String,
    val  lunchOneFood: String,
    val  lunchTwoFood: String,

    val  eveningMealCalorie: String,
    val  eveningMealOneFood: String,
    val  eveningMealTwoFood: String
)

data class MealData(
    val calorie: String,
    val firstFood: String,
    val secondFood: String
)

data class ImageFoodList(val time : String,
                         val foodImageOne : Int,
                         val foodImageTwo: Int)