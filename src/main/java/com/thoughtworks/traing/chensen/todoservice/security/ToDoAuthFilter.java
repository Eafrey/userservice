package com.thoughtworks.traing.chensen.todoservice.security;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.google.common.collect.ImmutableList;
import com.google.common.net.HttpHeaders;
import com.thoughtworks.traing.chensen.todoservice.model.User;
import com.thoughtworks.traing.chensen.todoservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ToDoAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    private static final byte[] SECRET_KEY = "kitty".getBytes(Charset.defaultCharset());

    public static String generateToken(int id, String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("name", userName);

        String token = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return token;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("incoming request {}", request.getServletPath());

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.isEmpty(token)) {

//            Optional<User> user = userService.verifiyInternalToken(token);
            String[] tokens = token.split(":");
            int id = Integer.parseInt(tokens[0]);

//            if (user.isPresent()) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(id, null,
                            ImmutableList.of(new SimpleGrantedAuthority("admin"),
                                    new SimpleGrantedAuthority("role")))
            );
//            }
        }

        filterChain.doFilter(request, response);
    }


}
