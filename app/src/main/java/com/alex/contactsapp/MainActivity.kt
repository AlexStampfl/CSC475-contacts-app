package com.alex.contactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.alex.contactsapp.data.ContactEntity
import com.alex.contactsapp.data.ContactViewModel
import com.alex.contactsapp.ui.theme.ContactsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ContactViewModel = hiltViewModel()
            val contacts by viewModel.contacts.collectAsState()
            val editingContact = remember { mutableStateOf<ContactEntity?>(null) }
            val showAddDialog = remember { mutableStateOf(false) }

            ContactsAppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Contact Book App") },
                            actions = {
                                IconButton(onClick = { showAddDialog.value = true }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Contact", tint = Color.White)
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.DarkGray,
                                titleContentColor = Color.White
                            )
                        )
                    },
                    bottomBar = {
                        BottomAppBar(containerColor = Color.DarkGray) {
                            Text(
                                text = "CSC475 Module 3 CTA 3 Option #2",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
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
                            onDelete = {
                                viewModel.deleteContact(it)
                                editingContact.value = null
                            },
                            onUpdate = { contact, newName, newPhone ->
                                viewModel.upsertContact(
                                    contact.copy(firstName = newName, phone = newPhone)
                                )
                                editingContact.value = null
                            }
                        )
                    }

                    if (showAddDialog.value) {
                        AddContactDialog(
                            onDismiss = { showAddDialog.value = false },
                            onAdd = { newContact ->
                                viewModel.upsertContact(newContact)
                                showAddDialog.value = false
                            }
                        )
                    }
                }
            }
        }
    }
}

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
    contacts: List<ContactEntity>,
    editingContact: MutableState<ContactEntity?>,
    onEdit: (ContactEntity) -> Unit,
    onDelete: (ContactEntity) -> Unit,
    onUpdate: (ContactEntity, String, String) -> Unit
) {
    Column {
        contacts.forEach { contact ->
            val isEditing = contact == editingContact.value
            ContactCard(
                contact = contact,
                isEditing = isEditing,
                onEdit = onEdit,
                onDelete = onDelete,
                onUpdate = onUpdate
            )
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
    onDismiss: () -> Unit,
    onAdd: (ContactEntity) -> Unit
) {
    val name = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if (name.value.isNotBlank() && phone.value.isNotBlank()) {
                    onAdd(ContactEntity(firstName = name.value.trim(), lastName = "", phone = phone.value.trim()))
                    onDismiss()
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add New Contact") },
        text = {
            Column {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
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
    contact: ContactEntity,
    isEditing: Boolean,
    onEdit: (ContactEntity) -> Unit,
    onDelete: (ContactEntity) -> Unit,
    onUpdate: (ContactEntity, String, String) -> Unit
) {
    var name by remember(contact) { mutableStateOf(contact.firstName) }
    var phone by remember(contact) { mutableStateOf(contact.phone) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
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
                Text(text = contact.firstName, style = MaterialTheme.typography.titleMedium)
                Text(text = contact.phone, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))
        if (isEditing) {
            Column {
                IconButton(onClick = { onUpdate(contact, name, phone) }) {
                    Icon(Icons.Default.Check, contentDescription = "Save")
                }
                IconButton(onClick = { onDelete(contact) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Contact", tint = Color.Red)
                }
            }
        } else {
            IconButton(onClick = { onEdit(contact) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Contact")
            }
        }
    }
}