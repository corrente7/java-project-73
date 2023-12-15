package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.service.UserDetailsServiceImpl;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get list of all users")
    })
    @GetMapping(path = "")
    public List<User> getUsers() {

        return userService.getUsers();
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get a specific user by id")
    })
    @GetMapping(path = "/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User successfully created"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);

    }

    @Operation(summary = "Update user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PutMapping(path = "/{id}")
    public User updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long id) {
        return userService.updateUser(userDto, id);
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete user by id")
    })
    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
