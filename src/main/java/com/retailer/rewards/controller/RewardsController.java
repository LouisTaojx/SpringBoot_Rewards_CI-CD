package com.retailer.rewards.controller;

import com.retailer.rewards.dto.RewardReportDto;
import com.retailer.rewards.service.RewardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardsController {
    private final RewardsService rewardsService;

    @GetMapping("/version")
    public String v() {
        return "v4";
    }

    @GetMapping("/{customerId}")
    public RewardReportDto getRewards(
            @PathVariable Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
            ){
        return rewardsService.getRewardsReport(customerId, start, end);
    }

}
