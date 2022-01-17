package com.example.projektpam.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingList (
    @ColumnInfo (name="title") val title: String,
    @PrimaryKey(autoGenerate = true) val listId:Int = 0,
)