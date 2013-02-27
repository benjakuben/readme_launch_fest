package com.teamtreehouse.readme;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "PmugPeajRehwPsVg1Q13zwm7MdPgeY7qklzDhxFT", "EMRq0inaQQm3B1mepq5hDUt3KlijzlYmjyQvwV58"); 

		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
