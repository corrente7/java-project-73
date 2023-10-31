package hexlet.code.service;

import hexlet.code.dto.LoginDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtTokenUtil jwtUtils;

    public String authenticate(LoginDto requestDto) {
        User existedUser = userRepository.findUserByEmailIgnoreCase(requestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Sign in failed. User not found!"));

        String passwordToCheck = requestDto.getPassword();
        if (!encoder.matches(passwordToCheck, existedUser.getPassword())) {
            throw new UsernameNotFoundException("Sign in failed. Incorrect password!");
        }
        return jwtUtils.generateToken(existedUser);
    }
}
