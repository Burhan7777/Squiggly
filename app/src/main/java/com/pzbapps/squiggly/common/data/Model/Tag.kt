package com.pzbapps.squiggly.common.data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey
    val id:Int,
    val name:String,
)
