package com.datahiveorg.donetik.ui.navigation

import android.net.Uri
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.feature.home.presentation.navigation.Feed
import com.datahiveorg.donetik.feature.home.presentation.navigation.NewTaskScreen
import com.datahiveorg.donetik.ui.components.AsyncImageLoader
import kotlinx.serialization.Serializable

interface FeatureScreen {

    fun buildNavOptions(builder: NavOptionsBuilder) {}
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

fun FeatureScreen.getFABDestination(): FeatureScreen {
    return when (this) {
        is Feed -> NewTaskScreen
        else -> this//trash code TODO(Find a better way when adding more screens)
    }
}

fun buildTopBarActions(
    featureScreen: FeatureScreen,
    imageUrl: Uri,
    onClick: () -> Unit
): List<TopBarAction> {
    return when (featureScreen) {
        is Feed -> {
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