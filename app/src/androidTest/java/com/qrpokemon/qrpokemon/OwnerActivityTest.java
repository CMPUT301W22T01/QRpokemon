package com.qrpokemon.qrpokemon;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.qrpokemon.qrpokemon.controllers.PlayerController;
import com.qrpokemon.qrpokemon.views.owner.OwnerActivity;
import com.qrpokemon.qrpokemon.views.qrinventory.QrInventoryActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

// this test will only work when the current player is the owner
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
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Check the back button of the owner page
     */
    @Test
    public void checkBackButton(){
        solo.clickOnView(solo.getView(R.id.admin_Button));
        solo.waitForActivity(OwnerActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", OwnerActivity.class);

        solo.clickOnView(solo.getView(R.id.owner_backBtn));
        solo.waitForActivity(OwnerActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", OwnerActivity.class);
    }

    /**
     * Check the if the player button works
     */
    @Test
    public void checkPlayerButton() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.admin_Button));
        solo.waitForActivity(OwnerActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", OwnerActivity.class);

        solo.clickOnView(solo.getView(R.id.player_button));
        solo.waitForActivity(OwnerActivity.class, 2000);

        RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.owner_recycler);
        solo.waitForActivity(OwnerActivity.class, 2000);
        assertNotNull(recyclerView.getAdapter().getItemCount());
    }

    /**
     * Check the if the code button works
     */
    @Test
    public void checkCodeButton() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.admin_Button));
        solo.waitForActivity(OwnerActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", OwnerActivity.class);

        solo.clickOnView(solo.getView(R.id.code_button));
        solo.waitForActivity(OwnerActivity.class, 10000);

        RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.owner_recycler);
        solo.waitForActivity(OwnerActivity.class, 2000);
        assertNotNull(recyclerView.getAdapter().getItemCount());
    }

}

