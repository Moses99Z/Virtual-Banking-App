package com.example.banking.data.database

import com.example.banking.data.model.User

class ListOfUsers {

    companion object {
        fun getUsers() : List<User> {
            return listOf(
                User(0, "Moses", "moses1234", "moses@gmail.com"),
                User(0, "John", "john1234", "john@gmail.com"),
                User(0, "James", "james1234", "james@gmail.com"),
                User(0, "Micheal", "micheal1234", "micheal@gmail.com"),
                User(0, "steven", "steven1234", "steven@gmail.com"),
                User(0, "Peter", "peter1234", "peter@gmail.com"),
            )
        }
    }

}