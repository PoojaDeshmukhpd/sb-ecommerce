package com.ecommerce.sb_ecomm.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@NamedEntityGraph
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING) // by default enum type will be stored as int to convert into string use this
    @Column(length = 20, name = "role_name")
    private UsersRole roleName;

    public Role(UsersRole roleName) {
        this.roleName = roleName;
    }
}
