package com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.notes

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.chaquo.python.Python
import java.io.File
import java.util.UUID

fun exportAndShareDocx(
    context: Context,
    title: String,
    content: String
) {
    var fileName = UUID.randomUUID()
    val docxFile = File(context.cacheDir, "$fileName.docx")
    println("TItle:$title")
    // Call the Python function to create the docx file
    val python = Python.getInstance()
    val module = python.getModule("shareNoteAsDocx")
    module.callAttr("export_docx", title, content, docxFile.absolutePath)

    // Share the generated .docx file
    shareDocx(context, docxFile)
}

// Function to share the generated .docx file
private fun shareDocx(context: Context, file: File) {
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" // Mime type for .docx
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grants temporary read permission to the recipient
    }

    // Start the share intent
    context.startActivity(Intent.createChooser(shareIntent, "Share Document"))
}
