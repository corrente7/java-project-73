package hexlet.code.controller;

import hexlet.code.dto.LoginDto;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.AuthServiceImpl;
import hexlet.code.util.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthServiceImpl authServiceImpl;


    @PostMapping(path = "/login")

    ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginDto requestDto) {
            return ResponseEntity.ok(authServiceImpl.authenticate(requestDto));
 }




//        } catch (DisabledException e) {
//            return new ResponseEntity<>("USER_DISABLED" + e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>("INVALID_CREDENTIALS"+e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }




}
