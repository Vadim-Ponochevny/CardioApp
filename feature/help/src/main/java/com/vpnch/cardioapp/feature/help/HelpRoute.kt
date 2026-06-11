package com.vpnch.cardioapp.feature.help

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.model.HelpContact
import com.vpnch.cardioapp.core.ui.CardioPreview
import kotlinx.coroutines.launch

@Composable
fun HelpRoute(
    modifier: Modifier = Modifier,
    viewModel: HelpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    HelpScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onCall = { phone -> dialPhone(context, phone) },
        onCopy = { phone ->
            copyPhone(context, phone)
            scope.launch {
                snackbarHostState.showSnackbar("Номер скопирован")
            }
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpScreen(
    uiState: HelpUiState,
    snackbarHostState: SnackbarHostState,
    onCall: (String) -> Unit,
    onCopy: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Помощь") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (uiState.contacts.isEmpty() && !uiState.isLoading) {
                Text(
                    text = "Контакты пока недоступны.",
                    style = MaterialTheme.typography.bodyLarge,
                )
                return@Column
            }

            uiState.contacts.forEach { contact ->
                HelpContactCard(
                    contact = contact,
                    onCall = { onCall(contact.phone) },
                    onCopy = { onCopy(contact.phone) },
                )
            }
        }
    }
}

@Composable
private fun HelpContactCard(
    contact: HelpContact,
    onCall: () -> Unit,
    onCopy: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(contact.title, style = MaterialTheme.typography.titleLarge)
            Text(contact.phone, style = MaterialTheme.typography.bodyLarge)

            Button(
                onClick = onCall,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Позвонить")
            }

            OutlinedButton(
                onClick = onCopy,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Скопировать номер")
            }
        }
    }
}

private fun dialPhone(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
}

private fun copyPhone(context: Context, phone: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("phone", phone))
}

@CardioPreview
@Composable
private fun HelpScreenPreview() {
    HelpScreen(
        uiState = HelpUiState(
            isLoading = false,
            contacts = listOf(
                HelpContact("1", "Доктор", "+644580", null, 0, true),
                HelpContact("2", "Запись на приём", "+780278", null, 1, true),
            ),
        ),
        snackbarHostState = SnackbarHostState(),
        onCall = {},
        onCopy = {},
    )
}
