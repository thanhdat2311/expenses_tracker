package vn.dathocjava.dathocjava_sample.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.dathocjava.dathocjava_sample.model.Category;
import vn.dathocjava.dathocjava_sample.model.MessageChat;

import java.util.List;

@Repository
public interface ChatMessageRepo extends JpaRepository<MessageChat,Long> {
    List<MessageChat> findAllByEmail(String email);
    @Modifying
    @Transactional
    @Query(value = "UPDATE chat_logs SET is_deleted = true WHERE email = :email", nativeQuery = true)
    void softDeleteByEmail(String email);
}
