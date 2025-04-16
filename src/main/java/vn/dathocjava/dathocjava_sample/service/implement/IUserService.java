package vn.dathocjava.dathocjava_sample.service.implement;

import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Min;
import vn.dathocjava.dathocjava_sample.dto.request.UserRequestDTO;
import vn.dathocjava.dathocjava_sample.dto.response.UserDetailResponse;
import vn.dathocjava.dathocjava_sample.response.PageResponse;
import vn.dathocjava.dathocjava_sample.util.UserStatus;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService {
    long saveUser(UserRequestDTO userRequestDTO) throws MessagingException, UnsupportedEncodingException;

    void updateUser(long userId, UserRequestDTO userRequestDTO) ;

    void changeStatus(long userId, UserStatus userStatus);

    void deleteUser(long userId);

    UserDetailResponse getUser ( long userId);

    List<UserDetailResponse> getAllUser(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAllUserByMutipleOrders(int pageNo, int pageSize, String... sorts);
    PageResponse<?> getAllUserBySortColumnAndSearch(int pageNo, int pageSize, String search, String sorts);
    void confirmUser(@Min(1) int userId, String secretCode );
}
