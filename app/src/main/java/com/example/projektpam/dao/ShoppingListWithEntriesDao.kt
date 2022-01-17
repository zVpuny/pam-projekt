package com.example.projektpam.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.projektpam.entity.ShoppingListWithEntries

@Dao
interface ShoppingListWithEntriesDao {
    @Transaction
    @Query("SELECT * FROM shopping_list")
    fun getShoppingListsWithEntries():List<ShoppingListWithEntries>


}