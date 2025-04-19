import com.example.ChartCreator;
import com.example.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;


public class ChartCreatorTest {

    public ChartCreator chartCreator;
    private ArrayList<String[]> testClicks;
    private ArrayList<String[]> testImpressions;
    private ArrayList<String[]> testInteractions;

    private static class TestLogManager extends LogManager {
        private final ArrayList<String[]> clicks;
        private final ArrayList<String[]> impressions;
        private final ArrayList<String[]> interactions;

        public TestLogManager(ArrayList<String[]> clicks, ArrayList<String[]> impressions, ArrayList<String[]> interactions) {
            this.clicks = clicks;
            this.impressions = impressions;
            this.interactions = interactions;
        }

    }
    @BeforeEach
    public void setUp() {
        testImpressions = new ArrayList<>();
        testImpressions.add(new String[]{"2015-01-01 16:12:47", "1", "Female", "25-34", "High", "Shopping", "15"});
        testImpressions.add(new String[]{"2015-01-02 09:45:23", "2", "Male", "35-44", "Medium", "News", "10"});
        testImpressions.add(new String[]{"2015-01-03 14:30:00", "3", "Female", "<25", "Low", "Social Media", "12"});
        testImpressions.add(new String[]{"2015-01-04 18:15:33", "4", "Male", "45-54", "High", "Travel", "20"});
        testImpressions.add(new String[]{"2015-02-03 10:00:00", "5", "Female", "25-34", "Medium", "Blog", "8"});
        testImpressions.add(new String[]{"2015-02-04 14:20:00", "6", "Male", "35-44", "High", "Hobbies", "18"});
        testImpressions.add(new String[]{"2015-02-05 11:30:00", "7", "Female", "<25", "Low", "Social Media", "9"});
        testImpressions.add(new String[]{"2015-02-06 16:45:00", "8", "Male", "45-54", "Medium", "Travel", "12"});

        testClicks = new ArrayList<>();
        testClicks.add(new String[]{"2015-01-01 16:12:50", "1", "5"});
        testClicks.add(new String[]{"2015-01-02 09:45:25", "2", "3"});
        testClicks.add(new String[]{"2015-01-03 14:30:05", "3", "4"});
        testClicks.add(new String[]{"2015-02-03 10:00:05", "5", "6"});
        testClicks.add(new String[]{"2015-02-04 14:20:10", "6", "7"});
        testClicks.add(new String[]{"2015-02-05 11:30:15", "7", "5"});


        testInteractions = new ArrayList<>();
        testInteractions.add(new String[]{"2015-01-01 16:12:50", "1", "2015-01-01 16:12:59", "5", "Yes"});
        testInteractions.add(new String[]{"2015-01-02 09:45:30", "2", "2015-01-02 09:45:41", "2", "No"});
        testInteractions.add(new String[]{"2015-01-03 14:30:10", "3", "n/a", "1", "No"});
        testInteractions.add(new String[]{"2015-02-03 10:00:10", "5", "2015-02-03 10:00:21", "3", "Yes"});
        testInteractions.add(new String[]{"2015-02-04 14:20:10", "6", "2015-02-04 14:40:11", "4", "No"});
        testInteractions.add(new String[]{"2015-02-05 11:30:20", "7", "n/a", "1", "No"});

        LogManager testLogManager = new TestLogManager(testClicks,testImpressions,testInteractions);
        chartCreator = new ChartCreator(testLogManager,0);

    }

    @Test
    public void testDailyImpressions() {
        Map<String, Integer> result = chartCreator.getDailyImpressions(testImpressions);
        assertEquals(1, result.get("2015-01-01"));
        assertEquals(1, result.get("2015-01-02"));
        assertEquals(1, result.get("2015-01-03"));
        assertEquals(1, result.get("2015-01-04"));
        assertEquals(1, result.get("2015-02-03"));
        assertEquals(1, result.get("2015-02-04"));
        assertEquals(1, result.get("2015-02-05"));
        assertEquals(1, result.get("2015-02-06"));

    }

    @Test
    public void testWeeklyImpressions() {
        Map<String, Integer> result = chartCreator.getWeeklyImpressions(testImpressions);
        assertEquals(4,result.get("Week 1"));
        assertEquals(2,result.get("Week 5"));
        assertEquals(2,result.get("Week 6"));
    }

    @Test
    public void testMonthlyImpressions() {
        Map<String, Integer> result = chartCreator.getMonthlyImpressions(testImpressions);
        assertEquals(4,result.get("Month 1"));
        assertEquals(4,result.get("Month 2"));
    }

    @Test
    public void testDailyClicks() {
        Map<String, Integer> result = chartCreator.getDailyClicks(testClicks);
        assertEquals(1,result.get("2015-01-01"));
        assertEquals(1,result.get("2015-01-02"));
        assertEquals(1,result.get("2015-01-03"));
        assertEquals(1,result.get("2015-02-03"));
        assertEquals(1,result.get("2015-02-04"));
        assertEquals(1,result.get("2015-02-05"));
    }

    @Test
    public void testWeeklyClicks() {
        Map<String, Integer> result = chartCreator.getWeeklyClicks(testClicks);
        assertEquals(3,result.get("Week 1"));
        assertEquals(2,result.get("Week 5"));
        assertEquals(1,result.get("Week 6"));
    }

    @Test
    public void testMonthlyClicks() {
        Map<String, Integer> result = chartCreator.getMonthlyClicks(testClicks);
        assertEquals(3,result.get("Month 1"));
        assertEquals(3,result.get("Month 2"));
    }

    @Test
    public void testDailyCost() {
        Map<String, Float> result = chartCreator.getDailyCost(testClicks,testImpressions);

        assertEquals(20f, result.get("2015-01-01"));
        assertEquals(13f, result.get("2015-01-02"));
        assertEquals(16f, result.get("2015-01-03"));
        assertEquals(20f, result.get("2015-01-04"));
        assertEquals(14f, result.get("2015-02-03"));
        assertEquals(25f, result.get("2015-02-04"));
        assertEquals(14f, result.get("2015-02-05"));
        assertEquals(12f, result.get("2015-02-06"));

    }

    @Test
    public void testWeeklyCost() {
        Map<String, Float> result = chartCreator.getWeeklyCost(testClicks,testImpressions);

        assertEquals(69f, result.get("Week 1"));
        assertEquals(39f, result.get("Week 5"));
        assertEquals(26f, result.get("Week 6"));
    }

    @Test
    public void testMonthlyCost() {
        Map<String, Float> result = chartCreator.getMonthlyCost(testClicks,testImpressions);

        assertEquals(69f, result.get("Month 1"));
        assertEquals(65f, result.get("Month 2"));

    }

    @Test
    public void testDailyConversions() {
        Map<String, Integer> result = chartCreator.getDailyConversions(testInteractions);

        assertEquals(1, result.get("2015-01-01"));
        assertEquals(0, result.get("2015-01-02"));
        assertEquals(0, result.get("2015-01-03"));
        assertEquals(1, result.get("2015-02-03"));
        assertEquals(0, result.get("2015-02-04"));
        assertEquals(0, result.get("2015-02-05"));

    }

    @Test
    public void testWeeklyConversions() {
        Map<String, Integer> result = chartCreator.getWeeklyConversions(testInteractions);

        assertEquals(1,result.get("Week 1"));
        assertEquals(1, result.get("Week 5"));
        assertEquals(0, result.get("Week 6"));
    }

    @Test
    public void testMonthlyConversions() {
        Map<String, Integer> result = chartCreator.getMonthlyConversions(testInteractions);

        assertEquals(1,result.get("Month 1"));
        assertEquals(1,result.get("Month 2"));
    }

    // Test bounces based on only one page viewed
    @Test
    public void testDefaultBounceDaily() {
        Map<String, Integer> result = chartCreator.getDailyBounces(testInteractions,0);
        assertEquals(0,result.get("2015-01-01"));
        assertEquals(0,result.get("2015-01-02"));
        assertEquals(1,result.get("2015-01-03"));
        assertEquals(0,result.get("2015-02-03"));
        assertEquals(0,result.get("2015-02-04"));
        assertEquals(1,result.get("2015-02-05"));
    }

    //Test bounces based on only one page viewed
    @Test
    public void testDefaultBounceWeekly() {
        Map<String, Integer> result = chartCreator.getWeeklyBounces(testInteractions,0);
        assertEquals(1,result.get("Week 1"));
        assertEquals(0,result.get("Week 5"));
        assertEquals(1,result.get("Week 6"));
    }

    //Test bounces based on only one page viewed
    @Test
    public void testDefaultBouncesMonthly() {
        Map<String, Integer> result = chartCreator.getMonthlyBounces(testInteractions,0);
        assertEquals(1,result.get("Month 1"));
        assertEquals(1,result.get("Month 2"));
    }

    // Test bounces on inside edge and outside edge
    @Test
    public void testEdgeBounceDaily() {
        Map<String, Integer> result = chartCreator.getDailyBounces(testInteractions,10);
        assertEquals(1,result.get("2015-01-01"));
        assertEquals(0,result.get("2015-01-02"));
        assertEquals(0,result.get("2015-02-03"));
        assertEquals(0,result.get("2015-02-04"));
    }

    // Test bounces on inside edge and outside edge
    @Test
    public void testEdgeBounceWeekly() {
        Map<String, Integer> result = chartCreator.getWeeklyBounces(testInteractions,10);
        assertEquals(1,result.get("Week 1"));
        assertEquals(0,result.get("Week 5"));
    }

    // Test bounces on inside edge and outside edge
    @Test
    public void testEdgeBounceMonthly() {
        Map<String, Integer> result = chartCreator.getMonthlyBounces(testInteractions,10);
        assertEquals(1,result.get("Month 1"));
        assertEquals(0,result.get("Month 2"));
    }








}
