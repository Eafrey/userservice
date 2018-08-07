package com.thoughtworks.traing.chensen.todoservice.security;

import com.google.common.collect.ImmutableList;
import com.google.common.net.HttpHeaders;
import com.thoughtworks.traing.chensen.todoservice.model.User;
import com.thoughtworks.traing.chensen.todoservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class ToDoAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    private static final byte[] SECRET_KEY = "kitty".getBytes(Charset.defaultCharset());

    public static String generateToken(int id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);

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
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.isEmpty(token)) {
            int id = getIdFromToken(token);


            Optional<User> user = userService.findById(id);

            if (id != -1 && user.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(user.get().getUserName(), null,
                                ImmutableList.of(new SimpleGrantedAuthority("admin"),
                                        new SimpleGrantedAuthority("role")))
                );
            }
        }

//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken("user", null,
//                        ImmutableList.of(new SimpleGrantedAuthority("admin"),
//                                new SimpleGrantedAuthority("role")))
//        );

        filterChain.doFilter(request, response);
    }

    public static int getIdFromToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            int id = (int) body.get("id");
            return id;

        } catch (Exception e) {
            return -1;
        }


    }

}
