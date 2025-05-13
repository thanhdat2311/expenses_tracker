package vn.dathocjava.dathocjava_sample.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    @NotBlank(message = "Category name cannot null")
    private String name;

    private String color;

    @NotNull(message = "Type can not null")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Loại chỉ được là INCOME hoặc EXPENSE")
    private String type;
}

