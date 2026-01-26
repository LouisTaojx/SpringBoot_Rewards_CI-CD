package com.retailer.rewards.service;

import com.retailer.rewards.dto.MonthlyRewardDto;
import com.retailer.rewards.dto.RewardReportDto;
import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.PurchaseTransaction;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.repository.CustomerRepo;
import com.retailer.rewards.repository.TransactionsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardsServiceImpl implements RewardsService{
    private final CustomerRepo customerRepo;
    private final TransactionsRepo transactionsRepo;

    @Override
    public RewardReportDto getRewardsReport(Long customerId, LocalDate startDate, LocalDate endDate) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        List<PurchaseTransaction> txns = transactionsRepo.findByCustomer_IdAndTxnDateBetween(customerId, startDate, endDate);

        Map<YearMonth, Long> pointsByMonth = new HashMap<>();
        long total = 0;

        for (PurchaseTransaction txn: txns){
            long points = calculatePoints(txn.getAmount());
            total +=  points;

            YearMonth ym = YearMonth.from(txn.getTxnDate());
            pointsByMonth.merge(ym, points, Long::sum);
        }

        List<MonthlyRewardDto> monthly = pointsByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new MonthlyRewardDto(e.getKey().toString(), e.getValue()))
                .toList();

        return new RewardReportDto(
                customer.getId(),
                customer.getName(),
                startDate.toString(),
                endDate.toString(),
                monthly,
                total
        );
    }

    static long calculatePoints(BigDecimal amount){
        if(amount ==null) return 0;

        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal fifty = BigDecimal.valueOf(50);
        BigDecimal hundred = BigDecimal.valueOf(100);

        BigDecimal over100 = amount.subtract(hundred).max(zero);
        BigDecimal over50To100 = amount.min(hundred).subtract(fifty).max(zero);

        BigDecimal points = over100.multiply(BigDecimal.valueOf(2)).add(over50To100);

        return points.longValue();
    }
}
