package vn.dathocjava.dathocjava_sample.service.implement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dathocjava.dathocjava_sample.dto.request.AddressDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserRequestDTO;
import vn.dathocjava.dathocjava_sample.dto.response.UserDetailResponse;
import vn.dathocjava.dathocjava_sample.model.Address;
import vn.dathocjava.dathocjava_sample.model.User;
import vn.dathocjava.dathocjava_sample.repository.UserRepo;
import vn.dathocjava.dathocjava_sample.util.UserStatus;
import vn.dathocjava.dathocjava_sample.util.UserType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepo userRepo;

    @Override
    public long saveUser(UserRequestDTO request) {
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .dateOfBirth(request.getDOB())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .status(request.getUserStatus())
                .type(UserType.valueOf(request.getType().toUpperCase()))
//                .addresses(convertToAddress(request.getAddresses()))
                .build();
        request.getAddresses().forEach(a ->
                user.saveAddress(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build()
                )
        );
        userRepo.save(user);
        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserRequestDTO userRequestDTO) {

    }

    @Override
    public void changeStatus(long userId, UserStatus userStatus) {

    }

    @Override
    public void deleteUser(long userId) {

    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = this.getUserDetail(userId);
        UserDetailResponse userDetailResponse;
        return  userDetailResponse = UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUser(int pageNo, int pageSize) {
        Pageable pageableUser = PageRequest.of(pageNo, pageSize);
        Page<User> pageUser = userRepo.findAll(pageableUser);
        return pageUser.stream().map(user -> UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()
        ).toList();
    }
    private Set<Address> convertToAddress(Set<AddressDTO> addresses) {
        Set<Address> result = new HashSet<>();
        addresses.forEach(a ->
                result.add(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build())
        );
        return result;
    }
    private User getUserDetail(long userId){
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cannot find user!"));
    }
}
