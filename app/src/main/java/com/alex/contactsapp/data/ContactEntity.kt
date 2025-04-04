package com.alex.contactsapp.data

// This file defines a Room Entity, a blueprint for a table in your database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts") // Entity is like a table
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val phone: String
) {
}