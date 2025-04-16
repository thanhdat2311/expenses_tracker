package vn.dathocjava.dathocjava_sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dathocjava.dathocjava_sample.model.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
}
