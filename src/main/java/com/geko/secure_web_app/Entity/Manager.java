package com.geko.secure_web_app.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "managers")
@Data
@AllArgsConstructor
public class Manager extends User {

    private String managerCode;

    public Manager() {
        this.setRole(Role.MANAGER);
    }
}
