package com.project.househealth.bootstrap;

import com.project.househealth.entity.*;
import com.project.househealth.enums.FrequencyType;
import com.project.househealth.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.project.househealth.enums.FrequencyType.MONTHLY;
import static com.project.househealth.enums.FrequencyType.WEEKLY;
import static com.project.househealth.enums.MetricType.BP;
import static com.project.househealth.enums.MetricType.SUGAR;
import static com.project.househealth.enums.Role.OBSERVER;
import static com.project.househealth.enums.Role.TRACKER;
import static com.project.househealth.enums.SugarType.FASTING;
import static com.project.househealth.enums.SugarType.POST_MEAL;

@Profile("dev")
@Transactional
@Component
public class DataSanityRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final FamilyMembershipRepository familyMembershipRepository;
    private final HealthLogRepository healthLogRepository;
    private final ReminderSettingsRepository reminderSettingsRepository;

    public DataSanityRunner(UserRepository userRepository, FamilyRepository familyRepository, FamilyMembershipRepository familyMembershipRepository,
                            HealthLogRepository healthLogRepository,  ReminderSettingsRepository reminderSettingsRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
        this.familyMembershipRepository = familyMembershipRepository;
        this.healthLogRepository = healthLogRepository;
        this.reminderSettingsRepository = reminderSettingsRepository;
    }

    private void testUser() {
        // User user1 = new User("Alexa", "Alexa10","Alexa20@gmail.com");
        // userRepository.save(user1);

        long userId = 1L;

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        System.out.println("Saved user with id: " + user.getUserId());
        System.out.println("Saved user with Email: " + user.getEmail());
        System.out.println("Saved user with Name: " + user.getName());
        System.out.println("Saved user with Password: " + user.getPasswordHash());
        System.out.println("Saved user with Created At: " + user.getCreatedAt());
    }

    private void testFamily(){

        String familyName = "Winston";

//        Family family1 = new Family(familyName);
//        familyRepository.save(family1);

        List<Family> family = familyRepository. findByFamilyName(familyName);
        System.out.println(family);
    }

    private void testFamilyMembership(){

//        Family familyId = familyRepository.findById(1L)
//                         .orElseThrow(() -> new RuntimeException("Family not found with id: 1L"));
//        User userId = userRepository.findById(1L)
//                     .orElseThrow(() -> new RuntimeException("User not found with id: 1L"));
//
//        FamilyMembership familyMembership1 = new FamilyMembership(userId, familyId, OBSERVER);
//        familyMembership1.disableNotifications();
//
//        familyMembershipRepository.save(familyMembership1);


        FamilyMembership familyMembership1 = familyMembershipRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("FamilyMembership not found with id: 1L"));

        familyMembership1.changeRole(TRACKER);
        familyMembershipRepository.save(familyMembership1);

        System.out.println(familyMembership1.getFamilyMembershipId());
        System.out.println(familyMembership1.getFamily());
        System.out.println(familyMembership1.getUser());
        System.out.println(familyMembership1.getRole());
        System.out.println(familyMembership1.getNotificationsEnabled());
    }

    private void testHealthLog(){

        User user1 = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found with id: 1L"));

        //HealthLog healthLog1 = new HealthLog(BP, user1);

        HealthLog healthLog1 = healthLogRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("HealthLog not found with id: 1L"));
        healthLog1.setSystolic(120);
        healthLog1.setDiastolic(80);

        //HealthLog healthLog2 = new HealthLog(SUGAR, user1);

        HealthLog healthLog2 = healthLogRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("HealthLog not found with id: 2L"));
        healthLog2.setSugarType(FASTING);
        healthLog2.setSugarValue(62);

       // HealthLog healthLog3 = new HealthLog(SUGAR, user1);

        HealthLog healthLog3 = healthLogRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("HealthLog not found with id: 3L"));
        healthLog3.setSugarType(POST_MEAL);
        healthLog3.setSugarValue(59);

        healthLogRepository.save(healthLog1);
        healthLogRepository.save(healthLog2);
        healthLogRepository.save(healthLog3);

        System.out.println("UserId 1 : HealthLog 1 (BP)");
        System.out.println("LoggedAt " + healthLog1.getLoggedAt());
        System.out.println("LoggedId " + healthLog1.getLogId());
        System.out.println("Systolic " + healthLog1.getSystolic());
        System.out.println("Diastolic " + healthLog1.getDiastolic());
        System.out.println("Metric " + healthLog1.getMetricType());

        System.out.println("UserId 1 : HealthLog 2 (SUGAR - FASTING)");
        System.out.println("LoggedAt " + healthLog2.getLoggedAt());
        System.out.println("LoggedId " + healthLog2.getLogId());
        System.out.println("SugarType " + healthLog2.getSugarType());
        System.out.println("SugarValue " + healthLog2.getSugarValue());

        System.out.println("UserId 1 : HealthLog 3 (SUGAR - POST_MEAL)");
        System.out.println("LoggedAt " + healthLog3.getLoggedAt());
        System.out.println("LoggedId " + healthLog3.getLogId());
        System.out.println("SugarType " + healthLog3.getSugarType());
        System.out.println("SugarValue " + healthLog3.getSugarValue());
    }

    private void testReminderSettings(){

          User user1 = userRepository.findById(1L)
                      .orElseThrow(() -> new RuntimeException("User not found with id: 1L"));
//
//        ReminderSettings reminder1 = new ReminderSettings(SUGAR,WEEKLY, user1);
//
//        reminder1.setFrequencyInterval(7);
//        reminder1.setNotificationsEnabled(true);

        ReminderSettings reminder1 = reminderSettingsRepository.findByUser(user1);

        reminder1.markTriggered();

        System.out.println("User1 Reminder Settings");
        System.out.println("ReminderId " + reminder1.getReminderId());
        System.out.println("User " +reminder1.getUser());
        System.out.println("FrequencyInterval " +reminder1.getFrequencyInterval());
        System.out.println("FrequencyType " +reminder1.getFrequencyType());
        System.out.println("MetricType " +reminder1.getMetricType());
        System.out.println("LastTriggeredAt " +reminder1.getLastTriggeredAt());
        System.out.println("NotificationsEnabled " +reminder1.getNotificationsEnabled());

    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("DataSanityRunner started");

        // testUser();
        // testFamily();
        // testFamilyMembership();
        // testHealthLog();
        testReminderSettings();

    }
}
