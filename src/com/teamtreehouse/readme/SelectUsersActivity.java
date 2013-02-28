package com.teamtreehouse.readme;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.RoboSherlockListActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class SelectUsersActivity extends RoboSherlockListActivity {
	
	public static final String TAG = SelectUsersActivity.class.getSimpleName();
	
	protected ParseObject[] mUsers;
	
	@InjectView (R.id.progressBar1) protected ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_users);
		
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		
		getAllUsers();
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
	
	private void getAllUsers() {
		mProgressBar.setVisibility(View.VISIBLE);
		

    	ParseQuery query = ParseUser.getQuery();
    	query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				mProgressBar.setVisibility(View.INVISIBLE);

				if (e == null) {
					objects = removeCurrentUser(objects);					
					mUsers = objects.toArray(new ParseObject[0]);
					
					// Get user relations
					ParseRelation userRelations = ParseUser.getCurrentUser().getRelation("UserRelation");
					userRelations.getQuery().findInBackground(new FindCallback() {
						public void done(List<ParseObject> results, ParseException e) {
							if (e == null) {
								UsersAdapter adapter = new UsersAdapter(SelectUsersActivity.this, mUsers, new ArrayList<ParseObject>(results));
								setListAdapter(adapter);
						    }
							else {
								Log.e(TAG, "Exception caught!", e);
						    }
						}
					});				
				}
				else {
					// Something went wrong.
					Toast.makeText(SelectUsersActivity.this, "Sorry, there was an error getting users!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private List<ParseObject> removeCurrentUser(List<ParseObject> objects) {
		ParseObject userToRemove = null;
		for (ParseObject user : objects) {
			if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
				userToRemove = user;
			}
		}
		
		if (userToRemove != null) {
			objects.remove(userToRemove);
		}

		return objects;
	}
}
