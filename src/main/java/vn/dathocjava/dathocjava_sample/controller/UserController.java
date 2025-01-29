package vn.dathocjava.dathocjava_sample.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.dathocjava.dathocjava_sample.dto.request.UserRequestDTO;
import vn.dathocjava.dathocjava_sample.response.ResponeSuccess;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.response.ResponseError;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    public ResponseError addUser(@Valid @RequestBody UserRequestDTO userrequestDTO){
        //return new ResponseData<>(HttpStatus.CREATED.value(),"Create successfully");
        return new ResponseError(HttpStatus.BAD_REQUEST.value(),"Create unsuccessfully!");
    }
    @PutMapping("/{userId}")
    public ResponeSuccess updateUser (@Min(1) @PathVariable("userId") int id,
                             @Valid @RequestBody UserRequestDTO userRequestDTO){
        System.out.println("request update user id = "+ id);
        return new ResponeSuccess(HttpStatus.ACCEPTED,"Update successfully");
    }
    @GetMapping("/{userId}")
    public ResponseData<List<UserRequestDTO>> getUser (@Min(1) @PathVariable("userId") int id){
        System.out.println("delete user id = "+ id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(),"delete user id = "+ id,
                List.of(new UserRequestDTO("thanh","dat","0123456789",
                        "datmkt2311@gmail.com"),
                        new UserRequestDTO("thanh", "dat", "0123456789",
                        "datmkt2311@gmail.com")));
    }
    @DeleteMapping("/{userId}")
    public ResponeSuccess deleteUser (@Min(1) @PathVariable("userId") int id){
        System.out.println("delete user id = "+ id);
        return new ResponeSuccess(HttpStatus.NO_CONTENT,"get user");
    }
}
// 24:10