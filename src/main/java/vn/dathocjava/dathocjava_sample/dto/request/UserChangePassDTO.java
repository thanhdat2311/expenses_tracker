package vn.dathocjava.dathocjava_sample.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePassDTO {
    @NotBlank(message = "Password can not be empty!")
    private String oldPassword;
    @NotBlank(message = "Password can not be empty!")
    private String newPassword;
    @NotBlank(message = "Retype Password can not be empty!")
    private  String retypePassword;

}