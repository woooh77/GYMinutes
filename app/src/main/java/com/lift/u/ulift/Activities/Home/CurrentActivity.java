package com.lift.u.ulift.Activities.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.lift.u.ulift.Adapters.WorkoutHistoryAdapter;
import com.lift.u.ulift.R;
import com.lift.u.ulift.models.HealthCards;
import com.lift.u.ulift.models.WorkoutHistory;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by balavigneshr on 10/3/15.
 */
public class CurrentActivity extends AppCompatActivity implements NumberPickerDialogFragment.NumberPickerDialogHandler  {
    ArrayList<HealthCards> cards;
    private TextView seat, hand, timer, next_exercise, previous_exercise, sets_view, timer_view, workout_history;
    private MaterialEditText weight, sets;
    float weight_float = 0.0f;
    int sets_int = 0, i, current_exercise = 0;
    private SwipeMenuListView listView;
    ArrayList<WorkoutHistory> array_list = new ArrayList<>();
    WorkoutHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_exercise);
        current_exercise = getIntent().getIntExtra("I", 0);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            cards = bundle.getParcelableArrayList("CARDS");
        seat = (TextView) findViewById(R.id.seat);
        hand = (TextView) findViewById(R.id.hand);
        timer = (TextView) findViewById(R.id.timer);
        weight = (MaterialEditText) findViewById(R.id.weight);
        sets = (MaterialEditText) findViewById(R.id.sets);
        next_exercise = (TextView) findViewById(R.id.next_exercise);
        previous_exercise = (TextView) findViewById(R.id.previous_excercise);
        sets_view = (TextView) findViewById(R.id.sets_view);
        timer_view = (TextView) findViewById(R.id.timer_view);
        listView = (SwipeMenuListView) findViewById(R.id.list);
        workout_history = (TextView) findViewById(R.id.workout_history);

        adapter = new WorkoutHistoryAdapter(this, array_list);
        listView.setAdapter(adapter);

        getValues();
        next_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cards != null && current_exercise != cards.size()) {
                    current_exercise++;
                    getValues();
                }
            }
        });

        previous_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cards != null && current_exercise != 0) {
                    current_exercise--;
                    getValues();
                }
            }
        });

        if (weight.getText() != null && !weight.getText().toString().isEmpty())
            weight_float = Float.parseFloat(weight.getText().toString());
        if (sets.getText() != null && !sets.getText().toString().isEmpty())
            sets_int = Integer.parseInt(sets.getText().toString());

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

        sets.setOnTouchListener(new View.OnTouchListener() {
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
                int min = number / 60;
                int sec = number % 60;
                timer.setText("Timer (" + number + ")");
                timer_view.setText(min + ":" + sec);
                break;
            case 3:
                weight.setText(String.valueOf(fullNumber));
                weight_float = (float) fullNumber;
                break;
            case 4:
                sets.setText(String.valueOf(number));
                sets_int = number;
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

    public void AddSets(View v) {
        sets_int++;
        sets.setText(String.valueOf(sets_int));
    }

    public void RemoveSets(View v) {
        if (sets_int != 0)
            sets_int--;
        sets.setText(String.valueOf(sets_int));
    }

    public void Clear(View v) {
        weight.getText().clear();
        sets.getText().clear();
        weight_float = 0.0f;
        sets_int = 0;
    }

    public void Save(View v) {
        if (weight_float != 0.0f && sets_int != 0) {
            array_list.add(new WorkoutHistory(weight_float, sets_int, 0, 0, 0));
            adapter.notifyDataSetChanged();
        }
    }

    private void getValues() {
        if (cards != null) {
            HealthCards card = cards.get(current_exercise);
            getSupportActionBar().setTitle(card.getWorkout());
            sets_view.setText("Sets: 0/" + card.getSets());
            timer_view.setText("00:45");
            workout_history.setBackgroundResource(getResources().getIdentifier(cards.get(current_exercise).getColor(), "color", "com.lift.u.ulift"));
            if (current_exercise == 0)
                previous_exercise.setText("");
            else
                previous_exercise.setText("< " + cards.get(current_exercise - 1).getWorkout());
            if (current_exercise == cards.size()-1)
                next_exercise.setText("");
            else
                next_exercise.setText(cards.get(current_exercise + 1).getWorkout() + " >");
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void deleteItem(int position) {
        array_list.remove(position);
        adapter.notifyDataSetChanged();
    }
}
