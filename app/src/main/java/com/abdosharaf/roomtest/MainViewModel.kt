package com.abdosharaf.roomtest

import androidx.lifecycle.ViewModel

class MainViewModel(private val database: NotesDatabase) : ViewModel() {

    // This will hold the list even if it changes
    val notesList = database.notesDao().getAllNotes()

    // Add the note to the database
    suspend fun addNote(note: Note) {
        database.notesDao().addNote(note)
    }
}