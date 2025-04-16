package vn.dathocjava.dathocjava_sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import vn.dathocjava.dathocjava_sample.model.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    // Starting with
    // Query value = "Select * from User u where u.lastName not like :lastName"
    List<User> findByLastNameStartingWith(String lastName);


    // Ending with
    // Query value = "Select * from User u where u.lastName not like :lastName"
    List<User> findByLastNameEndingWith(String lastName);


    // Containing
    // Query value = "select * from User u where u.lastName not like :lastName"
    List<User> findByLastNameContaining(String lastName);


    // In
    // Query value = "select * from User u where u.age in (:ages)"
    // List<User> findByAgeIn(Collection<Integer> ages);


    // True
    // Query value = "select * from User u where u.activated not true"
    //List<User> findByActivatedFalse();

    // IgnoreCase
    List<User> findByFirstNameIgnoreCase(String name);

    List<User> findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);

    //orderby
    List<User> findByFirstNameOrderByCreatedAtDesc(String name);

    User findByUsername(String username);

}
