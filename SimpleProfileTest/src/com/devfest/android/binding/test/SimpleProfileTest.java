package com.devfest.android.binding.test;

import java.util.UUID;

import com.devfest.android.binding.Profile;
import com.devfest.android.binding.ProfileActivity;
import com.devfest.android.binding.ProfileController;
import com.devfest.android.binding.R;
import com.jayway.android.robotium.solo.Solo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SimpleProfileTest extends
		ActivityInstrumentationTestCase2<ProfileActivity> {

	private Solo solo;
	private EditText firstNameET;
	private TextView firstNameTV;
	private Button saveButton;
	private Button cancelButton;
	private Activity mActivity;
	private Instrumentation mInstrumentation;

	public SimpleProfileTest() {
		super(ProfileActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mInstrumentation=getInstrumentation();
		solo = new Solo(mInstrumentation);
		mActivity=getActivity();
		firstNameET=(EditText)mActivity.findViewById(R.id.firstNameET);
		firstNameTV=(TextView)mActivity.findViewById(R.id.firstNameTV);
		saveButton=(Button)mActivity.findViewById(R.id.saveButton);
		cancelButton=(Button)mActivity.findViewById(R.id.cancelButton);
	}

	public void testSaveFirstName() {
		solo.clickOnActionBarItem(R.id.editAction);
		final String newFirstName = UUID.randomUUID().toString();
		mInstrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				firstNameET.setText(newFirstName);
				saveButton.performClick();
			}
		});
		assertEquals(newFirstName, firstNameTV.getText().toString());
		//What happen in the Model?
		ProfileController pController=new ProfileController(mActivity);
		Profile p=pController.retrieveProfile();
		assertEquals(newFirstName, p.getFirstName());
	}
	
	
	public void testCancelEdition() {
		solo.clickOnActionBarItem(R.id.editAction);
		CharSequence value=firstNameTV.getText();
		String oldFirstName=value!=null?value.toString():"";
		final String newFirstName = UUID.randomUUID().toString();
		mInstrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				firstNameET.setText(newFirstName);
				cancelButton.performClick();
			}
		});
		assertEquals(oldFirstName, firstNameTV.getText().toString());
	}

}
