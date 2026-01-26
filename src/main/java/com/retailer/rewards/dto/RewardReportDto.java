package com.retailer.rewards.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RewardReportDto {
    private Long customerId;
    private String customerName;
    private String startDate;
    private String endDate;

    private List<MonthlyRewardDto> monthly;
    private long totalPoints;
}
