package com.example.textnotesapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.room.Room
import com.example.textnotesapp.data.Category
import com.example.textnotesapp.data.CategoryRepository
import com.example.textnotesapp.data.Note
import com.example.textnotesapp.data.NoteRepository

class TextNotesApp : Application() {
    lateinit var notesDb: NoteRepository
    lateinit var categoryDb: CategoryRepository
    var notes: ArrayList<Note> = ArrayList()//stubNotes()
    var categorys: ArrayList<Category> = ArrayList()//stubCategorys()

    override fun onCreate() {
        super.onCreate()
        instance = this

        notesDb = Room
            .databaseBuilder(applicationContext, NoteRepository::class.java, "note-database")
            .build()

        categoryDb = Room
            .databaseBuilder(applicationContext, CategoryRepository::class.java, "category-database")
            .build()
    }

    fun showToast(msg: String, context: Context) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    // CALL GET NOTES PREVIOUSLY AND NOTIFYDATASETCHANGED LATER
    fun filterNotesByCategorys(categoryIsClicked: Map<String, Boolean>) {
        val selectedCategory: HashSet<String> = HashSet()
        for ((category, clicked) in categoryIsClicked) {
            if (clicked) {
                selectedCategory.add(category)
            }
        }
        if (selectedCategory.isEmpty()) return

        val tmpList = notes.filter { selectedCategory.contains(it.category) }
        notes.clear()
        notes.addAll(tmpList)
    }

    fun stubNotes(): ArrayList<Note> {
        val noteKek = Note(1, "Kek", "Kek", "Kek")
        val noteLol = Note(2, "Lol", "Lol", "Lol")
        val noteVeryLongCategory = Note(3, "VeryLongCategory", "VeryLongCategory", "VeryLongCategory")
        val noteJustAnotherCategory = Note(2, "JustAnotherCategory", "JustAnotherCategory", "JustAnotherCategory")
        val noteSalam = Note(2, "Salam ", "Salam", "Salam")

        return arrayListOf(noteKek, noteKek, noteKek, noteLol, noteLol, noteLol, noteLol, noteVeryLongCategory, noteVeryLongCategory, noteVeryLongCategory, noteJustAnotherCategory, noteJustAnotherCategory, noteSalam)
    }

    fun stubCategorys(): ArrayList<Category> {
        return arrayListOf(Category(1, "Kek"), Category(2, "Lol"), Category(3,"VeryLongCategory"), Category(4, "JustAnotherCategory"), Category(5,"Salam"))
    }

    companion object {
        lateinit var instance: TextNotesApp
            private set

        const val NOTE_ID_KEY = "note_id"
    }
}