package com.muhammedturgut.caremate.pose

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PoseDetector(private val context: Context) {

    private var poseLandmarker: PoseLandmarker? = null

    suspend fun initializeDetector(): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d("PoseDetector", "Starting pose detector initialization...")

            // Model dosyasının varlığını kontrol et
            val assetManager = context.assets
            val modelFiles = assetManager.list("") ?: emptyArray()
            Log.d("PoseDetector", "Available asset files: ${modelFiles.joinToString()}")

            // Model dosyası yolunu kontrol et - farklı isimler deneyebiliriz
            val possibleModelNames = listOf(
                "pose_landmarker_lite.task",
                "pose_landmarker.task",
                "pose_landmarker_full.task",
                "pose_landmarker_heavy.task"
            )

            var modelPath: String? = null
            for (modelName in possibleModelNames) {
                try {
                    val inputStream = assetManager.open(modelName)
                    inputStream.close()
                    modelPath = modelName
                    Log.d("PoseDetector", "Found model file: $modelName")
                    break
                } catch (e: Exception) {
                    Log.d("PoseDetector", "Model $modelName not found")
                }
            }

            if (modelPath == null) {
                Log.e("PoseDetector", "No pose landmarker model found in assets. Please add one of: ${possibleModelNames.joinToString()}")
                return@withContext false
            }

            val baseOptions = BaseOptions.builder()
                .setModelAssetPath(modelPath)
                .build()

            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setRunningMode(RunningMode.IMAGE)
                .setNumPoses(1) // Tek kişi için optimize et
                .setMinPoseDetectionConfidence(0.3f) // Threshold'u düşür
                .setMinPosePresenceConfidence(0.3f)
                .setMinTrackingConfidence(0.3f)
                .setOutputSegmentationMasks(false) // Performans için kapatılabilir
                .build()

            Log.d("PoseDetector", "Creating PoseLandmarker with options...")
            poseLandmarker = PoseLandmarker.createFromOptions(context, options)

            Log.d("PoseDetector", "PoseLandmarker created successfully")
            true
        } catch (e: Exception) {
            Log.e("PoseDetector", "Pose detector initialization failed", e)
            Log.e("PoseDetector", "Error message: ${e.message}")
            Log.e("PoseDetector", "Error cause: ${e.cause}")
            false
        }
    }

    suspend fun detectPose(bitmap: Bitmap): PoseLandmarkerResult? = withContext(Dispatchers.IO) {
        try {
            val detector = poseLandmarker
            if (detector == null) {
                Log.e("PoseDetector", "PoseLandmarker is not initialized")
                return@withContext null
            }

            Log.d("PoseDetector", "Converting bitmap to MPImage...")
            val mpImage = BitmapImageBuilder(bitmap).build()

            Log.d("PoseDetector", "Detecting pose...")
            val result = detector.detect(mpImage)

            Log.d("PoseDetector", "Pose detection completed. Found ${result.landmarks().size} pose(s)")
            result
        } catch (e: Exception) {
            Log.e("PoseDetector", "Pose detection failed", e)
            Log.e("PoseDetector", "Error details: ${e.message}")
            null
        }
    }

    fun close() {
        try {
            poseLandmarker?.close()
            poseLandmarker = null
            Log.d("PoseDetector", "PoseDetector closed successfully")
        } catch (e: Exception) {
            Log.e("PoseDetector", "Error closing PoseDetector", e)
        }
    }
}