package com.alex.contactsapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int

    @Query("SELECT * FROM contacts ORDER BY firstName ASC")
    fun getContactOrderedByFirstName(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts ORDER BY lastName ASC")
    fun getContactOrderedByLastName(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts ORDER BY phone ASC")
    fun getContactOrderedByPhone(): Flow<List<ContactEntity>>


}