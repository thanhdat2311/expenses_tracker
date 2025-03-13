package vn.dathocjava.dathocjava_sample.dto.response;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.dathocjava.dathocjava_sample.util.Gender;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class UserDetailResponse implements Serializable {
    private String firstName;

    private String lastName;


    private Date dateOfBirth;

    private String phone;

    private String email;
}
