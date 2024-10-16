

package com.example.proyecto1ppm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto1ppm.data.UserDao
import com.example.proyecto1ppm.data.UserDatabase
import com.example.proyecto1ppm.screen.GroupDetailsScreen
import com.example.proyecto1ppm.screen.HomeScreen
import com.example.proyecto1ppm.screen.RegistrationScreen
import com.example.proyecto1ppm.screen.SlideMenuScreen
import com.example.proyecto1ppm.screen.SplashScreen
import com.example.proyecto1ppm.screen.UserProfileScreen
import com.example.proyecto1ppm.ui.theme.Proyecto1ppmTheme

class MainActivity : ComponentActivity() {
    private lateinit var database: UserDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos
        database = UserDatabase.getDatabase(applicationContext)
        userDao = database.userDao()

        setContent {
            Proyecto1ppmTheme {
                // Pasamos el DAO a la navegación
                AppNavigation(userDao)
            }
        }
    }
}


@Composable
fun AppNavigation(userDao: UserDao) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash_screen") {
        // Splash Screen
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        // Registration Screen
        composable("registration_screen") {
            RegistrationScreen(navController = navController)
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
            SlideMenuScreen(navController = navController)
        }
    }
}

