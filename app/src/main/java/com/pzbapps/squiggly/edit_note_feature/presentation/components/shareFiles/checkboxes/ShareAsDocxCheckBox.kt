package com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.checkboxes

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider

import com.chaquo.python.Python
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun exportToDocxCheckBoxes(
    context: Context,
    title: String,
    checkboxStates: ArrayList<Boolean>,
    checkboxTexts: ArrayList<String>
) {


    val py = Python.getInstance()
    val pyModule =
        py.getModule("shareNotesAsDocxCheckbox")  // Assuming Python script name is 'docx_export.py'

    // Convert data to Python-friendly formats
//    val pyCheckboxStates = PyObject.fromJava(checkboxStates)
//    val pyCheckboxTexts = PyObject.fromJava(checkboxTexts)

    // Call the Python function to generate the docx file in memory
    val result = pyModule.callAttr(
        "export_to_docx_in_memory",
        title,
        checkboxStates.toTypedArray(),
        checkboxTexts.toTypedArray()
    )

    // Get the byte array of the file content
    val docxBytes = result.toJava(ByteArray::class.java)
    var fileName = UUID.randomUUID()

    // Create a temporary file to share
    val docxFile = File(context.cacheDir, "$fileName.docx")
    FileOutputStream(docxFile).use { outputStream ->
        outputStream.write(docxBytes)
    }

    // Share the file
    shareDocxFile(context, docxFile.absolutePath)
}

private fun shareDocxFile(context: Context, filePath: String) {
    val file = File(filePath)
    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        putExtra(Intent.EXTRA_STREAM, fileUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Share .docx using"))
}
