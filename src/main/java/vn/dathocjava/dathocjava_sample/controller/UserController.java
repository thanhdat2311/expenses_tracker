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

@RestController
@RequestMapping("/user")
public class UserController {
    @Operation(summary = "summary", description = "description", responses = {
            @ApiResponse(responseCode = "201",description = "user added successfully!"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
            value = """
                    {
                        "status": 201,
                        "message": "Create successfully",
                        "data": null
                    }
                    """)
            )
            )
    })
    @PostMapping("/")
    public ResponeSuccess addUser(@Valid @RequestBody UserRequestDTO userrequestDTO){
        return new ResponeSuccess(HttpStatus.CREATED,"Create successfully");

    }
    @PutMapping("/{userId}")
    public ResponeSuccess updateUser (@Min(1) @PathVariable("userId") int id,
                             @Valid @RequestBody UserRequestDTO userRequestDTO){
        System.out.println("request update user id = "+ id);
        return new ResponeSuccess(HttpStatus.ACCEPTED,"Update successfully");
    }
    @GetMapping("/{userId}")
    public ResponeSuccess getUser (@Min(1) @PathVariable("userId") int id){
        System.out.println("delete user id = "+ id);
        return new ResponeSuccess(HttpStatus.NO_CONTENT,"delete user id = "+ id);
    }
    @DeleteMapping("/{userId}")
    public ResponeSuccess deleteUser (@Min(1) @PathVariable("userId") int id){
        System.out.println("delete user id = "+ id);
        return new ResponeSuccess(HttpStatus.NO_CONTENT,"delete user id = "+ id);
    }
}
// 16:00