package vn.dathocjava.dathocjava_sample.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.dathocjava.dathocjava_sample.model.Transaction;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction,Long> {
 List<Transaction> findByUserId (Long userId);
 Page<Transaction> findAllByUserId(Long userId, Pageable pageable);

}
