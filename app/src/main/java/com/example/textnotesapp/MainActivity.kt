package com.example.textnotesapp

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.textnotesapp.data.CategoryUpperAdapter
import com.example.textnotesapp.data.NotesAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            // TODO: create new note
            val intent = Intent(applicationContext, NoteActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
                TextNotesApp.instance.notes.clear()
                TextNotesApp.instance.notes.addAll(TextNotesApp.instance.notesDb.noteDao().getAllNotes())
                TextNotesApp.instance.categorys.clear()
                TextNotesApp.instance.categorys.addAll(TextNotesApp.instance.categoryDb.categoryDao().getAllCategorys())
            viewUpperCategorys()
            viewNotes()
        }

        viewUpperCategorys()
        viewNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_category -> {
                val intent = Intent(applicationContext, CategoryActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        lifecycleScope.launch {
            TextNotesApp.instance.notes.clear()
            TextNotesApp.instance.notes.addAll(TextNotesApp.instance.notesDb.noteDao().getAllNotes())
            TextNotesApp.instance.categorys.clear()
            TextNotesApp.instance.categorys.addAll(TextNotesApp.instance.categoryDb.categoryDao().getAllCategorys())
            viewUpperCategorys()
            viewNotes()
        }

        viewUpperCategorys()
        viewNotes()
    }

    private fun viewNotes() {
        val viewManager = LinearLayoutManager(this)
        notesRecyclerView.apply {
            layoutManager = viewManager
            adapter = NotesAdapter(TextNotesApp.instance.notes)
        }
    }

    private fun viewUpperCategorys() {
        val viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.apply {
            layoutManager = viewManager
            adapter = CategoryUpperAdapter(TextNotesApp.instance.categorys.map { it.name }, lifecycleScope) {
                notesRecyclerView.adapter!!.notifyDataSetChanged()
            }
        }
    }
}
