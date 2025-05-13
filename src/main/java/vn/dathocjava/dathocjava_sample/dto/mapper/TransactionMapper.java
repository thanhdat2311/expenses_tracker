package vn.dathocjava.dathocjava_sample.dto.mapper;

import vn.dathocjava.dathocjava_sample.dto.request.TransactionDTO;
import vn.dathocjava.dathocjava_sample.dto.response.TransactionResponse;
import vn.dathocjava.dathocjava_sample.model.Category;
import vn.dathocjava.dathocjava_sample.model.Transaction;
import vn.dathocjava.dathocjava_sample.model.User;

import java.text.DecimalFormat;

public class TransactionMapper {

    public static Transaction toEntity(TransactionDTO dto, User user, Category category) {
        return Transaction.builder()
                .user(user)
                .category(category)
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .transactionDate(dto.getTransactionDate())
                .build();
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .formattedAmount(decimalFormat.format(transaction.getAmount()))
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .category(CategoryMapper.toResponse(transaction.getCategory()))
                .userEmail(transaction.getUser().getEmail()) // hoặc getId() nếu bạn muốn
                .build();
    }
}

