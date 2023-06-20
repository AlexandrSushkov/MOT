package dev.nelson.mot.main.presentations.nav

data class DrawerViewState(
    val motDrawerItems: List<MotDrawerItem> = initialMotDrawerItemsLists,
    val selectedItem: String = initialMotDrawerItemsLists.first().destination.route
)
