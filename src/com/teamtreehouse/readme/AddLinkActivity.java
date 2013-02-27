package com.teamtreehouse.readme;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.RoboSherlockActivity;
import com.parse.ParseObject;

public class AddLinkActivity extends RoboSherlockActivity {
	
	public static final String KEY_URL = "url";
	public static final String KEY_NOTES = "notes";
	public static final String POSTS = "Post";

	@InjectView (R.id.editText1) protected EditText mUrlField;
	@InjectView (R.id.editText2) protected EditText mNotesField;		
	@InjectView (R.id.button1) protected Button mSaveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_link);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		mSaveButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String url = mUrlField.getText().toString();
				String notes = mNotesField.getText().toString();
				
				if (!url.equals("")) {
					ParseObject post = new ParseObject(POSTS);
					post.put(KEY_URL, url);
					post.put(KEY_NOTES, notes);
					post.saveInBackground();
					finish();
				}
			}
		});
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case android.R.id.home:
        		finish();
        		break;
        	default:
        		return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
