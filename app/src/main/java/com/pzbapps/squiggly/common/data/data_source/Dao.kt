package com.pzbapps.squiggly.common.data.data_source

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.pzbapps.squiggly.common.data.Model.NoteBook
import com.pzbapps.squiggly.common.data.Model.Tag
import com.pzbapps.squiggly.main_screen.domain.model.Note

@androidx.room.Dao
interface Dao {

    @Insert
    suspend fun insertNote(note: Note): Long

    @Query("SELECT * from notes ORDER BY timeStamp DESC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * from notes ORDER BY timeModified DESC")
    fun getAllNotesByDateModified(): List<Note>

    @Delete
    fun deleteAllNotes(list: List<Note>)

    @Query("SELECT * from notes where id= :id")
    suspend fun getNoteById(id: Int): Note

    @Query("DELETE from notes where id= :id")
    suspend fun deleteNoteById(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM notes where notebook= :notebook ORDER BY timeStamp DESC")
    suspend fun getNoteByNotebook(notebook: String): List<Note>

    @Upsert
    suspend fun addNoteBook(notebook: NoteBook)

    @Upsert
    suspend fun addTag(tag: Tag)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotebook(notebook: NoteBook)

    @Query(
        """
    UPDATE notes 
    SET deletedNote = :deletedNote, 
        timePutInTrash = :timePutInTrash 
    WHERE id = :id
"""
    )
    suspend fun moveToTrashById(deletedNote: Boolean, timePutInTrash: Long, id: Int)

    @Query("UPDATE notes set archive= :archive WHERE id= :id")
    suspend fun moveToArchive(archive: Boolean, id: Int)

    @Query("UPDATE notes set locked= :locked WHERE id= :id")
    suspend fun lockOrUnlockNote(locked: Boolean, id: Int)

    @Query("UPDATE notes set notePinned= :notePinnedOrNot WHERE id= :id")
    suspend fun pinOrUnpinNote(notePinnedOrNot: Boolean, id: Int)

    @Query("SELECT * FROM notebook where name= :name")
    suspend fun getNotebookByName(name: String): NoteBook

    @Query("DELETE FROM notebook where name= :notebook")
    suspend fun deleteNotebook(notebook: String)

    @Query("SELECT * from notebook")
    suspend fun getAllNoteBooks(): List<NoteBook>

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<Tag>

    @Query("DELETE FROM notes where deletedNote = :deletedNote")
    suspend fun deleteNoteFromTrash(deletedNote: Boolean)

    @RawQuery
    fun checkpoint(supportSQLiteQuery: SupportSQLiteQuery): Int

}
