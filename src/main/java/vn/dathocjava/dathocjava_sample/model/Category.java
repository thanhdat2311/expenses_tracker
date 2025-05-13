package vn.dathocjava.dathocjava_sample.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseModel{


    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(length = 10, nullable = false)
    private String type; // "INCOME" hoặc "EXPENSE"
}
