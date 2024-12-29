package com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.bulletpoints

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun exportBulletPointsToDocx(context: Context, title: String, bulletPoints: ArrayList<String>) {
    // Initialize Python
    val py = Python.getInstance()
    val pyObj: PyObject =
        py.getModule("shareNotesAsDocxBulletPoints") // The name of your Python file without .py

    // Call the Python function to get the .docx binary content
    var fileName = UUID.randomUUID()
    val result: PyObject =
        pyObj.callAttr("export_bullet_points_to_docx", title, bulletPoints.toTypedArray())


    val docxBytes = result.toJava(ByteArray::class.java)
    // Save the binary content to a .docx file in cache
    val filePath = File(context.cacheDir, "$fileName.docx")
    FileOutputStream(filePath).use { fos ->
        fos.write(docxBytes)
    }

    // Now you can share the file or handle it as needed
    shareDocx(context, filePath)
}

private fun shareDocx(context: Context, file: File) {
    val fileUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        putExtra(Intent.EXTRA_STREAM, fileUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Share DOCX using"))
}
