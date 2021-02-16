package com.example.banking.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.banking.data.database.UserDatabase
import com.example.banking.data.model.History
import com.example.banking.data.model.User
import com.example.banking.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getInstance(application).userDao()
        repository = UserRepository(userDao)
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun getUser(username: String, password: String) : LiveData<User> {
        return repository.getUser(username, password)
    }

    fun getUsername(username: String) : LiveData<User> {
        return repository.getUsername(username)
    }

    fun updateBalance(username: String, balance: Double) {
        viewModelScope.launch {
            repository.updateBalance(username, balance)
        }
    }

    fun insertHistory(history: History) {
        viewModelScope.launch {
            repository.insertHistory(history)
        }
    }

    fun getFullHistory(name: String) : LiveData<List<History>> {
        return repository.getFullHistory(name)
    }

    fun deleteAccount(currentUser: User) {
        viewModelScope.launch {
            repository.deleteAccount(currentUser)
        }
    }

    fun changePassword(username: String, password: String) {
        viewModelScope.launch {
            repository.changePassword(username, password)
        }
    }

}