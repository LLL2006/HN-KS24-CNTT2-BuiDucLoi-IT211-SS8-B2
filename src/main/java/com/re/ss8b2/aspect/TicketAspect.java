package com.re.ss8b2.aspect;


import com.re.ss8b2.entity.ErrorLog;
import com.re.ss8b2.repository.ErrorLogRepository;
import com.re.ss8b2.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class TicketAspect {

    private final TicketRepository ticketRepository;
    private final ErrorLogRepository errorLogRepository;

    @Around("""
            execution(* com.re.ss8b2.service.TicketService.bookTicket(..))
            """)
    public Object sanitizePassengerName(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        String passengerName = (String) args[1];

        passengerName = passengerName.trim().toUpperCase();

        args[1] = passengerName;

        return joinPoint.proceed(args);
    }

    @Before("""
            execution(* com.re.ss8b2.service.TicketService.cancelTicket(..))
            && args(ticketId)
            """)
    public void checkCancelTime(Long ticketId) {

        LocalDateTime limitTime = LocalDateTime.now().plusHours(24);

        Boolean isLessThan24Hours =
                ticketRepository.isFlightLessThan24Hours(ticketId, limitTime);

        if (isLessThan24Hours) {
            throw new RuntimeException("Không được hủy vé trong vòng 24 giờ trước giờ bay");
        }
    }

    @AfterThrowing(
            pointcut = "execution(* com.re.ss8b2.service.TicketService.*(..))",
            throwing = "ex"
    )
    public void logError(JoinPoint joinPoint, Exception ex) {

        ErrorLog errorLog = ErrorLog.builder()
                .timestamp(LocalDateTime.now())
                .methodName(joinPoint.getSignature().getName())
                .exceptionMessage(ex.getMessage())
                .build();

        errorLogRepository.save(errorLog);
    }
}
