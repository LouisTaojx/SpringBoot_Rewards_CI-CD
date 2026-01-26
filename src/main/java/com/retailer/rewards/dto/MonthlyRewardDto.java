package com.retailer.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRewardDto {
    private String month;
    private long points;
}
