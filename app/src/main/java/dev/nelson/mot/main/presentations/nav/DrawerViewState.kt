package dev.nelson.mot.main.presentations.nav

data class DrawerViewState(
    val drawerItems: List<MotDestination> = initialDrawerItemsList,
    val selectedItem: String = initialDrawerItemsList.first().route
)
