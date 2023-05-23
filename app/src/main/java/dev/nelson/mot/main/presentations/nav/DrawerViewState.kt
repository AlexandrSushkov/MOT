package dev.nelson.mot.main.presentations.nav

import dev.nelson.mot.main.util.StringUtils

data class DrawerViewState(
    val drawerItems: List<MotDestinations> = initialDrawerItemsList,
    val selectedItem: String = StringUtils.EMPTY
)
