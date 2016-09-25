package com.sampi.luigirusso.thebookhunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class UserActivity extends BaseNavActivity {
    private Toolbar toolbar;
    private String user;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        super.setState(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreateDrawer();

        //Get username from Start Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            Log.i(String.valueOf(Log.INFO), "extras from startActivity: "+user);
            username = user.split(",")[1];

        }

        TextView toolbarText = (TextView) findViewById(R.id.mainToolbarBookUser);
        toolbarText.setText(username);
    }
}
