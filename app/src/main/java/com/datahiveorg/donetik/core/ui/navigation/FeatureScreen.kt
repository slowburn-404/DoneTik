package com.datahiveorg.donetik.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavOptionsBuilder
import coil3.compose.AsyncImagePainter
import com.datahiveorg.donetik.core.ui.components.AsyncImageLoader
import com.datahiveorg.donetik.feature.home.presentation.navigation.Feed
import com.datahiveorg.donetik.feature.home.presentation.navigation.NewTaskScreen
import com.datahiveorg.donetik.feature.home.presentation.navigation.TaskScreen
import kotlinx.serialization.Serializable

/**
 * Represents a screen within a feature module.
 *
 * This interface provides a common structure for screens, allowing them to define
 * custom navigation options and UI configurations.
 */
interface FeatureScreen {

    /**
     * Builds navigation options for the screen.
     * This function allows customization of navigation behavior when navigating to this screen.
     * For example, it can be used to clear the back stack or launch the screen in a single top mode.
     *
     * @param builder The [NavOptionsBuilder] to configure navigation options.
     */
    fun buildNavOptions(builder: NavOptionsBuilder) {}

    /**
     * Configuration for the screen's UI, such as whether to show the top bar or bottom bar.
     * Defaults to a new [ScreenUIConfig] instance with default values (all UI elements shown).
     */
    val screenUIConfig: ScreenUIConfig get() = ScreenUIConfig()
}

data class TopBarAction(
    val icon: @Composable () -> Unit,
    val description: String,
)

@Serializable
data object RouterScreen : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<RouterScreen> {
            inclusive = true
        }
    }
}

@Serializable
data object AuthFeature : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<OnBoardingFeature> {
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

@Serializable
data object LeaderBoardFeature : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<RouterScreen> {
            inclusive = true
        }
    }
}

@Serializable
data object ProfileFeature : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<RouterScreen> {
            inclusive = true
        }
    }
}

@Serializable
data object TeamsFeature : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<RouterScreen> {
            inclusive = true
        }
    }
}

fun FeatureScreen.getFABDestination(): FeatureScreen {
    return when (this) {
        is Feed -> NewTaskScreen
        else -> this//trash code TODO(Find a better way when adding more screens)
    }
}

fun buildTopBarActions(
    featureScreen: FeatureScreen,
    onClick: () -> Unit,
    painter: AsyncImagePainter
): List<TopBarAction> {
    return when (featureScreen) {
        is Feed -> {
            listOf(
                TopBarAction(
                    icon = {
                        IconButton(
                            onClick = onClick
                        ) {
                            AsyncImageLoader(
                                enterTransition = featureScreen.screenUIConfig.enterTransition,
                                exitTransition = featureScreen.screenUIConfig.exitTransition,
                                painter = painter
                            )
                        }
                    },
                    description = "Avatar"
                )
            )
        }

        is TaskScreen -> {
            listOf(
                TopBarAction(
                    icon = {
                        IconButton(
                            onClick = onClick
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Options"
                            )

                        }
                    },
                    description = "Options"
                )
            )
        }

        else -> emptyList()
    }
}