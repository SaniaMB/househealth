package com.project.househealth.service;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.User;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;
import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.enums.TrendStatus;
import com.project.househealth.repositories.HealthLogRepository;
import com.project.househealth.trendanalysis.BloodPressureTrendResponse;
import com.project.househealth.trendanalysis.SugarTrendResponse;
import com.project.househealth.trendanalysis.TrendAnalysisServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrendAnalysisServiceImplTest {

    @Mock
    private HealthLogRepository healthLogRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private TrendAnalysisServiceImpl trendAnalysisService;

    @Test
    void shouldReturnInsufficientDataWhenCurrentLogsLessThanThree() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                new HealthLog(MetricType.BP, user),
                new HealthLog(MetricType.BP, user)
        );

        List<HealthLog> previousLogs = List.of(
                new HealthLog(MetricType.BP, user),
                new HealthLog(MetricType.BP, user),
                new HealthLog(MetricType.BP, user)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.BP),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        BloodPressureTrendResponse response =
                trendAnalysisService.analyzeBloodPressureTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.INSUFFICIENT_DATA,
                response.getTrendStatus()
        );

        assertEquals(
                TrendPeriod.WEEK,
                response.getTrendPeriod()
        );
    }

@Test
void shouldReturnImprovingTrend() {

    Long userId = 1L;

    User user = new User("test", "pass", "test@gmail.com");
    user.setUserId(userId);

    HealthLog c1 = new HealthLog(MetricType.BP, user);
    c1.setSystolic(130);
    c1.setDiastolic(80);

    HealthLog c2 = new HealthLog(MetricType.BP, user);
    c2.setSystolic(130);
    c2.setDiastolic(80);

    HealthLog c3 = new HealthLog(MetricType.BP, user);
    c3.setSystolic(130);
    c3.setDiastolic(80);

    List<HealthLog> currentLogs = List.of(c1, c2, c3);

    HealthLog p1 = new HealthLog(MetricType.BP, user);
    p1.setSystolic(140);
    p1.setDiastolic(90);

    HealthLog p2 = new HealthLog(MetricType.BP, user);
    p2.setSystolic(140);
    p2.setDiastolic(90);

    HealthLog p3 = new HealthLog(MetricType.BP, user);
    p3.setSystolic(140);
    p3.setDiastolic(90);

    List<HealthLog> previousLogs = List.of(p1, p2, p3);

    when(currentUserService.getCurrentUser())
            .thenReturn(user);

    when(healthLogRepository
            .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                    eq(userId),
                    eq(MetricType.BP),
                    any(Instant.class),
                    any(Instant.class)
            ))
            .thenReturn(currentLogs)
            .thenReturn(previousLogs);

    BloodPressureTrendResponse response =
            trendAnalysisService.analyzeBloodPressureTrend(
                    TrendPeriod.WEEK
            );

    assertEquals(
            TrendStatus.IMPROVING,
            response.getTrendStatus()
    );

    assertEquals(130.0,
            response.getCurrentAverageSystolic());

    assertEquals(80.0,
            response.getCurrentAverageDiastolic());

    assertEquals(140.0,
            response.getPreviousAverageSystolic());

    assertEquals(90.0,
            response.getPreviousAverageDiastolic());
}

    private HealthLog createBPLog(
            User user,
            int systolic,
            int diastolic
    ) {
        HealthLog log = new HealthLog(MetricType.BP, user);
        log.setSystolic(systolic);
        log.setDiastolic(diastolic);
        return log;
    }

    @Test
    void shouldReturnWorseningTrend() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createBPLog(user, 130, 90),
                createBPLog(user, 130, 90),
                createBPLog(user, 130, 90)
        );

        List<HealthLog> previousLogs = List.of(
                createBPLog(user, 120, 80),
                createBPLog(user, 120, 80),
                createBPLog(user, 120, 80)
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.BP),
                        any(Instant.class),
                        any(Instant.class)))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        BloodPressureTrendResponse response =
                trendAnalysisService.analyzeBloodPressureTrend(TrendPeriod.WEEK);

        assertEquals(
                TrendStatus.WORSENING,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnStableTrend() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createBPLog(user, 122, 82),
                createBPLog(user, 122, 82),
                createBPLog(user, 122, 82)
        );

        List<HealthLog> previousLogs = List.of(
                createBPLog(user, 120, 80),
                createBPLog(user, 120, 80),
                createBPLog(user, 120, 80)
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.BP),
                        any(Instant.class),
                        any(Instant.class)))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        BloodPressureTrendResponse response =
                trendAnalysisService.analyzeBloodPressureTrend(TrendPeriod.WEEK);

        assertEquals(
                TrendStatus.STABLE,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnStableWhenSystolicImprovesAndDiastolicWorsens() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createBPLog(user, 130, 95),
                createBPLog(user, 130, 95),
                createBPLog(user, 130, 95)
        );

        List<HealthLog> previousLogs = List.of(
                createBPLog(user, 140, 80),
                createBPLog(user, 140, 80),
                createBPLog(user, 140, 80)
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.BP),
                        any(Instant.class),
                        any(Instant.class)))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        BloodPressureTrendResponse response =
                trendAnalysisService.analyzeBloodPressureTrend(TrendPeriod.WEEK);

        assertEquals(
                TrendStatus.STABLE,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnInsufficientDataWhenPreviousLogsLessThanThree() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createBPLog(user, 120, 80),
                createBPLog(user, 120, 80),
                createBPLog(user, 120, 80)
        );

        List<HealthLog> previousLogs = List.of(
                createBPLog(user, 120, 80),
                createBPLog(user, 120, 80)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.BP),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        BloodPressureTrendResponse response =
                trendAnalysisService.analyzeBloodPressureTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.INSUFFICIENT_DATA,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnImprovingWhenSystolicStableAndDiastolicImproving() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createBPLog(user, 122, 80),
                createBPLog(user, 122, 80),
                createBPLog(user, 122, 80)
        );

        List<HealthLog> previousLogs = List.of(
                createBPLog(user, 120, 90),
                createBPLog(user, 120, 90),
                createBPLog(user, 120, 90)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.BP),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        BloodPressureTrendResponse response =
                trendAnalysisService.analyzeBloodPressureTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.IMPROVING,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnImprovingWhenSystolicImprovingAndDiastolicStable() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createBPLog(user, 130, 82),
                createBPLog(user, 130, 82),
                createBPLog(user, 130, 82)
        );

        List<HealthLog> previousLogs = List.of(
                createBPLog(user, 140, 80),
                createBPLog(user, 140, 80),
                createBPLog(user, 140, 80)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.BP),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        BloodPressureTrendResponse response =
                trendAnalysisService.analyzeBloodPressureTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.IMPROVING,
                response.getTrendStatus()
        );
    }

    private HealthLog createSugarLog(
            User user,
            SugarType sugarType,
            int sugarValue
    ) {
        HealthLog log = new HealthLog(MetricType.SUGAR, user);
        log.setSugarType(sugarType);
        log.setSugarValue(sugarValue);
        return log;
    }

    @Test
    void shouldReturnInsufficientDataForFastingSugar() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 105)
        );

        List<HealthLog> previousLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 110),
                createSugarLog(user, SugarType.FASTING, 112),
                createSugarLog(user, SugarType.FASTING, 115)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndSugarTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.SUGAR),
                        eq(SugarType.FASTING),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        SugarTrendResponse response =
                trendAnalysisService.analyzeFastingSugarTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.INSUFFICIENT_DATA,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnImprovingFastingSugarTrend() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100)
        );

        List<HealthLog> previousLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 120),
                createSugarLog(user, SugarType.FASTING, 120),
                createSugarLog(user, SugarType.FASTING, 120)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndSugarTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.SUGAR),
                        eq(SugarType.FASTING),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        SugarTrendResponse response =
                trendAnalysisService.analyzeFastingSugarTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.IMPROVING,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnWorseningFastingSugarTrend() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 130),
                createSugarLog(user, SugarType.FASTING, 130),
                createSugarLog(user, SugarType.FASTING, 130)
        );

        List<HealthLog> previousLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndSugarTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.SUGAR),
                        eq(SugarType.FASTING),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        SugarTrendResponse response =
                trendAnalysisService.analyzeFastingSugarTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.WORSENING,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldReturnStableFastingSugarTrend() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 102),
                createSugarLog(user, SugarType.FASTING, 102),
                createSugarLog(user, SugarType.FASTING, 102)
        );

        List<HealthLog> previousLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndSugarTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.SUGAR),
                        eq(SugarType.FASTING),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        SugarTrendResponse response =
                trendAnalysisService.analyzeFastingSugarTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                TrendStatus.STABLE,
                response.getTrendStatus()
        );
    }

    @Test
    void shouldSetFastingSugarTypeInResponse() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100),
                createSugarLog(user, SugarType.FASTING, 100)
        );

        List<HealthLog> previousLogs = List.of(
                createSugarLog(user, SugarType.FASTING, 120),
                createSugarLog(user, SugarType.FASTING, 120),
                createSugarLog(user, SugarType.FASTING, 120)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndSugarTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.SUGAR),
                        eq(SugarType.FASTING),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        SugarTrendResponse response =
                trendAnalysisService.analyzeFastingSugarTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                SugarType.FASTING,
                response.getSugarType()
        );
    }

    @Test
    void shouldSetPostMealSugarTypeInResponse() {

        Long userId = 1L;

        User user = new User("test", "pass", "test@gmail.com");
        user.setUserId(userId);

        List<HealthLog> currentLogs = List.of(
                createSugarLog(user, SugarType.POST_MEAL, 100),
                createSugarLog(user, SugarType.POST_MEAL, 100),
                createSugarLog(user, SugarType.POST_MEAL, 100)
        );

        List<HealthLog> previousLogs = List.of(
                createSugarLog(user, SugarType.POST_MEAL, 120),
                createSugarLog(user, SugarType.POST_MEAL, 120),
                createSugarLog(user, SugarType.POST_MEAL, 120)
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(healthLogRepository
                .findByUser_UserIdAndMetricTypeAndSugarTypeAndLoggedAtBetween(
                        eq(userId),
                        eq(MetricType.SUGAR),
                        eq(SugarType.POST_MEAL),
                        any(Instant.class),
                        any(Instant.class)
                ))
                .thenReturn(currentLogs)
                .thenReturn(previousLogs);

        SugarTrendResponse response =
                trendAnalysisService.analyzePostMealSugarTrend(
                        TrendPeriod.WEEK
                );

        assertEquals(
                SugarType.POST_MEAL,
                response.getSugarType()
        );
    }



}
