package vn.dathocjava.dathocjava_sample.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="roles")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role extends BaseModel{
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="description",nullable = true)
    private String description;
    public static String ADMIN = "ADMIN";
    public static String SYSTEM_ADMIN = "SYSTEM_ADMIN";
    public static String USER = "USER";
}
