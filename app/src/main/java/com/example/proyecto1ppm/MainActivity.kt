

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
import com.example.proyecto1ppm.screen.GroupMembersScreen
import com.example.proyecto1ppm.screen.HomeScreen
import com.example.proyecto1ppm.screen.LoginScreen
import com.example.proyecto1ppm.screen.RegistrationScreen
import com.example.proyecto1ppm.screen.SlideMenuScreen
import com.example.proyecto1ppm.screen.SplashScreen
import com.example.proyecto1ppm.screen.UserProfileScreen
import com.example.proyecto1ppm.ui.theme.Proyecto1ppmTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import androidx.navigation.NavType

import androidx.navigation.navArgument
import com.example.proyecto1ppm.screen.ChatScreen


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
        // En tu función AppNavigation(), agrega la nueva ruta:

        composable(
            "group_members_screen/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedCourseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val courseId = URLDecoder.decode(encodedCourseId, StandardCharsets.UTF_8.toString())
            GroupMembersScreen(navController = navController, courseId = courseId)
        }
        composable(
            "chat_screen/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            ChatScreen(navController = navController, courseId = courseId)
        }


    }
}
