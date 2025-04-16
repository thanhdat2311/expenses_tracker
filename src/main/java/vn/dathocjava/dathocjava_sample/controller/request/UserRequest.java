package vn.dathocjava.dathocjava_sample.controller.request;

import vn.dathocjava.dathocjava_sample.util.Gender;
import vn.dathocjava.dathocjava_sample.util.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserRequest implements Serializable {
        private String firstName;
        private String lastName;
        private Gender gender;
        private Date birthday;
        private String username;
        private String email;
        private String phone;
        private UserType type;
        private List<AddressRequest> addresses; // home,office
}
