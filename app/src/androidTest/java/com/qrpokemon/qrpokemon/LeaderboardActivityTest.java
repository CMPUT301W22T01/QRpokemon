package com.qrpokemon.qrpokemon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.qrpokemon.qrpokemon.controllers.LeaderboardController;
import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.views.leaderboard.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class LeaderboardActivityTest {
    LeaderboardItem mockItem = new LeaderboardItem("mockItem", 1, 123, 999, 999);

    // Base class which will be extended and overridden to mock test cases
    static class MockLeaderboardControllerBase extends LeaderboardController {
        final protected LeaderboardItem mockItem;

        public MockLeaderboardControllerBase(LeaderboardItem mockItem) {
            this.mockItem = mockItem;
        }
    }

    @Rule
    public ActivityScenarioRule<LeaderboardActivity> activityRule =
            new ActivityScenarioRule<>(LeaderboardActivity.class);

    @Before
    public void setup() {
        // Make our mock player the current player
        PlayerController playerController = PlayerController.getInstance();
        playerController.setupPlayer(mockItem.getUsername(), new ArrayList<>(), new HashMap(),
                mockItem.getTotalScore(), mockItem.getTotalScore(), mockItem.getHighestUnique(),
                false, "id");
    }

    /**
     * Checks that the back button closes the activity
     */
    @Test
    public void checkBackButton() {
        onView(withId(R.id.leaderboard_back)).perform(click());

        // NOTE: Espresso stays open which prevents getting the DESTROYED state
        // Therefore, check that the state was changed by the button
        assertNotEquals(Lifecycle.State.RESUMED, activityRule.getScenario().getState());
    }

    /**
     * Checks that the list can successfully add an item
     */
    @Test
    public void checkListHasItem() {
        class MockLeaderboardController extends MockLeaderboardControllerBase {
            public MockLeaderboardController(LeaderboardItem mockItem) {
                super(mockItem);
            }

            @Override
            public void sortLeaderboard(Context context, LeaderboardList list, int sortMethod) {
                list.clear();
                list.add(mockItem);
                list.notifyListUpdate();

                // We need to test after it's guaranteed that the list updated
                assertEquals(1, ((LeaderboardActivity) context).getItemCount());
            }

            @Override
            public void updateRank(Context context, LeaderboardList list) {
            }
        }

        // Override the leaderboard controller so we can insert our own values
        MockLeaderboardController mockLeaderboardController = new MockLeaderboardController(mockItem);
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
    public void checkListHasManyItems() {
        class MockLeaderboardController extends MockLeaderboardControllerBase {
            public MockLeaderboardController(LeaderboardItem mockItem) {
                super(mockItem);
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

            @Override
            public void updateRank(Context context, LeaderboardList list) {
            }
        }

        // Override the leaderboard controller so we can insert our own values
        MockLeaderboardController mockLeaderboardController = new MockLeaderboardController(mockItem);
        activityRule.getScenario().onActivity(activity -> {
            activity.setLeaderboardController(mockLeaderboardController);
        });

        // Click the sort selector to update the list
        // NOTE: Test may finish before we can see the update
        onView(withId(R.id.leaderboard_sort_selector)).perform(click());
        onView(withText("High Scores")).perform(click());
    }

    /**
     * Checks that a player's ranking is displayed
     */
    @Test
    public void checkPersonalRank() {
        class MockLeaderboardController extends MockLeaderboardControllerBase {
            public MockLeaderboardController(LeaderboardItem mockItem) {
                super(mockItem);
            }

            @Override
            public void sortLeaderboard(Context context, LeaderboardList list, int sortMethod) {
                list.clear();
                list.add(mockItem);
                list.notifyListUpdate();

                updateRank(context, list);
            }

            @Override
            public void updateRank(Context context, LeaderboardList list) {
                ((LeaderboardActivity) context).setPersonalRank(mockItem.getRank(), mockItem.getUsername(),
                        mockItem.getHighestUnique(), mockItem.getQrQuantity(), mockItem.getTotalScore());

                // Checking here ensures the data has updated
                onView(withId(R.id.tv_leaderboard_player_rank)).check(matches(withText("Top\n10")));
                onView(withId(R.id.tv_leaderboard_player_username)).check(matches(withText(mockItem.getUsername())));
                onView(withId(R.id.tv_leaderboard_player_unique)).check(matches(withText(mockItem.getHighestUnique())));
                onView(withId(R.id.tv_leaderboard_player_qrcount)).check(matches(withText(mockItem.getQrQuantity())));
                onView(withId(R.id.tv_leaderboard_player_score)).check(matches(withText(mockItem.getTotalScore())));
            }
        }

        // Override the leaderboard controller so we can insert our own values
        MockLeaderboardController mockLeaderboardController = new MockLeaderboardController(mockItem);
        activityRule.getScenario().onActivity(activity -> {
            activity.setLeaderboardController(mockLeaderboardController);
        });

        // Click the sort selector to update the list
        // NOTE: Test may finish before we can see the update
        onView(withId(R.id.leaderboard_sort_selector)).perform(click());
        onView(withText("High Scores")).perform(click());
    }
}