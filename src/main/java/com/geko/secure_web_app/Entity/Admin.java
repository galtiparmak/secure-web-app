package com.geko.secure_web_app.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admins")
@Data
@AllArgsConstructor
public class Admin extends User {

    private String adminCode;

    public Admin() {
        this.setRole(Role.ADMIN);
    }
}

