package com.example.banking.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.banking.data.model.History
import com.example.banking.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users_table WHERE username = :username AND password = :password")
    fun get(username: String, password: String) : LiveData<User>

    @Query("SELECT * FROM users_table WHERE username = :username")
    fun getUser(username: String) : User

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users_table WHERE username = :username")
    fun getUsername(username: String) : LiveData<User>

    @Query("SELECT balance FROM users_table WHERE username = :username AND password = :password")
    fun getBalance(username: String, password: String) : Double

    @Query("UPDATE users_table SET balance = :balance WHERE username = :username")
    suspend fun updateBalance(username: String, balance: Double)


    @Query("UPDATE users_table SET password = :password WHERE username = :username")
    suspend fun changePassword(username: String, password: String)

    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * FROM history WHERE sender = :name OR receiver = :name")
    fun getFullHistory(name: String) : LiveData<List<History>>

    @Delete
    suspend fun deleteAccount(user : User)

    suspend fun insertUsers(users : List<User>) {
        for (user in users) {
            insert(user)
        }
    }


}