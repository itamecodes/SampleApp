package com.vivek.phunwaresampleapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.squareup.otto.Subscribe;
import com.vivek.phunwaresampleapp.R;
import com.vivek.phunwaresampleapp.dummy.DummyContent;
import com.vivek.phunwaresampleapp.utils.EventBus;
import com.vivek.phunwaresampleapp.utils.Venue;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link VenueListActivity} in two-pane mode (on tablets) or a
 * {@link VenueDetailActivity} on handsets.
 */
public class VenueDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	private ImageView mImageView;
	private TextView mVenueName,mVenueAddress,mVenueLocation;
	private AQuery mAquery;
	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public VenueDetailFragment() {
	}

	public static VenueDetailFragment newInstance(String name,String url,String location,String address){
		VenueDetailFragment venueDetailFragment=new VenueDetailFragment();
		
		Bundle args = new Bundle();
		args.putString("name",name);
		args.putString("imageurl",url);
		args.putString("address", address);
		args.putString("location",location);
		
		venueDetailFragment.setArguments(args);
		return venueDetailFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
	}
	
	public String getName(){
		Bundle bundle = getArguments();
		String name = bundle.getString("name");
		return name;
	}
	
	public String getImageUrl(){
		Bundle bundle = getArguments();
		String imageUrl = bundle.getString("imageurl");
		return imageUrl;
	}
	
	public String getAddress(){
		Bundle bundle = getArguments();
		String address = bundle.getString("address");
		return address;
	}
	
	public String getLocation(){
		Bundle bundle = getArguments();
		String location = bundle.getString("location");
		return location;
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
		Log.v("vivekqw","1212132");
		mAquery=new AQuery(getActivity(),rootView);
		mImageView=(ImageView)rootView.findViewById(R.id.imageview);
		mVenueName=(TextView) rootView.findViewById(R.id.venuedetailname);
		mVenueAddress=(TextView) rootView.findViewById(R.id.venuedetailaddress);
		mVenueLocation=(TextView) rootView.findViewById(R.id.venuedetaillocation);
		return rootView;
		
	}
	/*
	@Subscribe 
	public void onVenueClicked(Venue venue) {
		Log.v("vivek","received event");
		String url=venue.getImageUrl();
		
		mAquery.id(mImageView).image(
				url,
				true,
				true,
				200,
				0, null, 0,
				AQuery.RATIO_PRESERVE);
		mVenueAddress.setText(venue.getAddress());
		mVenueName.setText(venue.getName());
		mVenueLocation.setText(venue.getState()+" "+venue.getCity()+" "+venue.getZip());
	}*/
	
	@Override
	public void onResume(){
		super.onResume();
		EventBus.getInstance().register(this);
		
		
			mAquery.id(mImageView).image(
					getImageUrl(),
					true,
					true,
					200,
					0, null, 0,
					AQuery.RATIO_PRESERVE);
			mVenueAddress.setText(getAddress());
			mVenueName.setText(getName());
			mVenueLocation.setText(getLocation());
		
		
	}
	
	@Override
	public void onPause(){
		EventBus.getInstance().unregister(this);
		super.onPause();
	}
	
}
