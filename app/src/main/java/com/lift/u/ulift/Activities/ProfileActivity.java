package com.lift.u.ulift.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lift.u.ulift.R;
import com.parse.ParseUser;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

/**
 * Created by balavigneshr on 8/24/15.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        getSupportActionBar().setTitle(R.string.profile);
        buildDrawer();
    }

    public void logout(View v) {
        ParseUser.logOut();
        this.deleteDatabase("uLift");
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

    public void buildDrawer() {
        final ResideMenu resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.orange);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.attachToActivity(this);
        String titles[] = { getResources().getString(R.string.home), getResources().getString(R.string.profile), getResources().getString(R.string.analyze),
                getResources().getString(R.string.routines), getResources().getString(R.string.settings) };
        int icon[] = { R.drawable.home, R.drawable.profile, R.drawable.analyze, R.drawable.routine, R.drawable.settings };
        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            final int j = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    switch(j) {
                        case 0:
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            break;
                        case 3:
                            intent = new Intent(getApplicationContext(), RoutineActivity.class);
                            break;
                    }
                    if (intent != null) {
                        resideMenu.closeMenu();
                        startActivity(intent);
                    }
                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT);
        }
    }
}
