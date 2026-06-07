package com.realestate.service;

import com.realestate.dto.PerformanceStatsDTO;
import com.realestate.entity.*;
import com.realestate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ViewingRecordRepository viewingRecordRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MonthlyTargetRepository monthlyTargetRepository;

    @Autowired
    private PerformanceAssessmentRepository assessmentRepository;

    public List<PerformanceStatsDTO> calculateMonthlyPerformance(String month) {
        YearMonth ym = YearMonth.parse(month);
        LocalDateTime startTime = ym.atDay(1).atStartOfDay();
        LocalDateTime endTime = ym.atEndOfMonth().atTime(23, 59, 59);

        List<Agent> agents = agentRepository.findByStatus("在职");
        List<PerformanceStatsDTO> result = new ArrayList<>();

        for (Agent agent : agents) {
            PerformanceStatsDTO dto = new PerformanceStatsDTO();
            dto.setAgentId(agent.getId());
            dto.setAgentName(agent.getName());
            dto.setMonth(month);

            int viewingCount = countViewingRecords(agent.getName(), startTime, endTime);
            dto.setViewingCount(viewingCount);

            List<Subscription> deals = findDeals(agent.getName(), startTime, endTime);
            int dealCount = deals.size();
            dto.setDealCount(dealCount);

            BigDecimal dealAmount = deals.stream()
                    .map(Subscription::getTotalPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setDealAmount(dealAmount);

            Set<Long> viewingCustomers = getViewingCustomerIds(agent.getName(), startTime, endTime);
            int totalCustomers = viewingCustomers.size();
            dto.setTotalCustomers(totalCustomers);

            Set<Long> dealCustomerIds = deals.stream()
                    .map(Subscription::getCustomerId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            int dealCustomers = dealCustomerIds.size();
            dto.setDealCustomers(dealCustomers);

            BigDecimal conversionRate = totalCustomers > 0
                    ? BigDecimal.valueOf(dealCustomers * 100.0 / totalCustomers).setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            dto.setConversionRate(conversionRate);

            Optional<MonthlyTarget> targetOpt = monthlyTargetRepository
                    .findByAgentIdAndMonth(agent.getId(), month);
            if (targetOpt.isPresent()) {
                MonthlyTarget target = targetOpt.get();
                dto.setViewingTarget(target.getViewingTarget());
                dto.setDealTarget(target.getDealTarget());
                dto.setDealAmountTarget(target.getDealAmountTarget());
                dto.setConversionRateTarget(target.getConversionRateTarget());

                dto.setViewingCompletionRate(calculateRate(viewingCount, target.getViewingTarget()));
                dto.setDealCompletionRate(calculateRate(dealCount, target.getDealTarget()));
                dto.setDealAmountCompletionRate(calculateRate(dealAmount, target.getDealAmountTarget()));
                dto.setConversionCompletionRate(calculateRate(conversionRate, target.getConversionRateTarget()));
            } else {
                dto.setViewingTarget(0);
                dto.setDealTarget(0);
                dto.setDealAmountTarget(BigDecimal.ZERO);
                dto.setConversionRateTarget(BigDecimal.ZERO);
                dto.setViewingCompletionRate(BigDecimal.ZERO);
                dto.setDealCompletionRate(BigDecimal.ZERO);
                dto.setDealAmountCompletionRate(BigDecimal.ZERO);
                dto.setConversionCompletionRate(BigDecimal.ZERO);
            }

            Optional<PerformanceAssessment> assessmentOpt = assessmentRepository
                    .findByAgentIdAndMonth(agent.getId(), month);
            if (assessmentOpt.isPresent()) {
                PerformanceAssessment assessment = assessmentOpt.get();
                dto.setScore(assessment.getScore());
                dto.setGrade(assessment.getGrade());
                dto.setEvaluationRemark(assessment.getEvaluationRemark());
            }

            result.add(dto);

            savePerformanceAssessment(agent, month, viewingCount, dealCount, dealAmount,
                    totalCustomers, dealCustomers, conversionRate);
        }

        result.sort((a, b) -> {
            BigDecimal amountA = a.getDealAmount() != null ? a.getDealAmount() : BigDecimal.ZERO;
            BigDecimal amountB = b.getDealAmount() != null ? b.getDealAmount() : BigDecimal.ZERO;
            return amountB.compareTo(amountA);
        });

        for (int i = 0; i < result.size(); i++) {
            result.get(i).setRank(i + 1);
        }

        return result;
    }

    public PerformanceStatsDTO getAgentPerformance(Long agentId, String month) {
        List<PerformanceStatsDTO> all = calculateMonthlyPerformance(month);
        return all.stream()
                .filter(dto -> agentId.equals(dto.getAgentId()))
                .findFirst()
                .orElse(null);
    }

    private int countViewingRecords(String agentName, LocalDateTime start, LocalDateTime end) {
        return viewingRecordRepository.findByConditions(null, null, null,
                agentName, null, start, end).size();
    }

    private List<Subscription> findDeals(String agentName, LocalDateTime start, LocalDateTime end) {
        return subscriptionRepository.findByConditions(null, null, null, start, end)
                .stream()
                .filter(s -> agentName.equals(s.getAgentName())
                        && ("已签约".equals(s.getStatus()) || "已成交".equals(s.getStatus())))
                .collect(Collectors.toList());
    }

    private Set<Long> getViewingCustomerIds(String agentName, LocalDateTime start, LocalDateTime end) {
        return viewingRecordRepository.findByConditions(null, null, null,
                        agentName, null, start, end)
                .stream()
                .map(ViewingRecord::getCustomerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private BigDecimal calculateRate(Number actual, Number target) {
        if (target == null || target.doubleValue() == 0) {
            return BigDecimal.ZERO;
        }
        double actualVal = actual != null ? actual.doubleValue() : 0;
        return BigDecimal.valueOf(actualVal * 100.0 / target.doubleValue())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void savePerformanceAssessment(Agent agent, String month, int viewingCount,
                                           int dealCount, BigDecimal dealAmount,
                                           int totalCustomers, int dealCustomers,
                                           BigDecimal conversionRate) {
        PerformanceAssessment assessment = assessmentRepository
                .findByAgentIdAndMonth(agent.getId(), month)
                .orElse(new PerformanceAssessment());

        assessment.setAgentId(agent.getId());
        assessment.setAgentName(agent.getName());
        assessment.setMonth(month);
        assessment.setViewingCount(viewingCount);
        assessment.setDealCount(dealCount);
        assessment.setDealAmount(dealAmount);
        assessment.setTotalCustomers(totalCustomers);
        assessment.setDealCustomers(dealCustomers);
        assessment.setConversionRate(conversionRate);

        assessmentRepository.save(assessment);
    }

    public Map<String, Object> getRankingChartData(String month) {
        List<PerformanceStatsDTO> stats = calculateMonthlyPerformance(month);
        Map<String, Object> result = new HashMap<>();

        List<String> names = stats.stream()
                .map(PerformanceStatsDTO::getAgentName)
                .collect(Collectors.toList());

        List<Integer> viewingCounts = stats.stream()
                .map(PerformanceStatsDTO::getViewingCount)
                .collect(Collectors.toList());

        List<Integer> dealCounts = stats.stream()
                .map(PerformanceStatsDTO::getDealCount)
                .collect(Collectors.toList());

        List<BigDecimal> dealAmounts = stats.stream()
                .map(s -> s.getDealAmount() != null ? s.getDealAmount() : BigDecimal.ZERO)
                .collect(Collectors.toList());

        List<BigDecimal> conversionRates = stats.stream()
                .map(s -> s.getConversionRate() != null ? s.getConversionRate() : BigDecimal.ZERO)
                .collect(Collectors.toList());

        result.put("agentNames", names);
        result.put("viewingCounts", viewingCounts);
        result.put("dealCounts", dealCounts);
        result.put("dealAmounts", dealAmounts);
        result.put("conversionRates", conversionRates);
        result.put("performanceList", stats);

        return result;
    }

    public Map<String, Object> getRadarChartData(String month) {
        List<PerformanceStatsDTO> stats = calculateMonthlyPerformance(month);
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> indicator = Arrays.asList(
                createIndicator("带看量", 50),
                createIndicator("成交量", 20),
                createIndicator("成交总额(万)", 500),
                createIndicator("转化率(%)", 100),
                createIndicator("完成率(%)", 100)
        );

        List<Map<String, Object>> seriesData = new ArrayList<>();
        for (PerformanceStatsDTO dto : stats) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", dto.getAgentName());
            item.put("value", Arrays.asList(
                    dto.getViewingCount(),
                    dto.getDealCount(),
                    dto.getDealAmount() != null ? dto.getDealAmount().intValue() : 0,
                    dto.getConversionRate() != null ? dto.getConversionRate().intValue() : 0,
                    calculateOverallCompletionRate(dto).intValue()
            ));
            seriesData.add(item);
        }

        result.put("indicator", indicator);
        result.put("seriesData", seriesData);

        return result;
    }

    private Map<String, Object> createIndicator(String name, int max) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("max", max);
        return item;
    }

    private BigDecimal calculateOverallCompletionRate(PerformanceStatsDTO dto) {
        BigDecimal viewingRate = dto.getViewingCompletionRate() != null
                ? dto.getViewingCompletionRate() : BigDecimal.ZERO;
        BigDecimal dealRate = dto.getDealCompletionRate() != null
                ? dto.getDealCompletionRate() : BigDecimal.ZERO;
        BigDecimal amountRate = dto.getDealAmountCompletionRate() != null
                ? dto.getDealAmountCompletionRate() : BigDecimal.ZERO;
        BigDecimal conversionRate = dto.getConversionCompletionRate() != null
                ? dto.getConversionCompletionRate() : BigDecimal.ZERO;

        return viewingRate.add(dealRate).add(amountRate).add(conversionRate)
                .divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
    }
}
