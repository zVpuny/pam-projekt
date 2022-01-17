package com.example.projektpam.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektpam.R
import com.example.projektpam.dao.ShoppingListDao
import com.example.projektpam.dao.ShoppingListWithEntriesDao
import com.example.projektpam.database.AppDatabase
import com.example.projektpam.entity.ShoppingList
import com.example.projektpam.entity.ShoppingListWithEntries
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.StringBuilder

class ShoppingListActivity : AppCompatActivity() {
    lateinit var database: AppDatabase
    lateinit var shoppingListWithEntriesDao: ShoppingListWithEntriesDao
    lateinit var shoppingListDao: ShoppingListDao
    lateinit var shoppingLists: List<ShoppingListWithEntries>
    lateinit var shoppingListsRecyclerView: RecyclerView
    lateinit var shoppingListAdapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)
        this.supportActionBar?.title = getString(R.string.shopping_list_title)

        database = AppDatabase.getInstance(this)
        shoppingListWithEntriesDao = database.shoppingListWithEntriesDao()
        shoppingListDao = database.shoppingListDao()
        shoppingLists = shoppingListWithEntriesDao.getShoppingListsWithEntries()

        shoppingListsRecyclerView = findViewById(R.id.shoppingListsRecyclerView)
        shoppingListAdapter = ShoppingListAdapter(shoppingLists, this) { view ->
            onItemClicked(
                view
            )
        }
        shoppingListsRecyclerView.layoutManager = LinearLayoutManager(this)
        shoppingListsRecyclerView.adapter = shoppingListAdapter

        val addListButton: FloatingActionButton = findViewById(R.id.addNewListBtn)
        val listNameEditText: EditText = findViewById(R.id.shoppingListNameEditText)

        addListButton.setOnClickListener {
            if (listNameEditText.text.isNotEmpty()) {
                val shoppingList = ShoppingList(listNameEditText.text.toString())
                val insertedListId = shoppingListDao.insertList(shoppingList)
                val insertedListName = listNameEditText.text.toString()
                listNameEditText.text = null
                val intent = Intent(it.context, ShoppingListAddEntriesActivity::class.java)
                intent.putExtra("listId", insertedListId)
                intent.putExtra("listName", insertedListName)
                startActivityForResult(intent, 5)
            }
            else{
                listNameEditText.error = getString(R.string.list_name_error)

            }
        }
    }

    private fun onItemClicked(v: View) {
        if (v.visibility == View.VISIBLE) {
            v.visibility = View.GONE
        } else {
            v.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                shoppingListAdapter.updateDataSet()
                val addedListName = data?.getStringExtra("listName")
                val numberOfElementsAdded = data?.getIntExtra("numberOfElementsAdded", 0)
                val messageBuilder =
                    StringBuilder(getString(R.string.list_added_message)).append(addedListName).append(" (")
                        .append(numberOfElementsAdded).append(")")
                Toast.makeText(this, messageBuilder.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}