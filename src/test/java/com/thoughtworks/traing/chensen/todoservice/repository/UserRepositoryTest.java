package com.thoughtworks.traing.chensen.todoservice.repository;

import com.thoughtworks.traing.chensen.todoservice.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shoulReturnUserWithName() {
        userRepository.save(new User(0, "user-5", "123456"));

        Optional<User> user = userRepository.findByUserName("user-5");
        assertTrue(user.isPresent());
        assertThat(user.get().getUserName(), is("user-5"));
    }
}