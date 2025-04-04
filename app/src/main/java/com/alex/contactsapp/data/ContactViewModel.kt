package com.alex.contactsapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val dao: ContactDao
) : ViewModel() {

//    val contacts: StateFlow<List<ContactEntity>> =
    val contacts = dao.getContactOrderedByFirstName()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // Auto-load dummy contacts
    init {
        viewModelScope.launch {
            if (dao.getContactCount() == 0) {
                dao.upsertContact(ContactEntity(firstName = "Alex Johnson", lastName = "", phone = "262-555-1234"))
                dao.upsertContact(ContactEntity(firstName = "Jamie Doe", lastName = "", phone = "262-555-5678"))
                // Add more if you want
            }
        }
    }

    fun addContact(contact: ContactEntity) {
        viewModelScope.launch {
            dao.upsertContact(contact)
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            dao.deleteContact(contact)
        }
    }

    fun upsertContact(contact: ContactEntity) {
        viewModelScope.launch {
            dao.upsertContact(contact)
        }
    }
}