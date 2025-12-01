package com.skaet.ussdapp.ussdmodule.service.interfaces;

import com.skaet.ussdapp.ussdmodule.dtos.request.UssdRequest;
import com.skaet.ussdapp.ussdmodule.dtos.response.UssdResponse;

public interface UssdMenuService {
    UssdResponse processUssdRequest(UssdRequest request);
}
