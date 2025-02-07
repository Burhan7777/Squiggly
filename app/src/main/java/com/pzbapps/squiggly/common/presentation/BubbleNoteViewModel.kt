package com.pzbapps.squiggly.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pzbapps.squiggly.add_note_feature.data.repository.InsertNoteRepository
import com.pzbapps.squiggly.main_screen.domain.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BubbleNoteViewModel @Inject constructor(private val insertNoteRepository: InsertNoteRepository) :
    ViewModel() {

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            insertNoteRepository.insertNote(note)
        }
    }
}