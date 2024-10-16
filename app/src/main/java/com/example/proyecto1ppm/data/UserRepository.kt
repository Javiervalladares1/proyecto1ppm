package com.example.proyecto1ppm.data

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user:User){
        userDao.insert(user)
    }

    suspend fun deletelAllUsers(){
        userDao.deleteAll()
    }
}