package com.example.projektpam.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projektpam.R
import com.example.projektpam.entity.ShoppingListEntry

class ShoppingListEntryAdapter(
    private var dataSet: List<ShoppingListEntry>,
    private val onItemClicked: (checkBox: CheckBox, entry: ShoppingListEntry) -> Unit
) : RecyclerView.Adapter<ShoppingListEntryAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        private val onItemClicked: (checkBox: CheckBox, entry: ShoppingListEntry) -> Unit
    ) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val doneCheckBox: CheckBox
        val titleTextView: TextView
        var entry: ShoppingListEntry? = null

        init {
            view.setOnClickListener(this)
            doneCheckBox = view.findViewById(R.id.listEntryCheckBox)
            doneCheckBox.isEnabled = false
            titleTextView = view.findViewById(R.id.listEntryTitleTextView)
        }


        override fun onClick(v: View?) {
            val checkBox: CheckBox = v!!.findViewById(R.id.listEntryCheckBox)

            onItemClicked(checkBox, entry!!)

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_entry_item, parent, false)

        return ViewHolder(view, onItemClicked)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = dataSet[position].entryText
        holder.doneCheckBox.isChecked = dataSet[position].done

        holder.entry = dataSet[position]
    }

    override fun getItemCount() = dataSet.size
}