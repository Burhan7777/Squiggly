package com.pzbapps.squiggly.reminder_feature

import androidx.lifecycle.MutableLiveData
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.edit_note_feature.data.repository.EditNoteRepository
import com.pzbapps.squiggly.main_screen.domain.model.Note
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val editNoteRepository: EditNoteRepository,
) {


    suspend fun resetReminder(noteId: Int) {
        var note = editNoteRepository.getNotesById(noteId)
        var newNote = note.copy(reminder = 0)
        editNoteRepository.updateNote(newNote)
    }

}