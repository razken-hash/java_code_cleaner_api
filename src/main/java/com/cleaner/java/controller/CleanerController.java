package com.cleaner.java.controller;

import com.cleaner.java.models.CleanedJavaCode;
import com.cleaner.java.models.JavaCode;
import com.cleaner.java.service.JavaCodeCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CleanerController {

    private final JavaCodeCleaner javaCodeCleaner;

    @Autowired
    public CleanerController(JavaCodeCleaner javaCodeCleaner) {
        this.javaCodeCleaner = javaCodeCleaner;
    }

    @PostMapping("/clean")
    public CleanedJavaCode cleanedJavaCode(@RequestBody JavaCode javaCode) {
        String cleanedCode = javaCodeCleaner.cleanCode(javaCode);
        Double percentage = (double)javaCode.getCode().length() / cleanedCode.length();
        return new CleanedJavaCode(
                javaCode.getCode(),
                cleanedCode,
                percentage
        );
    }
}
