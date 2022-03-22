package com.qrpokemon.qrpokemon;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QrInventoryActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<QrInventoryActivity> rule =
            new ActivityTestRule<>(QrInventoryActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkBackButton() {
        // go to Qr Inventory Activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.QR_Inventory_Button));
        solo.waitForActivity(QrInventoryActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", QrInventoryActivity.class);

        // click back button
        solo.clickOnView(solo.getView(R.id.bt_back));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void checkDeleteButton() {
        // go to Qr Inventory Activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.QR_Inventory_Button));
        solo.waitForActivity(QrInventoryActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", QrInventoryActivity.class);

        //
        solo.clickInList(1);
    }
}
