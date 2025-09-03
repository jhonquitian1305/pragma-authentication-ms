package co.com.pragma.r2dbc.entity;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Roles")
@Getter
public class RolEntity {
    @Id
    private Long id;
    private String name;
}
