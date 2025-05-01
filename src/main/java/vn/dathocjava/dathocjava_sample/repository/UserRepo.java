package vn.dathocjava.dathocjava_sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.dathocjava.dathocjava_sample.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Boolean existsByEmail (String email);
    Optional<User> findByEmail(String email);
    List<User> findByEmailIn(List<String> emails);
}