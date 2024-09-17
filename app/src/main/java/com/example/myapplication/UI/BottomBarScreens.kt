package com.example.myapplication.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreens(
    val route : String,
    val title: String,
    val icon : ImageVector
) {
    object Characters: BottomBarScreens(
        route = "character",
        title = "Characters",
        icon = Icons.Default.Person

    )
    object Locations: BottomBarScreens(
        route = "locations",
        title = "Locations",
        icon = Icons.Default.LocationOn

    )
    object Episodes: BottomBarScreens(
        route = "episodes",
        title = "Episodes",
        icon = Icons.Default.PlayArrow

    )
}