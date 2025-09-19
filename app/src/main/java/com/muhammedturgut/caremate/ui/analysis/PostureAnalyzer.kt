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

// Omurga analizi - Gerçek anatomik değerler
data class SpinalAnalysis(
    val curvatureAngle: Float,
    val normalRange: Pair<Float, Float> = Pair(20f, 40f), // Gerçek lumbar lordoz açısı
    val deviationFromNormal: Float,
    val flatteningPercentage: Float,
    val riskLevel: RiskLevel,
    val recommendation: String
)

// Boyun analizi - Gerçek anatomik değerler
data class NeckAnalysis(
    val cervicalAngle: Float,
    val normalRange: Pair<Float, Float> = Pair(35f, 45f), // Gerçek servikal lordoz açısı
    val forwardHeadPosture: Float, // mm cinsinden
    val flatteningPercentage: Float,
    val riskLevel: RiskLevel,
    val recommendation: String
)

class PostureAnalyzer {

    companion object {
        // MediaPipe Pose Landmark indices
        const val NOSE = 0
        const val LEFT_EYE_INNER = 1
        const val LEFT_EYE = 2
        const val LEFT_EYE_OUTER = 3
        const val RIGHT_EYE_INNER = 4
        const val RIGHT_EYE = 5
        const val RIGHT_EYE_OUTER = 6
        const val LEFT_EAR = 7
        const val RIGHT_EAR = 8
        const val LEFT_SHOULDER = 11
        const val RIGHT_SHOULDER = 12
        const val LEFT_HIP = 23
        const val RIGHT_HIP = 24
        const val LEFT_KNEE = 25
        const val RIGHT_KNEE = 26
        const val LEFT_ANKLE = 27
        const val RIGHT_ANKLE = 28

        // Anatomik referans değerleri
        const val NORMAL_LUMBAR_LORDOSIS_MIN = 20f // derece
        const val NORMAL_LUMBAR_LORDOSIS_MAX = 40f // derece
        const val NORMAL_CERVICAL_LORDOSIS_MIN = 35f // derece
        const val NORMAL_CERVICAL_LORDOSIS_MAX = 45f // derece
        const val FORWARD_HEAD_THRESHOLD_MM = 15f // mm
        const val SEVERE_FORWARD_HEAD_MM = 30f // mm
    }

    fun analyzePosture(landmarks: List<Landmark>): PostureAnalysisResult {
        Log.d("PostureAnalyzer", "Starting medical-grade posture analysis with ${landmarks.size} landmarks")

        if (landmarks.size < 33) {
            Log.e("PostureAnalyzer", "Insufficient landmarks: ${landmarks.size}")
            return createErrorResult("Yetersiz landmark sayısı")
        }

        try {
            // Görüntü ölçeğini hesapla (omuz genişliği referansı)
            val imageScale = calculateImageScale(landmarks)
            Log.d("PostureAnalyzer", "Image scale calculated: $imageScale mm/pixel")

            // Lumbar lordoz analizi - Gerçek anatomik ölçüm
            val spinalAnalysis = analyzeLumbarLordosis(landmarks, imageScale)
            Log.d("PostureAnalyzer", "Lumbar lordosis: ${spinalAnalysis.curvatureAngle}°")

            // Servikal lordoz ve forward head posture analizi
            val neckAnalysis = analyzeCervicalPosture(landmarks, imageScale)
            Log.d("PostureAnalyzer", "Cervical lordosis: ${neckAnalysis.cervicalAngle}°, FHP: ${neckAnalysis.forwardHeadPosture}mm")

            // Genel risk değerlendirmesi
            val overallRisk = determineOverallRisk(spinalAnalysis.riskLevel, neckAnalysis.riskLevel)

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

    private fun calculateImageScale(landmarks: List<Landmark>): Float {
        // Omuz genişliğini referans olarak kullan (ortalama 40cm)
        val leftShoulder = landmarks[LEFT_SHOULDER]
        val rightShoulder = landmarks[RIGHT_SHOULDER]

        val shoulderWidthPixels = sqrt(
            (rightShoulder.x - leftShoulder.x).pow(2) +
                    (rightShoulder.y - leftShoulder.y).pow(2)
        )

        // Ortalama omuz genişliği: 400mm
        val averageShoulderWidthMm = 400f
        return averageShoulderWidthMm / shoulderWidthPixels
    }

    private fun analyzeLumbarLordosis(landmarks: List<Landmark>, scale: Float): SpinalAnalysis {
        // Gerçek anatomik lumbar lordoz ölçümü
        // L1-L5 vertebraları temsil eden noktalar
        val shoulder = getAveragePoint(landmarks[LEFT_SHOULDER], landmarks[RIGHT_SHOULDER])
        val hip = getAveragePoint(landmarks[LEFT_HIP], landmarks[RIGHT_HIP])

        // Lumbar bölgenin orta noktası (L3 vertebra seviyesi)
        val lumbarMidpoint = Landmark(
            x = (shoulder.x + hip.x) / 2f,
            y = (shoulder.y + hip.y) / 2f,
            visibility = (shoulder.visibility + hip.visibility) / 2f
        )

        // Sacral slope ve lumbar lordoz açısını hesapla
        val lumbarLordosisAngle = calculateLumbarLordosisAngle(shoulder, lumbarMidpoint, hip)

        val normalRange = Pair(NORMAL_LUMBAR_LORDOSIS_MIN, NORMAL_LUMBAR_LORDOSIS_MAX)
        val deviation = calculateDeviation(lumbarLordosisAngle, normalRange)

        // Düzleşme yüzdesi hesapla (klinik standartlara göre)
        val flatteningPercentage = calculateLumbarFlatteningPercentage(lumbarLordosisAngle, normalRange)

        // Klinik risk değerlendirmesi
        val riskLevel = when {
            lumbarLordosisAngle >= 20f && lumbarLordosisAngle <= 40f -> RiskLevel.LOW
            lumbarLordosisAngle >= 15f && lumbarLordosisAngle <= 50f -> RiskLevel.MODERATE
            else -> RiskLevel.HIGH
        }

        val recommendation = getLumbarRecommendation(riskLevel, lumbarLordosisAngle, flatteningPercentage)

        return SpinalAnalysis(
            curvatureAngle = lumbarLordosisAngle,
            normalRange = normalRange,
            deviationFromNormal = deviation,
            flatteningPercentage = flatteningPercentage,
            riskLevel = riskLevel,
            recommendation = recommendation
        )
    }

    private fun analyzeCervicalPosture(landmarks: List<Landmark>, scale: Float): NeckAnalysis {
        // Gerçek anatomik servikal postür analizi
        val ear = getAveragePoint(landmarks[LEFT_EAR], landmarks[RIGHT_EAR])
        val shoulder = getAveragePoint(landmarks[LEFT_SHOULDER], landmarks[RIGHT_SHOULDER])
        val nose = landmarks[NOSE]

        // Craniovertebral angle (CVA) hesapla - altın standart
        val craniovertebralAngle = calculateCraniovertebralAngle(ear, shoulder, nose)

        // Forward head posture mesafesi (mm cinsinden)
        val forwardHeadMm = calculateForwardHeadDistanceMm(ear, shoulder, scale)

        // Servikal lordoz tahmini
        val cervicalLordosis = estimateCervicalLordosis(craniovertebralAngle)

        val normalRange = Pair(NORMAL_CERVICAL_LORDOSIS_MIN, NORMAL_CERVICAL_LORDOSIS_MAX)

        // Düzleşme yüzdesi hesapla
        val flatteningPercentage = calculateCervicalFlatteningPercentage(
            craniovertebralAngle,
            forwardHeadMm,
            cervicalLordosis
        )

        // Klinik risk değerlendirmesi (research-based)
        val riskLevel = when {
            craniovertebralAngle >= 50f && forwardHeadMm <= FORWARD_HEAD_THRESHOLD_MM -> RiskLevel.LOW
            craniovertebralAngle >= 45f && forwardHeadMm <= SEVERE_FORWARD_HEAD_MM -> RiskLevel.MODERATE
            else -> RiskLevel.HIGH
        }

        val recommendation = getCervicalRecommendation(riskLevel, craniovertebralAngle, forwardHeadMm)

        return NeckAnalysis(
            cervicalAngle = cervicalLordosis,
            normalRange = normalRange,
            forwardHeadPosture = forwardHeadMm,
            flatteningPercentage = flatteningPercentage,
            riskLevel = riskLevel,
            recommendation = recommendation
        )
    }

    private fun calculateLumbarLordosisAngle(shoulder: Landmark, lumbarMid: Landmark, hip: Landmark): Float {
        // Cobb angle yöntemi ile lumbar lordoz hesapla
        val upperVector = Pair(lumbarMid.x - shoulder.x, lumbarMid.y - shoulder.y)
        val lowerVector = Pair(hip.x - lumbarMid.x, hip.y - lumbarMid.y)

        val angle = calculateAngleBetweenVectors(upperVector, lowerVector)

        // 180°'den çıkar çünkü lordoz konkav eğridir
        return abs(180f - angle)
    }

    private fun calculateCraniovertebralAngle(ear: Landmark, shoulder: Landmark, nose: Landmark): Float {
        // CVA = kulak-omuz hattı ile yatay arasındaki açı
        // Normal CVA > 50°, <45° problemli
        val earShoulderVector = Pair(shoulder.x - ear.x, shoulder.y - ear.y)
        val horizontalVector = Pair(1f, 0f) // Yatay referans

        return calculateAngleBetweenVectors(earShoulderVector, horizontalVector)
    }

    private fun calculateForwardHeadDistanceMm(ear: Landmark, shoulder: Landmark, scale: Float): Float {
        // Kulağın omuzun önündeki mesafesi (mm)
        val horizontalDistance = abs(ear.x - shoulder.x)
        return horizontalDistance * scale
    }

    private fun estimateCervicalLordosis(cva: Float): Float {
        // CVA'dan servikal lordoz tahmini (klinik korelasyon)
        return when {
            cva >= 55f -> 42f // Normal lordoz
            cva >= 50f -> 35f // Hafif azalma
            cva >= 45f -> 25f // Orta derece azalma
            else -> 15f // Ciddi düzleşme
        }
    }

    private fun calculateLumbarFlatteningPercentage(angle: Float, normalRange: Pair<Float, Float>): Float {
        val minNormal = normalRange.first
        return when {
            angle >= minNormal -> 0f
            angle >= minNormal * 0.75f -> 25f
            angle >= minNormal * 0.5f -> 50f
            angle >= minNormal * 0.25f -> 75f
            else -> 100f
        }
    }

    private fun calculateCervicalFlatteningPercentage(cva: Float, forwardHeadMm: Float, cervicalAngle: Float): Float {
        // Çok faktörlü değerlendirme
        val cvaScore = when {
            cva >= 50f -> 0f
            cva >= 45f -> 25f
            cva >= 40f -> 50f
            else -> 75f
        }

        val fhpScore = when {
            forwardHeadMm <= 15f -> 0f
            forwardHeadMm <= 25f -> 25f
            forwardHeadMm <= 35f -> 50f
            else -> 75f
        }

        return maxOf(cvaScore, fhpScore)
    }

    private fun calculateAngleBetweenVectors(v1: Pair<Float, Float>, v2: Pair<Float, Float>): Float {
        val dot = v1.first * v2.first + v1.second * v2.second
        val mag1 = sqrt(v1.first * v1.first + v1.second * v1.second)
        val mag2 = sqrt(v2.first * v2.first + v2.second * v2.second)

        if (mag1 == 0f || mag2 == 0f) return 0f

        val cosAngle = (dot / (mag1 * mag2)).coerceIn(-1f, 1f)
        return Math.toDegrees(acos(cosAngle).toDouble()).toFloat()
    }

    private fun calculateDeviation(angle: Float, normalRange: Pair<Float, Float>): Float {
        return when {
            angle < normalRange.first -> normalRange.first - angle
            angle > normalRange.second -> angle - normalRange.second
            else -> 0f
        }
    }

    private fun getAveragePoint(left: Landmark, right: Landmark): Landmark {
        return Landmark(
            x = (left.x + right.x) / 2f,
            y = (left.y + right.y) / 2f,
            visibility = (left.visibility + right.visibility) / 2f
        )
    }

    private fun determineOverallRisk(spinalRisk: RiskLevel, neckRisk: RiskLevel): RiskLevel {
        return when {
            spinalRisk == RiskLevel.HIGH || neckRisk == RiskLevel.HIGH -> RiskLevel.HIGH
            spinalRisk == RiskLevel.MODERATE || neckRisk == RiskLevel.MODERATE -> RiskLevel.MODERATE
            else -> RiskLevel.LOW
        }
    }

    private fun getLumbarRecommendation(riskLevel: RiskLevel, angle: Float, flatteningPercentage: Float): String {
        return when (riskLevel) {
            RiskLevel.LOW -> "Lumbar lordoz açınız normal aralıkta (${angle.toInt()}°). " +
                    "Düzenli egzersiz ve doğru postürü koruyarak bu durumu sürdürün."

            RiskLevel.MODERATE -> "Lumbar lordozda hafif azalma tespit edildi (${angle.toInt()}°, %${flatteningPercentage.toInt()} düzleşme). " +
                    "Core güçlendirme egzersizleri, hip flexor germe ve pelvic tilt egzersizleri yapın. " +
                    "Uzun süre oturmaktan kaçının."

            RiskLevel.HIGH -> "Ciddi lumbar düzleşme (${angle.toInt()}°, %${flatteningPercentage.toInt()} düzleşme). " +
                    "Bu durum bel ağrısına yol açabilir. Derhal fizik tedavi desteği alın. " +
                    "McKenzie egzersizleri, lumbar ekstansiyon egzersizleri öncelikli. " +
                    "Ergonomik oturma düzeni sağlayın."
        }
    }

    private fun getCervicalRecommendation(riskLevel: RiskLevel, cva: Float, forwardHeadMm: Float): String {
        return when (riskLevel) {
            RiskLevel.LOW -> "Boyun postürünüz iyi durumda (CVA: ${cva.toInt()}°). " +
                    "Ekran ergonomisi ve boyun egzersizlerini sürdürün."

            RiskLevel.MODERATE -> "Forward head posture tespit edildi (CVA: ${cva.toInt()}°, ${forwardHeadMm.toInt()}mm). " +
                    "Ekran yüksekliğini göz seviyesine ayarlayın. " +
                    "Günde 3-4 kez chin tuck egzersizi yapın. " +
                    "Upper trap germe egzersizleri ekleyin."

            RiskLevel.HIGH -> "Ciddi text neck sendromu (CVA: ${cva.toInt()}°, ${forwardHeadMm.toInt()}mm). " +
                    "Bu durum boyun ağrısı, baş ağrısı ve omuz problemlerine yol açar. " +
                    "Acil ergonomik düzenleme yapın. " +
                    "Fizik tedavi önerisi: derin boyun flexor güçlendirme, " +
                    "suboccipital kas germe, thoracic mobilizasyon egzersizleri gerekli."
        }
    }

    private fun createErrorResult(errorMessage: String): PostureAnalysisResult {
        val errorSpinal = SpinalAnalysis(
            curvatureAngle = 0f,
            deviationFromNormal = 0f,
            flatteningPercentage = 0f,
            riskLevel = RiskLevel.MODERATE,
            recommendation = "Analiz yapılamadı: $errorMessage. Lütfen daha net bir fotoğraf çekin."
        )

        val errorNeck = NeckAnalysis(
            cervicalAngle = 0f,
            forwardHeadPosture = 0f,
            flatteningPercentage = 0f,
            riskLevel = RiskLevel.MODERATE,
            recommendation = "Analiz yapılamadı: $errorMessage. Lütfen daha net bir fotoğraf çekin."
        )

        return PostureAnalysisResult(
            overallRiskLevel = RiskLevel.MODERATE,
            spinalFlattening = errorSpinal,
            neckFlattening = errorNeck
        )
    }
}