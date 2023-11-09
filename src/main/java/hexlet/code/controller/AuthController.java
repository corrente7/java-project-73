package hexlet.code.controller;

import hexlet.code.dto.LoginDto;
import hexlet.code.service.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthController {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @Operation(summary = "Log in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log in user")
    })

    @PostMapping(path = "/api/login")

    public String authUser(@Valid @RequestBody LoginDto requestDto) {
        return authServiceImpl.authenticate(requestDto);

//    ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginDto requestDto) {
//            return ResponseEntity.ok(authServiceImpl.authenticate(requestDto));
 }

//        } catch (DisabledException e) {
//            return new ResponseEntity<>("USER_DISABLED" + e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>("INVALID_CREDENTIALS"+e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

}

