package vn.dathocjava.dathocjava_sample.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    @JsonProperty("fullname")
    private String fullName;
    @NotBlank(message = "Email can not be empty!")
    private  String email;
    private String phone;
    private String address;
    @NotNull(message = "Role can not be empty!")
    private Long roleId;
    private int is_active;
}
