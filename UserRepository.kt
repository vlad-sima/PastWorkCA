package com.vacuna.data.repo

import com.vacuna.api.model.User
import com.vacuna.data.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Created by Vlad Sima on 8/22/2016.
 */

@Repository
open class UserRepository {

    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    open fun createUser(firstName: String, lastName: String, username: String, password: String, enabled: Boolean?) {
        try {
            jdbcTemplate!!.update("INSERT INTO users(firstName, lastName, username, password, enabled) VALUES (?, ?, ?, ?, ?)", firstName,
                    lastName, username, password, enabled)
        } catch (e: Exception) {
            println(e.message)
        }

    }

    open fun updateUser(id: Long, firstName: String, lastName: String, username: String, password: String) {
        try {
            jdbcTemplate!!.update("UPDATE users SET firstName = '" + firstName + "', lastName = '" + lastName + "', username = '"
                    + username + "', password = '" + password + "' WHERE id = " + id)
        } catch (e: Exception) {
            println(e.message)
        }

    }

    open fun deleteUserByID(id: Long) {
        try {
            jdbcTemplate!!.update("DELETE FROM users WHERE id = " + id)
        } catch (e: Exception) {
            print("invalid, could not delete user")
        }

    }

    open fun getUserById(id: Long?): User? {
        try {
            return jdbcTemplate!!.queryForObject("SELECT firstName, lastName, username, password, enabled, id FROM users u WHERE u.id = ?",
                    arrayOf<Any?>(id), UserMapper())
        } catch (e: Exception) {
            println(e.message)
            return null
        }

    }

    open  fun allUsers(): List<User>? {
            try {
                return jdbcTemplate!!.query("SELECT * FROM users", UserMapper())
            } catch (e: Exception) {
                println(e.message)
                return null
            }

        }

    open fun getUsersByAuthority(authority: String): List<User>? {
        try {
            return jdbcTemplate!!.query("SELECT * FROM users " +
                    "INNER JOIN authorities " +
                    "ON users.id = authorities.userId " +
                    "WHERE authorities.authority = '" + authority + "'", UserMapper())
        } catch (e: Exception) {
            println(e.message)
            return null
        }

    }


    open fun login(username: String, password: String): String? {
        try {
            val id = jdbcTemplate!!.queryForObject(
                    "SELECT id FROM users WHERE username = '$username' AND password = '$password'", String::class.java)
            return id
        } catch (e: Exception) {
            println(e.message)
            return null
        }

    }
}
