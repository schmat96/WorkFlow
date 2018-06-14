package net.ict.workflow.workflow;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CardViewInstrumentedTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("net.ict.workflow.workflow", appContext.getPackageName());
    }

    @Test
    public void checkCardRecylcerView() {
        final int EXPECTED_SIZE = 3;
        final int[] size = new int[1];
        onView(withId(R.id.card_view_container)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                size[0] = recyclerView.getChildCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
        assertEquals(EXPECTED_SIZE, size[0]);
    }

    @Test
    public void checkCardContainer() {
        final String EXPECTED_DAY = mActivityRule.getActivity().getApplicationContext().getString(R.string.Day);
        final String EXPECTED_WEEK = mActivityRule.getActivity().getApplicationContext().getString(R.string.Week);
        final String EXPECTED_MONTH = mActivityRule.getActivity().getApplicationContext().getString(R.string.Month);
        final CardView[] day = new CardView[1];
        final CardView[] week = new CardView[1];
        final CardView[] month = new CardView[1];
        onView(withId(R.id.card_view_container)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                day[0] = (CardView) recyclerView.getChildAt(0);
                week[0] = (CardView) recyclerView.getChildAt(1);
                month[0] = (CardView) recyclerView.getChildAt(2);
                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
        TextView dayView = day[0].findViewById(R.id.cardViewTitle);
        String dayText = dayView.getText().toString();
        TextView weekView = week[0].findViewById(R.id.cardViewTitle);
        String weekText = weekView.getText().toString();
        TextView monthView = month[0].findViewById(R.id.cardViewTitle);
        String monthText = monthView.getText().toString();

        assertEquals(EXPECTED_DAY, dayText);
        assertEquals(EXPECTED_WEEK, weekText);
        assertEquals(EXPECTED_MONTH, monthText);

    }
}
