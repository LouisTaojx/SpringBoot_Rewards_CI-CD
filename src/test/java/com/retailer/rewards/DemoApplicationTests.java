package com.retailer.rewards;

import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.PurchaseTransaction;
import com.retailer.rewards.repository.CustomerRepo;
import com.retailer.rewards.repository.TransactionsRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.LocalDate;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private TransactionsRepo transactionsRepo;

    @Test
    void getRewards_endToEnd_works() throws Exception {
        Customer c = new Customer();
        c.setName("Alice");
        c = customerRepo.save(c);

        PurchaseTransaction t1 = new PurchaseTransaction();
        t1.setCustomer(c);
        t1.setAmount(new BigDecimal("120.00"));
        t1.setTxnDate(LocalDate.of(2025, 1, 5));
        transactionsRepo.save(t1);

        mockMvc.perform(get("/api/rewards/" + c.getId())
                        .param("start", "2025-01-01")
                        .param("end", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.totalPoints").value(90));
    }
}

