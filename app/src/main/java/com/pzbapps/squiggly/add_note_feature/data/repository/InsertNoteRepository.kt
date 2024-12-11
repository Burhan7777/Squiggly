package com.pzbapps.squiggly.add_note_feature.data.repository

import com.pzbapps.squiggly.common.data.Model.NoteBook
import com.pzbapps.squiggly.common.data.Model.Tag
import com.pzbapps.squiggly.common.data.data_source.NoteDatabase
import com.pzbapps.squiggly.main_screen.domain.model.Note
import javax.inject.Inject

class InsertNoteRepository @Inject constructor(private val noteDatabase: NoteDatabase) {

    suspend fun insertNote(note: Note):Long {
       return noteDatabase.getDao().insertNote(note)
    }

    suspend fun addNoteBook(notebook: NoteBook) {
        noteDatabase.getDao().addNoteBook(notebook)
    }

    suspend fun addTag(tag: Tag){
        noteDatabase.getDao().addTag(tag)
    }

    suspend fun getAllNoteBooks(): List<NoteBook> {
        return noteDatabase.getDao().getAllNoteBooks()
    }


}