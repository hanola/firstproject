package no.example.firstproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {
	private static final String TAG = "no.example.firstproject";
	
	//creates variabel funny funny to store data from "saved" textedit
	private EditText editBox;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");
        //EditBox set to textedit "saved"
        editBox =(EditText)findViewById(R.id.saved);
    }
	
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
	}
	
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		//Retrives data from bundle object (memory) when app restarts. 
		SharedPreferences prefs = getPreferences(0); 
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            editBox.setText(restoredText, TextView.BufferType.EDITABLE);
 
            int selectionStart = prefs.getInt("selection-start", -1);
            int selectionEnd = prefs.getInt("selection-end", -1);
            if (selectionStart != -1 && selectionEnd != -1) {
                editBox.setSelection(selectionStart, selectionEnd);
            }
        }
	}
	
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		//Data from textedit "saved" saved in Bundle (memory)
		 SharedPreferences.Editor editor = getPreferences(0).edit();
	        editor.putString("text", editBox.getText().toString());
	        editor.putInt("selection-start", editBox.getSelectionStart());
	        editor.putInt("selection-end", editBox.getSelectionEnd());
	        editor.commit();
		
		
	}
	
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}
	
	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "onRestart");
	}
	
	protected void onDestory() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}
	
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
		
		//final EditText textBox = (EditText) findViewById(R.id.editText4);
		//CharSequence userText = textBox.getText();
		//outState.putCharSequence("savedText", userText);
	}
	
	protected void onRestoreInstanceState(Bundle savedState){
		//super.onRestoreInstanceState(savedState);
		Log.i(TAG, "onRestoreInstanceState");
		
		//final EditText textBox = (EditText) findViewById(R.id.editText4);
		//CharSequence userText = savedState.getCharSequence("savedText");
		
		//textBox.setText(userText);
		
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