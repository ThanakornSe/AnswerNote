package com.thans.answernote.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.thans.answernote.core.ui.theme.AnswerNoteTheme
import com.thans.answernote.presenter.ui.navigation.AnswerSheetMainScreen
import com.thans.answernote.presenter.ui.navigation.SummaryMainScreen
import com.thans.answernote.presenter.ui.screen.AnswerSheetScreen
import com.thans.answernote.presenter.ui.screen.SummaryScreen
import com.thans.answernote.presenter.viewmodel.AnswerSheetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnswerNoteTheme {
                val viewModel: AnswerSheetViewModel =
                    viewModel()
                //var showSummary by remember { mutableStateOf(false) }
                val backStack: NavBackStack<NavKey> =
                    rememberNavBackStack(AnswerSheetMainScreen)

                NavDisplay(
                    modifier = Modifier.fillMaxSize(),
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryDecorators = listOf(
                        // Add the default decorators for managing scenes and saving state
                        rememberSaveableStateHolderNavEntryDecorator(),
                        // Then add the view model store decorator
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    transitionSpec = {
                        // Slide in from right when navigating forward
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    },
                    popTransitionSpec = {
                        // Slide in from left when navigating back
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    },
                    entryProvider = entryProvider {
                        entry<AnswerSheetMainScreen> {
                            AnswerSheetScreen(
                                viewModel = viewModel,
                                onNavigateToSummary = { backStack.add(SummaryMainScreen) }
                            )
                        }
                        entry<SummaryMainScreen> {
                            SummaryScreen(
                                viewModel = viewModel,
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }
        }
    }
}
