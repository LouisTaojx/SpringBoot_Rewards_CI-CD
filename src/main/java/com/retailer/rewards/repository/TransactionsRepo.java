package com.retailer.rewards.repository;

import com.retailer.rewards.entity.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionsRepo extends JpaRepository<PurchaseTransaction, Long> {
    List<PurchaseTransaction> findByCustomer_IdAndTxnDateBetween(Long customerId, LocalDate start, LocalDate end);
}
