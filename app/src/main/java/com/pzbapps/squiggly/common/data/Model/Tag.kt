package com.pzbapps.squiggly.common.data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name:String,
    val dummyColumn:Int = 0
)
