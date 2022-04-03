package com.qrpokemon.qrpokemon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.qrpokemon.qrpokemon.views.profile.ProfileActivity;
import com.qrpokemon.qrpokemon.views.search.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {

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
     * Check US 03.02.01 of the profile page
     */
    @Test
    public void checkCreateQrCodeButton(){

        solo.clickOnView(solo.getView(R.id.avatar_imageView));
        solo.waitForActivity(ProfileActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnButton("Create QRcode");

        ImageView imageView = (ImageView) solo.getView(R.id.QR_profile_Iv);
        assertNotNull(imageView.getDrawable());

    }


    /**
     * Check the back button of the profile page
     */
    @Test
    public void checkBackButton(){

        solo.clickOnView(solo.getView(R.id.avatar_imageView));
        solo.waitForActivity(ProfileActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        solo.clickOnView(solo.getView(R.id.profile_back));
        solo.waitForActivity(ProfileActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Check US 03.02.01 of the profile page
     */
    @Test
    public void checkUserName(){

        solo.waitForActivity(MainActivity.class, 2000);
        TextView userNameInMain = (TextView) solo.getView(R.id.user_textView);

        solo.clickOnView(solo.getView(R.id.avatar_imageView));
        solo.waitForActivity(ProfileActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        TextView userNameInProfile = (TextView) solo.getView(R.id.tv_name);

        assertEquals(userNameInMain.getText(), userNameInProfile.getText());
    }



}
