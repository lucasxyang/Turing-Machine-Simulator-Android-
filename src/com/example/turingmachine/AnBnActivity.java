package com.example.turingmachine;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AnBnActivity extends Activity {

	private static final int MAX_LENGTH = 12;

	// input string
	String inputString = "";
	// remaining string
	// RENAME
	String input2 = "";

	// index of (arrow from the left)
	int arrowFirstIndex;
	// index of (arrow from the right)
	int arrowLastIndex;
	// later assign equal to firstIndex;
	// used to increment
	int cellsIndex = 0;

	// index of (cell from the left)
	int firstIndex = 0;
	// index of (cell from the right)
	int lastIndex = 0;

	int copyOfFirstIndex = 0;

	int arrowFirstCopy;
	int arrowLastCopy;
	
	// ???
	int counter = 0;

	// a left-right controller which keeps record on number of overruns
	int EnteredSuccess1 = 0;
	int EnteredSuccess2 = 0;

	// start from left
	boolean head = true;

	// array of square cells
	EditText[] cells = new EditText[MAX_LENGTH];
	// array of arrows
	EditText[] arrowcells = new EditText[MAX_LENGTH];
	// used to make the input at center
	private List<String> middleArray = new ArrayList<String>();

	// first char of inputString (later assignment)
	private char temp1;
	// last char of inputString (later assignment)
	private char temp2;

	// message to be shown when halt
	private final static Spanned HALT_MSG = Html
			.fromHtml("Halt: the input is not recognizable as a string of type "
					+ " \"A to the nth power B to the nth power\" "
					+ "(A<sup>n</sup>B<sup>n</sup>)");

	// message to be shown when success
	private final static Spanned SUCCEED_MSG = Html
			.fromHtml("Success: the input is recognized as a string of type "
					+ " \"A to the nth power B to the nth power\" "
					+ "(A<sup>n</sup>B<sup>n</sup>)"
					+ "<br>Note: This lang looks like Reg Lang while it is not");


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_an_bn);

		final EditText input = (EditText) findViewById(R.id.input);
		Button bt = (Button) findViewById(R.id.stepButton);
		TextView instru = (TextView) findViewById(R.id.instruView);
		bt.setEnabled(false);
		instru.setText("Press the 'Set' button to load numbers");

		// create a EditText array to store each tape cell
		int ID;
		for (int i = 0; i < MAX_LENGTH; i++) {
			ID = getResources().getIdentifier("cell" + i, "id",
					getPackageName());
			cells[i] = (EditText) findViewById(ID);
		}

		// create a EditText array to store arrow cells
		for (int i = 0; i < MAX_LENGTH; i++) {
			ID = getResources().getIdentifier("arrow" + i, "id",
					getPackageName());
			arrowcells[i] = (EditText) findViewById(ID);
		}

		// set button listener to get user input onto the tape
		Button loadInput = (Button) findViewById(R.id.setInput);
		loadInput.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				inputString = input.getText().toString();
				input2 = inputString;
				char[] inputArray = inputString.toCharArray();
				for (int i = 0; i < inputArray.length; i++) {
					middleArray.add(Character.toString(inputArray[i]));
				}
				cellsIndex = (MAX_LENGTH - inputArray.length) / 2;
				firstIndex = (MAX_LENGTH - inputArray.length) / 2;
				copyOfFirstIndex = (MAX_LENGTH - inputArray.length) / 2;
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
				for (int j = 0; j < MAX_LENGTH; j++) {
					cells[j].setText("");
				}

				// read input into the tape
				for (int i = 0; i < inputArray.length; i++, cellsIndex++) {
					cells[(cellsIndex)].setText(Character
							.toString(inputArray[i]));
				}
				head = true;

				// reset arrow cells
				for (int i = 0; i < MAX_LENGTH; i++) {
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

	public void stepAct(View view) {
		Button btn = (Button) findViewById(R.id.stepButton);
		btn.setEnabled(true);
		TextView tv = (TextView) findViewById(R.id.resultView);
		tv.setTextSize(15);
		final char op = inputString.charAt(0);
		final char ed = inputString.charAt(inputString.length() - 1);

		// left to right
		if (head) {
			// Remove the arrow on its left
			// except for when the index of first arrow is 0
			if (arrowFirstIndex != 0) {
				arrowcells[arrowFirstIndex - 1].setBackgroundResource(0);
			}
			// get the first char of remaining string
			temp1 = input2.charAt(0);
			// if left doesn't overrun, and not succeed yet,
			// mark start with X and move arrow from left to right
			if (arrowFirstIndex <= arrowLastIndex && EnteredSuccess1 == 0) {
				// mark X on current cellIndex. DO NOT update firstIndex (of cells)
				if (arrowFirstIndex == firstIndex + 1) {
					cells[firstIndex].setText("X");
				}
				// show arrow on current arrowIndex
				arrowcells[arrowFirstIndex].setBackgroundResource(R.drawable.arrow);
				// remove the arrow on its left
				if (arrowFirstIndex != 0) {
					arrowcells[arrowFirstIndex - 1].setBackgroundResource(0);
				}
				// update arrowIndex
				arrowFirstIndex++;
				head = true;
			}
			
			
			// if left overrun, and not succeed yet
			// then will start from tail
			if (arrowFirstIndex > arrowLastIndex && EnteredSuccess1 == 0) {
				// ???
				counter++;
				// move (arrowIndex of right a bit left)
				arrowLastIndex--;
				// ???
				if (counter == 1) {
					arrowFirstIndex = copyOfFirstIndex;
				}
				if (counter != 1) {
					arrowFirstIndex = firstIndex;
				}
				head = false;
			}
			
			
			
			// success condition
			// if length of remained is 1
			// we have to show overrun step on screen

			if (input2.length() == 1) {
				EnteredSuccess1++;
				// coming from right ? then go back to left side
				if (EnteredSuccess1 == 1) {
					arrowcells[firstIndex].setBackgroundResource(R.drawable.arrow);
					arrowcells[firstIndex + 1].setBackgroundResource(0);
				}
				// coming from left ? then overrun
				else if (EnteredSuccess1 == 2) {
					cells[firstIndex].setText("X");
					arrowcells[firstIndex].setBackgroundResource(0);
					arrowcells[firstIndex + 1].setBackgroundResource(R.drawable.arrow);
				}
				// have reached to the end of remaining string ? then
				else {
					arrowcells[firstIndex].setBackgroundResource(R.drawable.arrow);
					arrowcells[firstIndex + 1].setBackgroundResource(0);

					btn.setEnabled(false);
//					tv.setTextSize(15);
					tv.setText(HALT_MSG);
				}
				head = true;
			}
			
		}
		
		// right to left
		else {
			// remove the arrow on its right
			arrowcells[arrowLastIndex + 1].setBackgroundResource(0);
			// if: remained is longer than 0
			// then: take the last char
			if (input2.length() != 0) {
				temp2 = input2.charAt(input2.length() - 1);
			}
			// mark end with X and move arrow from right to left
			if( (temp1 == op) && (temp2 == ed) ){
				if (arrowLastIndex >= arrowFirstIndex && EnteredSuccess2 == 0) {
					if (arrowLastIndex == lastIndex - 1) {
						cells[lastIndex].setText("X");
					}
					arrowcells[arrowLastIndex].setBackgroundResource(R.drawable.arrow);
					arrowcells[arrowLastIndex + 1].setBackgroundResource(0);
					arrowLastIndex--;
				}
				if (arrowLastIndex < arrowFirstIndex && input2.length() > 1	&& EnteredSuccess2 == 0) {
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
						btn.setEnabled(false);
//						tv.setText("Success!");
						tv.setText(SUCCEED_MSG);
					}
					head = false;
				}
			} else {
				btn.setEnabled(false);
				tv.setText(HALT_MSG);
			}
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_an_bn, menu);
        return true;
    }
}
