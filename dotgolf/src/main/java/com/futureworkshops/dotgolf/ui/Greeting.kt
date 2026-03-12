package com.futureworkshops.dotgolf.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.futureworkshops.dotgolf.theme.R
import com.futureworkshops.dotgolf.ui.theme.DotGolfTheme
import com.futureworkshops.dotgolf.viewmodel.GreetingUiState
import com.futureworkshops.dotgolf.viewmodel.GreetingViewModel

@Composable
fun GreetingScreen(
    viewModel: GreetingViewModel? = null
) {
    val uiState = viewModel?.uiState?.collectAsState()?.value ?: GreetingUiState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Greeting(
            message = uiState.message,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Greeting(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    DotGolfTheme {
        Greeting(stringResource(R.string.greeting_fallback))
    }
}
