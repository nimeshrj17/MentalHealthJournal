package com.example.mentalhealthjournal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mentalhealthjournal.presentation.addentry.AddEntryScreen
import com.example.mentalhealthjournal.presentation.journal.EditJournalScreen
import com.example.mentalhealthjournal.presentation.auth.LoginScreen
import com.example.mentalhealthjournal.presentation.auth.PhoneAuthScreen
import com.example.mentalhealthjournal.presentation.chat.ChatBotScreen
import com.example.mentalhealthjournal.presentation.home.HomeScreen
import com.example.mentalhealthjournal.presentation.journal.EditJournalViewModel
import com.example.mentalhealthjournal.presentation.journal.JournalScreen
import com.example.mentalhealthjournal.presentation.journal.JournalViewModel
import com.example.mentalhealthjournal.presentation.settings.SettingsScreen
import com.example.mentalhealthjournal.service.AiService
import com.google.firebase.ai.GenerativeModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavigationRoutes.LOGIN ,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    )

    {
        composable(NavigationRoutes.ADD_ENTRY) {
            val viewModel: JournalViewModel= hiltViewModel()
            AddEntryScreen(viewModel = viewModel ,
                navController = navController)
        }
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                onAuthSuccess = {
                    navController.navigate(NavigationRoutes.HOME) {
                        popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(NavigationRoutes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(NavigationRoutes.JOURNAL) {
            val viewModel: JournalViewModel = hiltViewModel()
            val aiService = AiService()
            JournalScreen(
                viewModel = viewModel,
                aiService = aiService,
                onEditEntry = { entry ->
                    navController.navigate("${NavigationRoutes.EDIT_ENTRY}/${entry.id}")
                }
            )
        }

        composable(NavigationRoutes.ADD_ENTRY) {
            val viewModel: JournalViewModel = hiltViewModel()
            AddEntryScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = "${NavigationRoutes.EDIT_ENTRY}/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: 0
            val viewModel: JournalViewModel = hiltViewModel()
            EditJournalScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }

        composable(NavigationRoutes.SETTINGS) {
            SettingsScreen(navController = navController ,
                onLogout = { navController.navigate(NavigationRoutes.LOGIN)})
        }

        composable(NavigationRoutes.CHATBOT) {
            ChatBotScreen(navController = navController)
        }

        composable(NavigationRoutes.PHONE_AUTH) {
            PhoneAuthScreen(
                navController = navController,
                onVerificationSuccess = {
                    navController.navigate(NavigationRoutes.HOME) {
                        popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
