package com.lift.u.ulift.Activities.Home;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.lift.u.ulift.Adapters.WorkoutHistoryAdapter;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.lift.u.ulift.models.HealthCards;
import com.lift.u.ulift.models.WorkoutHistory;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by balavigneshr on 10/3/15.
 */
public class CurrentActivity extends AppCompatActivity implements NumberPickerDialogFragment.NumberPickerDialogHandler  {
    ArrayList<HealthCards> cards;
    private TextView seat, hand, timer, next_exercise, previous_exercise, sets_view, timer_view, workout_history, timer_2;
    private MaterialEditText weight, reps;
    float weight_float = 0.0f;
    int reps_int = 0, i, current_exercise = 0;
    private SwipeMenuListView listView;
    ArrayList<WorkoutHistory> array_list = new ArrayList<>();
    WorkoutHistoryAdapter adapter;
    private int default_time = 45, sets_completed = 0;
    HealthCards card;
    private String routine, workout, exercise_name;
    private CountDownTimer countdowntimer;
    boolean timer_finish = true;
    private ScaleInAnimationAdapter animationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_exercise);
        current_exercise = getIntent().getIntExtra("I", 0);
        routine = getIntent().getStringExtra("ROUTINE");
        workout = getIntent().getStringExtra("WORKOUT");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            cards = bundle.getParcelableArrayList("CARDS");
        seat = (TextView) findViewById(R.id.seat);
        hand = (TextView) findViewById(R.id.hand);
        timer = (TextView) findViewById(R.id.timer);
        weight = (MaterialEditText) findViewById(R.id.weight);
        reps = (MaterialEditText) findViewById(R.id.reps);
        next_exercise = (TextView) findViewById(R.id.next_exercise);
        previous_exercise = (TextView) findViewById(R.id.previous_excercise);
        sets_view = (TextView) findViewById(R.id.sets_view);
        timer_view = (TextView) findViewById(R.id.timer_view);
        listView = (SwipeMenuListView) findViewById(R.id.list);
        workout_history = (TextView) findViewById(R.id.workout_history);
        timer_2 = (TextView) findViewById(R.id.timer_2);
        adapter = new WorkoutHistoryAdapter(this, array_list);
        animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        getValues();
        next_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cards != null && current_exercise != cards.size()) {
                    current_exercise++;
                    getValues();
                    if (!timer_finish)
                        countdowntimer.cancel();
                }
            }
        });

        previous_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cards != null && current_exercise != 0) {
                    current_exercise--;
                    getValues();
                    if (!timer_finish)
                        countdowntimer.cancel();
                }
            }
        });

        if (weight.getText() != null && !weight.getText().toString().isEmpty())
            weight_float = Float.parseFloat(weight.getText().toString());
        if (reps.getText() != null && !reps.getText().toString().isEmpty())
            reps_int = Integer.parseInt(reps.getText().toString());

        seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setLabelText("Seat Height")
                        .setPlusMinusVisibility(View.GONE)
                        .setDecimalVisibility(View.GONE)
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                npb.show();
                i = 0;
            }
        });

        hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setLabelText("Hand Angle")
                        .setPlusMinusVisibility(View.GONE)
                        .setDecimalVisibility(View.GONE)
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                npb.show();
                i = 1;
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setLabelText("Additional Rest (sec)")
                        .setPlusMinusVisibility(View.GONE)
                        .setDecimalVisibility(View.GONE)
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                npb.show();
                i = 2;
            }
        });

        weight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    NumberPickerBuilder npb = new NumberPickerBuilder()
                            .setFragmentManager(getSupportFragmentManager())
                            .setPlusMinusVisibility(View.GONE)
                            .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                    npb.show();
                    i = 3;
                }
                return false;
            }
        });

        reps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    NumberPickerBuilder npb = new NumberPickerBuilder()
                            .setFragmentManager(getSupportFragmentManager())
                            .setPlusMinusVisibility(View.GONE)
                            .setDecimalVisibility(View.GONE)
                            .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                    npb.show();
                    i = 4;
                }
                return false;
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
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

                // delete
                new SweetAlertDialog(CurrentActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Delete entry?")
                        .setContentText("Are you sure you want to delete this set?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deleteItem(position);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        switch(i){
            case 0:
                seat.setText("Seat (" + number + ")");
                break;
            case 1:
                hand.setText("Hand (" + number + ")");
                break;
            case 2:
                default_time = number;
                timer.setText("Timer (" + number + ")");
                setTimerView(number, true);
                break;
            case 3:
                weight.setText(String.valueOf(fullNumber));
                weight_float = (float) fullNumber;
                break;
            case 4:
                reps.setText(String.valueOf(number));
                reps_int = number;
            default:
                break;
        }
    }

    public void AddWeight(View v) {
        weight_float++;
        weight.setText(String.valueOf(weight_float));
    }

    public void RemoveWeight(View v) {
        if (weight_float < 0)
            weight_float--;
        else
            weight_float = 0.0f;
        weight.setText(String.valueOf(weight_float));
    }

    public void AddReps(View v) {
        reps_int++;
        reps.setText(String.valueOf(reps_int));
    }

    public void RemoveReps(View v) {
        if (reps_int != 0)
            reps_int--;
        reps.setText(String.valueOf(reps_int));
    }

    public void Clear(View v) {
        weight.getText().clear();
        reps.getText().clear();
        weight_float = 0.0f;
        reps_int = 0;
        setTimerView(default_time, true);
    }

    public void Save(View v) {
        final SlidingPaneLayout countdown = (SlidingPaneLayout) findViewById(R.id.countdown);
        if (weight_float != 0.0f && reps_int != 0) {
            WorkoutHistory set = new WorkoutHistory(reps_int, weight_float, sets_completed);
            array_list.add(set);
            adapter.notifyDataSetChanged();
            animationAdapter.notifyDataSetChanged();
            setSetsView(0);
            saveUserSet();
            if (sets_completed == card.getSets()) {
                if (current_exercise != cards.size()) {
                    final Dialog dialog = new Dialog(CurrentActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog.setContentView(R.layout.complete_set);
                    TextView nExerciseName = (TextView) dialog.findViewById(R.id.nExerciseName);
                    countdowntimer = new CountDownTimer(default_time * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            int number = (int) millisUntilFinished / 1000;
                            TextView timer_dialog = (TextView) dialog.findViewById(R.id.dialogTimer);
                            timer_dialog.setText(setTimerView(number, true));
                        }
                        public void onFinish() {
                            TextView timer_dialog = (TextView) dialog.findViewById(R.id.dialogTimer);
                            timer_dialog.setText("0:00");
                            timer_finish = true;
                        }
                    }.start();
                    nExerciseName.setText(cards.get(current_exercise + 1).getExercise_name());
                    nExerciseName.setTextColor(getResources().getIdentifier(card.getColor(), "color", getPackageName()));
                    Button recordMore = (Button) dialog.findViewById(R.id.record_more);
                    recordMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button proceedForward = (Button) dialog.findViewById(R.id.proceed_forward);
                    proceedForward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cards != null && current_exercise != cards.size()) {
                                current_exercise++;
                                getValues();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                    card.setProgress(2);
                } else if (current_exercise == cards.size()) {

                }
            } else {
                Animation previousAnim = AnimationUtils.loadAnimation(CurrentActivity.this, R.anim.bottom_up);
                final Animation nextAnim = AnimationUtils.loadAnimation(CurrentActivity.this, R.anim.bottom_down);
                countdown.startAnimation(previousAnim);
                countdown.setVisibility(View.VISIBLE);
                timer_finish = false;
                countdowntimer = new CountDownTimer(default_time * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int number = (int) millisUntilFinished / 1000;
                        setTimerView(number, true);
                    }
                    public void onFinish() {
                        countdown.startAnimation(nextAnim);
                        countdown.setVisibility(View.GONE);
                        setTimerView(default_time, false);
                        timer_finish = true;
                    }
                }.start();
            }
        }
    }

    private void setSetsView(int i) {
        switch (i) {
            case 0:
                sets_completed++;
                break;
            case 1:
                sets_completed--;
                break;
            case 2:
                sets_completed=0;
            default:
                break;
        }
        sets_view.setText("Sets: " + sets_completed + "/" + card.getSets());
    }

    private void getValues() {
        timer_finish = true;
        setTimerView(default_time, true);
        if (cards != null) {
            card = cards.get(current_exercise);
            exercise_name = card.getExercise_name();
            getSupportActionBar().setTitle(card.getExercise_name());
            setTimerView(default_time, true);
            workout_history.setBackgroundResource(getResources().getIdentifier(cards.get(current_exercise).getColor(), "color", "com.lift.u.ulift"));
            if (current_exercise == 0)
                previous_exercise.setText("");
            else
                previous_exercise.setText("< " + cards.get(current_exercise - 1).getExercise_name());
            if (current_exercise == cards.size()-1)
                next_exercise.setText("");
            else
                next_exercise.setText(cards.get(current_exercise + 1).getExercise_name() + " >");

            displayUserSet();
        }
    }

    private String setTimerView(int second, boolean all) {
        int min = second / 60;
        int sec = second % 60;
        String seconds = String.valueOf(sec);
        String minutes = String.valueOf(min);
        if (min < 10)
            minutes = "0" + min;
        if (sec < 10)
            seconds = "0" + sec;
        String time = minutes + ":" + seconds;
        timer_view.setText(time);
        if (all)
            timer_2.setText(time);
        return time;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void deleteItem(int position) {
        deleteUserSet(array_list.get(position));
        array_list.remove(position);
        adapter.notifyDataSetChanged();
        animationAdapter.notifyDataSetChanged();
        setSetsView(1);
    }

    private void deleteUserSet(WorkoutHistory set) {
        Log.e("Deleting ", set.toString());
        SQLiteDatabase wdb = new DatabaseHelper(this).getWritableDatabase();
        String selection = Tables.UserSet.ExerciseName + "= ? AND "
                + Tables.UserSet.WorkoutName + "= ? AND "
                + Tables.UserSet.RoutineName + "= ? AND "
                + Tables.UserSet.Reps + "= ? AND "
                + Tables.UserSet.Sets + "= ? AND "
                + Tables.UserSet.Weight + "= ?"
                ;
        String[] selectionArgs = {card.getExercise_name(), workout, routine, String.valueOf(set.getReps()), String.valueOf(set.getSets()), String.valueOf(set.getWeight())};
        wdb.delete(Tables.UserSet.table_name, selection, selectionArgs);
        wdb.close();
    }

    private void saveUserSet() {
        SQLiteDatabase db = (new DatabaseHelper(CurrentActivity.this)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tables.UserSet.RoutineName, routine);
        values.put(Tables.UserSet.ExerciseName, exercise_name);
        values.put(Tables.UserSet.Reps, String.valueOf(reps_int));
        values.put(Tables.UserSet.Weight, String.valueOf(weight_float));
        values.put(Tables.UserSet.Sets, String.valueOf(sets_completed));
        values.put(Tables.UserSet.WorkoutName, workout);
        values.put(Tables.UserSet.UserId, ParseUser.getCurrentUser().getObjectId());

        db.insert(
                Tables.UserSet.table_name,
                null,
                values);
        db.close();
    }

    private void displayUserSet() {
        sets_completed = 0;
        array_list.clear();
        SQLiteDatabase db = new DatabaseHelper(CurrentActivity.this).getReadableDatabase();
        String[] values = {routine, exercise_name};
        Cursor c = db.rawQuery("SELECT " + Tables.UserSet.ExerciseName + ", " + Tables.UserSet.Sets
                + ", " + Tables.UserSet.Reps + ", " + Tables.UserSet.Weight + " from " + Tables.UserSet.table_name
                + " where " + Tables.UserSet.RoutineName + "=? and " + Tables.UserSet.ExerciseName + "=?", values);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String reps = c.getString(c.getColumnIndexOrThrow(Tables.UserSet.Reps));
                String sets = c.getString(c.getColumnIndexOrThrow(Tables.UserSet.Sets));
                String weight = c.getString(c.getColumnIndexOrThrow(Tables.UserSet.Weight));
                sets_completed = sets_completed + 1;
                WorkoutHistory set = new WorkoutHistory(Integer.valueOf(reps), Float.valueOf(weight), Integer.valueOf(sets));
                array_list.add(set);
                c.moveToNext();
            }
        }
        adapter.notifyDataSetChanged();
        animationAdapter.notifyDataSetChanged();
        sets_view.setText("Sets: " + sets_completed + "/" + card.getSets());
        db.close();
    }
}
