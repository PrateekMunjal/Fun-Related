package com.firstandroidapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
	SharedPreferences sp;
	//ImageView pic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.setApplicationId(APP_ID);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.firstandroidapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));


            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        sp = getPreferences(MODE_PRIVATE);
        String access_token = sp.getString("access_token",null);
        long expires = sp.getLong("access_expires",0);
        
        if(access_token!=null)
        {
        	fb.setAccessToken(access_token);
        }
        if(expires!=0)
        {
        	fb.setAccessExpires(expires);
        }
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
				public void showPic()
				{
					ImageView img ;
					img = (ImageView)findViewById(R.id.ImgView);
					img.setVisibility(View.VISIBLE);
				}
				
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
					Editor editor = sp.edit();
					editor.putString("access_token",fb.getAccessToken());
					editor.putLong("access_expires",fb.getAccessExpires());
					editor.commit();
					showPic();
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