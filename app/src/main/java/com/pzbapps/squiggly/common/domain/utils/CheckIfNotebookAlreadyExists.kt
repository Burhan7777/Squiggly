package com.pzbapps.squiggly.common.domain.utils

import androidx.compose.runtime.MutableState
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel

fun checkIfNotebookAlreadyExists(
    viewModel: MainActivityViewModel,
    notebookText: MutableState<String>
): Boolean {
    viewModel.getAllNotebooks()
    var listOfNotebooks = viewModel.listOfNoteBooks
    for (i in listOfNotebooks) {
        if (i.name == notebookText.value) {
            return true
        }
    }
    return false
}