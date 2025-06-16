package com.finance.tracker.arfath.controller.auth;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    
    private static final Pattern HAS_UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern HAS_SPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    private static final int MIN_LENGTH = 8;
    
    public boolean isValid(String password) {
        return password != null &&
               password.length() >= MIN_LENGTH &&
               HAS_UPPERCASE.matcher(password).find() &&
               HAS_LOWERCASE.matcher(password).find() &&
               HAS_SPECIAL.matcher(password).find();
    }
    
    public List<String> getValidationErrors(String password) {
        List<String> errors = new ArrayList<>();
        
        if (password == null || password.length() < MIN_LENGTH) {
            errors.add("Password must be at least " + MIN_LENGTH + " characters long");
        }
        
        if (password != null && !HAS_UPPERCASE.matcher(password).find()) {
            errors.add("Password must contain at least one uppercase letter");
        }
        
        if (password != null && !HAS_LOWERCASE.matcher(password).find()) {
            errors.add("Password must contain at least one lowercase letter");
        }
        
        if (password != null && !HAS_SPECIAL.matcher(password).find()) {
            errors.add("Password must contain at least one special character");
        }
        
        return errors;
    }
}