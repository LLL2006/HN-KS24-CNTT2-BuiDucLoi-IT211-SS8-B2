package com.re.ss8b2.repository;


import com.re.ss8b2.entity.Flight;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Flight findByFlightNumber(String flightNumber);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Flight f
            SET f.availableSeats = f.availableSeats - 1
            WHERE f.flightNumber = :flightNumber
            AND f.availableSeats > 0
            """)
    int decreaseSeat(@Param("flightNumber") String flightNumber);
}
