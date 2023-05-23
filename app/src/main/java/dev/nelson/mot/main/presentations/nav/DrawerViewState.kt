package dev.nelson.mot.main.presentations.nav

data class DrawerViewState(
    val drawerItems: List<MotDestinations> = initialDrawerItemsList,
    val selectedItem: String = initialDrawerItemsList.first().route
)
