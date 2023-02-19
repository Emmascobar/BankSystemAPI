package com.ironhack.model.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class Admin extends User{
    public Admin() {
    }

    public Admin(String name, String username, String password) {
        super(name, username, password);
    }
}
