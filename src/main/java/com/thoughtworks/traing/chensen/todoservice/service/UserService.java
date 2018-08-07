package com.thoughtworks.traing.chensen.todoservice.service;

import com.thoughtworks.traing.chensen.todoservice.model.User;
import com.thoughtworks.traing.chensen.todoservice.repository.UserRepository;
import com.thoughtworks.traing.chensen.todoservice.security.ToDoAuthFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;
//    public static int curLogedId;

    public User find(Integer id) {
        return Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(null);
    }

    public String add(User user) {
        Optional<User> user1 = userRepository.findByUserName(user.getUserName());
        if (user1.isPresent()) {
//            return new ResponseEntity(HttpStatus.CONFLICT);
            return "user is already exist";
        }
        String password = user.getPassword();
        String encodePassword = encoder.encode(password);
        user.setPassword(encodePassword);
        userRepository.save(user);
//        return ResponseEntity.ok("register success");
        return "register success";
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean verfiy(String userName, String password) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
//            return user.map(User::getPassword).filter(p -> p.equals(password)).isPresent();
            return user.map(User::getPassword)
                    .filter(p -> encoder.matches(password, p) || p.equals(password))
                    .isPresent();

        } else {
            return false;
        }
    }

    public String login(User user) {

        String userName = user.getUserName();
        String password = user.getPassword();
        if (!verfiy(userName, password)) {
//            return new ResponseEntity(HttpStatus.BAD_GATEWAY);
            return "log in failed";
        }
        Optional<User> userInDB = userRepository.findByUserName(userName);
        int id = userInDB.get().getId();
        String token = ToDoAuthFilter.generateToken(id);

//        return ResponseEntity.ok(token);
        return token;
    }

    public Optional<User> findById(int id) {
        return userRepository.findUserById(id);
    }

    public ResponseEntity verifiyToken(String token) {
        int id = ToDoAuthFilter.getIdFromToken(token);
        Optional<User> user = userRepository.findUserById(id);
        if(id != -1 && user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized...");
        }
    }
}
