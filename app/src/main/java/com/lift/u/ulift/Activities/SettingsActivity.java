package com.lift.u.ulift.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lift.u.ulift.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

/**
 * Created by balavigneshr on 10/12/15.
 */
public class SettingsActivity extends AppCompatActivity {
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        getSupportActionBar().setTitle(R.string.settings);
        buildDrawer();
        ListView lv = (ListView) findViewById(R.id.listSettings);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_settings_item);
        lv.setAdapter(adapter);
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
                        case 1:
                            intent = new Intent(getApplicationContext(), ProfileActivity.class);
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
