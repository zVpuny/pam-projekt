package com.example.projektpam.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projektpam.R
import com.example.projektpam.entity.ShoppingListEntry

class ShoppingListEntryAddPreviewAdapter(
    private val dataSet: List<ShoppingListEntry>,
    private val onButtonClick: (position: Int) -> Unit
) :
    RecyclerView.Adapter<ShoppingListEntryAddPreviewAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val onButtonClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val entryPreviewTextView: TextView
        val removeEntryButton: ImageButton


        init {
            entryPreviewTextView = view.findViewById(R.id.entryPreviewTextView)
            removeEntryButton = view.findViewById(R.id.removeEntryButton)
            removeEntryButton.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            onButtonClick(adapterPosition)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_add_entry_preview_item, parent, false)

        return ViewHolder(view, onButtonClick)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.entryPreviewTextView.text = dataSet[position].entryText
    }

    override fun getItemCount() = dataSet.size


}