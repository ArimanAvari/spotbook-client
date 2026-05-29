package com.spotbook.personalguide.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spotbook.personalguide.data.local.AppDatabase
import com.spotbook.personalguide.presentation.auth.AuthViewModel
import com.spotbook.personalguide.presentation.auth.LoginScreen
import com.spotbook.personalguide.presentation.auth.RegisterScreen
import com.spotbook.personalguide.presentation.groups.GroupListScreen
import com.spotbook.personalguide.presentation.groups.GroupPlacesScreen
import com.spotbook.personalguide.presentation.groups.GroupViewModel
import com.spotbook.personalguide.presentation.places.PlaceDetailsScreen
import com.spotbook.personalguide.presentation.places.PlaceEditScreen
import com.spotbook.personalguide.presentation.places.PlaceListScreen
import com.spotbook.personalguide.presentation.places.PlaceViewModel
import com.spotbook.personalguide.presentation.sync.SyncScreen
import com.spotbook.personalguide.presentation.sync.SyncViewModel

@Composable
fun AppNavGraph(database: AppDatabase) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val placeViewModel: PlaceViewModel = viewModel(
        factory = PlaceViewModel.Factory(database.placeDao(), database.groupDao())
    )
    val groupViewModel: GroupViewModel = viewModel(
        factory = GroupViewModel.Factory(database.groupDao(), database.placeDao())
    )
    val syncViewModel: SyncViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Login.route
    ) {
        composable(AppRoute.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(AppRoute.Places.route) {
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(AppRoute.Register.route) }
            )
        }

        composable(AppRoute.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(AppRoute.Places.route) {
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoute.Places.route) {
            PlaceListScreen(
                viewModel = placeViewModel,
                onAddClick = {
                    placeViewModel.startCreate()
                    navController.navigate(AppRoute.PlaceCreate.route)
                },
                onPlaceClick = { navController.navigate(AppRoute.placeDetails(it)) },
                onGroupsClick = { navController.navigate(AppRoute.Groups.route) },
                onSyncClick = { navController.navigate(AppRoute.Sync.route) },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(AppRoute.Places.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.PlaceCreate.route) {
            PlaceEditScreen(
                title = "Новое место",
                viewModel = placeViewModel,
                placeId = null,
                onBackClick = { navController.popBackStack() },
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoute.PlaceDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: return@composable
            PlaceDetailsScreen(
                viewModel = placeViewModel,
                placeId = id,
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(AppRoute.placeEdit(id)) },
                onDeleted = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoute.PlaceEdit.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: return@composable
            PlaceEditScreen(
                title = "Редактирование",
                viewModel = placeViewModel,
                placeId = id,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(AppRoute.Groups.route) {
            GroupListScreen(
                viewModel = groupViewModel,
                onBackClick = { navController.popBackStack() },
                onGroupClick = { navController.navigate(AppRoute.groupPlaces(it)) }
            )
        }

        composable(
            route = AppRoute.GroupPlaces.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: return@composable
            GroupPlacesScreen(
                groupId = id,
                groupViewModel = groupViewModel,
                placeViewModel = placeViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoute.Sync.route) {
            SyncScreen(
                viewModel = syncViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

sealed class AppRoute(val route: String) {
    data object Login : AppRoute("login")
    data object Register : AppRoute("register")
    data object Places : AppRoute("places")
    data object PlaceCreate : AppRoute("places/create")
    data object PlaceDetails : AppRoute("places/{id}") {
        fun create(id: Long) = "places/$id"
    }
    data object PlaceEdit : AppRoute("places/{id}/edit") {
        fun create(id: Long) = "places/$id/edit"
    }
    data object Groups : AppRoute("groups")
    data object GroupPlaces : AppRoute("groups/{id}/places") {
        fun create(id: Long) = "groups/$id/places"
    }
    data object Sync : AppRoute("sync")

    companion object {
        fun placeDetails(id: Long) = PlaceDetails.create(id)
        fun placeEdit(id: Long) = PlaceEdit.create(id)
        fun groupPlaces(id: Long) = GroupPlaces.create(id)
    }
}
