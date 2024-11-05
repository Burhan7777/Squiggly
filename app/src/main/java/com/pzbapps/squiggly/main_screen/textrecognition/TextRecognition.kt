package com.pzbapps.squiggly.main_screen.textrecognition

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun TextRecognition(
    result: MutableState<GmsDocumentScanningResult?>,
    showTextRecognitionDialog: MutableState<Boolean>,
    recognizedText: MutableState<StringBuilder>,
    showProgressBarOfExtractingText: MutableState<Boolean>,
    selectedScript: MutableState<String>
) {

    val context = LocalContext.current
    lateinit var recognizer: TextRecognizer
    LaunchedEffect(true) {
        recognizedText.value.clear()
    }
    var listOfImageUris = remember { mutableStateListOf<Uri?>() }
    LaunchedEffect(result.value) {
        listOfImageUris.clear()
        result.value?.pages?.forEach { page ->
            listOfImageUris.add(page.imageUri)
        }
    // Clear previous text on each new scan
    }

    println(listOfImageUris.size)

    LaunchedEffect(result.value) {
        showProgressBarOfExtractingText.value = true
        if (selectedScript.value == "English") {
            recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        } else if (selectedScript.value == "Hindi") {
            recognizer =
                TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())
        } else if (selectedScript.value == "Japanese") {
            recognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
        } else if (selectedScript.value == "Korean") {
            recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        } else if (selectedScript.value == "Chinese") {
            recognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
        }
        for (uri in listOfImageUris) {
            uri?.let {
                val image = InputImage.fromFilePath(context, it)

                // Suspend and await recognition result
                val textResult = withContext(Dispatchers.IO) {
                    runCatching { recognizer.process(image).await() }
                        .getOrNull() // Get text or null if failed
                }

                // If text is retrieved, append to recognizedText
                textResult?.textBlocks?.forEach { block ->
                    recognizedText.value.append("${block.text}\n\n")
                }
            }
        }

        // Show dialog only after all text blocks have been processed
        showProgressBarOfExtractingText.value = false
        if (recognizedText.value.isNotEmpty()) {
            showTextRecognitionDialog.value = true
            println("VALUE: ${recognizedText.value}")
        } else {
            Toast.makeText(context, "Failed to recognize text", Toast.LENGTH_SHORT).show()
        }
    }
}





