package com.devfest.android.binding;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class ProfileActivity extends Activity implements OnClickListener {

	private TextView firstNameTV;
	private TextView lastNameTV;
	private TextView emailTV;
	private TextView addressTV;
	private EditText firstNameET;
	private EditText lastNameET;
	private EditText emailET;
	private EditText addressET;
	private Button saveButton;
	private Button cancelButton;
	private LinearLayout saveButtonContainer;

	private Profile mProfile;
	private Profile newProfile;
	private ProfileController mProfilecontroller;

	private boolean editing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		firstNameTV = (TextView) findViewById(R.id.firstNameTV);
		lastNameTV = (TextView) findViewById(R.id.lastNameTV);
		emailTV = (TextView) findViewById(R.id.emailTV);
		addressTV = (TextView) findViewById(R.id.addressTV);
		firstNameET = (EditText) findViewById(R.id.firstNameET);
		lastNameET = (EditText) findViewById(R.id.lastNameET);
		emailET = (EditText) findViewById(R.id.emailET);
		addressET = (EditText) findViewById(R.id.addressET);
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButtonContainer = (LinearLayout) findViewById(R.id.saveButtonContainer);
		saveButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
		mProfilecontroller = new ProfileController(this);
		mProfile = mProfilecontroller.retrieveProfile();
		updateFields();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!editing) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.profile, menu);
			return true;
		} else {
			return super.onCreateOptionsMenu(menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.editAction:
			startEdition();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateFields() {
		if (editing) {
			firstNameTV.setVisibility(View.GONE);
			lastNameTV.setVisibility(View.GONE);
			emailTV.setVisibility(View.GONE);
			addressTV.setVisibility(View.GONE);
			firstNameET.setVisibility(View.VISIBLE);
			lastNameET.setVisibility(View.VISIBLE);
			emailET.setVisibility(View.VISIBLE);
			addressET.setVisibility(View.VISIBLE);
			if (mProfile != null) {
				firstNameET.setText(mProfile.getFirstName());
				lastNameET.setText(mProfile.getLastName());
				emailET.setText(mProfile.getEmail());
				addressET.setText(mProfile.getAddress());
			}
		} else {
			firstNameTV.setVisibility(View.VISIBLE);
			lastNameTV.setVisibility(View.VISIBLE);
			emailTV.setVisibility(View.VISIBLE);
			addressTV.setVisibility(View.VISIBLE);
			firstNameET.setVisibility(View.GONE);
			lastNameET.setVisibility(View.GONE);
			emailET.setVisibility(View.GONE);
			addressET.setVisibility(View.GONE);
			if (mProfile != null) {
				firstNameTV.setText(mProfile.getFirstName());
				lastNameTV.setText(mProfile.getLastName());
				emailTV.setText(mProfile.getEmail());
				addressTV.setText(mProfile.getAddress());
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == saveButton) {
			Editable newEntry=firstNameET.getText();
			newProfile.setFirstName(newEntry!=null?newEntry.toString():null);
			newEntry=lastNameET.getText();
			newProfile.setLastName(newEntry!=null?newEntry.toString():null);
			newEntry=emailET.getText();
			newProfile.setEmail(newEntry!=null?newEntry.toString():null);
			newEntry=addressET.getText();
			newProfile.setAddress(newEntry!=null?newEntry.toString():null);
			mProfilecontroller.saveProfile(newProfile);
			mProfile = newProfile;
		}
		finishEdition();
	}

	@Override
	public void onBackPressed() {
		if (editing) {
			finishEdition();
		} else {
			super.onBackPressed();
		}
	}

	private void finishEdition() {
		editing = false;
		saveButtonContainer.setVisibility(View.GONE);
		updateFields();
		invalidateOptionsMenu();
	}

	private void startEdition() {
		editing = true;
		newProfile = new Profile(mProfile);
		saveButtonContainer.setVisibility(View.VISIBLE);
		updateFields();
		invalidateOptionsMenu();
	}

}
