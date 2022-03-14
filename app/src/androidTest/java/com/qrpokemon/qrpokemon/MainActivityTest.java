package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        FileSystemController fileSystemController = new FileSystemController();
        fileSystemController.writeToFile(solo.getCurrentActivity(), "firstRun", null);
        fileSystemController.writeToFile(solo.getCurrentActivity(), "name", "Bob");
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkQrInventoryButton(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.QR_Inventory_Button)); //Click on QR INVENTORY Button
        solo.assertCurrentActivity("Wrong Activity", QrInventoryActivity.class);
    }


    @Test
    public void checkClickingProfile() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.avatar_imageView));;
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

    }

    @Test
    public void checkMapButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Map_Button)); //Click on MAP Button
        solo.assertCurrentActivity("Wrong Activity", MapActivity.class);
    }

//    @Test
//    public void checkSearchButton() {
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("SEARCH"); //Click on SEARCH Button
//        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
//    }

    @Test
    public void checkLeaderboardButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Leaderboard_Button));//Click on LEADERBOARD Button
        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
    }

    @After
    public void cleanUp(){
//        FileSystemController fileSystemController = new FileSystemController();
//        fileSystemController.deleteFile(solo.getCurrentActivity());
    }
}
