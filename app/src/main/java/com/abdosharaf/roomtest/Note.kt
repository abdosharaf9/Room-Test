package com.abdosharaf.roomtest

import androidx.room.Entity
import androidx.room.PrimaryKey

// I made the entity more simpler just to test it
@Entity("notes_table")
data class Note(
    val text: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
