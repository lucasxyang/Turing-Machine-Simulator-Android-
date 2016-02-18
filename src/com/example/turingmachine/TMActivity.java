package com.example.turingmachine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;

public class TMActivity extends Activity {


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void AlgRadioButtonClicked(View view){
    	boolean checked = ((RadioButton) view).isChecked();
    	Intent intent;
    	   switch(view.getId()) {
           case R.id.unaryAddition:
               if (checked){
               intent = new Intent(this, UnaryAddActivity.class);
               startActivity(intent);
               }
               break;
           case R.id.palindrome:
               if (checked){
               intent = new Intent(this, PalinAlgActivity.class);
               startActivity(intent);
               }
               break;
           case R.id.AnBn:
               if (checked){
               intent = new Intent(this, AnBnActivity.class);
               startActivity(intent);
               }
               break;
       }
    	
    	
    }
}
