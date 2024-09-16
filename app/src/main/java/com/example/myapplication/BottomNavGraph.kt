package com.example.myapplication

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreens.Characters.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable(BottomBarScreens.Characters.route) {
            CharactersScreen()
        }
        composable(BottomBarScreens.Episodes.route) {
            EpisodesScreen()
        }
        composable(BottomBarScreens.Locations.route) {
            LocationsScreen()
        }
    }
}