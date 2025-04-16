package vn.dathocjava.dathocjava_sample.service.implement.impl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.dathocjava.dathocjava_sample.dto.request.AddressDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserRequestDTO;
import vn.dathocjava.dathocjava_sample.dto.response.UserDetailResponse;
import vn.dathocjava.dathocjava_sample.exception.ResourceNotFoundException;
import vn.dathocjava.dathocjava_sample.model.Address;
import vn.dathocjava.dathocjava_sample.model.User;
import vn.dathocjava.dathocjava_sample.repository.SearchRepository;
import vn.dathocjava.dathocjava_sample.repository.UserRepo;
import vn.dathocjava.dathocjava_sample.response.PageResponse;
import vn.dathocjava.dathocjava_sample.service.implement.IUserService;
import vn.dathocjava.dathocjava_sample.util.UserStatus;
import vn.dathocjava.dathocjava_sample.util.UserType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final SearchRepository searchRepo;
    private final MailService mailService;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long saveUser(UserRequestDTO request)  throws MessagingException, UnsupportedEncodingException {
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
        if(user.getId() != 0 ){
           // mailService.sendConfirmLink(user.getEmail(), user.getId(),"secretcode");
            String message = String.format("%s,%s,%s",
                    user.getEmail(),
                    user.getId(),"secretcode");
            kafkaTemplate.send("confirm-account-topic",message);
        }
        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserRequestDTO request) {
        User user = getUserById(userId);
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setDateOfBirth(request.getDOB());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        if (!request.getEmail().equals(user.getEmail())) {
            // check email from database if not exist then allow update email otherwise throw exception
            user.setEmail(request.getEmail());
        }
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(request.getUserStatus());
        user.setType(UserType.valueOf(request.getType().toUpperCase()));
        user.setAddresses(convertToAddress(request.getAddresses()));
        userRepo.save(user);
        log.info("User has updated successfully, userId={}", userId);

    }

    @Override
    public void changeStatus(long userId, UserStatus userStatus) {

    }

    @Override
    public void deleteUser(long userId) {
        userRepo.deleteById(userId);
        log.info("User has deleted permanent successfully, userId={}", userId);
    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = this.getUserDetail(userId);
        UserDetailResponse userDetailResponse;
        return  userDetailResponse = UserDetailResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUser(int pageNo, int pageSize, String sortBy ) {
        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)){
            //id:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher =pattern.matcher(sortBy);
            if (matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageableUser = PageRequest.of(pageNo,
                pageSize,
                Sort.by(sorts));
        Page<User> pageUser = userRepo.findAll(pageableUser);
        return pageUser.stream().map(user -> UserDetailResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()
        ).toList();
    }

    @Override
    public PageResponse<?> getAllUserByMutipleOrders(int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy: sorts) {
            if (StringUtils.hasLength(sortBy)) {
                //id:asc|desc
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    }
                    if (matcher.group(3).equalsIgnoreCase("desc")) {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }
        Pageable pageableUser = PageRequest.of(pageNo,
                pageSize,
                Sort.by(orders));
        Page<User> pageUser = userRepo.findAll(pageableUser);

        List<UserDetailResponse> responses = pageUser.stream().map(user -> UserDetailResponse
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .build()
                ).toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(pageUser.getTotalPages())
                .items(responses)
                .build();
}

    @Override
    public PageResponse<?> getAllUserBySortColumnAndSearch(int pageNo, int pageSize, String search, String sorts) {
        return searchRepo.getAllUserBySortColumnAndSearch( pageNo,  pageSize,  search,  sorts);
    }

    @Override
    public void confirmUser(int userId, String secretCode) {
        log.info("Confirmed!");
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
    private User getUserById(long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not Found User!!"));
    }
}
