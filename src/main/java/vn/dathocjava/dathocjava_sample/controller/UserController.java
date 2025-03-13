package vn.dathocjava.dathocjava_sample.controller;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.dathocjava.dathocjava_sample.dto.request.UserRequestDTO;
import vn.dathocjava.dathocjava_sample.dto.response.UserDetailResponse;
import vn.dathocjava.dathocjava_sample.response.ResponeSuccess;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.response.ResponseError;
import vn.dathocjava.dathocjava_sample.service.implement.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
    @PostMapping(value = "")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO userrequestDTO) {
        try {
            long userId = userService.saveUser(userrequestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Add user Successfully", userId);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }

    @PutMapping("/{userId}")
    public ResponeSuccess updateUser(@PathVariable("userId") @Min(1) int id,
                                     @Valid @RequestBody UserRequestDTO userRequestDTO) {
        System.out.println("request update user id = " + id);
        return new ResponeSuccess(HttpStatus.ACCEPTED, "Update successfully");
    }

    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable("userId") @Min(1) int id) {
        try {
            UserDetailResponse userDetailResponse = userService.getUser(id);
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get User Successfully!",
                    userDetailResponse);
        } catch (Exception e) {
           return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Get Fail!");
        }
    }
    @GetMapping("/list")
    public ResponseData<List<UserDetailResponse>> getUserList(@RequestParam("pageNo") int pageNo,
                                                        @RequestParam("pageSize") int pageSize
    ) {
        try {
            List<UserDetailResponse> userDetailResponseList = userService.getAllUser(pageNo,pageSize);
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get User Successfully!",
                    userDetailResponseList
            );
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Get Fail!");
        }
    }
    @DeleteMapping("/{userId}")
    public ResponeSuccess deleteUser(@Min(1) @PathVariable("userId") int id) {
        System.out.println("delete user id = " + id);
        return new ResponeSuccess(HttpStatus.NO_CONTENT, "get user");
    }
}
// 24:10