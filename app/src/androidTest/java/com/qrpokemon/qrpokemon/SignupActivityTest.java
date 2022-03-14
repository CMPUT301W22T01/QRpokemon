package com.qrpokemon.qrpokemon;

import static org.junit.Assert.*;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.*;


import com.robotium.solo.Solo;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SignupActivityTest {
    private Solo solo;
    private DatabaseController databaseController = DatabaseController.getInstance();
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
        databaseController.deleteData("Player", "ABCD");
    }

    @Test
    public void enterInputs() throws Exception{

        solo.enterText((EditText) solo.getView(R.id.et_name), "ABCD");
        solo.enterText((EditText) solo.getView(R.id.et_email), "123@321.com");
        solo.enterText((EditText) solo.getView(R.id.et_phone), "123456");
        solo.waitForActivity(MyprofileActivity.class, 2000);
        solo.clickOnView(solo.getView(R.id.bt_submit));
        solo.assertCurrentActivity("Not in MyprofileActivity", MainActivity.class);


    }
}