package com.pzbapps.squiggly.bubble_note_feature.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pzbapps.squiggly.R
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class BubbleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show()
            QuickNoteScreen { finish() }
        }
    }

    @Composable
    fun QuickNoteScreen(onClose: () -> Unit) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onClose() }, contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Title") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Write your note...") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        saveNote(title, content)
                        onClose()
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }

    fun saveNote(title: String, content: String) {
//    val note = Note(title = title, content = content, timestamp = System.currentTimeMillis())
//    CoroutineScope(Dispatchers.IO).launch {
//        NotesDatabase.getInstance().noteDao().insertNote(note)
//    }
    }
}




