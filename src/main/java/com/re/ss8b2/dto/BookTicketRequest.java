package com.re.ss8b2.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookTicketRequest {

    @NotBlank(message = "Flight number không được để trống")
    private String flightNumber;

    @NotBlank(message = "Tên hành khách không được để trống")
    private String passengerName;
}