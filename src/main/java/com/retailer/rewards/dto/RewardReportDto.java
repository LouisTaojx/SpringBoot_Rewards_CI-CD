package com.retailer.rewards.dto;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardReportDto {
    private Long customerId;
    private String customerName;
    private String startDate;
    private String endDate;

    private List<MonthlyRewardDto> monthly;
    private long totalPoints;
}
