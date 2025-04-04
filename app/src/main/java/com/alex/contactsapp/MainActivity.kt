package com.alex.contactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.contactsapp.ui.theme.ContactsAppTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.vector.ImageVector


data class Contact(val name: String, val phone: String)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactsAppTheme {
                val contacts = remember {
                    mutableStateListOf(
                        Contact("Alex Johnson", "(262) 555-1234"),
                        Contact("Jamie Doe", "(262) 555-5678"),
                        Contact("Taylor Smith", "(262) 555-8765"),
                        Contact("John Jones", "(262) 555-8765"),
                        Contact("Bruce Wayne", "(262) 555-8765"),
                        Contact("Clark Kent", "(262) 555-8765"),
                        Contact("Edgar Allen Poe", "(262) 555-8765"),
                        Contact("Wall-E & Eva", "(262) 555-8765"),
                        Contact("James Taylor", "(262) 555-8765"),
                        Contact("John Denver", "(262) 555-8765"),
                        Contact("Barry Allen", "(262) 555-8765"),
                        Contact("Diana Prince", "(262) 555-8765"),
                        Contact("Taylor Swift", "(262) 555-8765"),
                        Contact("John McAfee", "(262) 555-8765"),
                        Contact("Taylor Smith", "(262) 555-8765"),
                        Contact("Grigori Rasputin", "(262) 555-8765"),
                        Contact("Al Green", "(262) 555-8765"),
                        Contact("Larry Bird", "(262) 555-8765"),
                        Contact("Jimmy Johnson", "(262) 555-8765"),
                        Contact("Taylor Radcliff", "(262) 555-8765")
                    ).sortedBy { it.name }.toMutableStateList()
                }

                val editingContact = remember { mutableStateOf<Contact?>(null) }
                val showAddDialog =
                    remember { mutableStateOf(false) } // Controls whether visible or hidden

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Contact Book App") },
                            actions = {
                                IconButton(onClick = {
                                    showAddDialog.value = true
                                }) { // making dialog visible
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Contact",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.DarkGray,
                                titleContentColor = Color.White
                            ),
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            containerColor = Color.DarkGray
                        ) {
                            Text(
                                text = "CSC475 Module 3 CTA 3 Option #2",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f)) // Pushes icon to the end
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                            .padding(8.dp)
                            .fillMaxSize()
                    ) {
                        ContactList(
                            contacts = contacts,
                            editingContact = editingContact,
                            onEdit = { contact -> editingContact.value = contact },
                            onDelete = { contact -> editingContact.value = contact },
                            onUpdate = { contact, newName, newPhone ->
                                val index = contacts.indexOf(contact)
                                if (index != -1) {
                                    contacts[index] = Contact(newName, newPhone)
                                    contacts.sortBy { it.name }
                                }
                                editingContact.value = null
                            }
                        )
                    }
                } // ends of innerPadding of Scaffold

                if (showAddDialog.value) {
                    AddContactDialog(
                        onDismiss = { showAddDialog.value = false },
                        onAdd = { newContact ->
                            contacts.add(newContact)
                            contacts.sortBy { it.name }
                        }
                    )
                }

            }
        } // SetContent ends
    }
}

    // Profile Picture Placeholder
    @Composable
    fun ProfileIcon() {
        Image(
            painter = rememberVectorPainter(Icons.Default.AccountCircle),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    fun ContactList(
        contacts: MutableList<Contact>,
        editingContact: MutableState<Contact?>,
        onEdit: (Contact) -> Unit,
        onDelete: (Contact) -> Unit,
        onUpdate: (Contact, String, String) -> Unit
    ) {
        Column {
            contacts.forEach { contact ->
                val isEditing = contact == editingContact.value
                ContactCard(
                    contact = contact,
                    isEditing = contact == editingContact.value,
                    onEdit = { editingContact.value = contact },
                    onDelete = {
                        contacts.remove(it)
                        if (editingContact.value == it) editingContact.value = null
                    },
                    onUpdate = { updatedContact, newName, newPhone ->
                        val index = contacts.indexOf(updatedContact)
                        if (index != -1) {
                            contacts[index] = Contact(newName, newPhone)
                            contacts.sortBy { it.name }
                        }
                        editingContact.value = null
                    }
                )

                // Add divider line
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }


    @Composable
    fun AddContactDialog(
        onDismiss: () -> Unit, // Unit: "This function returns nothing"
        onAdd: (Contact) -> Unit // Unit equivalent to Java's void
    ) {
        val name = remember { mutableStateOf("") }
        val phone = remember { mutableStateOf("") }

        androidx.compose.material3.AlertDialog( // this is what creates the dialog box
            onDismissRequest = onDismiss,
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    if (name.value.isNotBlank() && phone.value.isNotBlank()) {
                        onAdd(Contact(name.value.trim(), phone.value.trim()))
                        onDismiss()
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = { // A composable that serves as a dismiss button
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            },
            title = { Text("Add New Contact") },
            text = { // parameter
                Column {
                    androidx.compose.material3.OutlinedTextField(
                        value = name.value, // links field to the name state
                        onValueChange = { name.value = it }, // updates state when the user types
                        label = { Text("Name") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.material3.OutlinedTextField(
                        value = phone.value,
                        onValueChange = { phone.value = it },
                        label = { Text("Phone Number") }
                    )
                }
            }
        )
    }

    @Composable
    fun ContactCard(
        contact: Contact,
        isEditing: Boolean,
        onEdit: (Contact) -> Unit,
        onDelete: (Contact) -> Unit,
        onUpdate: (Contact, String, String) -> Unit
    ) {
        // Initialize name and phone only when editing starts
        var name by remember(contact) { mutableStateOf(contact.name) }
        var phone by remember(contact) { mutableStateOf(contact.phone) } // study this later
        // remember(contact) only resets name and phone when the contact changes (starts editing)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Replace with actual image
            ProfileIcon()
            Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") }
                        )
                    } else {
                        Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = contact.phone, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                    Spacer(modifier = Modifier.width(12.dp))
                    if (isEditing) {
                        // Save and Trash Buttons
                        Column {
                            IconButton(onClick = { onUpdate(contact, name, phone) }) {
                                Icon(Icons.Default.Check, contentDescription = "Save")
                            }
                            IconButton(onClick = { onDelete(contact) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete Contact",
                                    tint = Color.Red)
                            }
                        }
                    } else {
                        // Show edit icon only if not already editing this contact
                            IconButton(onClick = { onEdit(contact) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Contact")
                        }
                    }
                }
            }
