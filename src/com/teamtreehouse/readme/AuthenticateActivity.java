package com.teamtreehouse.readme;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.RoboSherlockActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class AuthenticateActivity extends RoboSherlockActivity {
	
	protected String mAction;
	
	@InjectView (R.id.editText1) protected EditText mEmailField;
	@InjectView (R.id.editText2) protected EditText mPasswordField;
	@InjectView (R.id.button1) protected Button mButton;
	@InjectView (R.id.progressBar1) protected ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate);
		
		ActionBar actionBar = getSupportActionBar();
		
		Bundle bundle = getIntent().getExtras();
		mAction = bundle.getString(LoginOrSignupActivity.TYPE);
		
		if (mAction.equals(LoginOrSignupActivity.SIGNUP)) {
			mButton.setText(mAction);
			actionBar.setTitle(mAction);
		}
				
		mButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				mProgressBar.setVisibility(View.VISIBLE);
				
				String username = mEmailField.getText().toString();
				String password = mPasswordField.getText().toString();
				
				if (mAction.equals(LoginOrSignupActivity.SIGNUP)) {
					ParseUser user = new ParseUser();
					user.setUsername(username);
					user.setPassword(password);
					 			 
					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							mProgressBar.setVisibility(View.INVISIBLE);
							if (e == null) {
								// Hooray! Let them use the app now.
								startActivity(new Intent(AuthenticateActivity.this, MainFeedActivity.class));
							} else {
								// Sign up didn't succeed. Look at the ParseException to figure out what went wrong
								Toast.makeText(AuthenticateActivity.this, "Sign up failed! Try again.", Toast.LENGTH_LONG).show();
							}
						}
					});
				}
				else {
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						public void done(ParseUser user, ParseException e) {
							mProgressBar.setVisibility(View.INVISIBLE);
							if (user != null) {
								// Hooray! The user is logged in.
								startActivity(new Intent(AuthenticateActivity.this, MainFeedActivity.class));
						    } else {
						    	// Login failed. Look at the ParseException to see what happened.
								Toast.makeText(AuthenticateActivity.this, "Login failed! Try again.", Toast.LENGTH_LONG).show();
						    }
						}
					});
				}
			}
		});
	}

}
