package com.qrpokemon.qrpokemon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.qrpokemon.qrpokemon.activities.leaderboard.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

// TODO: Use JUnit5?
@RunWith(AndroidJUnit4.class)
public class LeaderboardActivityTest {

    @Rule
    public ActivityScenarioRule<LeaderboardActivity> activityRule =
            new ActivityScenarioRule<>(LeaderboardActivity.class);

    /**
     * Checks that the back button closes the activity
     */
    @Test
    public void checkBackButton() {
        onView(withId(R.id.leaderboard_back)).perform(click());

        // NOTE: Expresso stays open which prevents getting the DESTROYED state
        // Therefore, check that the state was changed by the button
        assertNotEquals(Lifecycle.State.RESUMED, activityRule.getScenario().getState());
    }

    /**
     * Checks that the list can successfully add an item
     */
    @Test
    public void checkLeaderboardListHasItem() {
        class MockLeaderboardController extends LeaderboardController {
            final private LeaderboardItem mockItem;
            //final private LeaderboardItem mockItem2;

            MockLeaderboardController() {
                mockItem = new LeaderboardItem("mockItem", 0, 123, 999, 999);
                //mockItem = new LeaderboardItem("testItem2", 0, 0, 0, 0);
            }

            @Override
            public void sortLeaderboard(Context context, LeaderboardList list, int sortMethod) {
                list.clear();
                list.add(mockItem);
                list.notifyListUpdate();


                // We need to test after it's guaranteed that the list updated
                assertEquals(1, ((LeaderboardActivity) context).getItemCount());
            }
        }

        // Override the leaderboard controller so we can insert our own values
        MockLeaderboardController mockLeaderboardController = new MockLeaderboardController();
        activityRule.getScenario().onActivity(activity -> {
            activity.setLeaderboardController(mockLeaderboardController);
        });

        // Click the sort selector to update the list
        // NOTE: Test may finish before we can see the update
        onView(withId(R.id.leaderboard_sort_selector)).perform(click());
        onView(withText("High Scores")).perform(click());
    }

    /**
     * Checks that the list can add many items
     */
    @Test
    public void checkLeaderboardListHasManyItems() {
        class MockLeaderboardController extends LeaderboardController {
            final private LeaderboardItem mockItem;

            MockLeaderboardController() {
                mockItem = new LeaderboardItem("mockItem", 0, 0, 0, 0);
            }

            @Override
            public void sortLeaderboard(Context context, LeaderboardList list, int sortMethod) {
                list.clear();
                for (int i = 0; i < 100; i++) {
                    list.add(mockItem);
                }
                list.notifyListUpdate();

                // We need to test after it's guaranteed that the list updated
                assertEquals(100, ((LeaderboardActivity) context).getItemCount());
            }
        }

        // Override the leaderboard controller so we can insert our own values
        MockLeaderboardController mockLeaderboardController = new MockLeaderboardController();
        activityRule.getScenario().onActivity(activity -> {
            activity.setLeaderboardController(mockLeaderboardController);
        });

        // Click the sort selector to update the list
        onView(withId(R.id.leaderboard_sort_selector)).perform(click());
        onView(withText("High Scores")).perform(click());
    }

    // TODO: Verify sorting works
    // TODO: Check rank displayed
}