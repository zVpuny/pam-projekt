package com.example.projektpam.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.projektpam.entity.ShoppingListEntry

@Dao
interface ShoppingListEntryDao {
    @Insert
    fun insertEntries(vararg entries:ShoppingListEntry)

    @Delete
    fun deleteEntry(entry: ShoppingListEntry)

    @Update
    fun updateEntry(entry: ShoppingListEntry)
}