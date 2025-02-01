package vn.dathocjava.dathocjava_sample.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.dathocjava.dathocjava_sample.util.*;

import java.io.Serializable;
import java.util.Date;

public class UserRequestDTO implements Serializable {
    @NotBlank(message = " First Name Not blank")
    private String firstname;
    @NotBlank(message = "Last Name Not blank")
    private String lastname;
    //@Pattern(regexp = "^\\d{10}$", message = "phone invalid format")
    @NotBlank(message = "Phone cannot Blank")
    @PhoneNumber
    private String phone;
    @NotBlank(message = "Email cannot Blank")
    @Email (message = "email invalid")
    private String email;
    // cái này chỉ dùng được với status
    //@Pattern(regexp = "^ACTIVE|INACTIVE|NONE$", message = "status must be one in {ACTIVE, INACTIVE, NONE}")
    @NotNull(message = "user status cannot null")
    @EnumPattern(name = "userStatus", regexp = "ACTIVE|INACTIVE|NONE")
    private UserStatus userStatus;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date DOB;



    @GenderSubset(anyOf = {Gender.MALE,Gender.FEMALE,Gender.OTHER})
    private Gender gender;
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }



    public UserRequestDTO(String firstname, String lastname, String phone, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserRequestDTO() {
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
