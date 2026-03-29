package com.example.leavewise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.leavewise.ui.AttendanceViewModel
import com.example.leavewise.ui.AuthViewModel
import com.example.leavewise.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val attendanceViewModel: AttendanceViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val startDestination = if (authViewModel.currentUser.value != null) "dashboard" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                viewModel = attendanceViewModel,
                authViewModel = authViewModel,
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("semester") {
            SemesterSettingsScreen(
                viewModel = attendanceViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("timetable") {
            TimetableScreen(
                viewModel = attendanceViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("planner") {
            AttendancePlannerScreen(
                viewModel = attendanceViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("actual") {
            ActualAttendanceScreen(
                viewModel = attendanceViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("subjects") {
            SubjectAttendanceScreen(
                viewModel = attendanceViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("warnings") {
            WarningsScreen(
                viewModel = attendanceViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}