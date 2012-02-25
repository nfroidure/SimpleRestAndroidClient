package com.elitwork.simplerestandroidclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    if(req.execute("",""))
		{
		int i = req.getResponseCode();
		if ((i >= 200) || (i < 300))
		Toast.makeText(this, req.getResponseContent(), 0).show();
		}
	else
		{
		Toast.makeText(this, "Unable to execute the request", 0).show();
		}
    finish();
  }
}

/* Location:           /home/nfroidure/Bureau/apk/dex2jar-0.0.9.7/classes-dex2jar.jar
 * Qualified Name:     fr.ecogom.vigisystem.Connect
 * JD-Core Version:    0.6.0
 */
