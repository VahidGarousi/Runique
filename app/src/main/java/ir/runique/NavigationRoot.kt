package ir.runique

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ir.runique.auth.presentation.intro.IntroScreenRoot
import ir.runique.auth.presentation.login.LoginScreenRoot
import ir.runique.auth.presentation.register.RegisterScreenRoot
import ir.runique.run.presentation.run_overview.RunOverviewScreenRoot

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "run" else "auth"
    ) {
        authGraph(navController = navController)
        runGraph(navController = navController)
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "intro",
        route = "auth"
    ) {
        composable(
            route = "intro"
        ) {
            IntroScreenRoot(
                onSignUpClick = { navController.navigate("register") },
                onSignInClick = { navController.navigate("login") }
            )
        }
        composable(
            route = "register"
        ) {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = { navController.navigate("login") }
            )
        }
        composable(
            route = "login"
        ) {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate("run") {
                        popUpTo("auth") {
                            inclusive = true
                            saveState = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate("register") {
                        popUpTo("login") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}


private fun NavGraphBuilder.runGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "run_overview",
        route = "run"
    ) {
        composable(
            route = "run_overview"
        ) {
            RunOverviewScreenRoot()
        }
    }
}