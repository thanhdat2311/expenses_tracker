package vn.dathocjava.dathocjava_sample.service.implement;

import vn.dathocjava.dathocjava_sample.dto.request.UserRequestDTO;
import vn.dathocjava.dathocjava_sample.dto.response.UserDetailResponse;
import vn.dathocjava.dathocjava_sample.util.UserStatus;

import java.util.List;

public interface IUserService {
    long saveUser(UserRequestDTO userRequestDTO);

    void updateUser(long userId, UserRequestDTO userRequestDTO);

    void changeStatus(long userId, UserStatus userStatus);

    void deleteUser(long userId);

    UserDetailResponse getUser ( long userId);

    List<UserDetailResponse> getAllUser(int pageNo, int pageSize);

}
