package com.example.textnotesapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.textnotesapp.data.Note
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class NoteActivity : AppCompatActivity() {
    var id = Int.MIN_VALUE
    var newNote = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        newNote = if (intent?.extras?.containsKey(TextNotesApp.NOTE_ID_KEY) == true) {
            id = intent!!.extras!!.getInt(TextNotesApp.NOTE_ID_KEY)
            lifecycleScope.launch {
                val note = TextNotesApp.instance.notesDb.noteDao().getNote(id)
                noteTitle.setText(note.title)
                categoryTextView.setText(note.category)
                noteBody.setText(note.body)
            }
            false
        } else {
            id = Random.nextInt()
            noteTitle.text.clear()
            noteBody.text.clear()
            true
        }

        saveChanges.setOnClickListener {
            if (noteTitle.text.isEmpty()) {
                TextNotesApp.instance.showToast("Please type title first", this)
                return@setOnClickListener
            }
            saveChanges(newNote)
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        editCategoryButton.setOnClickListener {
            if (noteTitle.text.isEmpty()) {
                TextNotesApp.instance.showToast("Please type title first", this)
                return@setOnClickListener
            }
            val intent = Intent(applicationContext, CategoryActivity::class.java)
            //saveChanges.performClick()
            saveChanges(newNote)
            intent.putExtra(TextNotesApp.NOTE_ID_KEY, id)
            startActivity(intent)
        }

        deleteNoteButton.setOnClickListener {
            lifecycleScope.launch {
                val notes = TextNotesApp.instance.notesDb.noteDao().getAllNotes()
                val notesIds = notes.map { it.id }
                if (id in notesIds) {
                    TextNotesApp.instance.notesDb.noteDao().delete(notes.find { it.id == id }!!)
                }
                val intent = Intent(applicationContext, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        newNote = false
        if (intent?.extras?.containsKey(TextNotesApp.NOTE_ID_KEY) == true) {
            id = intent.getIntExtra(TextNotesApp.NOTE_ID_KEY, 228)
            lifecycleScope.launch {
                val note = TextNotesApp.instance.notesDb.noteDao().getNote(id)
                categoryTextView.text = note.category
            }
        }
    }

    private fun saveChanges(newNote: Boolean) {
        val note = Note(
            id,
            noteTitle.text.toString(),
            categoryTextView.text.toString(),
            noteBody.text.toString()
        )

        lifecycleScope.launch {
            if (newNote) {
                TextNotesApp.instance.notesDb.noteDao().insert(note)
            } else {
                TextNotesApp.instance.notesDb.noteDao().update(note)
            }
            TextNotesApp.instance.showToast("Changes saved", this@NoteActivity)
        }
    }
}
