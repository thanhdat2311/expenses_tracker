package vn.dathocjava.dathocjava_sample.dto.request;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class ChatMessageDTO {
    private String email;

    @NotNull(message = "isFromUser cannot be null")
    private Boolean fromUser;

    @NotBlank(message = "Message cannot be null or empty")
    private String message;

    @NotNull(message = "isNewChat cannot be null")
    private Boolean isNewChat;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    @NotNull(message = "createdAt cannot be null")
    private Date createdAt;
}
