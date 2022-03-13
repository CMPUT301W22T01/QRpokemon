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

    @Rule
    public ActivityTestRule<SignupActivity> rule = new ActivityTestRule<>(SignupActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Asserts that the current activity is the SignupActivity
        solo.assertCurrentActivity("Wrong Activity", SignupActivity.class);
    }

    @Test
    public void enterInputs() throws Exception{
        solo.enterText((EditText) solo.getView(R.id.et_name), "ABCD");
        solo.enterText((EditText) solo.getView(R.id.et_email), "123@321.com");
        solo.enterText((EditText) solo.getView(R.id.et_phone), "123456");

        solo.clickOnView(solo.getView(R.id.bt_submit));
        solo.waitForActivity(MyprofileActivity.class, 2000);
        solo.assertCurrentActivity("Not in MyprofileActivity", MyprofileActivity.class);
    }
}