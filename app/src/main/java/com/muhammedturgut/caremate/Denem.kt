package com.muhammedturgut.caremate

/*
@Composable
fun PositionAnalysisScreen(
    navControllerAppHost: NavController,
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {
    val uiState by analysisViewModel.uiState.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        // Üst bar

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp , vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){

            Icon(Icons.Default.ArrowBack, contentDescription = "Geri",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = {
                        navControllerAppHost.navigate("NavBarHostScreen") {
                            popUpTo(navControllerAppHost.graph.id) { inclusive = true }
                        }
                }))

            Text(
                text = "Postür Analizi Sonuçları",
                fontFamily = PoppinSemiBold,
                fontSize = 20.sp,
                color = Color(0xFF70A056),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )

            Icon(painter = painterResource(R.drawable.cancel_icon), contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp))

        }


        when {
            uiState.analysisResult != null -> {
                // Analiz sonuçları
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Landmark'ları göster
                    if (uiState.landmarks.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    uiState.landmarks.forEach { point ->
                                        drawCircle(
                                            color = Color.Green,
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
                    }



                    // Boyun Analizi
                    item {
                        NeckAnalysisCard(uiState.analysisResult!!.neckFlattening)
                    }

                    // Yeniden analiz butonu
                    item {
                        OutlinedButton(
                            onClick = {
                                navControllerAppHost.navigate("PostureCameraScreen")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(painter = painterResource(R.drawable.change_camera), contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Yeni Analiz Yap")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OverallRiskCard(riskLevel: RiskLevel) {
    val backgroundColor = Color(android.graphics.Color.parseColor(riskLevel.color))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (riskLevel) {
                        RiskLevel.LOW -> "✓"
                        RiskLevel.MODERATE -> "!"
                        RiskLevel.HIGH -> "⚠"
                    },
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Genel Durum",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = riskLevel.description,
                color = backgroundColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}



@Composable
private fun NeckAnalysisCard(neckAnalysis: com.muhammedturgut.caremate.pose.NeckAnalysis) {
    val riskColor = Color(android.graphics.Color.parseColor(neckAnalysis.riskLevel.color))

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Boyun Eğriliği",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = neckAnalysis.riskLevel.description,
                    color = riskColor,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Açı bilgisi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ölçülen Açı:")
                Text("${neckAnalysis.cervicalAngle.toInt()}°")
            }

            // Normal aralık
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Normal Aralık:")
                Text("${neckAnalysis.normalRange.first.toInt()}-${neckAnalysis.normalRange.second.toInt()}°")
            }

            // Düzleşme yüzdesi
            if (neckAnalysis.flatteningPercentage > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Düzleşme:")
                    Text("${neckAnalysis.flatteningPercentage.toInt()}%")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = neckAnalysis.recommendation,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        riskColor.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            )
        }
    }
}
 */