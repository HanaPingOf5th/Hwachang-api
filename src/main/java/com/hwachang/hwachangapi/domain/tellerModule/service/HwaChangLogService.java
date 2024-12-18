package com.hwachang.hwachangapi.domain.tellerModule.service;

import com.hwachang.hwachangapi.domain.customerModule.repository.ReviewRepository;
import com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog.DailyLog;
import com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog.LogData;
import com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog.MonthlyLog;
import com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog.WeeklyLog;
import com.hwachang.hwachangapi.domain.tellerModule.dto.TellerMainResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.TellerReviewResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class HwaChangLogService {

    private final TellerRepository tellerRepository;
    private final TellerConsultingRoomRepository consultingRoomRepository;
    private final ReviewRepository reviewRepository;

    private List<LocalDateTime> read(TellerEntity teller, LocalDateTime startDate, LocalDateTime endDate) {
        return consultingRoomRepository.findAllByDate(teller.getId(), startDate, endDate);
    }

    private List<Integer> countByHour(List<LocalDateTime> dateTimes) {
        List<Integer> counts = new ArrayList<>(Collections.nCopies(24, 0));
        for (LocalDateTime dateTime: dateTimes) {
            int hour = dateTime.getHour();
            counts.set(hour, counts.get(hour)+1);
        }
        return counts;
    }

    private List<Integer> countByDay(List<LocalDateTime> dateTimes, LocalDateTime startDate, LocalDateTime endDate) {
        int days = (int) ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;
        log.info("days : {}", days);
        log.info("startDate: {}, endDate : {}", startDate, endDate);
        List<Integer> counts = new ArrayList<>(Collections.nCopies(days, 0));
        for (LocalDateTime dateTime: dateTimes) {
            int dayIndex = (int) ChronoUnit.DAYS.between(startDate.toLocalDate(), dateTime.toLocalDate());
            counts.set(dayIndex, counts.get(dayIndex) + 1);
        }
        return counts;
    }

    public LogData readGraphData(TellerEntity teller) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime twoDaysAgo = now.minusDays(2);
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        LocalDateTime twoWeeksAgo = now.minusWeeks(2);
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        LocalDateTime twoMonthsAgo = now.minusMonths(2);
        
        // 조회
        List<Integer> todayList = countByHour(read(teller, yesterday, now));
        List<Integer> yesterdayList = countByHour(read(teller, twoDaysAgo, yesterday));
        List<Integer> thisweekList = countByDay(read(teller, oneWeekAgo, now), oneWeekAgo, now);
        List<Integer> lastWeekList = countByDay(read(teller, twoWeeksAgo, oneWeekAgo), twoWeeksAgo, oneWeekAgo);
        List<Integer> thismonthList = countByDay(read(teller, oneMonthAgo, now), oneMonthAgo, now);
        List<Integer> lastMonthList = countByDay(read(teller, twoMonthsAgo, oneMonthAgo), twoMonthsAgo, oneMonthAgo);

        return LogData.builder()
                .dailyLog(DailyLog.builder()
                        .today(todayList)
                        .yesterday(yesterdayList)
                        .build())
                .weeklyLog(WeeklyLog.builder()
                        .thisWeek(thisweekList)
                        .lastWeek(lastWeekList)
                        .build())
                .monthlyLog(MonthlyLog.builder()
                        .thisMonth(thismonthList)
                        .lastMonth(lastMonthList)
                        .build())
                .build();
    }

    public TellerMainResponseDto getTellerDashboardData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        TellerEntity teller = tellerRepository.findTellerByUserName(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        int avgScore = reviewRepository.findAverageNpsByTellerId(teller.getId());
        int sumCustomer = reviewRepository.countCustomersByTellerId(teller.getId());
        LogData logData = readGraphData(teller);
        List<String> reviews = reviewRepository.findReviewEntitiesByTellerId(teller.getId());

        return TellerMainResponseDto.builder()
                .avgScore(avgScore)
                .sumCustomer(sumCustomer)
                .hwachangLog(logData)
                .reviews(reviews)
                .build();
    }

    public TellerReviewResponseDto getTellerReviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        TellerEntity teller = tellerRepository.findTellerByUserName(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<String> reviews = reviewRepository.findReviewEntitiesByTellerId(teller.getId());

        return TellerReviewResponseDto.builder()
                .reviews(reviews)
                .build();
    }
}
