package com.example.textnotesapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.textnotesapp.data.Category
import com.example.textnotesapp.data.CategoryAdapter
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        addCategoryButton.setOnClickListener {
            val categoryName = addCategoryText.text.toString()
            if (categoryName.isEmpty()) {
                TextNotesApp.instance.showToast("Category name should contain at least one symbol", this)
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val categorys = TextNotesApp.instance.categoryDb.categoryDao().getAllCategorys()
                val categorysNames = categorys.map { it.name }
                if (categorysNames.contains(categoryName)) {
                    TextNotesApp.instance.showToast("Such category name already exist", this@CategoryActivity)
                    return@launch
                }

                TextNotesApp.instance.categoryDb.categoryDao().insert(Category(Random.nextInt(), categoryName))
                downloadCategorys()
                viewCategorys()
            }
        }

        lifecycleScope.launch { downloadCategorys() }

        viewCategorys()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun viewCategorys() {
        val viewManager = LinearLayoutManager(this)
        chooseCategoryRecView.apply {
            layoutManager = viewManager
            adapter = CategoryAdapter(TextNotesApp.instance.categorys) {
                lifecycleScope.launch {
                    if (intent?.extras?.containsKey(TextNotesApp.NOTE_ID_KEY) == true) {
                        val id = intent.getIntExtra(TextNotesApp.NOTE_ID_KEY, 1488)
                        val note = TextNotesApp.instance.notesDb.noteDao().getNote(id)
                        val newNote = note.copy(category = it.name)
                        TextNotesApp.instance.notesDb.noteDao().update(newNote)
                        val intent = Intent(applicationContext, NoteActivity::class.java)
                        intent.putExtra(TextNotesApp.NOTE_ID_KEY, id)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    suspend fun downloadCategorys() {
        val allCaterys = TextNotesApp.instance.categoryDb.categoryDao().getAllCategorys()
        TextNotesApp.instance.categorys.clear()
        TextNotesApp.instance.categorys.addAll(allCaterys)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        lifecycleScope.launch {
            downloadCategorys()
        }
    }
}
