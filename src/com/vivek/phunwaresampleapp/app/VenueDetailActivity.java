package com.vivek.phunwaresampleapp.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.vivek.phunwaresampleapp.R;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link VenueListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link VenueDetailFragment}.
 */
public class VenueDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			Intent intentFromListActivity=getIntent();
			String name=intentFromListActivity.getStringExtra("name");
			String url=intentFromListActivity.getStringExtra("imageurl");
			String address=intentFromListActivity.getStringExtra("address");
			String locationName=intentFromListActivity.getStringExtra("venuelocation");
			ArrayList<String> scheduleList=intentFromListActivity.getStringArrayListExtra("schedulelist");
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			
			VenueDetailFragment venueDetailFragment=VenueDetailFragment.newInstance(name, url, locationName, address,scheduleList);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.item_detail_container, venueDetailFragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this,
					new Intent(this, VenueListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
