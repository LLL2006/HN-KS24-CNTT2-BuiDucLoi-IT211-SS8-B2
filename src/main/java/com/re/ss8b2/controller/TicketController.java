package com.re.ss8b2.controller;


import com.re.ss8b2.dto.ApiDataResponse;
import com.re.ss8b2.dto.BookTicketRequest;
import com.re.ss8b2.entity.Ticket;
import com.re.ss8b2.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/book")
    public ResponseEntity<ApiDataResponse<Ticket>> bookTicket(
            @Valid @RequestBody BookTicketRequest request
    ) {

        Ticket ticket = ticketService.bookTicket(
                request.getFlightNumber(),
                request.getPassengerName()
        );

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Đặt vé thành công",
                        ticket,
                        HttpStatus.OK
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/cancel/{ticketId}")
    public ResponseEntity<ApiDataResponse<?>> cancelTicket(
            @PathVariable Long ticketId
    ) {

        ticketService.cancelTicket(ticketId);

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Hủy vé thành công",
                        null,
                        HttpStatus.OK
                ),
                HttpStatus.OK
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiDataResponse<?>> handleValidation(
            MethodArgumentNotValidException e
    ) {

        String message = e.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        false,
                        message,
                        null,
                        HttpStatus.BAD_REQUEST
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiDataResponse<?>> handleException(Exception e) {

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        false,
                        e.getMessage(),
                        null,
                        HttpStatus.BAD_REQUEST
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
