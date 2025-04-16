package vn.dathocjava.dathocjava_sample.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.dathocjava.dathocjava_sample.dto.request.UserRequestDTO;
import vn.dathocjava.dathocjava_sample.dto.response.UserDetailResponse;
import vn.dathocjava.dathocjava_sample.response.PageResponse;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.response.ResponseError;
import vn.dathocjava.dathocjava_sample.service.implement.impl.UserService;

import java.io.IOException;
import java.util.List;
import static jakarta.servlet.RequestDispatcher.ERROR_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    // post
    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
    @PostMapping("/add")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO userrequestDTO) {
        try {
            long userId = userService.saveUser(userrequestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Add user Successfully", userId);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Confirm user", description = "Send a request via this API to confirm user")
    @GetMapping("/confirm/{userId}")
    public ResponseData<String> confirm(@Min(1) @PathVariable int userId,
                                        @RequestParam String verifyCode,
                                        HttpServletResponse httpServletResponse) throws IOException {
        log.info("Confirm user, userId={}, verifyCode={}", userId, verifyCode);

        try {
            userService.confirmUser(userId, verifyCode);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User has confirmed successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Confirm was failed");
        }finally {
            httpServletResponse.sendRedirect("https://www.facebook.com/");
        }
    }



    // put
    @PutMapping("/{userId}")
    public ResponseData<Void> updateUser(@PathVariable @Min(1) long userId, @Valid @RequestBody UserRequestDTO request) {
        log.info("Request update userId={}", userId);

        try {
            userService.updateUser(userId, request);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update successfully!");
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // get
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
                                                        @RequestParam("pageSize") int pageSize,
                                                              @RequestParam(required = false) String sortBy
    ) {
        try {
            List<UserDetailResponse> userDetailResponseList = userService.getAllUser(pageNo,pageSize, sortBy);
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get User Successfully!",
                    userDetailResponseList
            );
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Get Fail!");
        }
    }

    @Operation(method = "GET", summary = "get list of Users with multiple sort", description = "Multiple sort get list")
    @GetMapping("/list/mutiple-order")
    public ResponseData<PageResponse<?>> getUserListMultipleSort(@RequestParam("pageNo") int pageNo,
                                                                 @RequestParam("pageSize") int pageSize,
                                                                 @RequestParam(required = false) String... sortBy
    ) {
        try {
            PageResponse userDetailResponseList = userService.getAllUserByMutipleOrders(pageNo,pageSize, sortBy);
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get User Successfully!",
                    userDetailResponseList
            );
        } catch (Exception e) {
            return  new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @Operation(method = "GET", summary = "get list of Users with multiple sort", description = "Multiple sort get list")
    @GetMapping("/list/search-order")
    public ResponseData<PageResponse<?>> getUserListbySortAndSearch(@RequestParam("pageNo") int pageNo,
                                                                 @RequestParam("pageSize") int pageSize,
                                                                    @RequestParam("search") String search,
                                                                 @RequestParam(required = false) String sortBy
    ) {
        try {
            PageResponse userDetailResponseList = userService.getAllUserBySortColumnAndSearch(pageNo,pageSize, search ,sortBy);
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get User Successfully!",
                    userDetailResponseList
            );
        } catch (Exception e) {
            return  new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // delete

    @Operation(summary = "Delete user permanently", description = "Send a request via this API to delete user permanently")
    @DeleteMapping("/{userId}")
    public ResponseData<Void> deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") long userId) {
        log.info("Request delete userId={}", userId);

        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete successfully!");
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }

}
// 24:10