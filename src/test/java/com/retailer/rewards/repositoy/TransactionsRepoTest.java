package com.retailer.rewards.repositoy;

import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.PurchaseTransaction;

import com.retailer.rewards.repository.CustomerRepo;
import com.retailer.rewards.repository.TransactionsRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback
class TransactionsRepoTest {

    @Autowired
    private TransactionsRepo transactionsRepo;

    @Autowired
    private CustomerRepo customerRepo;

//    @BeforeEach
//    void cleanDb() {
//        transactionsRepo.deleteAll();
//        customerRepo.deleteAll();
//    }

    @Test
    void findByCustomerAndDateRange_returnsOnlyMatchingRows() {
        Customer c = new Customer();
        c.setName("Alice");
        c = customerRepo.save(c);

        PurchaseTransaction jan = new PurchaseTransaction();
        jan.setCustomer(c);
        jan.setAmount(new BigDecimal("70.00"));
        jan.setTxnDate(LocalDate.of(2025, 1, 5));
        transactionsRepo.save(jan);

        PurchaseTransaction feb = new PurchaseTransaction();
        feb.setCustomer(c);
        feb.setAmount(new BigDecimal("120.00"));
        feb.setTxnDate(LocalDate.of(2025, 2, 10));
        transactionsRepo.save(feb);

        List<PurchaseTransaction> result =
                transactionsRepo.findByCustomer_IdAndTxnDateBetween(
                        c.getId(),
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 1, 31)
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualTo(new BigDecimal("70.00"));
    }
}
