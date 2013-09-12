package com.vivek.phunwaresampleapp.app;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.vivek.phunwaresampleapp.R;
import com.vivek.phunwaresampleapp.utils.EventBus;

/**
 * A fragment representing a single Venue detail screen. This fragment is either
 * contained in a {@link VenueListActivity} in two-pane mode (on tablets) or a
 * {@link VenueDetailActivity} on handsets.
 */
public class VenueDetailFragment extends Fragment {
	
	private ImageView mImageView;
	private TextView mVenueName, mVenueAddress, mVenueLocation;
	private AQuery mAquery;
	//the share action provider for sharing the address of teh venue
	private ShareActionProvider mShareActionProvider;
	//preset image to be shown before image loads
	private Bitmap mImageLoading;
	//the container in which the schedules are added as text views
	private LinearLayout mContainer;
	

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public VenueDetailFragment() {
	}

	/**
	 * Takes the params sent and puts it into a bundle so that they can be accessed and displayed
	 * @param name
	 * @param url
	 * @param location
	 * @param address
	 * @param scheduleList
	 * @return venueDetailFragment
	 */
	public static VenueDetailFragment newInstance(String name, String url,
			String location, String address, ArrayList<String> scheduleList) {
		VenueDetailFragment venueDetailFragment = new VenueDetailFragment();

		Bundle args = new Bundle();
		args.putString("name", name);
		args.putString("imageurl", url);
		args.putString("address", address);
		args.putString("location", location);
		args.putStringArrayList("scheduleList", scheduleList);
		venueDetailFragment.setArguments(args);
		
		return venueDetailFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Set has options menu to true so that we can add share action provider to the action bar
		 */
		setHasOptionsMenu(true);
		mImageLoading = BitmapFactory.decodeResource(getActivity()
				.getResources(), R.drawable.imageloading);

	}
	
	/**
	 * The getter for the name set in the bundle
	 * @return
	 */
	public String getName() {
		Bundle bundle = getArguments();
		String name = bundle.getString("name");
		return name;
	}

	public String getImageUrl() {
		Bundle bundle = getArguments();
		String imageUrl = bundle.getString("imageurl");
		return imageUrl;
	}

	public String getAddress() {
		Bundle bundle = getArguments();
		String address = bundle.getString("address");
		return address;
	}

	public String getLocation() {
		Bundle bundle = getArguments();
		String location = bundle.getString("location");
		return location;
	}
	
	public ArrayList<String> getScheduleList() {
		Bundle bundle = getArguments();
		ArrayList<String> scheduleList = bundle.getStringArrayList("scheduleList");
		return scheduleList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
		Log.v("vivekqw", "1212132");
		mAquery = new AQuery(getActivity(), rootView);
		mImageView = (ImageView) rootView.findViewById(R.id.imageview);
		mVenueName = (TextView) rootView.findViewById(R.id.venuedetailname);
		mVenueAddress = (TextView) rootView
				.findViewById(R.id.venuedetailaddress);
		mVenueLocation = (TextView) rootView
				.findViewById(R.id.venuedetaillocation);
		mContainer=(LinearLayout)rootView.findViewById(R.id.thecontainer);
		return rootView;

	}

	/*
	 * @Subscribe public void onVenueClicked(Venue venue) {
	 * Log.v("vivek","received event"); String url=venue.getImageUrl();
	 * 
	 * mAquery.id(mImageView).image( url, true, true, 200, 0, null, 0,
	 * AQuery.RATIO_PRESERVE); mVenueAddress.setText(venue.getAddress());
	 * mVenueName.setText(venue.getName());
	 * mVenueLocation.setText(venue.getState
	 * ()+" "+venue.getCity()+" "+venue.getZip()); }
	 */

	@Override
	public void onResume() {
		super.onResume();
		EventBus.getInstance().register(this);

		mAquery.id(mImageView).image(getImageUrl(), true, true, 200,
				R.drawable.imagenotfound, mImageLoading, 0,
				AQuery.RATIO_PRESERVE);
		mVenueAddress.setText(getAddress());
		mVenueName.setText(getName());
		mVenueLocation.setText(getLocation());
		LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		for(String scheduleString:getScheduleList()){
			TextView schedItem=new TextView(getActivity());
			schedItem.setLayoutParams(layoutParams);
			schedItem.setPadding(12, 10, 0, 0);
			schedItem.setTextSize(15.0f);
			schedItem.setText(scheduleString);
			mContainer.addView(schedItem);
		}
		

	}

	@Override
	public void onPause() {
		EventBus.getInstance().unregister(this);
		super.onPause();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
		super.onCreateOptionsMenu(menu, menuInflater);
		menuInflater.inflate(R.menu.shareaction, menu);
		MenuItem shareItem = menu.findItem(R.id.shareactionitem);
		mShareActionProvider = (ShareActionProvider) MenuItemCompat
				.getActionProvider(shareItem);
		mShareActionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		mShareActionProvider.setShareIntent(getDefaultIntent());
	}

	private Intent getDefaultIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "Please come to " + mVenueName
				+ " " + mVenueAddress + " " + mVenueLocation);
		return shareIntent;
	}

}
