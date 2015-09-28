package com.lift.u.ulift.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by balavigneshr on 8/26/15.
 */
public class RoutineActivity extends AppCompatActivity {
    LinearLayout no_routine;
    SwipeMenuListView listView;
    LinkedHashMap<String, String> routine_list = new LinkedHashMap<>();
    ArrayAdapter<String> itemsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_activity);
        no_routine = (LinearLayout) findViewById(R.id.no_routine_text);
        SharedPreferences settings = this.getSharedPreferences("Routine", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        getSupportActionBar().setTitle(R.string.routines);
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        buildDrawer();
        getRoutines();
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                editItem.setWidth(dp2px(90));
                editItem.setTitle("EDIT");
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                swipeMenu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(R.color.error_red);
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("DELETE");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                swipeMenu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // edit
                        Intent intent = new Intent(RoutineActivity.this, AddRoutineInfo.class);
                        intent.putExtra("ROUTINE_NAME", routine_list.get(position));
                        startActivity(intent);
                        break;
                    case 1:
                        // delete
                        new SweetAlertDialog(RoutineActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Won't be able to recover this!")
                                .setConfirmText("Yes,delete it.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        deleteItem(position);
                                        sweetAlertDialog
                                                .setTitleText("Deleted!")
                                                .showCancelButton(false)
                                                .setContentText("Your routine has been deleted!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                })
                                .setCancelText("No,keep it.")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>(routine_list.keySet())) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                if (routine_list.size() != 0) {
                    textView.setHeight(150);
                    String color = new ArrayList<>(routine_list.values()).get(position);
                    if (!color.isEmpty()) {
                        Resources resources = RoutineActivity.this.getResources();
                        textView.setBackgroundColor(resources.getColor(resources.getIdentifier(color, "color", getApplicationContext().getPackageName())));
                        textView.setTextColor(resources.getColor(R.color.white));
                    }
                }
                return textView;
            }
        };
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                    long id) {
                                                String routine_name = (String) parent.getItemAtPosition(position);
                                                String color = new ArrayList<>(routine_list.values()).get(position);
                                                SharedPreferences pref = getSharedPreferences("Routine", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor edit = pref.edit();
                                                edit.putString("ROUTINE_NAME", routine_name);
                                                edit.putString("COLOR", color);
                                                edit.commit();
                                                Intent intent = new Intent(getBaseContext(), WorkoutActivity.class);
                                                startActivity(intent);
                                            }
                                        }
        );
        if (routine_list.size() > 0)
            no_routine.setVisibility(View.GONE);
    }

    private void deleteItem(int position) {
        SQLiteDatabase wdb = new DatabaseHelper(this).getWritableDatabase();
        String selection = Tables.RoutineInformation.routineName + "= ?";
        String routine = new ArrayList<>(routine_list.keySet()).get(position);
        String[] selectionArgs = {routine};
        wdb.delete(Tables.RoutineInformation.table_name, selection, selectionArgs);
        wdb.delete(Tables.UserWorkout.table_name, selection, selectionArgs);
        routine_list.remove(new ArrayList<>(routine_list.keySet()).get(position));
        itemsAdapter.remove(itemsAdapter.getItem(position));
        itemsAdapter.notifyDataSetChanged();
        if (routine_list.size() == 0)
            no_routine.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_only, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(this, AddRoutineInfo.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void buildDrawer() {
        final ResideMenu resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.orange);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
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
                    switch (j) {
                        case 0:
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            break;
                        case 1:
                            intent = new Intent(getApplicationContext(), ProfileActivity.class);
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

    private void getRoutines() {
        SQLiteDatabase rdb = (new DatabaseHelper(this)).getReadableDatabase();
        String[] projection = {Tables.RoutineInformation.routineName, Tables.RoutineInformation.color};
        Cursor c = rdb.rawQuery("SELECT " + projection[0] + ", " + projection[1] + " from " + Tables.RoutineInformation.table_name + ";", null);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i ++) {
                routine_list.put(c.getString(c.getColumnIndexOrThrow(projection[0])),
                        c.getString(c.getColumnIndexOrThrow(projection[1])));
                c.moveToNext();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
