package com.thoughtworks.traing.chensen.todoservice.controller;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ToDoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoRepository toDoRepository;

    @Test
    public void shoulReturnUnauthenticate() throws Exception {

        mockMvc.perform(get("/todos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shoulReturnTodosWhenHadAuthenticate() throws Exception {
        when(toDoRepository.findTodoInfosByCreateByIs(1))
                .thenReturn(ImmutableList.of(new Todo("user-1", true, false, null, true, false, new Date(), 1),
                        new Todo("user-2", true, false, null, true, false, new Date(), 1)));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user", null,
                        ImmutableList.of(new SimpleGrantedAuthority("admin"),
                                new SimpleGrantedAuthority("role")))
        );



        mockMvc.perform(get("/todos")
//                .with(authenticate(new UsernamePasswordAuthenticationToken(
//                        userFixture, null, Collections.emptyList()
//                )))
                 )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(111))
                .andExpect(jsonPath("$[0].content").value("user-1"));
    }
}