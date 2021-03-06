package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.qrpokemon.qrpokemon.views.profile.ProfileActivity;
import com.qrpokemon.qrpokemon.controllers.DatabaseProxy;
import com.robotium.solo.Solo;

import org.junit.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SignupActivityTest {
    private Solo solo;
    private DatabaseProxy databaseProxy = DatabaseProxy.getInstance();
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

        // Asserts that the current activity is the SignupActivity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        databaseProxy.deleteData("Player", "Heather");
    }

    /**
     * Check if the user story US 03.01.01 is working properly
     * check if the sign up page successfully added a player
     * check if the information entered in sign up page is the same as the display in profile
     */
    @Test
    public void testUS_03_01_01() throws Exception{

        databaseProxy.deleteData("Player", "Heather");
        // enter user information in signup page
        solo.enterText((EditText) solo.getView(R.id.et_name), "Heather");
        solo.enterText((EditText) solo.getView(R.id.et_email), "123@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.et_phone), "7777777");
        solo.clickOnView(solo.getView(R.id.bt_submit));
        solo.waitForActivity(MainActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // go to profile activity
        solo.clickOnView(solo.getView(R.id.avatar_imageView));
        solo.waitForActivity(ProfileActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        // check the profile name
        TextView textViewName = (TextView)solo.getView(R.id.tv_name);
        String displayName = textViewName.getText().toString();
        assertEquals("Heather", displayName);
        // check the profile email
        TextView textViewEmail = (TextView)solo.getView(R.id.tv_email);
        String displayEmail = textViewEmail.getText().toString();
        assertEquals("123@gmail.com", displayEmail);
        // check the profile phone number
        TextView textViewPhone = (TextView)solo.getView(R.id.tv_phone);
        String displayPhone = textViewPhone.getText().toString();
        assertEquals("7777777", displayPhone);
        // generate qr code for the current player
        solo.clickOnButton("Create QRcode");
        // press back button to go back to main menu
        solo.clickOnView(solo.getView(R.id.profile_back));
        solo.waitForActivity(MainActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
}