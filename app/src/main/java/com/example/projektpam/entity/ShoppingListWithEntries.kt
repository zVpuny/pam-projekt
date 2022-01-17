package com.example.projektpam.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class ShoppingListWithEntries(
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "listId",
        entityColumn = "shoppingListId"
    )
    val lists:List<ShoppingListEntry>
)