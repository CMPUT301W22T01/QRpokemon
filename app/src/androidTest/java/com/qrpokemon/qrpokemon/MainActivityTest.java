package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;


    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void start() throws Exception{
        ActivityScenario scenario = rule.getScenario();
    }

//    @Test
//    public void checkQrInventoryButton(){
//        ActivityScenario scenario = rule.getScenario();
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("QR INVENTORY"); //Click on QR INVENTORY Button
//        solo.assertCurrentActivity("Wrong Activity", QrInventoryActivity.class);
//    }
//
//
//    @Test
//    public void checkClickingProfile() {
//        ActivityScenario scenario = rule.getScenario();
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnImage(1);
//        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
//
//    }
//
//    @Test
//    public void checkMapButton() {
//        ActivityScenario scenario = rule.getScenario();
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("MAP"); //Click on MAP Button
//        solo.assertCurrentActivity("Wrong Activity", MapActivity.class);
//    }
//
//    @Test
//    public void checkSearchButton() {
//        ActivityScenario scenario = rule.getScenario();
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("SEARCH"); //Click on SEARCH Button
//        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
//    }
//
//    @Test
//    public void checkLeaderboardButton() {
//        ActivityScenario scenario = rule.getScenario();
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("LEADERBOARD"); //Click on LEADERBOARD Button
//        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
//    }
//

}
