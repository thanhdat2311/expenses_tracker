package vn.dathocjava.dathocjava_sample.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "chat_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = false")
public class MessageChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "is_from_user", nullable = false)
    private boolean isFromUser;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_new_chat", nullable = false)
    private boolean isNewChat = false;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;


    @Column(name = "created_at")
    private Date createdAt = new Date();
}

