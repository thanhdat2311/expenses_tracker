package vn.dathocjava.dathocjava_sample.service.interfaceClass;

import vn.dathocjava.dathocjava_sample.dto.request.UserChangePassDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserUpdateDTO;
import vn.dathocjava.dathocjava_sample.model.User;

import java.util.List;

public interface IUserService {
    User createUser (UserDTO userDTO) throws Exception ;
    String login(String phone, String password, Long RoleId) throws Exception;
    User updateUser(UserUpdateDTO userUpdateDTO, String userEmail)throws Exception;
    List<User> getUserList() ;
    User getUserDetails(String token) throws Exception;
    User changePassword(UserChangePassDTO userChangePassDTO, String token) throws Exception;
    Boolean deleteUser( String userEmail) throws Exception;
}
