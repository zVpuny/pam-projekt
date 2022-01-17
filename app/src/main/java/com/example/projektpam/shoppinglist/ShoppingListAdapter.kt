package com.example.projektpam.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektpam.R
import com.example.projektpam.database.AppDatabase
import com.example.projektpam.entity.ShoppingListEntry
import com.example.projektpam.entity.ShoppingListWithEntries

class ShoppingListAdapter(
    private var dataSet: List<ShoppingListWithEntries>,
    context: Context,
    private val onItemClicked: (v: View) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {
    val appDatabase = AppDatabase.getInstance(context)

    class ViewHolder(view: View, private val onItemClicked: (v: View) -> Unit) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val listTitleTextView: TextView
        val listEntriesRecyclerView: RecyclerView
        val deleteListButton: ImageButton

        init {
            view.setOnClickListener(this)
            listTitleTextView = view.findViewById(R.id.listTitleTextView)
            listEntriesRecyclerView = view.findViewById(R.id.listEntriesRecyclerView)
            deleteListButton = view.findViewById(R.id.deleteListButton)
        }

        override fun onClick(v: View?) {
            val view: View = v!!.findViewById(R.id.listEntriesRecyclerView)
            onItemClicked(view)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shopping_list_item, viewGroup, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listTitleTextView.text = dataSet[position].shoppingList.title
        val listEntryAdapter =
            ShoppingListEntryAdapter(dataSet[position].lists) { checkBox, entry ->
                onEntryClicked(checkBox, entry)
            }
        holder.listEntriesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.listEntriesRecyclerView.adapter = listEntryAdapter
        holder.deleteListButton.setOnClickListener {
            deleteList(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    private fun onEntryClicked(checkBox: CheckBox, shoppingListEntry: ShoppingListEntry) {
        val entryToUpdate = ShoppingListEntry(
            shoppingListEntry.shoppingListId,
            shoppingListEntry.entryText,
            !shoppingListEntry.done,
            shoppingListEntry.entryId
        )
        checkBox.isChecked = !shoppingListEntry.done
        appDatabase.shoppingListEntryDao().updateEntry(entryToUpdate)
        updateDataSet()
    }

    fun updateDataSet() {
        dataSet = appDatabase.shoppingListWithEntriesDao().getShoppingListsWithEntries()
        notifyDataSetChanged()
    }

    fun deleteList(list: ShoppingListWithEntries) {
        list.lists.forEach {
            appDatabase.shoppingListEntryDao().deleteEntry(it)
        }
        appDatabase.shoppingListDao().deleteList(list.shoppingList)
        updateDataSet()
    }


}