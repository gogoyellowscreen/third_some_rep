package com.example.textnotesapp.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Placeholder
import androidx.recyclerview.widget.RecyclerView
import com.example.textnotesapp.NoteActivity
import com.example.textnotesapp.R
import com.example.textnotesapp.TextNotesApp
import kotlinx.android.synthetic.main.item_view.view.*

private const val PLACEHOLDER = "click pencil to choose"
class NotesAdapter (
    private val notes: List<Note>
    ) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    class NoteViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(note: Note) {
            with(root) {
                title.text = note.title
                val length = Math.min(note.body.length, 30)
                val bodyPart = if (length < 30) note.body else "${note.body.substring(0, 30)}..."
                body.text = bodyPart
                category.text = if (note.category == PLACEHOLDER) "" else note.category
                this.setOnClickListener {
                    val intent = Intent(it.context, NoteActivity::class.java)
                    intent.putExtra(TextNotesApp.NOTE_ID_KEY, note.id)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val holder = NoteViewHolder(
            LayoutInflater.
            from(parent.context).
            inflate(R.layout.item_view, parent, false)
        )

        return holder
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }
}
