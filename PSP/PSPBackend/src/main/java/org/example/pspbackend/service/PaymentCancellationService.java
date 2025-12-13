package org.example.pspbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.model.Payment;
import org.example.pspbackend.model.enums.PaymentStatus;
import org.example.pspbackend.repository.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled service that cancels payments that have been in CREATED or INITIATED status
 * for more than 5 minutes.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentCancellationService {

    private final PaymentRepository paymentRepository;
    private static final int CANCELLATION_THRESHOLD_MINUTES = 5;

    /**
     * Runs every 5 minutes to cancel stale CREATED and INITIATED payments.
     * Cron expression: at second 0, every 5 minutes
     */
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void cancelStalePayments() {
        log.debug("Starting scheduled payment cancellation check");
        
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(CANCELLATION_THRESHOLD_MINUTES);
        
        // Find stale CREATED payments
        List<Payment> staleCreatedPayments = paymentRepository.findByPaymentStatusAndCreatedAtBefore(
                PaymentStatus.CREATED,
                thresholdTime
        );
        
        // Find stale INITIATED payments
        List<Payment> staleInitiatedPayments = paymentRepository.findByPaymentStatusAndCreatedAtBefore(
                PaymentStatus.INITIATED,
                thresholdTime
        );
        
        List<Payment> stalePayments = new java.util.ArrayList<>();
        stalePayments.addAll(staleCreatedPayments);
        stalePayments.addAll(staleInitiatedPayments);
        
        if (stalePayments.isEmpty()) {
            log.debug("No stale payments found to cancel");
            return;
        }
        
        log.info("Found {} stale payment(s) to cancel", stalePayments.size());
        
        // Update status to CANCELLED for all stale payments
        int cancelledCount = 0;
        for (Payment payment : stalePayments) {
            payment.setPaymentStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(payment);
            cancelledCount++;
            log.info("Cancelled payment with ID: {} (status: {}, created at: {})", 
                    payment.getId(), payment.getPaymentStatus(), payment.getCreatedAt());
        }
        
        log.info("Successfully cancelled {} payment(s)", cancelledCount);
    }
}

