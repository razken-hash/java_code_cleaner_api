package com.cleaner.java.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CleanedJavaCode {
    private String originalCode;
    private String cleanedCode;
    private Double percentage;
}
