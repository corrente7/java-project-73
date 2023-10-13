package hexlet.code.controller;

import hexlet.code.dto.AuthResponseDto;
import hexlet.code.dto.AuthRequestDto;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.AuthServiceImpl;
import hexlet.code.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
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
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequest) throws Exception {
//
//        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//        final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
//
//        return ResponseEntity.ok(new AuthenticateResponseDto(token));
//    }


    ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequestDto requestDto) {
        return ResponseEntity.ok(authServiceImpl.authenticate(requestDto));
    }
//        try {

//            Authentication authentication =
//                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);





//        } catch (DisabledException e) {
//            return new ResponseEntity<>("USER_DISABLED" + e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>("INVALID_CREDENTIALS"+e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }




}
