package com.abdosharaf.roomtest

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert
    suspend fun addNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    // If you return a LiveData, Room will take care of the changes of this query, this means
    // when the notes table is updated in the database, Room will invoke this function to return
    // the updated data to you.
    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<Note>>
}