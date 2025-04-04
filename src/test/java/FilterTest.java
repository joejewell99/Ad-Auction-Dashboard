import com.example.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FilterTest {

    public Filter filter;
    public ArrayList<String[]> testImpressions;

    @BeforeEach
    public void setUp() {
        filter = new Filter();
        testImpressions = new ArrayList<>();

        testImpressions.add(new String[]{"2015-01-20 16:12:47", "1", "Female", "25-34", "High", "Shopping", "15"});
        testImpressions.add(new String[]{"2015-01-21 09:45:23", "2", "Male", "35-44", "Medium", "News", "10"});
        testImpressions.add(new String[]{"2015-01-22 14:30:00", "3", "Female", "<25", "Low", "Social Media", "12"});
        testImpressions.add(new String[]{"2015-01-23 18:15:33", "4", "Male", "45-54", "High", "Travel", "20"});

    }

    @Test
    public void maleTest(){
        ArrayList<String[]> result = filter.genderFilter(testImpressions, "Male");
        assertEquals("2", result.get(0)[1]);
        assertEquals("4", result.get(1)[1]);
    }

    @Test
    public void femaleTest(){
        ArrayList<String[]> result = filter.genderFilter(testImpressions, "Female");
        assertEquals(2, result.size());
        assertEquals("1", result.get(0)[1]);
        assertEquals("3", result.get(1)[1]);
    }

    @Test
    void anyGenderTest() {
        ArrayList<String[]> result = filter.genderFilter(testImpressions, "");
        assertEquals(4, result.size());
    }


    @Test
    void highIncomeTest() {
        ArrayList<String[]> result = filter.incomeFilter(testImpressions, "High");
        assertEquals("1", result.get(0)[1]);
        assertEquals("4", result.get(1)[1]);
    }


    @Test
    void mediumIncomeTest() {
        ArrayList<String[]> result = filter.incomeFilter(testImpressions, "Medium");
        assertEquals("2", result.get(0)[1]);
    }


    @Test
    void lowIncomeTest() {
        ArrayList<String[]> result = filter.incomeFilter(testImpressions, "Low");
        assertEquals("3", result.get(0)[1]);
    }

    @Test
    void anyIncomeTest() {
        ArrayList<String[]> result = filter.incomeFilter(testImpressions, "");
        assertEquals(4, result.size());
    }

    @Test
    void multipleContextTest() {
        ArrayList<String> contexts = new ArrayList<>(Arrays.asList("News", "Travel"));
        ArrayList<String[]> result = filter.contextFilter(testImpressions, contexts);
        assertEquals("2", result.get(0)[1]);
        assertEquals("4", result.get(1)[1]);
    }

    @Test
    void anyContextTest() {
        ArrayList<String> emptyContexts = new ArrayList<>();
        ArrayList<String[]> result = filter.contextFilter(testImpressions, emptyContexts);
        assertEquals(4, result.size());
    }

    @Test
    void multipleAgesTest() {
        ArrayList<String> ages = new ArrayList<>(Arrays.asList("35-44", "45-54"));
        ArrayList<String[]> result = filter.ageFilter(testImpressions, ages);
        assertEquals("2", result.get(0)[1]);
        assertEquals("4", result.get(1)[1]);
    }

    @Test
    void anyAgeTest() {
        ArrayList<String> emptyAges = new ArrayList<>();
        ArrayList<String[]> result = filter.ageFilter(testImpressions, emptyAges);
        assertEquals(4, result.size());
    }




}
