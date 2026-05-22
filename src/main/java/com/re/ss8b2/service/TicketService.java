package com.re.ss8b2.service;

import com.re.ss8b2.entity.Flight;
import com.re.ss8b2.entity.Ticket;
import com.re.ss8b2.repository.FlightRepository;
import com.re.ss8b2.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;

    public Ticket bookTicket(String flightNumber, String passengerName) {

        Flight flight = flightRepository.findByFlightNumber(flightNumber);

        if (flight == null) {
            throw new RuntimeException("Không tìm thấy chuyến bay");
        }

        int result = flightRepository.decreaseSeat(flightNumber);

        if (result == 0) {
            throw new RuntimeException("Chuyến bay đã hết vé");
        }

        Ticket ticket = Ticket.builder()
                .passengerName(passengerName)
                .flightId(flight.getId())
                .status("BOOKED")
                .build();

        return ticketRepository.save(ticket);
    }

    public void cancelTicket(Long ticketId) {

        if (!ticketRepository.existsById(ticketId)) {
            throw new RuntimeException("Không tìm thấy vé");
        }

        ticketRepository.cancelTicket(ticketId);
    }
}
