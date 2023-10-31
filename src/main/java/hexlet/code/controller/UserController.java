package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.exception.UserNotFoundException;
import hexlet.code.model.User;
import hexlet.code.model.UserRole;
import hexlet.code.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping(path = "")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public User getUser(@PathVariable long id) {
        return userRepository.findById(id)
               .orElseThrow(() -> new UserNotFoundException("User" + id + "not found"));
    }

    @PostMapping(path = "")
    public User createUser(@Valid @RequestBody UserDto userDto) {

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }

    @PatchMapping(path = "/{id}")
    public User updateUser(@RequestBody UserDto userDto, @PathVariable long id) {
        if (!userRepository.existsById(id)) {
            // Если не существует, возвращаем код ответа 404
            throw new UserNotFoundException("User" + id + "not found");
        }
        User user = userRepository.findById(id).get();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(@PathVariable long id) {
        if (!userRepository.existsById(id)) {
            // Если не существует, возвращаем код ответа 404
            throw new UserNotFoundException("User" + id + "not found");
        }
        userRepository.deleteById(id);
    }


}
