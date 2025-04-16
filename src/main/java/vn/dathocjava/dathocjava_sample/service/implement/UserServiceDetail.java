package vn.dathocjava.dathocjava_sample.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.dathocjava.dathocjava_sample.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class UserServiceDetail {
    private final UserRepo userRepo;
    public UserDetailsService UserServiceDetail(){
        return userRepo::findByUsername;
    }
}
