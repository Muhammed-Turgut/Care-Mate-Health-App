package com.muhammedturgut.caremate.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.analysis.AnalysisViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@Composable
fun PostureCameraScreen(
    cameraPermissionViewModel: CameraPermissionViewModel = hiltViewModel(),
    analysisViewModel: AnalysisViewModel = hiltViewModel(),
    navControllerAppHost: NavController,
    isTablet: Boolean,
    maxWidth: Dp,
    maxHeight: Dp
) {
    val permissionState by cameraPermissionViewModel.permissionsGranted.collectAsState()
    val analysisState by analysisViewModel.uiState.collectAsState()
    var permissionRequested by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val isTorchOn = remember { mutableStateOf(false) }
    val flash = hasFlashLightDetailed(context)
    var showInstructions by remember { mutableStateOf(true) }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
            // Postür analizi için arka kamerayı varsayılan yap
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionRequested = true
        cameraPermissionViewModel.updatePermissionStatus(result)
    }

    // İzin kontrolü
    LaunchedEffect(permissionState, permissionRequested) {
        if (!permissionRequested) {
            launcher.launch(CameraPermissionViewModel.CAMERAX_PERMISSONS)
        }
    }

    // Analiz tamamlandığında navigasyon
    LaunchedEffect(analysisState.analysisResult) {
        if (analysisState.analysisResult != null) {
            navControllerAppHost.navigate("PostureAnalysisResultScreen") {
                popUpTo("PostureCameraScreen") { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val (changeButton, cancelButton, cameraPreview, bottomRow, instructionCard, statusCard) = createRefs()

                // Kamera önizleme
                CameraPreview(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(cameraPreview) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    lifecycleOwner = lifecycleOwner
                )

                // Kamera değiştirme butonu
                IconButton(
                    onClick = {
                        controller.cameraSelector =
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else CameraSelector.DEFAULT_BACK_CAMERA
                    },
                    modifier = Modifier
                        .size(if (isTablet) 82.dp else 42.dp)
                        .constrainAs(changeButton) {
                            start.linkTo(parent.start, margin = 24.dp)
                            top.linkTo(parent.top, margin = 24.dp)
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.change_camera),
                        contentDescription = "Kamera Değiştir"
                    )
                }

                // İptal butonu
                IconButton(
                    onClick = {
                        navControllerAppHost.navigate("NavBarHostScreen") {
                            popUpTo(navControllerAppHost.graph.id) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .size(if (isTablet) 82.dp else 42.dp)
                        .constrainAs(cancelButton) {
                            end.linkTo(parent.end, margin = 24.dp)
                            top.linkTo(parent.top, margin = 24.dp)
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.cancel_icon),
                        contentDescription = "İptal"
                    )
                }

                // Detector durumu kartı
                if (!analysisState.isDetectorInitialized && !showInstructions) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .constrainAs(statusCard) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(changeButton.bottom, margin = 16.dp)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (analysisState.isLoading)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (analysisState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "⚠",
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (analysisState.isLoading)
                                        "Pose Detector Başlatılıyor..."
                                    else "Pose Detector Hazır Değil",
                                    fontWeight = FontWeight.Bold,
                                    color = if (analysisState.isLoading)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else MaterialTheme.colorScheme.onErrorContainer
                                )

                                if (!analysisState.isLoading) {
                                    Text(
                                        text = "Model dosyası yüklenmedi veya başlatma hatası",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }

                            if (!analysisState.isLoading) {
                                TextButton(
                                    onClick = { analysisViewModel.retryInitialization() }
                                ) {
                                    Text("Yeniden Dene")
                                }
                            }
                        }
                    }
                }

                // Talimat kartı
                if (showInstructions) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .constrainAs(instructionCard) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(changeButton.bottom, margin = 16.dp)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.7f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Postür Analizi Talimatları",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                TextButton(
                                    onClick = { showInstructions = false }
                                ) {
                                    Text("✕", color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = """
                                    📸 Yan profilden durun
                                    👤 Tam vücut görünür olsun
                                    📏 Doğal postürünüzü koruyun
                                    💡 İyi ışıklandırma altında olun
                                    📐 Kamera göz hizasında olsun
                                """.trimIndent(),
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }

                // Alt kontroller
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(bottomRow) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Galeri butonu
                    Image(
                        painter = painterResource(R.drawable.gallery_icon),
                        contentDescription = "Galeri",
                        modifier = Modifier
                            .size(if (isTablet) 72.dp else 44.dp)
                            .clickable {
                                // Galeri açma işlevi eklenebilir
                            }
                    )

                    Spacer(modifier = Modifier.width(32.dp))

                    // Fotoğraf çekme butonu
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        if (analysisState.isLoading && analysisState.isDetectorInitialized) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(if (isTablet) 98.dp else 58.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp
                            )
                        }

                        val isButtonEnabled = analysisState.isDetectorInitialized &&
                                !analysisState.isLoading

                        Image(
                            painter = painterResource(R.drawable.take_photo),
                            contentDescription = "Fotoğraf Çek",
                            modifier = Modifier
                                .size(if (isTablet) 98.dp else 58.dp)
                                .clickable(enabled = isButtonEnabled) {
                                    if (isButtonEnabled) {
                                        takePhotoForPostureAnalysis(
                                            controller = controller,
                                            context = context,
                                            analysisViewModel = analysisViewModel
                                        )
                                    }
                                }
                        )

                        // Buton devre dışı ise overlay göster
                        if (!isButtonEnabled) {
                            Box(
                                modifier = Modifier
                                    .size(if (isTablet) 98.dp else 58.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.5f),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!analysisState.isDetectorInitialized && !analysisState.isLoading) {
                                    Text(
                                        text = "!",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    // Flaş butonu
                    if (flash) {
                        Image(
                            painter = painterResource(
                                if (isTorchOn.value) R.drawable.flash_on_icon
                                else R.drawable.flash_off_icon
                            ),
                            contentDescription = if (isTorchOn.value) "Flaşı Kapat" else "Flaşı Aç",
                            modifier = Modifier
                                .size(if (isTablet) 72.dp else 44.dp)
                                .clickable {
                                    controller.enableTorch(!isTorchOn.value)
                                    isTorchOn.value = !isTorchOn.value
                                }
                        )
                    } else {
                        // Flaş yoksa boş alan
                        Spacer(modifier = Modifier.size(if (isTablet) 72.dp else 44.dp))
                    }
                }
            }

            // Hata mesajı gösterme
            analysisState.errorMessage?.let { errorMessage ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (!analysisState.isDetectorInitialized) "Başlatma Hatası" else "Analiz Hatası",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!analysisState.isDetectorInitialized) {
                                Button(
                                    onClick = { analysisViewModel.retryInitialization() }
                                ) {
                                    Text("Yeniden Dene")
                                }
                            }
                            OutlinedButton(
                                onClick = { analysisViewModel.clearError() }
                            ) {
                                Text("Tamam")
                            }
                        }
                    }
                }
            }

            // Analiz durumu gösterme (sadece analiz sırasında)
            if (analysisState.isLoading && analysisState.isDetectorInitialized) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Postür Analiz Ediliyor...",
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Lütfen bekleyiniz",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Detector başlatılıyor overlay'i
            if (analysisState.isLoading && !analysisState.isDetectorInitialized) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Pose Detector Başlatılıyor...",
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "İlk başlatma biraz zaman alabilir.\nModel dosyası yükleniyor...",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// Postür analizi için özel fotoğraf çekme fonksiyonu
private fun takePhotoForPostureAnalysis(
    controller: LifecycleCameraController,
    context: Context,
    analysisViewModel: AnalysisViewModel
) {
    val file = File(context.cacheDir, "posture_photo_${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                try {
                    var bitmap = BitmapFactory.decodeFile(file.absolutePath)

                    // Görüntü döndürme işlemleri
                    val rotation = controller.cameraInfo?.sensorRotationDegrees?.toFloat() ?: 0f
                    val matrix = android.graphics.Matrix()

                    if (controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                        // Ön kamera için yansıtma
                        matrix.preScale(-1f, 1f)
                    }
                    matrix.postRotate(rotation)

                    bitmap = Bitmap.createBitmap(
                        bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                    )

                    // Postür analizi başlat
                    analysisViewModel.analyzeImage(bitmap)

                    // Geçici dosyayı sil
                    file.delete()

                } catch (e: Exception) {
                    Log.e("PostureCamera", "Bitmap processing failed", e)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("PostureCamera", "Photo capture failed", exception)
            }
        }
    )
}

// Mevcut yardımcı fonksiyonlar
private fun saveBitmapToFile(context: Context, bitmap: Bitmap): String? {
    return try {
        val filename = "posture_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        file.absolutePath
    } catch (e: Exception) {
        Log.e("PostureCamera", "Save bitmap failed", e)
        null
    }
}

fun ImageProxy.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer // Y
    val uBuffer = planes[1].buffer // U
    val vBuffer = planes[2].buffer // V

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

fun hasFlashLightDetailed(context: Context): Boolean {
    return try {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList

        for (cameraId in cameraIds) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

            if (flashAvailable == true) {
                return true
            }
        }
        false
    } catch (e: Exception) {
        Log.e("FlashCheck", "Flash kontrol hatası: ${e.message}")
        false
    }
}