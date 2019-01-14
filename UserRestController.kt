package com.vacuna.api.controller

import com.vacuna.api.model.User
import com.vacuna.api.response.APIResponse
import com.vacuna.data.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * Created by Vlad Sima on 8/23/2016.
 */

@RestController
@RequestMapping("api/v1/users")
class UserRestController {

    @Autowired
    private lateinit var userRepo: UserRepository


    @RequestMapping(value = "", method = arrayOf(RequestMethod.GET))
    fun allUsers()= APIResponse(200, true, userRepo.allUsers())

    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.GET))
    fun getUserById(@PathVariable id: Long): APIResponse {

        val user = userRepo.getUserById(id)

        if (user != null)
            return APIResponse(200, true, user)
        else
            return APIResponse(404, false, "A user with the ID $id does not exist.")
    }


    @RequestMapping(value = "/authority/{authority}", method = arrayOf(RequestMethod.GET))
    fun getUsersByAuthority(@PathVariable authority: String): APIResponse {

        if (authority.equals("employee", ignoreCase = true) || authority.equals("manager", ignoreCase = true))
            return APIResponse(200, true, userRepo.getUsersByAuthority(authority))
        else
            return APIResponse(400, false, "That authority does not exist.")
    }


    @RequestMapping(value = "/name/{id}", method = arrayOf(RequestMethod.GET))
    fun getUserFullNameById(@PathVariable id: Long): APIResponse {

        val user = userRepo.getUserById(id)

        if (user != null) {
            val employeeFullName = user.firstName + " " + user.lastName
            return APIResponse(200, true, employeeFullName)
        } else
            return APIResponse(404, false, "Could not retrieve full name. A user with the ID $id does not exist.")
    }


    @ResponseBody
    @RequestMapping(value = "/updateUser", method = arrayOf(RequestMethod.PUT), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun updateEmployee(@RequestBody user: User): APIResponse {

        val userTemp = userRepo.getUserById(user.id)

        if (userTemp != null) {

            userRepo.updateUser(user.id, user.firstName,
                    user.lastName, user.username, user.password)

            return APIResponse(200, true, "Updated user.")
        } else
            return APIResponse(404, false, "A user with the id: " + user.id + " does not exist")
    }


    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.DELETE))
    fun deleteUserById(@PathVariable id: Long): APIResponse {

        val user = userRepo.getUserById(id)

        if (user != null) {

            userRepo.deleteUserByID(id)

            return APIResponse(200, true, "Deleted user with id: " + id)
        } else
            return APIResponse(404, false, "A user with the id $id does not exist.")
    }


    @ResponseBody
    @RequestMapping(value = "/signUp", method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun signUpEmployee(@RequestBody user: User): APIResponse {


            userRepo.createUser(user.firstName, user.lastName,
                    user.username, user.password, user.enabled)

            return APIResponse(200, true, "Sign up was successful.")

        //    return APIResponse(400, false, "Values have not been specified for all required properties.")
    }


    @ResponseBody
    @RequestMapping(value = "/logIn", method = arrayOf(RequestMethod.PUT), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun logInEmployee(@RequestBody user: User): APIResponse {

        val id = userRepo.login(user.username, user.password)

        if (id != null)
            return APIResponse(200, true, id)
        else
            return APIResponse(404, false, "A user with that username and password does not exist.")
    }
}
