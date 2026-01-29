package com.retailer.rewards.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class RewardsServiceImplPointsTest {

    @Test
    void calculatePoints_nullAmount_returns0() {
        assertThat(RewardsServiceImpl.calculatePoints(null)).isEqualTo(0L);
    }

    @Test
    void calculatePoints_under50_returns0() {
        assertThat(RewardsServiceImpl.calculatePoints(new BigDecimal("49.99"))).isEqualTo(0L);
        assertThat(RewardsServiceImpl.calculatePoints(new BigDecimal("0"))).isEqualTo(0L);
    }

    @Test
    void calculatePoints_between50And100_returnsAmountMinus50() {
        // 50 -> 0 points (since over50To100 = min(50,100)-50 = 0)
        assertThat(RewardsServiceImpl.calculatePoints(new BigDecimal("50.00"))).isEqualTo(0L);

        // 70 -> 20 points
        assertThat(RewardsServiceImpl.calculatePoints(new BigDecimal("70.00"))).isEqualTo(20L);

        // 100 -> 50 points
        assertThat(RewardsServiceImpl.calculatePoints(new BigDecimal("100.00"))).isEqualTo(50L);
    }

    @Test
    void calculatePoints_over100_returns50Plus2xOver100() {
        // 120 -> 50 + 2*20 = 90
        assertThat(RewardsServiceImpl.calculatePoints(new BigDecimal("120.00"))).isEqualTo(90L);

        // 101 -> 50 + 2*1 = 52
        assertThat(RewardsServiceImpl.calculatePoints(new BigDecimal("101.00"))).isEqualTo(52L);
    }
}
