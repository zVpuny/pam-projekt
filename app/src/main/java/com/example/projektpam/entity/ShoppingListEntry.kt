package com.example.projektpam.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_entry")
data class ShoppingListEntry (
    val shoppingListId: Int,
    val entryText: String,
    val done:Boolean,
    @PrimaryKey(autoGenerate = true) val entryId: Int = 0
)