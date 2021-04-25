package com.example.textnotesapp.data

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.textnotesapp.R
import com.example.textnotesapp.TextNotesApp
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.category_item_upper_view.view.*
import kotlinx.coroutines.launch

private const val NOT_CLICKED = "#D1D1D1"
private const val CLICKED = "#2196F3"

class CategoryUpperAdapter (
    private val categorys: List<String>,
    private val scope: LifecycleCoroutineScope,
    private val onClick: () -> Unit,
) : RecyclerView.Adapter<CategoryUpperAdapter.CategoryUpperViewHolder>() {
    val categoryIsClicked = mutableMapOf(*(categorys.map { Pair(it, false) }).toTypedArray())

    class CategoryUpperViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(category: String) {
            with(root) {
                categoryUpperTitle.text = category
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryUpperViewHolder {
        val holder = CategoryUpperViewHolder(
            LayoutInflater.
            from(parent.context).
            inflate(R.layout.category_item_upper_view, parent, false)
        )

        /*holder.root.upperCategoryHolder.setOnClickListener {
            // TODO: it.background =
            onClick(categorys[holder.adapterPosition])
        }*/
        holder.root.upperCategoryHolder.setOnClickListener {
            Log.d("XYU", holder.root.upperCategoryHolder.solidColor.toString())
            scope.launch {
                val allNotes = TextNotesApp.instance.notesDb.noteDao().getAllNotes()
                TextNotesApp.instance.notes.clear()
                TextNotesApp.instance.notes.addAll(allNotes)
                val category = categorys[holder.adapterPosition]
                if (categoryIsClicked[category] == true) {
                    categoryIsClicked[category] = false
                    holder.root.upperCategoryHolder.setBackgroundColor(Color.parseColor(NOT_CLICKED))
                } else {
                    categoryIsClicked[category] = true
                    holder.root.upperCategoryHolder.setBackgroundColor(Color.parseColor(CLICKED))
                }
                TextNotesApp.instance.filterNotesByCategorys(categoryIsClicked)
                onClick()
            }
        }
        return holder
    }
    override fun getItemCount(): Int = categorys.size

    override fun onBindViewHolder(holder: CategoryUpperViewHolder, position: Int) {
        holder.bind(categorys[position])
    }
}