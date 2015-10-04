package com.lift.u.ulift.Activities.Home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lift.u.ulift.R;
import com.lift.u.ulift.models.HealthCards;

import java.util.ArrayList;

/**
 * Created by balavigneshr on 10/3/15.
 */
public class CurrentActivity extends AppCompatActivity {
    ArrayList<HealthCards> cards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_exercise);

        Bundle bundle = getIntent().getExtras();
        cards = bundle.getParcelableArrayList("CARDS");
    }
}
