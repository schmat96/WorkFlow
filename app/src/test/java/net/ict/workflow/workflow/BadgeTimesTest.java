package net.ict.workflow.workflow;

import net.ict.workflow.workflow.model.BadgeTimes;
import net.ict.workflow.workflow.model.CardType;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class BadgeTimesTest {

    private BadgeTimes badgeTimes;
    private LocalDateTime ldt;

    @Before
    public void setup() {
        badgeTimes = new BadgeTimes();
        ldt = LocalDateTime.of(2018,06,14,8,30);
    }

    @Test
    public void getTimeStampsInDateTests() {
        assertEquals(6, badgeTimes.getTimeStampsInDate(ldt.toLocalDate()).size());
        assertEquals(0, badgeTimes.getTimeStampsInDate(ldt.plusYears(10).toLocalDate()).size());
        assertEquals(5, badgeTimes.getTimeStampsInDate(ldt.plusDays(1).toLocalDate()).size());
    }

    @Test
    public void lookForBadgedTimeTests() {
        assertNotNull(badgeTimes.lookForBadgedTime(ldt, CardType.DAY)[0]);
        assertNull(badgeTimes.lookForBadgedTime(ldt.plusYears(10), CardType.DAY));
    }

    @Test
    public void getBadgedTimeTest() {
        assertEquals(3.5f, badgeTimes.getBadgedTime(CardType.DAY, ldt), 0.0f);
        assertEquals(4.66666f, badgeTimes.getBadgedTime(CardType.DAY, ldt.plusDays(1)), 0.1f);
        assertEquals(8.16666f, badgeTimes.getBadgedTime(CardType.WEEK, ldt), 0.1f);

    }

    @Test
    public void getSecondsBetweenDaysTest() {
        assertEquals(3.5f, badgeTimes.getSecondsBetweenDays(1, ldt.toLocalDate()), 0.1f);
        assertEquals(badgeTimes.getBadgedTime(CardType.DAY, ldt.plusDays(1)), badgeTimes.getSecondsBetweenDays(1, ldt.plusDays(1).toLocalDate()), 0.0f);
        //assertEquals(3.5f, badgeTimes.getSecondsBetweenDays(1, ldt.toLocalDate()), 0.1f);
    }

    @Test
    public void removeWithValueTest() {
        badgeTimes.removeWithValue(null);
        getTimeStampsInDateTests();
        int beforeDeletion = badgeTimes.getTimeStampsInDate(ldt.toLocalDate()).size();
        badgeTimes.removeWithValue(ldt);
        assertEquals(beforeDeletion-1, badgeTimes.getTimeStampsInDate(ldt.toLocalDate()).size());

    }

}
