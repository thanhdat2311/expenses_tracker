package vn.dathocjava.dathocjava_sample.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.dathocjava.dathocjava_sample.dto.request.UserChangePassDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserLoginDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserUpdateDTO;
import vn.dathocjava.dathocjava_sample.model.User;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.response.UserResponse;
import vn.dathocjava.dathocjava_sample.service.implement.UserService;

import java.util.List;

@RestController
@RequestMapping("expenses-tracker/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {

            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                // đổi sang báo cả status
                return ResponseEntity.badRequest().body("Wrong Retype Password");
            }
            User userRegister = userService.createUser(userDTO);
            if (userRegister == null) {
                return ResponseEntity.badRequest().body("Email is exist");
            }
            ;
            return ResponseEntity.ok(userRegister);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData<?>> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                     BindingResult result) {
        try {
            String token = userService.login(userLoginDTO.getEmail(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId()
            );
            return ResponseEntity.ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Login successfully!",token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));


        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getTasksByCompanyId() {
        try {
            List<User> userList = userService.getUserList();
            List<UserResponse> userResponse = UserResponse.fromListUser(userList);
            return ResponseEntity.ok().body(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/details")
    public ResponseData<?> getUserDetails(@RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7);
            User user = userService.getUserDetails(token.toString());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(),
                    "Get User Detail successfully!",
                    UserResponse.fromUser(user));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_GATEWAY.value(), e.getMessage());
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePassDTO userChangePassDTO, @RequestHeader("Authorization") String token, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            token = token.substring(7);
            User user = userService.changePassword(userChangePassDTO, token);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{userEmail}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                        @PathVariable String userEmail,
                                        BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            User user = userService.updateUser(userUpdateDTO, userEmail);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userEmail}")
    public ResponseEntity<?> deleteUser( @PathVariable String userEmail) {
        try {
            if(userEmail == null || userEmail.trim().isEmpty()){
                return ResponseEntity.badRequest().body("User email cannot be null r empty!");
            }
            Boolean deleteUser = userService.deleteUser(userEmail);
            if (deleteUser) {
                return ResponseEntity.ok().body("Delete Successfully!");
            } else {
                return ResponseEntity.ok().body("Delete UnSuccessfully!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
