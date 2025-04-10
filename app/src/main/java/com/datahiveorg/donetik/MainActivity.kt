package com.datahiveorg.donetik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.datahiveorg.donetik.ui.components.ScreenTitle
import com.datahiveorg.donetik.ui.components.SnackBar
import com.datahiveorg.donetik.ui.navigation.RootNavGraph
import com.datahiveorg.donetik.ui.theme.DoneTikTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            val snackBarHostState = SnackbarHostState()

            //val currentScreen = navHostController.currentBackStackEntry?.destination?.route

            DoneTikTheme {
                Scaffold(
//                    topBar = {
//                        ScreenTitle(
//                            title = "DoneTik",
////                            onNavigateUp = {
////                                navHostController.navigateUp()
////                            },
////                            feature = AuthenticationScreen.LoginScreen
//                        )
//                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackBarHostState,
                        ) {
                            SnackBar(
                                message = it.visuals.message
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                ) { innerPadding ->
                    RootNavGraph(
                        paddingValues = innerPadding,
                        snackBarHostState = snackBarHostState,
                        navHostController = navHostController,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoneTikTheme {
    }
}