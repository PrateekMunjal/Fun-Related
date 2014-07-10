package com.firstandroidapp;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Settings;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class MainActivity extends Activity {
	String APP_ID = "409704949167512";
	Facebook fb = new Facebook(APP_ID);
	//ImageView pic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.setApplicationId(APP_ID);
        //pic = (ImageView)findViewById(R.id.Profile_pic);
	}
	public void login(View v)
	{
		if(fb.isSessionValid())
		{
			//logout
			try {
				fb.logout(getApplicationContext());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//login
			fb.authorize(MainActivity.this,new DialogListener()
			{
				
				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this,"ON COMPLETE",Toast.LENGTH_SHORT).show();
						
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this,"ON CANCEL",Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		fb.authorizeCallback(requestCode, resultCode, data);
	}	
	
}