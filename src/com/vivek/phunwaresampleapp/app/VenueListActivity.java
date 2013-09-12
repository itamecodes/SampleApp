package com.vivek.phunwaresampleapp.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;
import com.vivek.phunwaresampleapp.R;
import com.vivek.phunwaresampleapp.utils.DataReceivedSignal;
import com.vivek.phunwaresampleapp.utils.EventBus;
import com.vivek.phunwaresampleapp.utils.ScheduleItem;
import com.vivek.phunwaresampleapp.utils.Venue;

/**
 * An activity representing a list of Venues. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of venues, which when touched, lead to a
 * {@link VenueDetailActivity} representing venue details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of venues is a
 * {@link VenueListFragment} and the item details (if present) is a
 * {@link VenueDetailFragment}.
 * <p>
 */
public class VenueListActivity extends ActionBarActivity{

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	/**
	 * the progress bar in our activity layout
	 */
	private ProgressBar mProgBar;
	/**
	 * an array of days of week to cater to Calendar.DAY_OF_WEEK
	 */
	private String[] mDaysOfWeek = { "Sunday","Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday" };
	/**
	 * An array to get the string represntations of Calendar.AM_PM
	 */
	private String[] mAmPm = { "am", "pm" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		if(savedInstanceState==null){
			mProgBar = (ProgressBar) findViewById(R.id.progressBarcelebrity);
			mProgBar.setVisibility(View.VISIBLE);
		}
		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;
		}
	}

	/**
	 * Receives the signal from event bus that the data has been loaded from the server and remove the progressbar
	 * @param dataReceived
	 */
	@Subscribe
	public void onDataLoaded(DataReceivedSignal dataReceived) {
		mProgBar.setVisibility(View.GONE);
	}

	/**
	 * Receives the event click sent by the event bus, along with the venue which has been clicked
	 * @param venue
	 */
	@Subscribe
	public void onVenueClicked(Venue venue) {
		if (!mTwoPane) {
			/**
			 * if this is not a two pane layout put the required details to be displayed in an intent and send to the 
			 * actovity hosting the detailfragment
			 */
			Intent detailIntent = new Intent(this, VenueDetailActivity.class);
			detailIntent.putExtra("imageurl", venue.getImageUrl());
			detailIntent.putExtra("name", venue.getName());
			detailIntent.putExtra("venuelocation", venue.getState() + " "
					+ venue.getCity() + " " + venue.getZip());
			detailIntent.putExtra("address", venue.getAddress());
			detailIntent.putStringArrayListExtra("schedulelist", getScheduleStringArray(venue.getSchedule()));
			startActivity(detailIntent);
		} else {
			/**
			 * if it is a two pane layout get a new instance of the frgamnet and replace it into the container
			 */
			VenueDetailFragment venueDetailFragment = VenueDetailFragment
					.newInstance(venue.getName(), venue.getImageUrl(),
							venue.getState() + " "
									+ venue.getCity() + " " + venue.getZip(), venue.getAddress(),getScheduleStringArray(venue.getSchedule()));
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, venueDetailFragment).commit();
		}

	}
	
	/**
	 * Register the event bus
	 */
	@Override
	public void onResume() {
		super.onResume();
		EventBus.getInstance().register(this);
	}

	/**
	 * Unregister the event bus
	 */
	@Override
	public void onPause() {
		EventBus.getInstance().unregister(this);
		super.onPause();
	}

	/**
	 * A helper function to get the string array to display the schedules
	 * @param scheduleList
	 * @return schedulearray which contains the string of all schedules
	 */
	private ArrayList<String> getScheduleStringArray(List<ScheduleItem> scheduleList){
		Calendar cal = Calendar.getInstance();
		ArrayList<String> scheduleArray=new ArrayList<String>();
		for(ScheduleItem scheduleItem:scheduleList){
			Date startDate=ScheduleItem.convertToDefaultTimeZOne(scheduleItem.getStartDate());
			Date endDate=ScheduleItem.convertToDefaultTimeZOne(scheduleItem.getEndDate());
			cal.setTime(startDate);
		    int month = cal.get(Calendar.MONTH);
		    int date = cal.get(Calendar.DAY_OF_MONTH);
		    int day=cal.get(Calendar.DAY_OF_WEEK);
		    
		    int hourstart=cal.get(Calendar.HOUR);
		    int minutestart=cal.get(Calendar.MINUTE);
		    int ampmstart=cal.get(Calendar.AM_PM);
		    
		    cal.setTime(endDate);
		    int hourend=cal.get(Calendar.HOUR);
		    int minuteend=cal.get(Calendar.MINUTE);
		    int ampmend=cal.get(Calendar.AM_PM);
		    
		    String dayString=mDaysOfWeek[day-1];
		    String displayString=dayString+" "+(month+1)+"/"+date+" "+hourstart+":"+minutestart+""+mAmPm[ampmstart]+" to "+hourend+":"+minuteend+""+mAmPm[ampmend];
		    scheduleArray.add(displayString);
			
		}
		return scheduleArray;
	}
}
