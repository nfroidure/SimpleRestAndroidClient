package com.elitwork.simplerestandroidclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class SimpleRestAndroidClient extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    /** New Request */
    public void newRequest(View theButton)
    {
        startActivity(new Intent(this, NewRequest.class));
    }
}
