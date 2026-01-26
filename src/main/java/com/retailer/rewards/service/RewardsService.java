package com.retailer.rewards.service;

import com.retailer.rewards.dto.RewardReportDto;

import java.time.LocalDate;

public interface RewardsService {
    RewardReportDto getRewardsReport(Long customerId, LocalDate startDate, LocalDate endDate);
}
