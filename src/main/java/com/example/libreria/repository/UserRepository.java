package com.example.libreria.repository;

import com.example.libreria.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findById(int id);

    @Query("select u from User u where credentials.username= :username")
    User findByUsername(String username);

    @Query("select u from User u where credentials.username= :username and credentials.password = :password")
    User login(String username, String password);


}
