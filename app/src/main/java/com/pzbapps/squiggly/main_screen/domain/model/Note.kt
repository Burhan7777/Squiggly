package com.pzbapps.squiggly.main_screen.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pzbapps.squiggly.common.domain.utils.Constant

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var archive: Boolean = false,
    var notebook: String = Constant.NOT_CATEGORIZED,
    var locked: Boolean = false,
    var listOfCheckedNotes: ArrayList<String> = arrayListOf(),
    var listOfCheckedBoxes: ArrayList<Boolean> = arrayListOf(),
    var listOfBulletPointNotes: ArrayList<String> = arrayListOf(),
    var timeStamp: Long = 0,
    var editTime: String = "",
    var color: Int = 0,
    var timePutInTrash: Long = 0,
    var deletedNote: Boolean = false,
    var notePinned: Boolean = false,
    var timeModified: Long = 0,
    var reminder: Long = 0
)
