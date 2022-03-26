package com.qrpokemon.qrpokemon;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<SearchActivity> rule =
            new ActivityTestRule<>(SearchActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    @Test
    public void checkBackButton(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Search_Button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.clickOnView(solo.getView(R.id.button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
    @Test
    public void playerSearch() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Search_Button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.enterText((EditText) solo.getView(R.id.sv), "Heather");
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.clickOnView(solo.getView(R.id.button));
        solo.assertCurrentActivity("Not in SearchActivity", MainActivity.class);
    }
    @Test
    public void locationSearch() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Search_Button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.sv), "Edmonton");
        solo.waitForActivity(SearchActivity.class, 2000);
        ListView listview = (ListView) rule.getActivity().findViewById(R.id.search_listview);
        assertThat((String)listview.getItemAtPosition(0), is("Edmonton"));
    }
}
