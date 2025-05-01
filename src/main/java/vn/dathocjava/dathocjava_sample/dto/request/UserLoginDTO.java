package vn.dathocjava.dathocjava_sample.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Email can not be empty!")
    private  String email;
    @NotBlank(message = "Password can not be empty!")
    private String password;
    @NotNull(message = "Password can not be empty!")
    private Long roleId;
}