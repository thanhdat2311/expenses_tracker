package vn.dathocjava.dathocjava_sample.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;
import java.util.Date;
@Getter
@Builder
@AllArgsConstructor
public class UserDetailResponse implements Serializable {
    private Long id;

    private String firstName;

    private String lastName;


    private Date dateOfBirth;

    private String phone;

    private String email;

}
