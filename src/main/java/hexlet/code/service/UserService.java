package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.model.UserRole;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private PasswordEncoder encoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public User createUser(UserDto userDto) {

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }

    public User updateUser(UserDto userDto, long id) {

        User user = userRepository.findById(id).orElseThrow();

        User currentUser = userDetailsServiceImpl.getCurrentUserName();

        if (!Objects.equals(currentUser, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(user.getRole());
        return userRepository.save(user);
    }

    public void deleteUser(long id) {

        User user = userRepository.findById(id).orElseThrow();
        User currentUser = userDetailsServiceImpl.getCurrentUserName();

        if (!Objects.equals(currentUser, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        userRepository.deleteById(id);
    }

}
