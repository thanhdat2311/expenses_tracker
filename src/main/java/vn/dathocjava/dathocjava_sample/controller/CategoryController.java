package vn.dathocjava.dathocjava_sample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.dathocjava.dathocjava_sample.dto.request.CategoryDTO;
import vn.dathocjava.dathocjava_sample.dto.response.PageResponse;
import vn.dathocjava.dathocjava_sample.dto.response.TransactionResponse;
import vn.dathocjava.dathocjava_sample.model.Category;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.service.implement.CategoryService;

import java.util.List;

@RestController
@RequestMapping("expenses-tracker/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<?> getAllTransaction() {
        try {
            List<Category> categoryList = categoryService.getAllCategory();
            return ResponseEntity
                    .ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get Category List Successfully", categoryList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @PostMapping("/createCategory")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), errorMessages.toString()));
            }
            Category category = categoryService.createCategory(categoryDTO);
            return ResponseEntity
                    .ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Created Successfully", category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
}
