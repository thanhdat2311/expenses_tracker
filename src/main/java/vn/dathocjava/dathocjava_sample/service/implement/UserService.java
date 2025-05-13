package vn.dathocjava.dathocjava_sample.service.implement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.dathocjava.dathocjava_sample.components.JwtTokenUtil;
import vn.dathocjava.dathocjava_sample.dto.request.UserChangePassDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserUpdateDTO;
import vn.dathocjava.dathocjava_sample.model.User;
import vn.dathocjava.dathocjava_sample.repository.UserRepo;
import vn.dathocjava.dathocjava_sample.service.interfaceClass.IUserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        return null;
    }

    @Override
    public String login(String email, String password, Long RoleId) throws Exception {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new Exception("Invalid phone number/password");
        } else {
            System.out.println("Have User");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid phone number/password");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public User updateUser(UserUpdateDTO userUpdateDTO, String userEmail) throws Exception {
        return null;
    }

    @Override
    public List<User> getUserList() {
        return List.of();
    }

    @Override
    public User getUserDetails(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }

        String email = jwtTokenUtil.extractEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found"));
        return user;
    }

    @Override
    public User changePassword(UserChangePassDTO userChangePassDTO, String token) throws Exception {
        return null;
    }

    @Override
    public Boolean deleteUser(String userEmail) throws Exception {
        return null;
    }
}
