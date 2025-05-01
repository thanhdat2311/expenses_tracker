package vn.dathocjava.dathocjava_sample.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import vn.dathocjava.dathocjava_sample.model.Role;
import vn.dathocjava.dathocjava_sample.model.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String email;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long is_active;


    public static List<UserResponse> fromListUser(List<User> userList){
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList){
            UserResponse userResponse = UserResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .role(user.getRole())
                    .is_active( user.getIs_active() != null ? Long.valueOf(user.getIs_active()):1)
                    .build();
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }
    public static UserResponse fromUser(User user){
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .build();

        return userResponse;
    }
}
