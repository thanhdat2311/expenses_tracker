package vn.dathocjava.dathocjava_sample.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.dathocjava.dathocjava_sample.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    @NotNull(message = "Category cannot null")
    private Long categoryId;
    @NotBlank(message = "Email cannot null")
    private String userEmail;
    @NotNull(message = "Amount cannot null")
    private BigDecimal amount;
    private String description;
    @NotNull(message = "Date cannot null")
    private Date transactionDate;
    public TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .categoryId(transaction.getCategory().getId())
                .userEmail(transaction.getUser().getEmail())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

}

