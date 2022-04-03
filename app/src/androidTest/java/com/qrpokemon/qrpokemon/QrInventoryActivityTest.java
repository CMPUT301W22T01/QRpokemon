package com.qrpokemon.qrpokemon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static java.lang.Boolean.FALSE;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import androidx.test.rule.ActivityTestRule;


import com.qrpokemon.qrpokemon.controllers.LeaderboardController;
import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.controllers.QrInventoryController;
import com.qrpokemon.qrpokemon.models.Player;
import com.qrpokemon.qrpokemon.views.leaderboard.*;
import com.qrpokemon.qrpokemon.views.map.MapActivity;
import com.qrpokemon.qrpokemon.views.profile.ProfileActivity;
import com.qrpokemon.qrpokemon.views.qrinventory.QrInventoryActivity;
import com.qrpokemon.qrpokemon.views.search.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class QrInventoryActivityTest {

    private Solo solo;
    private PlayerController playerController = PlayerController.getInstance();

    @Rule
    public ActivityTestRule<QrInventoryActivity> rule =
            new ActivityTestRule<>(QrInventoryActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkQrInventoryButton(){
        solo.assertCurrentActivity("Wrong Activity", QrInventoryActivity.class);
    }


}