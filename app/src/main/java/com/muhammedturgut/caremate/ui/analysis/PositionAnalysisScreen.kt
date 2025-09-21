@file:OptIn(ExperimentalMaterial3Api::class)

package com.muhammedturgut.caremate.ui.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import com.muhammedturgut.caremate.R
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.muhammedturgut.caremate.pose.NeckAnalysis
import com.muhammedturgut.caremate.pose.RiskLevel
import com.muhammedturgut.caremate.pose.SpinalAnalysis
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinRegular
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold
import javax.annotation.meta.When



@Composable
fun PositionAnalysisScreen(navControllerAppHost: NavController,
                           analysisViewModel: AnalysisViewModel = hiltViewModel()) {

    val uiState by analysisViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)),
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            when{
                uiState.analysisResult != null ->{

                    if (uiState.landmarks.isNotEmpty()){

                        // Header Section
                        HeaderSection(uiState)
                    }
                }


                else ->{
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent, // Kartın arka plan rengi
                                contentColor = Color.Transparent        // İçerikteki metin/icon rengi
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Henüz analiz yapılmamış",
                                    fontFamily = PoppinSemiBold,
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Postür analiziniz için kamera ile fotoğraf çekin",
                                    textAlign = TextAlign.Center,
                                    fontFamily = PoppinRegular,
                                    fontSize = 14.sp,
                                    color = Color(0xFF6D6C6C)
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(
                                    onClick = {
                                        navControllerAppHost.navigate("PostureCameraScreen")
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF70A056), // Arka plan rengi
                                        contentColor = Color.White,         // Metin/ikon rengi
                                        disabledContainerColor = Color.Gray, // Buton devre dışı iken arka plan
                                        disabledContentColor = Color.DarkGray // Buton devre dışı iken metin
                                    )
                                ) {
                                    Text("Analiz Başlat")
                                }
                            }
                        }
                    }
                }

            }


            Spacer(modifier = Modifier.height(16.dp))

            // Analysis Results Section
            SpinalAnalysisResultsCard(uiState.analysisResult!!.spinalFlattening)

            Spacer(modifier = Modifier.height(16.dp))

            NeckAnalysisResultsCard(uiState.analysisResult!!.neckFlattening)
        }
    }
}

@Composable
private fun HeaderSection(uiState: AnalysisUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Bar
        NavigationBar()

        Spacer(modifier = Modifier.height(16.dp))

        // Analysis Canvas Card
        AnalysisCanvasCard(uiState)

        // Overall Status
        OverallStatusSection(uiState.analysisResult!!.overallRiskLevel)

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun NavigationBar() {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Color(0xFF70A056))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button
        NavigationButton(
            iconRes = R.drawable.chevron_left_icon,
            onClick = { /* Handle back navigation */ }
        )

        // Title
        Text(
            text = "Postür Analiz Sonuçları",
            fontFamily = PoppinSemiBold,
            fontSize = 16.sp,
            color = Color.White
        )

        // Close Button
        NavigationButton(
            iconRes = R.drawable.cancel_icon,
            onClick = { /* Handle close */ }
        )
    }
}

@Composable
private fun NavigationButton(
    iconRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            tint = Color(0xFF70A056),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun AnalysisCanvasCard(uiState: AnalysisUiState) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE7E4E4), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color(0xFFF8F8F8))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            uiState.landmarks.forEach { point ->
                drawCircle(
                    color = Color.Red,
                    radius = 6f,
                    center = Offset(
                        x = size.width * point.x,
                        y = size.height * point.y
                    )
                )
            }
        }
    }
}

@Composable
private fun OverallStatusSection(riskLevel: RiskLevel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Genel Durum",
                fontSize = 20.sp,
                fontFamily = PoppinSemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${riskLevel.description}",
                fontSize = 18.sp,
                fontFamily = PoppinMedium,
                color = Color(0xFFFF0000)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Lütfen bir ortopedi,\nbeyin sinir cerrahı\nveya bir fizyoterapi doktoruna görünün",
            fontSize = 15.sp,
            fontFamily = PoppinMedium,
            color = Color.Black,
            lineHeight = 20.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun SpinalAnalysisResultsCard(spinalAnalysis: SpinalAnalysis) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bel Eğriliği",
                    fontFamily = PoppinSemiBold,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Text(
                    text = spinalAnalysis.riskLevel.description,
                    fontFamily = PoppinSemiBold,
                    color = Color(0xFFFF0000),
                    fontSize = 14.sp
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MeasurementRow(
                    label = "Ölçülen Açı",
                    value = "${spinalAnalysis.curvatureAngle.toInt()}°"
                )

                MeasurementRow(
                    label = "Normal Aralık",
                    value = "${spinalAnalysis.normalRange.first.toInt()}-${spinalAnalysis.normalRange.second.toInt()}°"
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4FA5E3))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "${spinalAnalysis.flatteningPercentage.toInt()}%",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = PoppinSemiBold
                    )
                }

                // Content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF70A056).copy(alpha = 0.25f))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${spinalAnalysis.recommendation}",
                        fontFamily = PoppinRegular,
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Exercise Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4FA5E3))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Egzersiz Önerileri",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = PoppinSemiBold
                    )
                }

                // Exercise List
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(exerciseItemList) { item ->
                        ExerciseItemRow(item)
                    }
                }
            }
        }
    }
}


@Composable
private fun NeckAnalysisResultsCard(neckAnalysis: NeckAnalysis) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Boyun Eğriliği",
                    fontFamily = PoppinSemiBold,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Text(
                    text = neckAnalysis.riskLevel.description,
                    fontFamily = PoppinSemiBold,
                    color = Color(0xFFFF0000),
                    fontSize = 14.sp
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MeasurementRow(
                    label = "Ölçülen Açı",
                    value = "${neckAnalysis.cervicalAngle.toInt()}"
                )

                MeasurementRow(
                    label = "Normal Aralık",
                    value = "${neckAnalysis.normalRange.first.toInt()}-${neckAnalysis.normalRange.second.toInt()}°"
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4FA5E3))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "${neckAnalysis.flatteningPercentage.toInt()}%",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = PoppinSemiBold
                    )
                }

                // Content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF70A056).copy(alpha = 0.25f))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${neckAnalysis.recommendation}",
                        fontFamily = PoppinRegular,
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Exercise Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4FA5E3))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Egzersiz Önerileri",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = PoppinSemiBold
                    )
                }

                // Exercise List
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(exerciseItemList) { item ->
                        ExerciseItemRow(item)
                    }
                }
            }
        }
    }
}



@Composable
private fun MeasurementRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = PoppinRegular,
            fontSize = 14.sp,
            color = Color.Black
        )

        Text(
            text = value,
            fontFamily = PoppinMedium,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}



@Composable
fun ExerciseItemRow(exerciseItem: ExerciseItem) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Lottie Animation
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(exerciseItem.animation)
            )
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(80.dp)
            )

            // Exercise Details
            Text(
                text = exerciseItem.again,
                fontSize = 13.sp,
                fontFamily = PoppinMedium,
                color = Color(0xFF7C7C7C),
                textAlign = TextAlign.Center
            )

            Text(
                text = exerciseItem.time,
                fontSize = 13.sp,
                fontFamily = PoppinMedium,
                color = Color(0xFF7C7C7C),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data Classes and Constants
val exerciseItemList = listOf(
    ExerciseItem("10 Reps", "3 min", R.raw.exercise),
    ExerciseItem("15 Reps", "3 min", R.raw.t_plank_exercise),
    ExerciseItem("8 Reps", "3 min", R.raw.burpee_and_jump_exercise),
    ExerciseItem("12 Reps", "3 min", R.raw.squat_reach),
    ExerciseItem("19 Reps", "3 min", R.raw.bulgarian_split_squat_jump),
    ExerciseItem("5 Reps", "3 min", R.raw.side_hip_abduction),
    ExerciseItem("12 Reps", "3 min", R.raw.squat_kicks)
)

data class ExerciseItem(
    val again: String,
    val time: String,
    val animation: Int
)

@Preview( showBackground = true)
@Composable
private fun Show(){
    val navControllerAppHost  = rememberNavController()

    //PostureAnalysisResults()

    //PositionAnalysisScreen(navControllerAppHost = navControllerAppHost)
}