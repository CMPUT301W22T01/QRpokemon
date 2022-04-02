package com.qrpokemon.qrpokemon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import static java.lang.Boolean.FALSE;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.qrpokemon.qrpokemon.controllers.LeaderboardController;
import com.qrpokemon.qrpokemon.controllers.QrInventoryController;
import com.qrpokemon.qrpokemon.models.Player;
import com.qrpokemon.qrpokemon.views.leaderboard.*;
import com.qrpokemon.qrpokemon.views.qrinventory.QrInventoryActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class QrInventoryActivityTest {

    ArrayList<String> qrInventory = new ArrayList<>();
    Integer qrCount = 0;
    Integer totalScore = 0;
    String id = "";
    Integer highestUnique = 0;
    Player mockPlayer = new Player("QrTestUser",  qrInventory, null, qrCount, totalScore, id, highestUnique, FALSE);

    // Base class which will be extended and overridden to mock test cases
    static class MockQrInventoryControllerBase extends QrInventoryController {
        final protected String mockHash;

        public MockQrInventoryControllerBase(String mockPlayer) {
            this.mockHash = mockPlayer;
        }
    }

    @Rule
    public ActivityScenarioRule<QrInventoryActivity> activityRule =
            new ActivityScenarioRule<>(QrInventoryActivity.class);

    /**
     * Checks that the back button closes the activity
     */
    @Test
    public void checkBackButton() {
        onView(withId(R.id.bt_back)).perform(click());

        // NOTE: Espresso stays open which prevents getting the DESTROYED state
        // Therefore, check that the state was changed by the button
        assertNotEquals(Lifecycle.State.RESUMED, activityRule.getScenario().getState());
    }

}