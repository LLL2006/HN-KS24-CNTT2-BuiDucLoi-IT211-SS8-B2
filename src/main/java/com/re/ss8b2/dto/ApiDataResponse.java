package com.re.ss8b2.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponse<T> {

    private Boolean success;

    private String message;

    private T data;

    private HttpStatus status;
}
