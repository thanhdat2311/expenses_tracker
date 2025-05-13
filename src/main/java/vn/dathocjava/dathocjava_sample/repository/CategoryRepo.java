package vn.dathocjava.dathocjava_sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.dathocjava.dathocjava_sample.model.Category;
import vn.dathocjava.dathocjava_sample.model.User;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
