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

import com.qrpokemon.qrpokemon.views.search.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchActivityTest {
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
     * Check the back button of the search page
     */
    @Test
    public void checkBackButton(){
        solo.clickOnView(solo.getView(R.id.Search_Button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);

        solo.clickOnView(solo.getView(R.id.button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Check the if search for player works
     */
    @Test
    public void playerSearch() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Search_Button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.sv), "Krista");
        solo.waitForActivity(SearchActivity.class, 200);
        solo.waitForText("Krista", 2, 3000);
        solo.clickOnView(solo.getView(R.id.search_button));
        solo.waitForActivity(SearchActivity.class, 2000);

        ListView listview = (ListView) solo.getView(R.id.search_listview);
        View view = listview.getAdapter().getView(0, null, listview);
        TextView tv = view.findViewById(R.id.tv);
        assertEquals(tv.getText(), "Krista");
    }


    /**
     * Check the if search for location works
     */
    @Test
    public void locationSearch() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Search_Button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.sv), "Calgary");
        solo.waitForText("Calgary", 1, 2000);
        solo.clickOnView(solo.getView(R.id.search_button));
        solo.waitForActivity(SearchActivity.class, 2000);

        ListView listview = (ListView) solo.getView(R.id.search_listview);
        View view = listview.getAdapter().getView(0, null, listview);
        solo.clickOnView(view);
        solo.waitForText("7D75F8FA97CA6D7C7", 1, 5000);
        assertTrue(listview.getCount() > 0);
    }


    /**
     * Check the if partial search works
     */
    @Test
    public void partialSearch() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.Search_Button));
        solo.waitForActivity(SearchActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.sv), "Calg");
        solo.waitForActivity(SearchActivity.class, 200);
        solo.clickOnView(solo.getView(R.id.search_button));
        solo.waitForActivity(SearchActivity.class, 2000);

        ListView listview = (ListView) solo.getView(R.id.search_listview);
        View view = listview.getAdapter().getView(0, null, listview);
        TextView tv = view.findViewById(R.id.tv);
        assertEquals(tv.getText(), "Calgary");
    }
//    @Test
//    public void testShouldShowTheItemDetailWhenAnItemIsClicked() throws Exception {
//        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
//        final ListView listview = (ListView) rule.getActivity().findViewById(R.id.search_listview);
//
//        instrumentation.runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                int position = 0;
//                listview.performItemClick(listview.getChildAt(position), position, listview.getAdapter().getItemId(position));
//            }
//        });
//
//        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(SearchActivity.class.getName(), null, false);
//        Activity itemDetailActivity = instrumentation.waitForMonitorWithTimeout(monitor, 5000);
//
//        TextView detailView = (TextView) itemDetailActivity.findViewById(R.id.search_listview);
//        assertThat(detailView.getText().toString(), is("7D75F8FA97CA6D7C7"));
//    }
}
