package com.thoughtworks.traing.chensen.todoservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class JwtTest {

    @Test
    public void generateJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "user-3");
        claims.put("password", "123456");

        byte[] secretKey = "kitty".getBytes(Charset.defaultCharset());

        //Generate
        String token = Jwts.builder()
                .addClaims(claims)
//                .setExpiration(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        System.out.println(token);


        // Parse & Verification
        Claims body = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        assertThat(body.get("name"), is("user-3"));
        assertThat(body.get("password"), is("123456"));

        System.out.println(body);
    }
}
