package com.vpnch.cardioapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.feature.onboarding.OnboardingRoute
import com.vpnch.cardioapp.navigation.CardioNavHost

@Composable
fun CardioApp(
    modifier: Modifier = Modifier,
    initialRoute: String? = null,
) {
    val viewModel: CardioAppViewModel = hiltViewModel()
    val isProfileComplete by viewModel.isProfileComplete.collectAsState(initial = null)

    when (isProfileComplete) {
        null -> Unit
        false -> OnboardingRoute(
            onComplete = { /* recomposition triggers automatically */ },
            modifier = modifier,
        )
        true -> CardioNavHost(
            initialRoute = initialRoute,
            modifier = modifier,
        )
    }
}
