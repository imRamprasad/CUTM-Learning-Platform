package com.cutm.platform.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cutm.platform.models.Contest;
import com.cutm.platform.models.Course;
import com.cutm.platform.models.Discussion;
import com.cutm.platform.models.Notification;
import com.cutm.platform.models.Problem;
import com.cutm.platform.models.ProblemRef;
import com.cutm.platform.models.User;
import com.cutm.platform.repositories.ContestRepository;
import com.cutm.platform.repositories.CourseRepository;
import com.cutm.platform.repositories.DiscussionRepository;
import com.cutm.platform.repositories.NotificationRepository;
import com.cutm.platform.repositories.ProblemRepository;
import com.cutm.platform.repositories.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedCourses();
        seedProblems();
        seedContests();
        seedDiscussions();
        seedNotifications();
    }

    private void seedUsers() {
        LocalDateTime now = LocalDateTime.now();

        // Always ensure the demo admin account exists
        if (userRepository.findByEmail("admin@codelearn.com").isEmpty()) {
        User admin = new User();
        admin.setEmail("admin@codelearn.com");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole("ADMIN");
        admin.setStatus("ACTIVE");
        admin.setEmailVerified(true);
        admin.setCreatedAt(now);
        admin.setUpdatedAt(now);
        admin.setProblemsSolved(0);
        admin.setProblemsAttempted(0);
        admin.setRank(0);
        admin.setTotalPoints(0);
        userRepository.save(admin);
        }

        // Always ensure the demo student account exists
        if (userRepository.findByEmail("student@codelearn.com").isEmpty()) {
        User student = new User();
        student.setEmail("student@codelearn.com");
        student.setUsername("student");
        student.setPassword(passwordEncoder.encode("Student@123"));
        student.setFirstName("Alex");
        student.setLastName("Student");
        student.setRole("STUDENT");
        student.setStatus("ACTIVE");
        student.setEmailVerified(true);
        student.setCreatedAt(now);
        student.setUpdatedAt(now);
        student.setProblemsSolved(12);
        student.setProblemsAttempted(25);
        student.setRank(15);
        student.setTotalPoints(480);
        userRepository.save(student);
        }
    }

    private void seedCourses() {
        if (courseRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        Course dsa = new Course();
        dsa.setTitle("DSA Foundations");
        dsa.setSlug("dsa-foundations");
        dsa.setDescription("Core problem solving and data structures");
        dsa.setLevel("BEGINNER");
        dsa.setTopics(Arrays.asList("Arrays", "Strings", "HashMap"));
        dsa.setModules(new ArrayList<>());
        dsa.setEnrolledCount(1240);
        dsa.setCompletedCount(410);
        dsa.setAverageRating(4.6);
        dsa.setReviews(new ArrayList<>());
        dsa.setIsPublished(true);
        dsa.setStatus("PUBLISHED");
        dsa.setCreatedBy("system");
        dsa.setPrerequisites(new ArrayList<>());
        dsa.setCreatedAt(now);
        dsa.setUpdatedAt(now);
        courseRepository.save(dsa);

        Course dp = new Course();
        dp.setTitle("Dynamic Programming Mastery");
        dp.setSlug("dynamic-programming-mastery");
        dp.setDescription("DP states, transitions, and optimization");
        dp.setLevel("ADVANCED");
        dp.setTopics(Arrays.asList("Memoization", "Tabulation"));
        dp.setModules(new ArrayList<>());
        dp.setEnrolledCount(430);
        dp.setCompletedCount(90);
        dp.setAverageRating(4.7);
        dp.setReviews(new ArrayList<>());
        dp.setIsPublished(true);
        dp.setStatus("PUBLISHED");
        dp.setCreatedBy("system");
        dp.setPrerequisites(new ArrayList<>());
        dp.setCreatedAt(now);
        dp.setUpdatedAt(now);
        courseRepository.save(dp);
    }

    private void seedProblems() {
        if (problemRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        Problem p1 = new Problem();
        p1.setSlug("two-sum");
        p1.setTitle("Two Sum");
        p1.setDescription("Find indices of two numbers adding to target");
        p1.setDifficulty("EASY");
        p1.setStatus("PUBLISHED");
        p1.setIsPublished(true);
        p1.setCategories(Arrays.asList("Array", "Hash Table"));
        p1.setTags(Arrays.asList("array", "hashmap"));
        p1.setProblemStatement("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.");
        p1.setHints("Use a hash map to store visited values.");
        p1.setEditorialLinks(new ArrayList<>());
        p1.setTestCases(new ArrayList<>());
        p1.setHiddenTestCases(new ArrayList<>());
        p1.setCodeTemplates(new ArrayList<>());
        p1.setTotalSubmissions(2300);
        p1.setTotalAccepted(1200);
        p1.setAcceptanceRate(52.17);
        p1.setTotalSolved(980);
        p1.setSupportedLanguages(Arrays.asList("java", "python", "cpp", "javascript"));
        p1.setSolutions(new ArrayList<>());
        p1.setTimeLimit(2);
        p1.setMemoryLimit(256);
        p1.setCreatedBy("system");
        p1.setCreatedAt(now);
        p1.setUpdatedAt(now);
        problemRepository.save(p1);

        Problem p2 = new Problem();
        p2.setSlug("longest-substring-without-repeating-characters");
        p2.setTitle("Longest Substring Without Repeating Characters");
        p2.setDescription("Length of longest substring without duplicate chars");
        p2.setDifficulty("MEDIUM");
        p2.setStatus("PUBLISHED");
        p2.setIsPublished(true);
        p2.setCategories(Arrays.asList("String", "Sliding Window"));
        p2.setTags(Arrays.asList("string", "sliding-window"));
        p2.setProblemStatement("Given a string s, find the length of the longest substring without repeating characters.");
        p2.setHints("Maintain a moving window and last seen index.");
        p2.setEditorialLinks(new ArrayList<>());
        p2.setTestCases(new ArrayList<>());
        p2.setHiddenTestCases(new ArrayList<>());
        p2.setCodeTemplates(new ArrayList<>());
        p2.setTotalSubmissions(1900);
        p2.setTotalAccepted(860);
        p2.setAcceptanceRate(45.26);
        p2.setTotalSolved(640);
        p2.setSupportedLanguages(Arrays.asList("java", "python", "cpp", "javascript"));
        p2.setSolutions(new ArrayList<>());
        p2.setTimeLimit(2);
        p2.setMemoryLimit(256);
        p2.setCreatedBy("system");
        p2.setCreatedAt(now);
        p2.setUpdatedAt(now);
        problemRepository.save(p2);
    }

    private void seedContests() {
        if (contestRepository.count() > 0) {
            return;
        }

        List<Problem> problems = problemRepository.findAll();
        List<ProblemRef> problemRefs = new ArrayList<>();
        int seq = 1;
        for (Problem problem : problems) {
            ProblemRef ref = new ProblemRef();
            ref.setProblemId(problem.getId());
            ref.setSequenceNumber(seq++);
            ref.setPoints("EASY".equals(problem.getDifficulty()) ? 100 : 200);
            problemRefs.add(ref);
        }
        LocalDateTime now = LocalDateTime.now();

        Contest upcoming = new Contest();
        upcoming.setTitle("Weekly Challenge");
        upcoming.setSlug("weekly-challenge");
        upcoming.setDescription("Weekly practice contest");
        upcoming.setStartTime(now.plusDays(1));
        upcoming.setEndTime(now.plusDays(1).plusHours(2));
        upcoming.setDurationMinutes(120);
        upcoming.setProblems(problemRefs);
        upcoming.setParticipantIds(new ArrayList<>());
        upcoming.setRegistrationCount(0);
        upcoming.setScoringType("ICPC");
        upcoming.setPartialScoring(0);
        upcoming.setStandings(new ArrayList<>());
        upcoming.setStatus("UPCOMING");
        upcoming.setIsPublic(true);
        upcoming.setCreatedBy("system");
        upcoming.setCreatedAt(now);
        upcoming.setUpdatedAt(now);
        contestRepository.save(upcoming);
    }

    private void seedDiscussions() {
        if (discussionRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Discussion d = new Discussion();
        d.setProblemId(null);
        d.setTitle("How to start with dynamic programming?");
        d.setContent("Looking for a beginner friendly DP roadmap.");
        d.setAuthorId("system");
        d.setAuthorName("admin");
        d.setUpvotes(5);
        d.setUpvotedBy(new ArrayList<>());
        d.setDownvotes(0);
        d.setDownvotedBy(new ArrayList<>());
        d.setReplies(new ArrayList<>());
        d.setReplyCount(0);
        d.setTags(Arrays.asList("dp", "beginner"));
        d.setCategory("General");
        d.setStatus("ACTIVE");
        d.setIsPinned(false);
        d.setCreatedAt(now);
        d.setUpdatedAt(now);
        discussionRepository.save(d);
    }

    private void seedNotifications() {
        if (notificationRepository.count() > 0) {
            return;
        }

        User student = userRepository.findByEmail("student@codelearn.com").orElse(null);
        if (student == null) {
            return;
        }

        Notification n = new Notification();
        n.setUserId(student.getId());
        n.setTitle("Welcome to CodeLearn");
        n.setMessage("Your account is ready. Start solving problems now.");
        n.setType("SYSTEM");
        n.setIsRead(false);
        n.setActionUrl("/student/dashboard");
        n.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(n);
    }
}
