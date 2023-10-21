package com.abdosharaf.roomtest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abdosharaf.roomtest.NotesDatabase.Companion.getDatabase
import com.abdosharaf.roomtest.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val notesAdapter = NotesAdapter()
    private val database: NotesDatabase by lazy { getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get ViewModel instance, because we need to pass the database to it, so we used the
        // Factory!!
        val viewModelFactory = MainViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        setupRecycler()
        initMainClicks()

        // Observe the list changes and submit it to the recycler
        viewModel.notesList.observe(this) {
            notesAdapter.submitList(it)
        }
    }

    private fun setupRecycler() {
        binding.rvNotes.apply {
            adapter = notesAdapter
            setHasFixedSize(false)
        }
    }

    private fun initMainClicks() {
        binding.btnAddNote.setOnClickListener {
            val noteText = binding.etNote.text.toString()

            if (noteText.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.please_write_a_note_first),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Use this to close the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this@MainActivity.currentFocus?.windowToken, 0)

                // Clear the edit text and make it unfocused
                binding.etNote.clearFocus()
                binding.etNote.setText("")

                lifecycleScope.launch(Dispatchers.IO) {
                    // Add the note to the database
                    val note = Note(noteText)
                    viewModel.addNote(note)
                }
            }
        }
    }
}