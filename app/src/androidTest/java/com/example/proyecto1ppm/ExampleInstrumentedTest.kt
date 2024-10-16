package com.example.proyecto1ppm

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.proyecto1ppm.data.User
import com.example.proyecto1ppm.data.UserDao
import com.example.proyecto1ppm.data.UserDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(AndroidJUnit4::class)
class UserDatabaseTest {

    private lateinit var db: UserDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        // Crear una instancia de la base de datos en memoria para pruebas
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).build()

        userDao = db.user_table()
    }

    @After
    fun tearDown() {
        // Cerrar la base de datos después de las pruebas
        db.close()
    }

    @Test
    fun insertAndGetUser() = runBlocking {
        // Crear un usuario
        val user = User(email = "test@example.com", password = "password123")
        userDao.insert(user)

        // Obtener el usuario de la base de datos
        val retrievedUser = userDao.getUserById(user.id)

        // Verificar que el usuario sea el mismo
        assertEquals(user.email, retrievedUser?.email)
        assertEquals(user.password, retrievedUser?.password)
    }

    @Test
    fun signUpWithEmail_SavesUserToRoom() = runBlocking {
        val email = "test@example.com"
        val password = "password123"

        // Simular el registro de un usuario
        authRepository.signUpWithEmail(email, password)

        // Obtener el usuario de Room
        val retrievedUser = userDao.getAllUsers()

        // Verificar que el usuario se haya guardado
        assertEquals(1, retrievedUser.size)
        assertEquals(email, retrievedUser[0].email)
    }



}
