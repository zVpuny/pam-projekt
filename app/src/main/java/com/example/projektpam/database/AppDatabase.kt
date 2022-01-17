package com.example.projektpam.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projektpam.dao.ShoppingListDao
import com.example.projektpam.dao.ShoppingListEntryDao
import com.example.projektpam.dao.ShoppingListWithEntriesDao
import com.example.projektpam.entity.ShoppingList
import com.example.projektpam.entity.ShoppingListEntry
import com.example.projektpam.entity.ShoppingListWithEntries
import java.util.concurrent.Executors

@Database(entities = [ShoppingList::class,ShoppingListEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingListEntryDao(): ShoppingListEntryDao
    abstract fun shoppingListWithEntriesDao(): ShoppingListWithEntriesDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(
            context: Context
        ): AppDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "shopping_lists"
            ).allowMainThreadQueries().build()


        }
    }
}