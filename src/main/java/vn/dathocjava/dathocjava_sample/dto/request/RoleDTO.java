package vn.dathocjava.dathocjava_sample.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotEmpty(message = "Role e can not be empty!")
    private String name;
    private  String description;
}
