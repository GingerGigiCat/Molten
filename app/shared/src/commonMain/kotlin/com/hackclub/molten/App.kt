package com.hackclub.molten

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hackclub.molten.auth.AuthState
import com.hackclub.molten.theming.WavyShape
import com.hackclub.molten.ui.HomePage
import com.hackclub.molten.ui.NavigationButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class ScreenObject

@Serializable
@SerialName("home")
class HomeScreenObject() : ScreenObject()

@Serializable
@SerialName("projects")
data class ProjectsScreenObject(
    val projectId: String? = null
) : ScreenObject()

@Serializable
@SerialName("shop")
class ShopScreenObject : ScreenObject()

@Serializable
@SerialName("event")
class EventScreenObject : ScreenObject()

@Composable
@Preview
fun App(onNavHostReady: suspend (NavController) -> Unit = {}) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }

    MoltenTheme(true) {
        val auth = remember { AuthState() }
        val user by auth.user.collectAsState()
        val notice by auth.notice.collectAsState()

        LaunchedEffect(Unit) {
            auth.bootstrap()
        }

        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                // Top Bar
                Card(
                    shape = WavyShape(),
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    colors = CardColors(
                        containerColor = Color(0xFFFC4C02),
                        contentColor = Color(0xFFFFFBFF),
                        disabledContainerColor = Color(0xFFFC4C02),
                        disabledContentColor = Color(0xFFFFFBFF)
                    )
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NavigationButton("Home", { navController.navigate(HomeScreenObject()) }, Modifier.weight(1f))

                        NavigationButton("Projects", { navController.navigate(ProjectsScreenObject()) }, Modifier.weight(1f))

                        NavigationButton("Shop", { navController.navigate(ShopScreenObject()) }, Modifier.weight(1f))

                        NavigationButton("Event", { navController.navigate(EventScreenObject()) }, Modifier.weight(1f))

                        if (user == null) {
                            NavigationButton("Sign in", { auth.signIn() }, Modifier.weight(1f))
                        } else {
                            NavigationButton("Sign out", { auth.signOut() }, Modifier.weight(1f))
                        }
                    }
                }

                user?.let {
                    Text(
                        "Signed in as ${it.displayName}",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 15.dp),
                    )
                }
                notice?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 15.dp),
                    )
                }

                // Main Content
                NavHost(
                    navController = navController,
                    startDestination = HomeScreenObject(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable<HomeScreenObject> {
                        HomePage()
                    }
                    composable<ProjectsScreenObject> {
                        Text("Projects", modifier = Modifier.padding(15.dp))
                    }
                    composable<ShopScreenObject> {
                        Text("Shop", modifier = Modifier.padding(15.dp))
                    }
                    composable<EventScreenObject> {
                        Text("Event", modifier = Modifier.padding(15.dp))
                    }
                }
            }
        }
    }
}
