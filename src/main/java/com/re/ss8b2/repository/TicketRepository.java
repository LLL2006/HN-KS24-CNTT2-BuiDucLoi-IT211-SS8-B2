package com.re.ss8b2.repository;

import com.re.ss8b2.entity.Ticket;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Modifying
    @Transactional
    @Query("""
            UPDATE Ticket t
            SET t.status = 'CANCELED'
            WHERE t.id = :ticketId
            """)
    int cancelTicket(@Param("ticketId") Long ticketId);

    @Query("""
            SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
            FROM Ticket t, Flight f
            WHERE t.flightId = f.id
            AND t.id = :ticketId
            AND f.departureTime < :limitTime
            """)
    Boolean isFlightLessThan24Hours(@Param("ticketId") Long ticketId,
                                    @Param("limitTime") LocalDateTime limitTime);
}
