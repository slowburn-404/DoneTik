package com.datahiveorg.donetik.ui.navigation

import androidx.navigation.NavHostController
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

class NavigatorFactory(
    val koin: Koin,
    val navHostController: NavHostController
) {
    inline fun <reified T : Any> create(): T {
        return koin.get<T> { parametersOf(navHostController) }
    }
}