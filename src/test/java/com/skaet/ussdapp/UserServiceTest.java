package com.skaet.ussdapp;

import com.skaet.ussdapp.usermodule.dtos.request.CreateUserAccountRequest;
import com.skaet.ussdapp.usermodule.dtos.response.CreateUserAccountResponse;
import com.skaet.ussdapp.usermodule.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Test
    public void testThatUsersCanCreateAccounts(){
        CreateUserAccountRequest signupRequest = new CreateUserAccountRequest();
        signupRequest.setPhoneNumber("08147995494");
        signupRequest.setPinHashed("1234");
        CreateUserAccountResponse createUserAccountResponse = userService.createAccount(signupRequest);
        assertThat(createUserAccountResponse).isNotNull();
    }
}
