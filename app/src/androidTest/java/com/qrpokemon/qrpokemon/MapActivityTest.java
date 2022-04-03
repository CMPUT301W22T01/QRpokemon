package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.qrpokemon.qrpokemon.views.map.MapActivity;
import com.qrpokemon.qrpokemon.views.search.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

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
     * Check the back button of the map page
     */
    @Test
    public void checkBackButton(){
        solo.clickOnView(solo.getView(R.id.Map_Button));
        solo.waitForActivity(MapActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", MapActivity.class);

        solo.clickOnView(solo.getView(R.id.button));
        solo.waitForActivity(MapActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

}
