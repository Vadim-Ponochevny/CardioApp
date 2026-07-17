package com.vpnch.cardioapp.feature.today.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.survey.SurveyLink

@Composable
fun SurveyCard(
    surveyLink: SurveyLink?,
    onOpenSurvey: (SurveyLink) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Опрос", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = surveyLink != null,
                onClick = { surveyLink?.let(onOpenSurvey) },
            ) {
                Text("Пройти опрос")
            }
        }
    }
}