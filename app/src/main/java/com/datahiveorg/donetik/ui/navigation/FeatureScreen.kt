package com.datahiveorg.donetik.ui.navigation

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.components.AsyncImageLoader
import kotlinx.serialization.Serializable

interface FeatureScreen {

    fun buildNavOptions(builder: NavOptionsBuilder) {}

    val title: String get() = ""

    //have default values and override only when necessary
    val hasTopAppBar: Boolean get() = false
    val hasBottomBar: Boolean get() = false
    val hasNavIcon: Boolean get() = false
    val topBarActions: List<TopBarAction> get() = emptyList()
    val hasFAB: Boolean get() = false

    @get:DrawableRes
    val bottomNavIconRes: Int? get() = null

    @get:DrawableRes
    val navIconRes: Int? get() = null
}

data class TopBarAction(
    val icon: @Composable () -> Unit,
    val description: String,
)

@Serializable
data object RouterScreen : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {}
}

@Serializable
data object AuthFeature : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<RouterScreen> {
            inclusive = true
        }
    }
}

@Serializable
data object OnBoardingFeature : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<RouterScreen> {
            inclusive = true
        }
    }
}


@Serializable
data object HomeFeature : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<RouterScreen> {
            inclusive = true
        }
    }
}

fun FeatureScreen.getFABDestination(): FeatureScreen {
    return when (this) {
        is HomeScreen.Feed -> HomeScreen.NewTaskScreen
        else -> this//trash code TODO(Find a better way when adding more screens)
    }
}

fun buildTopBarActions(
    featureScreen: FeatureScreen,
    imageUrl: Uri,
    onClick: () -> Unit
): List<TopBarAction> {
    return when (featureScreen) {
        is HomeScreen.Feed -> {
            listOf(
                TopBarAction(
                    icon = {
                        val context = LocalContext.current

                        IconButton(
                            onClick = onClick
                        ) {
                            AsyncImageLoader(
                                imageUrl = imageUrl,
                                context = context,
                            )
                        }
                    },
                    description = "Avatar"
                )
            )
        }

        else -> emptyList()
    }
}