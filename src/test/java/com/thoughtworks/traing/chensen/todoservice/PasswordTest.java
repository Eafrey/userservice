package com.thoughtworks.traing.chensen.todoservice;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PasswordTest {
    @Test
    public void shouldEncryptPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("password"));
        System.out.println(encoder.encode("password"));
        System.out.println(encoder.encode("password"));
        System.out.println(encoder.encode("password"));
        assertTrue(encoder.matches("password","$2a$10$x9R2JvzNs7g0FeP66QYxdOTDt31KIUrk.WYFlyYUv7JQj3QiZmzeW"));
        assertFalse(encoder.matches("password","$2a$10$x9R2JvzNs7g0FeP66QYxdOTDt31KIUrk.WYFlyYUv7JQj3QiZmW"));
    }
}
