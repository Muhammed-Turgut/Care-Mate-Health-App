package com.muhammedturgut.caremate.pose

import android.util.Log
import com.muhammedturgut.caremate.ui.analysis.Landmark
import kotlin.math.*

// Risk seviyeleri
enum class RiskLevel(val description: String, val color: String) {
    LOW("İyi Durum", "#4CAF50"),
    MODERATE("Orta Risk", "#FF9800"),
    HIGH("Yüksek Risk", "#F44336")
}

// Postür analiz sonucu
data class PostureAnalysisResult(
    val overallRiskLevel: RiskLevel,
    val spinalFlattening: SpinalAnalysis,
    val neckFlattening: NeckAnalysis,
    val timestamp: Long = System.currentTimeMillis()
)

// Omurga analizi
data class SpinalAnalysis(
    val curvatureAngle: Float,
    val normalRange: Pair<Float, Float> = Pair(20f, 40f), // Normal lordoz açısı
    val flatteningPercentage: Float,
    val riskLevel: RiskLevel,
    val recommendation: String
)

// Boyun analizi
data class NeckAnalysis(
    val cervicalAngle: Float,
    val normalRange: Pair<Float, Float> = Pair(35f, 45f), // Normal servikal lordoz
    val flatteningPercentage: Float,
    val riskLevel: RiskLevel,
    val recommendation: String
)

class PostureAnalyzer {

    // MediaPipe Pose Landmark indeksleri
    companion object {
        const val NOSE = 0
        const val LEFT_EYE_INNER = 1
        const val LEFT_EYE = 2
        const val LEFT_EYE_OUTER = 3
        const val RIGHT_EYE_INNER = 4
        const val RIGHT_EYE = 5
        const val RIGHT_EYE_OUTER = 6
        const val LEFT_EAR = 7
        const val RIGHT_EAR = 8
        const val MOUTH_LEFT = 9
        const val MOUTH_RIGHT = 10
        const val LEFT_SHOULDER = 11
        const val RIGHT_SHOULDER = 12
        const val LEFT_ELBOW = 13
        const val RIGHT_ELBOW = 14
        const val LEFT_WRIST = 15
        const val RIGHT_WRIST = 16
        const val LEFT_PINKY = 17
        const val RIGHT_PINKY = 18
        const val LEFT_INDEX = 19
        const val RIGHT_INDEX = 20
        const val LEFT_THUMB = 21
        const val RIGHT_THUMB = 22
        const val LEFT_HIP = 23
        const val RIGHT_HIP = 24
        const val LEFT_KNEE = 25
        const val RIGHT_KNEE = 26
        const val LEFT_ANKLE = 27
        const val RIGHT_ANKLE = 28
        const val LEFT_HEEL = 29
        const val RIGHT_HEEL = 30
        const val LEFT_FOOT_INDEX = 31
        const val RIGHT_FOOT_INDEX = 32
    }

    fun analyzePosture(landmarks: List<Landmark>): PostureAnalysisResult {
        Log.d("PostureAnalyzer", "Starting posture analysis with ${landmarks.size} landmarks")

        if (landmarks.size < 33) {
            Log.e("PostureAnalyzer", "Insufficient landmarks: ${landmarks.size}")
            return createErrorResult("Yetersiz landmark sayısı")
        }

        try {
            // Omurga analizi
            val spinalAnalysis = analyzeSpinalCurvature(landmarks)
            Log.d("PostureAnalyzer", "Spinal analysis completed: ${spinalAnalysis.riskLevel}")

            // Boyun analizi
            val neckAnalysis = analyzeCervicalCurvature(landmarks)
            Log.d("PostureAnalyzer", "Neck analysis completed: ${neckAnalysis.riskLevel}")

            // Genel risk seviyesi belirleme
            val overallRisk = determineOverallRisk(spinalAnalysis.riskLevel, neckAnalysis.riskLevel)
            Log.d("PostureAnalyzer", "Overall risk determined: $overallRisk")

            return PostureAnalysisResult(
                overallRiskLevel = overallRisk,
                spinalFlattening = spinalAnalysis,
                neckFlattening = neckAnalysis
            )

        } catch (e: Exception) {
            Log.e("PostureAnalyzer", "Analysis failed", e)
            return createErrorResult("Analiz sırasında hata: ${e.message}")
        }
    }

    private fun analyzeSpinalCurvature(landmarks: List<Landmark>): SpinalAnalysis {
        // Omurga eğriliği için önemli noktalar
        val shoulder = landmarks[LEFT_SHOULDER] // Sol omuz
        val hip = landmarks[LEFT_HIP] // Sol kalça
        val knee = landmarks[LEFT_KNEE] // Sol diz

        // Omurga hattının açısını hesapla
        val spinalAngle = calculateSpinalAngle(shoulder, hip, knee)

        // Normal aralık: 20-40 derece
        val normalRange = Pair(20f, 40f)
        val flatteningPercentage = calculateFlatteningPercentage(spinalAngle, normalRange)

        val riskLevel = when {
            spinalAngle >= normalRange.first && spinalAngle <= normalRange.second -> RiskLevel.LOW
            flatteningPercentage < 20 -> RiskLevel.MODERATE
            else -> RiskLevel.HIGH
        }

        val recommendation = getSpinalRecommendation(riskLevel, flatteningPercentage)

        return SpinalAnalysis(
            curvatureAngle = spinalAngle,
            normalRange = normalRange,
            flatteningPercentage = flatteningPercentage,
            riskLevel = riskLevel,
            recommendation = recommendation
        )
    }

    private fun analyzeCervicalCurvature(landmarks: List<Landmark>): NeckAnalysis {
        // Boyun eğriliği için önemli noktalar
        val ear = landmarks[LEFT_EAR] // Sol kulak
        val shoulder = landmarks[LEFT_SHOULDER] // Sol omuz
        val nose = landmarks[NOSE] // Burun

        // Boyun açısını hesapla
        val cervicalAngle = calculateCervicalAngle(ear, shoulder, nose)

        // Normal aralık: 35-45 derece
        val normalRange = Pair(35f, 45f)
        val flatteningPercentage = calculateFlatteningPercentage(cervicalAngle, normalRange)

        val riskLevel = when {
            cervicalAngle >= normalRange.first && cervicalAngle <= normalRange.second -> RiskLevel.LOW
            flatteningPercentage < 25 -> RiskLevel.MODERATE
            else -> RiskLevel.HIGH
        }

        val recommendation = getCervicalRecommendation(riskLevel, flatteningPercentage)

        return NeckAnalysis(
            cervicalAngle = cervicalAngle,
            normalRange = normalRange,
            flatteningPercentage = flatteningPercentage,
            riskLevel = riskLevel,
            recommendation = recommendation
        )
    }

    private fun calculateSpinalAngle(shoulder: Landmark, hip: Landmark, knee: Landmark): Float {
        // Omuz-kalça ve kalça-diz vektörleri arasındaki açı
        val vector1 = Pair(hip.x - shoulder.x, hip.y - shoulder.y)
        val vector2 = Pair(knee.x - hip.x, knee.y - hip.y)

        val angle = calculateAngleBetweenVectors(vector1, vector2)
        return abs(180 - angle) // 180'den çıkararak lordoz açısını elde et
    }

    private fun calculateCervicalAngle(ear: Landmark, shoulder: Landmark, nose: Landmark): Float {
        // Kulak-omuz ve burun-kulak vektörleri arasındaki açı
        val vector1 = Pair(shoulder.x - ear.x, shoulder.y - ear.y)
        val vector2 = Pair(nose.x - ear.x, nose.y - ear.y)

        return calculateAngleBetweenVectors(vector1, vector2)
    }

    private fun calculateAngleBetweenVectors(vector1: Pair<Float, Float>, vector2: Pair<Float, Float>): Float {
        val dot = vector1.first * vector2.first + vector1.second * vector2.second
        val magnitude1 = sqrt(vector1.first * vector1.first + vector1.second * vector1.second)
        val magnitude2 = sqrt(vector2.first * vector2.first + vector2.second * vector2.second)

        if (magnitude1 == 0f || magnitude2 == 0f) return 0f

        val cosAngle = dot / (magnitude1 * magnitude2)
        val clampedCos = cosAngle.coerceIn(-1f, 1f)

        return Math.toDegrees(acos(clampedCos).toDouble()).toFloat()
    }

    private fun calculateFlatteningPercentage(angle: Float, normalRange: Pair<Float, Float>): Float {
        val midNormal = (normalRange.first + normalRange.second) / 2
        val deviation = abs(angle - midNormal)
        val maxDeviation = max(midNormal - normalRange.first, normalRange.second - midNormal)

        return if (maxDeviation > 0) {
            (deviation / maxDeviation * 100).coerceAtMost(100f)
        } else 0f
    }

    private fun determineOverallRisk(spinalRisk: RiskLevel, neckRisk: RiskLevel): RiskLevel {
        return when {
            spinalRisk == RiskLevel.HIGH || neckRisk == RiskLevel.HIGH -> RiskLevel.HIGH
            spinalRisk == RiskLevel.MODERATE || neckRisk == RiskLevel.MODERATE -> RiskLevel.MODERATE
            else -> RiskLevel.LOW
        }
    }

    private fun getSpinalRecommendation(riskLevel: RiskLevel, flatteningPercentage: Float): String {
        return when (riskLevel) {
            RiskLevel.LOW -> "Bel eğriliğiniz normal aralıkta. Bu durumu korumak için düzenli egzersiz yapın ve doğru oturuş pozisyonu alın."

            RiskLevel.MODERATE -> "Bel eğriliğinizde orta düzeyde değişiklik tespit edildi (${flatteningPercentage.toInt()}% sapma). " +
                    "Sırt kaslarınızı güçlendiren egzersizler yapın ve uzun süre aynı pozisyonda kalmaktan kaçının."

            RiskLevel.HIGH -> "Bel eğriliğinizde ciddi düzeyde düzleşme tespit edildi (${flatteningPercentage.toInt()}% sapma). " +
                    "Fizik tedavi uzmanına danışmanızı öneriyoruz. Düzenli stretching ve core güçlendirme egzersizleri yapın."
        }
    }

    private fun getCervicalRecommendation(riskLevel: RiskLevel, flatteningPercentage: Float): String {
        return when (riskLevel) {
            RiskLevel.LOW -> "Boyun eğriliğiniz normal aralıkta. Bu durumu korumak için boyun egzersizleri yapın ve ekran yüksekliğinizi ayarlayın."

            RiskLevel.MODERATE -> "Boyun eğriliğinizde orta düzeyde değişiklik tespit edildi (${flatteningPercentage.toInt()}% sapma). " +
                    "Boyun stretching egzersizleri yapın ve masa başı duruşunuzu düzeltin."

            RiskLevel.HIGH -> "Boyun eğriliğinizde ciddi düzeyde düzleşme tespit edildi (${flatteningPercentage.toInt()}% sapma). " +
                    "Fizik tedavi uzmanına danışmanızı öneriyoruz. 'Text neck' durumundan kaçının ve düzenli boyun egzersizleri yapın."
        }
    }

    private fun createErrorResult(errorMessage: String): PostureAnalysisResult {
        val errorAnalysis = SpinalAnalysis(
            curvatureAngle = 0f,
            flatteningPercentage = 0f,
            riskLevel = RiskLevel.HIGH,
            recommendation = "Analiz yapılamadı: $errorMessage"
        )

        val errorNeckAnalysis = NeckAnalysis(
            cervicalAngle = 0f,
            flatteningPercentage = 0f,
            riskLevel = RiskLevel.HIGH,
            recommendation = "Analiz yapılamadı: $errorMessage"
        )

        return PostureAnalysisResult(
            overallRiskLevel = RiskLevel.HIGH,
            spinalFlattening = errorAnalysis,
            neckFlattening = errorNeckAnalysis
        )
    }
}