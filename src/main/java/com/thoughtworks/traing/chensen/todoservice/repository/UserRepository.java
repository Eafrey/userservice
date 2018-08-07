package com.thoughtworks.traing.chensen.todoservice.repository;

import com.thoughtworks.traing.chensen.todoservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByUserName(String userNmae);

    public Optional<User> findUserById(int id);
}
