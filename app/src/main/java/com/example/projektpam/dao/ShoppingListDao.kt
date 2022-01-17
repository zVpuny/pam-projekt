package com.example.projektpam.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.projektpam.entity.ShoppingList

@Dao
interface ShoppingListDao {
    @Insert
    fun insertList(shoppingList: ShoppingList):Long

    @Delete
    fun deleteList(shoppingList: ShoppingList)
}