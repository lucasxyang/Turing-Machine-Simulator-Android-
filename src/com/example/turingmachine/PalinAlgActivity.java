package com.example.turingmachine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PalinAlgActivity extends Activity {

	String inputString = "";
	String input2 = "";
	int arrowFirstIndex;
	int arrowLastIndex;
	int cellsIndex = 0;
	int firstIndex = 0;
	int copyOfFirstIndex = 0;
	int lastIndex = 0;
	int arrowFirstCopy;
	int arrowLastCopy;
	int counter = 0;
	int EnteredSuccess1 = 0;
	int EnteredSuccess2 = 0;
	EditText[] cells = new EditText[12];
	EditText[] arrowcells = new EditText[12];
	boolean head = true;
	List<String> middleArray = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_palindrome_alg);
		final EditText input = (EditText) findViewById(R.id.input);
		Button bt = (Button) findViewById(R.id.stepButton);
		TextView instru = (TextView) findViewById(R.id.instruView);
		bt.setEnabled(false);
		instru.setText("Press the 'Set' button to load numbers");

		// create a EditText array to store each tape cell
		int ID;
		for (int i = 0; i < 12; i++) {
			ID = getResources().getIdentifier("cell" + i, "id",
					getPackageName());
			cells[i] = (EditText) findViewById(ID);
		}

		// create a EditText array to store arrow cells
		for (int i = 0; i < 12; i++) {
			ID = getResources().getIdentifier("arrow" + i, "id",
					getPackageName());
			arrowcells[i] = (EditText) findViewById(ID);
		}

		// set button listener to get user input into the tape
		Button loadInput = (Button) findViewById(R.id.setInput);
		loadInput.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				inputString = input.getText().toString();
				input2 = inputString;
				char[] inputArray = inputString.toCharArray();
				for (int i = 0; i < inputArray.length; i++) {
					middleArray.add(Character.toString(inputArray[i]));
				}
				cellsIndex = (12 - inputArray.length) / 2;
				firstIndex = (12 - inputArray.length) / 2;
				copyOfFirstIndex = (12 - inputArray.length) / 2;
				lastIndex = copyOfFirstIndex + inputString.length() - 1;
				arrowFirstIndex = firstIndex;
				arrowLastIndex = lastIndex + 1;
				counter = 0;
				EnteredSuccess1 = 0;
				EnteredSuccess2 = 0;
				// no input situation
				Button bt = (Button) findViewById(R.id.stepButton);
				TextView instru = (TextView) findViewById(R.id.instruView);
				if ((input2).matches("")) {
					bt.setEnabled(false);
				}
				// update the tape
				for (int j = 0; j < 12; j++) {
					cells[j].setText("");
				}

				// read input into the tape
				for (int i = 0; i < inputArray.length; i++, cellsIndex++) {
					cells[(cellsIndex)].setText(Character
							.toString(inputArray[i]));
				}
				head = true;

				// reset arrow cells
				for (int i = 0; i < 12; i++) {
					arrowcells[i].setBackgroundResource(0);
				}
				TextView tv = (TextView) findViewById(R.id.resultView);
				if (!(input2.matches(""))) {
					bt.setEnabled(true);
					instru.setText("Press the 'Step' button to see results");
				}
				tv.setText("");
			}
		});

	}

	char temp1;
	char temp2;

	public void stepAct(View view) {

		Button bt = (Button) findViewById(R.id.stepButton);
		TextView tv = (TextView) findViewById(R.id.resultView);
		bt.setEnabled(true);
		// left to right
		if (head) {
			if (arrowFirstIndex != 0) {
				arrowcells[arrowFirstIndex - 1].setBackgroundResource(0);
			}
			temp1 = input2.charAt(0);

			// mark start with and move arrow from left to right
			if (arrowFirstIndex <= arrowLastIndex && EnteredSuccess1 == 0) {
				if (arrowFirstIndex == firstIndex + 1) {
					cells[firstIndex].setText("X");
				}
				arrowcells[arrowFirstIndex].setBackgroundResource(R.drawable.arrow);
				if (arrowFirstIndex != 0) {
					arrowcells[arrowFirstIndex - 1].setBackgroundResource(0);
				}
				arrowFirstIndex++;
				head = true;
			}
			if (arrowFirstIndex > arrowLastIndex && EnteredSuccess1 == 0) {
				counter++;
				arrowLastIndex--;
				if (counter == 1) {
					arrowFirstIndex = copyOfFirstIndex;
				}
				if (counter != 1) {
					arrowFirstIndex = firstIndex;
				}
				head = false;
			}
			// success condition
			if (input2.length() == 1) {
				EnteredSuccess1++;
				if (EnteredSuccess1 == 1) {
					arrowcells[firstIndex].setBackgroundResource(R.drawable.arrow);
					arrowcells[firstIndex + 1].setBackgroundResource(0);
				} else if (EnteredSuccess1 == 2) {
					cells[firstIndex].setText("X");
					arrowcells[firstIndex].setBackgroundResource(0);
					arrowcells[firstIndex + 1]
							.setBackgroundResource(R.drawable.arrow);
				} else {
					arrowcells[firstIndex]
							.setBackgroundResource(R.drawable.arrow);
					arrowcells[firstIndex + 1].setBackgroundResource(0);
					tv.setText("Success!");
					bt.setEnabled(false);
				}
				head = true;
			}
		}
		// right to left
		else {
			arrowcells[arrowLastIndex + 1].setBackgroundResource(0);
			if (input2.length() != 0) {
				temp2 = input2.charAt(input2.length() - 1);
			}
			// mark end with X and move arrow from right to left
			if (temp1 == (temp2)) {
				if (arrowLastIndex >= arrowFirstIndex && EnteredSuccess2 == 0) {
					if (arrowLastIndex == lastIndex - 1) {
						cells[lastIndex].setText("X");
					}
					arrowcells[arrowLastIndex].setBackgroundResource(R.drawable.arrow);
					arrowcells[arrowLastIndex + 1].setBackgroundResource(0);
					arrowLastIndex--;
				}
				if (arrowLastIndex < arrowFirstIndex && input2.length() > 1
						&& EnteredSuccess2 == 0) {
					input2 = input2.substring(1, input2.length() - 1);
					firstIndex++;
					lastIndex--;
					arrowLastIndex--;
					counter++;
					arrowLastIndex = lastIndex + 1;
					arrowFirstIndex++;
					head = true;
				}
				// success condition
				if (input2.length() == 0) {
					EnteredSuccess2++;
					if (EnteredSuccess2 == 2) {
						arrowcells[lastIndex].setBackgroundResource(0);
						arrowcells[lastIndex + 1]
								.setBackgroundResource(R.drawable.arrow);
						bt.setEnabled(false);
						tv.setText("Success!");
					}
					head = false;
				}
			} else {
				bt.setEnabled(false);
				tv.setText("Halt: the input is not a palindrome");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_palindrome_alg, menu);
		return true;
	}
}
