package com.pzbdownloaders.scribble.common.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.pzbdownloaders.scribble.common.domain.utils.Constant
import com.pzbdownloaders.scribble.common.presentation.components.AlertDialogBoxTrialEnded
import com.pzbdownloaders.scribble.main_screen.domain.model.Note
import com.pzbdownloaders.scribble.ui.theme.ScribbleTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var viewModel: MainActivityViewModel
    lateinit var result: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getAllNotebooks() // WE LOAD THE NOTEBOOKS IN THE START ONLY SO THAT TO SHOW THEM EVERYWHERE NEEDED.
        val sharedPreferences = getSharedPreferences("rememberUser", Context.MODE_PRIVATE)
        result = sharedPreferences.getString("LoggedInUser", "nothing")!!

        val conMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        Log.i("network", netInfo.toString())
        deleteTrashNotes(viewModel, this)


        setContent {
            var showTrialEndedDialogBox = remember {
                mutableStateOf(false)
            }
            ScribbleTheme {
                // A surface container using the 'background' color from the theme
                var selectedIItem = remember {
                    mutableStateOf(0)
                }
                var selectedNote = remember {
                    mutableStateOf(0)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        viewModel,
                        this@MainActivity,
                        result,
                        selectedIItem,
                        selectedNote
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ScribbleTheme {
    }
}


fun deleteTrashNotes(viewModel: MainActivityViewModel, activity: MainActivity) {
    viewModel.getAllNotes()
    viewModel.listOfNotesLiveData.observe(activity) {
        var notesInTrash = mutableStateOf(SnapshotStateList<Note>())
       // println(it.size)
        for (i in it) {
            if (i.deletedNote) {
                notesInTrash.value.add(i)
            }
        }

        println(notesInTrash.value.size)
        for (i in notesInTrash.value) {
            if ((System.currentTimeMillis() - i.timePutInTrash) > 60000) {
                viewModel.deleteNoteById(i.id)
                Toast.makeText(activity, "Trash cleared", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

