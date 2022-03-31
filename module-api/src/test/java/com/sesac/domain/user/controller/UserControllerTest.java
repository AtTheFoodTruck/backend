package com.sesac.domain.user.controller;

import com.sesac.domain.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                //.apply() -> security
                .build();
    }

    @Test
    public void 회원가입() {

        //given
        String email = "jaemin2@";
        String username = "";
        String password;
        String bNo;
        String phoneNum;

    }


}