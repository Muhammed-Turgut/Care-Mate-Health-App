package com.muhammedturgut.caremate.ui.analysis

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedturgut.caremate.pose.PoseDetector
import com.muhammedturgut.caremate.pose.PostureAnalyzer
import com.muhammedturgut.caremate.pose.PostureAnalysisResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalysisUiState(
    val isLoading: Boolean = false,
    val analysisResult: PostureAnalysisResult? = null,
    val errorMessage: String? = null,
    val landmarks: List<Landmark> = emptyList(),
    val isDetectorInitialized: Boolean = false // Detector durumunu takip et
)

data class Landmark(
    val x: Float,
    val y: Float,
    val visibility: Float = 1f
)

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    private var poseDetector: PoseDetector? = null
    private val postureAnalyzer = PostureAnalyzer()

    init {
        Log.d("AnalysisViewModel", "ViewModel initialized, starting pose detector...")
        initializePoseDetector()
    }

    private fun initializePoseDetector() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                Log.d("AnalysisViewModel", "Creating PoseDetector instance...")
                poseDetector = PoseDetector(context)

                Log.d("AnalysisViewModel", "Initializing detector...")
                val initialized = poseDetector?.initializeDetector() ?: false

                if (initialized) {
                    Log.d("AnalysisViewModel", "Pose detector initialized successfully")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isDetectorInitialized = true,
                        errorMessage = null
                    )
                } else {
                    Log.e("AnalysisViewModel", "Pose detector initialization failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isDetectorInitialized = false,
                        errorMessage = "Pose detector başlatılamadı. Lütfen model dosyasının assets klasöründe olduğunu kontrol edin."
                    )
                }
            } catch (e: Exception) {
                Log.e("AnalysisViewModel", "Initialization exception", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isDetectorInitialized = false,
                    errorMessage = "Başlatma hatası: ${e.message}"
                )
            }
        }
    }

    fun analyzeImage(bitmap: Bitmap) {
        Log.d("AnalysisViewModel", "analyzeImage called with bitmap: ${bitmap.width}x${bitmap.height}")

        if (!_uiState.value.isDetectorInitialized) {
            Log.w("AnalysisViewModel", "Detector not initialized, attempting to reinitialize...")
            _uiState.value = _uiState.value.copy(
                errorMessage = "Pose detector henüz hazır değil. Lütfen bekleyin veya uygulamayı yeniden başlatın."
            )
            // Detector'ı yeniden başlatmayı dene
            initializePoseDetector()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                Log.d("AnalysisViewModel", "Starting pose detection...")
                val poseResult = poseDetector?.detectPose(bitmap)
                Log.d("AnalysisViewModel", "Pose detection result: $poseResult")

                if (poseResult != null && poseResult.landmarks().isNotEmpty()) {
                    Log.d("AnalysisViewModel", "Pose detected successfully, found ${poseResult.landmarks().size} poses")
                    Log.d("AnalysisViewModel", "First pose has ${poseResult.landmarks()[0].size} landmarks")

                    // MediaPipe landmark'larını UI landmark'larına dönüştür
                    val landmarks = poseResult.landmarks()[0].map { landmark ->
                        Landmark(
                            x = landmark.x(),
                            y = landmark.y(),
                            visibility = landmark.visibility().orElse(1f)
                        )
                    }

                    Log.d("AnalysisViewModel", "Converted ${landmarks.size} landmarks, starting posture analysis...")

                    // Postür analizini yap - landmarks listesini geçir
                    val analysisResult = postureAnalyzer.analyzePosture(landmarks)

                    Log.d("AnalysisViewModel", "Posture analysis completed successfully")
                    Log.d("AnalysisViewModel", "Analysis result: $analysisResult")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        analysisResult = analysisResult,
                        landmarks = landmarks
                    )
                } else {
                    Log.w("AnalysisViewModel", "No pose detected in image")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Herhangi bir poz algılanamadı. Lütfen:\n" +
                                "• Tam vücut görünür olacak şekilde fotoğraf çekin\n" +
                                "• İyi ışıklandırma altında olun\n" +
                                "• Kamera ile aranızda uygun mesafe olsun\n" +
                                "• Yan profilden durmayı deneyin"
                    )
                }
            } catch (e: Exception) {
                Log.e("AnalysisViewModel", "Analysis failed", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Analiz sırasında hata oluştu: ${e.message}\n\nLütfen tekrar deneyin."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetAnalysis() {
        Log.d("AnalysisViewModel", "resetAnalysis called - keeping detector alive")
        _uiState.value = AnalysisUiState(
            isDetectorInitialized = _uiState.value.isDetectorInitialized,
            // analysisResult'ı temizleme - sonuç ekranı için koru
        )
    }

    fun retryInitialization() {
        Log.d("AnalysisViewModel", "Retrying detector initialization...")
        initializePoseDetector()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AnalysisViewModel", "ViewModel clearing, closing pose detector...")
        poseDetector?.close()
    }
}