package com.example.banking.data.repository

import androidx.lifecycle.LiveData
import com.example.banking.data.database.UserDao
import com.example.banking.data.model.History
import com.example.banking.data.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    fun getUser(username: String, password: String) : LiveData<User> {
        return userDao.get(username, password)
    }

    fun getUsername(username: String) : LiveData<User> {
        return userDao.getUsername(username)
    }

    suspend fun updateBalance(username: String, balance: Double) {
        userDao.updateBalance(username, balance)
    }

    suspend fun insertHistory(history: History) {
        userDao.insert(history)
    }

    fun getFullHistory(name: String) : LiveData<List<History>> {
        return userDao.getFullHistory(name)
    }

    suspend fun deleteAccount(currentUser: User) {
        return userDao.deleteAccount(currentUser)
    }

    suspend fun changePassword(username: String, password: String) {
        userDao.changePassword(username, password)
    }


}