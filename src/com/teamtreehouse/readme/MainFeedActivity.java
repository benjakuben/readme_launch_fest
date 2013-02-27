package com.teamtreehouse.readme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.RoboSherlockListActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class MainFeedActivity extends RoboSherlockListActivity {

	public static final String TAG = MainFeedActivity.class.getSimpleName();
	
	@InjectView (R.id.progressBar1) protected ProgressBar mProgressBar;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getLatestPosts();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    TextView urlLabel = (TextView) v.findViewById(android.R.id.text1);
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.setData(Uri.parse(urlLabel.getText().toString()));
	    startActivity(intent);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.activity_main_list, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.addButton:
        		startActivity(new Intent(this, AddLinkActivity.class));
        		return true;
            case R.id.followButton:
            	startActivity(new Intent(this, SelectUsersActivity.class));
                return true;
            case R.id.logoutButton:
            	ParseUser.logOut();
            	Intent intent = new Intent(this, LoginOrSignupActivity.class);
            	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    protected void getLatestPosts() {
    	mProgressBar.setVisibility(View.VISIBLE);
    	
    	ParseQuery query = new ParseQuery(AddLinkActivity.POSTS);
    	query.findInBackground(new FindCallback() {
    		public void done(List<ParseObject> results, ParseException e) {
    	    	mProgressBar.setVisibility(View.INVISIBLE);
    			if (e == null) {
    				ArrayList<HashMap<String, String>> articles = new ArrayList<HashMap<String,String>>();
    				for(ParseObject result : results) {
    					HashMap<String, String> article = new HashMap<String, String>();
    					article.put(AddLinkActivity.KEY_NOTES, result.getString(AddLinkActivity.KEY_NOTES));
    					article.put(AddLinkActivity.KEY_URL, result.getString(AddLinkActivity.KEY_URL));
    					articles.add(article);
    				}
    				SimpleAdapter adapter = new SimpleAdapter(MainFeedActivity.this, articles,
    						android.R.layout.simple_list_item_2,
    						new String[] { AddLinkActivity.KEY_NOTES, AddLinkActivity.KEY_URL },
    						new int[] { android.R.id.text1, android.R.id.text2 });
					setListAdapter(adapter);
	    	    } 
    			else {
	    	    	Log.e(TAG, "Exception caught!", e);
	    	    }
    		}
    	});
    }
}