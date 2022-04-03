package com.qrpokemon.qrpokemon;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.views.owner.OwnerActivity;
import com.qrpokemon.qrpokemon.views.qrinventory.QrInventoryActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OwnerActivityTest {
    private Solo solo;
    private PlayerController playerController = PlayerController.getInstance();

//    @Override

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<> (MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkQrInventoryButton() {
        solo.assertCurrentActivity("Wrong Activity", QrInventoryActivity.class);
    }
}
