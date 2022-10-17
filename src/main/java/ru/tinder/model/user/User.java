package ru.tinder.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String username;

    private String password;

    private UserInfo userInfo;

    private Role role;

    @Override
    public String toString() {
        return "User to string method. Rewrite";
    }

}
