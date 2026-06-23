package com.vpnch.cardioapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vpnch.cardioapp.navigation.CardioNavHost

@Composable
fun CardioApp(
    modifier: Modifier = Modifier,
) {
    CardioNavHost(modifier = modifier)
}