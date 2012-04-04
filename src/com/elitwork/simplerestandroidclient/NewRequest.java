package com.elitwork.simplerestandroidclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.io.IOException;

public class NewRequest extends Activity
{

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.newrequest);
  }

  public void processRequest(View theButton)
  {
    //Object localObject = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    //String str1 = ((SharedPreferences)localObject).getString("username", "");
    //String str2 = ((SharedPreferences)localObject).getString("password", "");
    RestRequest req= new RestRequest("http://www.elitwork.com.ewk/robots.txt", RestRequest.HTTP_GET);
	try
		{
		int i=req.execute("","");
		if ((i >= 200) || (i < 300))
			Toast.makeText(this, req.getResponseContent(), 0).show();
		}
	catch(IOException e)
			{
			Toast.makeText(this, "Unable to execute the request", 0).show();
			e.printStackTrace();
			}
	finish();
  }
}
