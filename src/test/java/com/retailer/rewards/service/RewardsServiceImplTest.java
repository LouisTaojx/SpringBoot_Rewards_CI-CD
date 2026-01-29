package com.retailer.rewards.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.retailer.rewards.dto.RewardReportDto;
import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.PurchaseTransaction;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.repository.CustomerRepo;
import com.retailer.rewards.repository.TransactionsRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RewardsServiceImplTest {

    @Mock CustomerRepo customerRepo;
    @Mock TransactionsRepo transactionsRepo;

    @InjectMocks RewardsServiceImpl rewardsService;

    @Test
    void getRewardsReport_throwsWhenCustomerMissing() {
        when(customerRepo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                rewardsService.getRewardsReport(
                        999L,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 1, 31)
                )
        ).isInstanceOf(CustomerNotFoundException.class);

        verify(customerRepo).findById(999L);
        verifyNoInteractions(transactionsRepo);
    }

    @Test
    void getRewardsReport_aggregatesPointsByMonth_andTotal() {
        long customerId = 1L;
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 2, 28);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Alice");

        PurchaseTransaction tJan = new PurchaseTransaction();
        tJan.setAmount(new BigDecimal("120.00")); // 90 points
        tJan.setTxnDate(LocalDate.of(2025, 1, 5));

        PurchaseTransaction tFeb = new PurchaseTransaction();
        tFeb.setAmount(new BigDecimal("70.00")); // 20 points
        tFeb.setTxnDate(LocalDate.of(2025, 2, 10));

        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionsRepo.findByCustomer_IdAndTxnDateBetween(customerId, start, end))
                .thenReturn(List.of(tJan, tFeb));

        RewardReportDto dto = rewardsService.getRewardsReport(customerId, start, end);

        assertThat(dto).isNotNull();

        // Adjust these getters/record components to match your actual DTO
        assertThat(dto.getCustomerId()).isEqualTo(customerId);
        assertThat(dto.getCustomerName()).isEqualTo("Alice");
        assertThat(dto.getStartDate()).isEqualTo("2025-01-01");
        assertThat(dto.getEndDate()).isEqualTo("2025-02-28");

        assertThat(dto.getTotalPoints()).isEqualTo(110L);

        assertThat(dto.getMonthly()).hasSize(2);
        assertThat(dto.getMonthly().get(0).getMonth()).isEqualTo("2025-01");
        assertThat(dto.getMonthly().get(0).getPoints()).isEqualTo(90L);
        assertThat(dto.getMonthly().get(1).getMonth()).isEqualTo("2025-02");
        assertThat(dto.getMonthly().get(1).getPoints()).isEqualTo(20L);

        verify(customerRepo).findById(customerId);
        verify(transactionsRepo).findByCustomer_IdAndTxnDateBetween(customerId, start, end);
        verifyNoMoreInteractions(customerRepo, transactionsRepo);
    }
}
