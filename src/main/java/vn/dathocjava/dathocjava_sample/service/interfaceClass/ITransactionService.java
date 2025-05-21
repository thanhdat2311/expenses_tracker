package vn.dathocjava.dathocjava_sample.service.interfaceClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import vn.dathocjava.dathocjava_sample.dto.request.TransactionDTO;
import vn.dathocjava.dathocjava_sample.dto.response.PageResponse;
import vn.dathocjava.dathocjava_sample.dto.response.TransactionResponse;
import vn.dathocjava.dathocjava_sample.model.Transaction;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ITransactionService {
    TransactionResponse createTransaction(TransactionDTO transactionDTO) throws Exception;

    Long deleteTransaction(Long transactionId) throws Exception;

    TransactionResponse updateTransaction(Long transactionId, TransactionDTO transactionDTO) throws Exception;

    PageResponse<List<TransactionResponse>> getAllTransaction(String token, int pageNo, int pageSize, String sortBy);

    void monthlyReportGroup(String message)  throws JsonProcessingException;
}