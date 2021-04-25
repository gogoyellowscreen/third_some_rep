package com.example.textnotesapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.textnotesapp.R
import com.example.textnotesapp.TextNotesApp
import kotlinx.android.synthetic.main.category_item_view.view.*
import kotlinx.android.synthetic.main.item_view.view.*

class CategoryAdapter (
    private val categorys: List<Category>,
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(category: Category) {
            with(root) {
                categoryTitle.text = category.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val holder = CategoryViewHolder(
            LayoutInflater.
            from(parent.context).
            inflate(R.layout.category_item_view, parent, false)
        )

        holder.root.categoryHolder.setOnClickListener {
            onClick(categorys[holder.adapterPosition])
        }

        return holder
    }

    override fun getItemCount(): Int = categorys.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categorys[position])
    }
}
