package com.lift.u.ulift.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.lift.u.ulift.DBObjects.DBConstants;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by balavigneshr on 8/28/15.
 */
public class AddRoutineInfo extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    MaterialEditText level, color, type;
    MaterialEditText routine_name;
    MaterialEditText summary;
    MaterialEditText desc;
    MaterialEditText goal;
    MaterialEditText duration;
    String routineName;
    boolean edit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_routine);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        routine_name = (MaterialEditText) findViewById(R.id.routine_name);
        summary = (MaterialEditText) findViewById(R.id.summary);
        desc = (MaterialEditText) findViewById(R.id.desc);
        goal = (MaterialEditText) findViewById(R.id.goal);
        duration = (MaterialEditText) findViewById(R.id.duration);
        level = (MaterialEditText) findViewById(R.id.level);
        color = (MaterialEditText) findViewById(R.id.color);
        type = (MaterialEditText) findViewById(R.id.type);

        if (getIntent().hasExtra("ROUTINE_NAME")) {
            routineName = getIntent().getStringExtra("ROUTINE_NAME");
            if (routineName != null || !routineName.isEmpty()) {
                getAndSetValues();
                edit = true;
            }
        }

        level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheet.createBuilder(getApplicationContext(), getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles(getResources().getString(R.string.beginners), getResources().getString(R.string.intermediate), getResources().getString(R.string.advanced), getResources().getString(R.string.monster))
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean b) {
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                                String level_text = "";
                                switch (i) {
                                    case 0:
                                        level_text = getResources().getString(R.string.beginners);
                                        break;
                                    case 1:
                                        level_text = getResources().getString(R.string.intermediate);
                                        break;
                                    case 2:
                                        level_text = getResources().getString(R.string.advanced);
                                        break;
                                    case 3:
                                        level_text = getResources().getString(R.string.monster);
                                        break;
                                }
                                level.setText(level_text);
                            }
                        }).show();
            }
        });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheet.createBuilder(getApplicationContext(), getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles(getResources().getString(R.string.red), getResources().getString(R.string.blue), getResources().getString(R.string.orange),
                                getResources().getString(R.string.pink), getResources().getString(R.string.magenta), getResources().getString(R.string.brown),
                                getResources().getString(R.string.coffee), getResources().getString(R.string.maroon), getResources().getString(R.string.mint))
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean b) {
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                                String color_text = "";
                                switch (i) {
                                    case 0:
                                        color_text = getResources().getString(R.string.red);
                                        break;
                                    case 1:
                                        color_text = getResources().getString(R.string.blue);
                                        break;
                                    case 2:
                                        color_text = getResources().getString(R.string.orange);
                                        break;
                                    case 3:
                                        color_text = getResources().getString(R.string.pink);
                                        break;
                                    case 4:
                                        color_text = getResources().getString(R.string.magenta);
                                        break;
                                    case 5:
                                        color_text = getResources().getString(R.string.brown);
                                        break;
                                    case 6:
                                        color_text = getResources().getString(R.string.coffee);
                                        break;
                                    case 7:
                                        color_text = getResources().getString(R.string.maroon);
                                        break;
                                    case 8:
                                        color_text = getResources().getString(R.string.mint);
                                        break;
                                }
                                color.setText(color_text);
                            }
                        }).show();
            }
        });
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheet.createBuilder(getApplicationContext(), getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles(getResources().getString(R.string.strength), getResources().getString(R.string.bodybuilding), getResources().getString(R.string.sports))
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean b) {
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                                String type_text = "";
                                switch (i) {
                                    case 0:
                                        type_text = getResources().getString(R.string.strength);
                                        break;
                                    case 1:
                                        type_text = getResources().getString(R.string.bodybuilding);
                                        break;
                                    case 2:
                                        type_text = getResources().getString(R.string.sports);
                                        break;
                                }
                                type.setText(type_text);
                            }
                        }).show();
            }
        });

    }

    public void onSave(View v) throws InterruptedException{
        if (routine_name.getText() == null || routine_name.getText().toString().trim().length() == 0)
            routine_name.setError("Routine name required");
        else {
            String routine_text = routine_name.getText().toString().trim();
            String summary_str = summary.getText().toString();
            String des_str = desc.getText().toString();
            String goal_str = goal.getText().toString();
            String duration_str = duration.getText().toString();
            String color_str = color.getText().toString();
            String level_str = level.getText().toString();
            String type_str = type.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] args = new String[]{routine_text,
                    summary_str, des_str, goal_str, duration_str, color_str, level_str, type_str};
//                    isValid(summary), isValid(desc), isValid(goal), isValid(duration),
//                    isValid(color), isValid(level), isValid(type)};

            SQLiteDatabase rdb = dbHelper.getReadableDatabase();
            if (!edit) {
                Cursor cursor = rdb.rawQuery(DBConstants.get_count(Tables.RoutineInformation.routineName,
                        Tables.RoutineInformation.table_name, routine_text), null);

                if (cursor.getCount() == 0) {
                    db.execSQL(DBConstants.insert_routine, args);
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText("New Routine Created!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent routine = new Intent(getApplicationContext(), RoutineActivity.class);
                                    startActivity(routine);
                                }
                            })
                            .show();
                } else {
                    routine_name.setError("Routine already exists.");
                }
            } else {
//                ContentValues values = new ContentValues();
//                values.put(Tables.RoutineInformation.routineName, routine_text);
//
//                // Which row to update, based on the ID
//                String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
//                String[] selectionArgs = { String.valueOf(rowId) };
//
//                int count = db.update(
//                        FeedReaderDbHelper.FeedEntry.TABLE_NAME,
//                        values,
//                        selection,
//                        selectionArgs);
//                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Success")
//                        .setContentText("Routine Updated Successfully!")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                Intent routine = new Intent(getApplicationContext(), RoutineActivity.class);
//                                startActivity(routine);
//                            }
//                        })
//                        .show();
            }
        }
    }

    private void getAndSetValues() {
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String[] values = {routineName};
        Cursor c = db.rawQuery("SELECT " + Tables.RoutineInformation.summary + ", " + Tables.RoutineInformation.desc
                + ", " + Tables.RoutineInformation.goal + " from " + Tables.RoutineInformation.duration
                + ", " + Tables.RoutineInformation.color + ", " + Tables.RoutineInformation.type + " from " + Tables.RoutineInformation.level
                + " where " + Tables.RoutineInformation.routineName + "=?", values);
        c.moveToFirst();
        routine_name.setText(routineName);
        summary.setText(c.getString(c.getColumnIndexOrThrow(Tables.RoutineInformation.summary)));
        goal.setText(c.getString(c.getColumnIndexOrThrow(Tables.RoutineInformation.goal)));
        desc.setText(c.getString(c.getColumnIndexOrThrow(Tables.RoutineInformation.desc)));
        color.setText(c.getString(c.getColumnIndexOrThrow(Tables.RoutineInformation.color)));
        level.setText(c.getString(c.getColumnIndexOrThrow(Tables.RoutineInformation.level)));
        type.setText(c.getString(c.getColumnIndexOrThrow(Tables.RoutineInformation.type)));
        duration.setText(c.getString(c.getColumnIndexOrThrow(Tables.RoutineInformation.duration)));
    }

    private String isValid(MaterialEditText field) {
        String value = " ";
        if (field.getText() != null || field.getText().toString().trim().length() > 0)
            value = field.getText().toString();
        return value;
    }
}