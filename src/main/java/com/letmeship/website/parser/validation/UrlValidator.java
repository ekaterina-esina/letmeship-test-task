package com.letmeship.website.parser.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class UrlValidator implements ConstraintValidator<Url, String> {
    @Override
    public void initialize(Url constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            URL url = new URL(s);
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
