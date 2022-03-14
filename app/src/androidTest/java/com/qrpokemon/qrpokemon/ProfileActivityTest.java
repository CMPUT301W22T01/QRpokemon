package com.qrpokemon.qrpokemon;

import android.app.Activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {

    private Solo solo;


    @Rule
    public ActivityTestRule<ProfileActivity> rule =
            new ActivityTestRule<>(ProfileActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

//    @Test
//    public void checkCreateQrCodeButton(){
//        solo.assertCurrentActivity("Wrong Activity", SignupActivity.class);
//        solo.clickOnButton("Create QRcode");
//    }


    // Currently the test will fail because it was empty in signup page
    /**
     * Check the back button of the profile page
     */
    @Test
    public void checkBackButton(){

        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.iv_back));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

}
