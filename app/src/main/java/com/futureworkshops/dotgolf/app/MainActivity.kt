package com.futureworkshops.dotgolf.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.futureworkshops.dotgolf.ui.GreetingScreen
import com.futureworkshops.dotgolf.ui.theme.DotGolfTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DotGolfTheme {
                DotGolfApp()
            }
        }
    }
}

@Serializable
data object GreetingRoute : NavKey

@Composable
private fun DotGolfApp() {
    val backStack = rememberNavBackStack(GreetingRoute)
    val greetingEntryProvider = remember {
        entryProvider {
            entry<GreetingRoute> {
                GreetingScreen(viewModel = hiltViewModel())
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            @Suppress("UNCHECKED_CAST")
            greetingEntryProvider(key as GreetingRoute) as NavEntry<NavKey>
        },
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DotGolfAppPreview() {
    DotGolfTheme {
        GreetingScreen()
    }
}
