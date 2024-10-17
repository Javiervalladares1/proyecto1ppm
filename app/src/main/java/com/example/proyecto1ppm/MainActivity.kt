

package com.example.proyecto1ppm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto1ppm.screen.GroupDetailsScreen
import com.example.proyecto1ppm.screen.HomeScreen
import com.example.proyecto1ppm.screen.LoginScreen
import com.example.proyecto1ppm.screen.RegistrationScreen
import com.example.proyecto1ppm.screen.SlideMenuScreen
import com.example.proyecto1ppm.screen.SplashScreen
import com.example.proyecto1ppm.screen.UserProfileScreen
import com.example.proyecto1ppm.ui.theme.Proyecto1ppmTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto1ppmTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context= LocalContext.current
    NavHost(navController = navController, startDestination = "splash_screen") {
        // Splash Screen
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        // Registration Screen
        composable("registration_screen") {
            RegistrationScreen(navController = navController)
        }
        composable("LoginScreen"){
            LoginScreen(navController = navController)
        }
        // Home Screen
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
        // Group Detail Screen
        composable("group_detail_screen") {
            GroupDetailsScreen(navController = navController)
        }
        // User Profile Screen
        composable("user_profile_screen") {
            UserProfileScreen(navController = navController)
        }
        // Slide Menu Screen
        composable("slide_menu_screen") {
            SlideMenuScreen(navController = navController, context = context)
        }

    }
}
