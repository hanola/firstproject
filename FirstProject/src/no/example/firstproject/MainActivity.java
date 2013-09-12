package no.example.firstproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
       //Actions when pressing a button
		
	//Go to download page
	public void goToDownloadDemo(View view) {
	    Intent intent = new Intent(this, DownloadMainActivity.class);
	    startActivity(intent);
        }
	//Go to Sqlite page
	public void goToSqlDemo(View view) {
	    Intent intent = new Intent(this, SqlActivity.class);
	    startActivity(intent);
	    } 
	//go to Google map page
	public void goToGoogleMapDemo(View view) {
	    Intent intent = new Intent(this, GooglemapActivity.class);
	    startActivity(intent);
	    }
}