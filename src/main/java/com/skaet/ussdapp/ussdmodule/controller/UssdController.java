package com.skaet.ussdapp.ussdmodule.controller;

import com.skaet.ussdapp.common.dto.ApiResponse;
import com.skaet.ussdapp.ussdmodule.dtos.request.UssdRequest;
import com.skaet.ussdapp.ussdmodule.dtos.response.UssdResponse;
import com.skaet.ussdapp.ussdmodule.service.interfaces.UssdMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UssdController {

    private final UssdMenuService ussdMenuService;

    public UssdController(UssdMenuService ussdMenuService) {
        this.ussdMenuService = ussdMenuService;
    }

    @PostMapping(value = "/ussd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> handleUssdRequest(UssdRequest request) {
        UssdResponse response = ussdMenuService.processUssdRequest(request);
        String prefix = response.isContinueSession() ? "CON " : "END ";
        return ResponseEntity.ok(prefix + response.getMessage());
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .data("USSD Service is running")
                .isSuccessful(true)
                .message("Service is healthy")
                .status(HttpStatus.OK.value())
                .build();
        
        return ResponseEntity.ok(response);
    }
}
