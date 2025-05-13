package vn.dathocjava.dathocjava_sample.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.dathocjava.dathocjava_sample.dto.request.TransactionDTO;
import vn.dathocjava.dathocjava_sample.dto.request.UserDTO;
import vn.dathocjava.dathocjava_sample.dto.response.PageResponse;
import vn.dathocjava.dathocjava_sample.dto.response.TransactionResponse;
import vn.dathocjava.dathocjava_sample.model.Transaction;
import vn.dathocjava.dathocjava_sample.model.User;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.service.implement.TransactionService;
import vn.dathocjava.dathocjava_sample.service.implement.UserService;

import java.util.List;

@RestController
@RequestMapping("expenses-tracker/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final UserService userService;
    private final TransactionService transactionService;

    @PostMapping("/createTransaction")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
                                               BindingResult result) {
        try {

            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), errorMessages.toString()));
            }
            TransactionResponse transactionResponse = transactionService.createTransaction(transactionDTO);
            return ResponseEntity
                    .ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Create successfully!", transactionResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<?> deleteTransaction(@Min(value = 1) @PathVariable Long transactionId) {
        try {


            Long transaction = transactionService.deleteTransaction(transactionId);
            return ResponseEntity
                    .ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Delete successfully, transaction id: " + transaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransaction(@RequestHeader("Authorization") String authHeader,
                                                      @RequestParam("pageNo") int pageNo,
                                                      @RequestParam("pageSize") int pageSize,
                                                      @RequestParam(required = false) String sortBy) {
        try {
            String token = authHeader.substring(7); // cắt bỏ "Bearer "
            PageResponse<List<TransactionResponse>> transaction = transactionService.getAllTransaction(token,pageNo,pageSize,sortBy);
            return ResponseEntity
                    .ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get Transaction List Successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> getAllTransaction(
                                               @Min(value = 1) @PathVariable Long id,
                                               @RequestBody TransactionDTO transactionDTO
                                               ) {
        try {

            TransactionResponse transactionResponse = transactionService.updateTransaction(id,transactionDTO);
            return ResponseEntity
                    .ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update Successfully", transactionResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

}
