package com.finance.tracker.arfath.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;
}