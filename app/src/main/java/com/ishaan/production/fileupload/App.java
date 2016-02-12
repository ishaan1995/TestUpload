package com.ishaan.production.fileupload;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by ishaan on 18/1/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // [Optional] Power your app with Local Datastore. For more info, go to
// https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);

        //Parse.initialize(this);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                        .applicationId("myAppId")

                        .server("http://192.168.1.108:1337/parse/")

                        .build()

        );
    }
}
