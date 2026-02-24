package aliceGlow.example.aliceGlow.exception;

import java.time.LocalDate;

public class CashBoxAlreadyExistsForDateException extends IllegalStateException {

    public CashBoxAlreadyExistsForDateException(LocalDate businessDate) {
        super("CashBox already exists for businessDate=" + businessDate);
    }
}
