package com.example.contentdemoapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.contentdemoapp.ui.theme.ContentDemoAppTheme
import com.example.contentproviders.data.Contact

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyContent()
                }
            }
        }
    }
}

@Composable
fun MyContent() {

    var granted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> granted = isGranted }

    if (granted) {
        doGrantedStuff()
    } else {
        doNotGrantedStuff()
    }

    SideEffect {
        launcher.launch(Manifest.permission.READ_CONTACTS)
    }

}

@Composable
fun doNotGrantedStuff() {
    Column {
        Text(text = "You are NOT allowed")

    }
}

// Phase 1
@Composable
fun doGrantedStuff() {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {

        Text(
            "Contacts:",
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold
        )

        var contactListState :MutableState<List<Contact>> = remember {mutableStateOf(emptyList<Contact>())}

        loadContacts(LocalContext.current, contactListState)


        for (contact in contactListState.value) {
            Text(
                "${contact.phoneNumber}   ${contact.name} ",
                modifier = Modifier.padding(10.dp, 2.dp)
            )
        }
    }

}



fun loadContacts(context: Context, contactListState : MutableState<List<Contact>>) {
// *EXPLAIN*
    val projection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER
    )

// *EXPLAIN*
    val cursor = context.getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        null,
        null,
        null
    )

    // *EXPLAIN*
    cursor?.use {
        while (it.moveToNext()) {

            val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data._ID))

            val displayName =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME))

            val hasPhoneNumber =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

            val phoneSelection =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID.toString() + " = ?"
            val phoneSelectionArgs = arrayOf("${id}")

// *EXPLAIN*
            if (hasPhoneNumber == "1") {
                val phoneNumberCursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    phoneSelection,
                    phoneSelectionArgs,
                    null
                );

                // *EXPLAIN*
                phoneNumberCursor?.use {
                    it.moveToNext()

                    val phoneNumber = phoneNumberCursor.getString(
                        phoneNumberCursor.getColumnIndexOrThrow(ContactsContract.Data.DATA1)
                    )
                    // *EXPLAIN*
                    (contactListState.value + listOf<Contact>(Contact(displayName, phoneNumber))).also {
                        contactListState.value = it
                    }

                }
            }
        }
    }
}

