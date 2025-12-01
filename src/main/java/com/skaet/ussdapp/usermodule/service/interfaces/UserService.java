package com.skaet.ussdapp.usermodule.service.interfaces;
import com.skaet.ussdapp.usermodule.dtos.request.CreateUserAccountRequest;
import com.skaet.ussdapp.usermodule.dtos.response.CreateUserAccountResponse;
public interface UserService {
    CreateUserAccountResponse createAccount(CreateUserAccountRequest signupRequest);

}
