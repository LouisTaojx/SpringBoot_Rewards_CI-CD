package com.retailer.rewards.controller;

import com.retailer.rewards.dto.MonthlyRewardDto;
import com.retailer.rewards.dto.RewardReportDto;
import com.retailer.rewards.service.RewardsService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.*;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardsController.class)
class RewardsControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardsService rewardsService;

    @Test
    void versionEndpoint_returnsV4() throws Exception {
        mockMvc.perform(get("/api/rewards/version"))
                .andExpect(status().isOk())
                .andExpect(content().string("v4"));
    }

    @Test
    void getRewards_returnsJson() throws Exception {
        RewardReportDto dto = new RewardReportDto();
        dto.setCustomerId(1L);
        dto.setCustomerName("Alice");
        dto.setStartDate("2025-01-01");
        dto.setEndDate("2025-01-31");
        dto.setTotalPoints(110L);
        dto.setMonthly(
                List.of(new MonthlyRewardDto("2025-01", 110L))
        );

        when(rewardsService.getRewardsReport(eq(1L), any(), any()))
                .thenReturn(dto);

        mockMvc.perform(get("/api/rewards/1")
                        .param("start", "2025-01-01")
                        .param("end", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.totalPoints").value(110))
                .andExpect(jsonPath("$.monthly[0].month").value("2025-01"))
                .andExpect(jsonPath("$.monthly[0].points").value(110));
    }
}

