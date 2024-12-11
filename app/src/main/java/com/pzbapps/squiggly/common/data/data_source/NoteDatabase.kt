package com.pzbapps.squiggly.common.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pzbapps.squiggly.common.data.Model.DummyTable
import com.pzbapps.squiggly.common.data.Model.NoteBook
import com.pzbapps.squiggly.common.data.Model.Tag
import com.pzbapps.squiggly.common.domain.utils.ConverterBoolean
import com.pzbapps.squiggly.common.domain.utils.ConverterString
import com.pzbapps.squiggly.main_screen.domain.model.Note

@Database(entities = [Note::class, NoteBook::class, DummyTable::class, Tag::class], version = 22)
@TypeConverters(ConverterBoolean::class, ConverterString::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getDao(): Dao
}