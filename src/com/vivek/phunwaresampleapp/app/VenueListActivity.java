package com.vivek.phunwaresampleapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.squareup.otto.Subscribe;
import com.vivek.phunwaresampleapp.R;
import com.vivek.phunwaresampleapp.utils.EventBus;
import com.vivek.phunwaresampleapp.utils.Venue;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link VenueDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link VenueListFragment} and the item details (if present) is a
 * {@link VenueDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link VenueListFragment.Callbacks} interface to listen for item selections.
 */
public class VenueListActivity extends ActionBarActivity implements
		VenueListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;


		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Subscribe
	public void onVenueClicked(Venue venue) {
		if (!mTwoPane) {
			Intent detailIntent = new Intent(this, VenueDetailActivity.class);
			detailIntent.putExtra("imageurl", venue.getImageUrl());
			detailIntent.putExtra("name", venue.getName());
			detailIntent.putExtra("venuelocation", venue.getState() + " "
					+ venue.getCity() + " " + venue.getZip());
			detailIntent.putExtra("address", venue.getAddress());
			startActivity(detailIntent);
		} else {
			VenueDetailFragment venueDetailFragment = VenueDetailFragment
					.newInstance(venue.getName(), venue.getImageUrl(),
							venue.getState() + " "
									+ venue.getCity() + " " + venue.getZip(), venue.getAddress());
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, venueDetailFragment).commit();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		EventBus.getInstance().register(this);
	}

	@Override
	public void onPause() {
		EventBus.getInstance().unregister(this);
		super.onPause();
	}

	/**
	 * Callback method from {@link VenueListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			// dont do anything, the venue
			Bundle arguments = new Bundle();
			arguments.putString(VenueDetailFragment.ARG_ITEM_ID, id);
			VenueDetailFragment fragment = new VenueDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, VenueDetailActivity.class);
			detailIntent.putExtra(VenueDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
