package com.lift.u.ulift;

import com.parse.Parse;

/**
 * Created by balavigneshr on 8/24/15.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, getResources().getString(R.string.parse_app_id), getResources().getString(R.string.parse_client_key));
    }
}
