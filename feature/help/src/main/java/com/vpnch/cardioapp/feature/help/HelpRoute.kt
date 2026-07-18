package com.vpnch.cardioapp.feature.help

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.model.help.HelpContact
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import kotlinx.coroutines.launch

private val SCREEN_BG = Color(0xFFF6F6F6)
private val CONTENT_SPACING = 12.dp
private val SCREEN_PADDING_H = 12.dp
private val SCREEN_PADDING_TOP = 12.dp

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
        onCall = { phone ->
            viewModel.onCallPressed()
            dialPhone(context, phone)
        },
        onCopy = { phone ->
            copyPhone(context, phone)
            scope.launch {
                snackbarHostState.showSnackbar("Номер скопирован")
            }
        },
        onOpenSurvey = { url ->
            uiState.surveyId?.let { viewModel.onSurveyOpened(it) }
            openSurveyInBrowser(context, url)
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
    onOpenSurvey: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SCREEN_BG)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SCREEN_PADDING_H)
            .padding(top = SCREEN_PADDING_TOP),
        verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING),
    ) {
        Text(
            text = "Помощь",
            style = CardioTheme.typography.screenTitle,
            color = CardioTheme.colors.textMain,
        )

        Image(
            painter = painterResource(id = R.drawable.help),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(4.dp))

        if (uiState.contacts.isEmpty() && uiState.surveyUrl == null && !uiState.isLoading) {
            Text(
                text = "Контакты пока недоступны.",
                style = CardioTheme.typography.bodyLarge,
                color = CardioTheme.colors.textSecondary,
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        uiState.contacts.forEach { contact ->
            HelpContactCard(
                contact = contact,
                onCall = { onCall(contact.phone) },
                onCopy = { onCopy(contact.phone) },
            )
        }

        uiState.surveyUrl?.let { surveyUrl ->
            SurveyCard(
                isNew = uiState.surveyIsNew,
                onOpenSurvey = { onOpenSurvey(surveyUrl) },
            )
            Text(
                text = "* Опрос откроется в браузере на вашем устройстве.",
                style = CardioTheme.typography.navLabel,
                color = CardioTheme.colors.textSecondary,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SurveyCard(
    isNew: Boolean,
    onOpenSurvey: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Text(
                    text = "Опрос",
                    style = CardioTheme.typography.dialogTitle,
                    color = CardioTheme.colors.textMain,
                )
                if (isNew) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF4CAF50), CircleShape),
                    )
                }
            }
            Button(
                onClick = onOpenSurvey,
                modifier = Modifier.fillMaxWidth().height(72.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isNew) CardioTheme.colors.primary else Color(0xFFE0E0E0),
                    contentColor = if (isNew) CardioTheme.colors.onPrimary else CardioTheme.colors.textSecondary,
                ),
                shape = RoundedCornerShape(36.dp),
            ) {
                Text(
                    text = if (isNew) "Пройти опрос" else "Открыть ещё раз",
                    style = CardioTheme.typography.actionLabel,
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = contact.title,
                style = CardioTheme.typography.dialogTitle,
                color = CardioTheme.colors.textMain,
            )
            Text(
                text = contact.phone,
                style = CardioTheme.typography.navLabel,
                color = CardioTheme.colors.textMain,
            )
            Button(
                onClick = onCall,
                modifier = Modifier.fillMaxWidth().height(72.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardioTheme.colors.actionCall,
                    contentColor = CardioTheme.colors.onPrimary,
                ),
                shape = RoundedCornerShape(36.dp),
            ) {
                Text(
                    text = "Позвонить",
                    style = CardioTheme.typography.actionLabel,
                    color = CardioTheme.colors.textMain,
                )
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

private fun openSurveyInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@CardioPreview
@Composable
private fun HelpScreenPreview() {
    HelpScreen(
        uiState = HelpUiState(
            isLoading = false,
            surveyUrl = "https://forms.yandex.ru/u/6a2abbf095add53c1f78ef01/",
            surveyIsNew = true,
            contacts = listOf(
                HelpContact("1", "Доктор", "+644580", null, 0, true),
                HelpContact("2", "Запись на приём", "+780278", null, 1, true),
            ),
        ),
        snackbarHostState = SnackbarHostState(),
        onCall = {},
        onCopy = {},
        onOpenSurvey = {},
    )
}
