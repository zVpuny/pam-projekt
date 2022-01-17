package com.example.projektpam.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektpam.R
import com.example.projektpam.dao.ShoppingListEntryDao
import com.example.projektpam.database.AppDatabase
import com.example.projektpam.entity.ShoppingListEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingListAddEntriesActivity : AppCompatActivity() {
    lateinit var database: AppDatabase
    lateinit var shoppingListEntryDao: ShoppingListEntryDao
    lateinit var entryEditText: EditText
    lateinit var adapter: ShoppingListEntryAddPreviewAdapter

    var entries = ArrayList<ShoppingListEntry>()

    var listId: Long = -1
    lateinit var listName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list_add_entries)

        database = AppDatabase.getInstance(this)
        shoppingListEntryDao = database.shoppingListEntryDao()
        val intent = intent
        listId = intent.getLongExtra("listId", -1)
        listName = intent.getStringExtra("listName")!!
        this.supportActionBar?.title = listName

        val previewRecyclerView: RecyclerView = findViewById(R.id.entriesPreviewRecyclerView)
        adapter = ShoppingListEntryAddPreviewAdapter(entries) { position ->
            onPreviewEntryRemoveClicked(position)
        }
        previewRecyclerView.layoutManager = LinearLayoutManager(this)
        previewRecyclerView.adapter = adapter


        entryEditText = findViewById(R.id.entryEditText)
        val addEntryButton: FloatingActionButton = findViewById(R.id.addEntryBtn)
        addEntryButton.setOnClickListener {
            if(entryEditText.text.isNotEmpty()){
                val entry = ShoppingListEntry(listId.toInt(), entryEditText.text.toString(), false)
                entries.add(entry)
                adapter.notifyItemInserted(entries.size - 1)
            }
            else{
                entryEditText.error = getString(R.string.list_element_error)

            }
        }

        val confirmButton: FloatingActionButton = findViewById(R.id.confirmEntriesButton)
        confirmButton.setOnClickListener {
            shoppingListEntryDao.insertEntries(entries = entries.map { it }.toTypedArray())
            val resultIntent = Intent();
            resultIntent.putExtra("numberOfElementsAdded", entries.size)
            resultIntent.putExtra("listName", listName)
            setResult(RESULT_OK, resultIntent)
            finish()
        }


    }

    fun onPreviewEntryRemoveClicked(position: Int) {
        entries.removeAt(position)
        adapter.notifyItemRemoved(position)

    }
}