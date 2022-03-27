package com.qrpokemon.qrpokemon;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.qrpokemon.qrpokemon.activities.leaderboard.LeaderboardActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LeaderboardActivityTest {
    // TODO: Use Mockito to simulate requirements (preferable to live database)
    private Solo solo;
    private LeaderboardActivity leaderboardActivity;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Create a solo instance before all tests
     * @throws Exception Failed to create solo instance
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the activity
     * @throws Exception Could not get the Activity
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Checks the back button returns us to MainActivity
     */
    @Test
    public void checkBackButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Leaderboard_Button));
        solo.waitForActivity(LeaderboardActivity.class, 2000);

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        solo.clickOnView(solo.getView(R.id.leaderboard_back));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Close activity after each test
     * @throws Exception Failed to close all opened activities
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}