package com.vivek.phunwaresampleapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vivek.phunwaresampleapp.R;
import com.vivek.phunwaresampleapp.utils.Venue;

public class VenueAdapter extends ArrayAdapter<Venue> {
	
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private int mVenueItemView;
	
	public VenueAdapter(Context context,int venueItemView) {
		super(context, venueItemView);
		mContext=context;
		mVenueItemView=venueItemView;
		mLayoutInflater=LayoutInflater.from(mContext);
		
	}

	public View getView(int position,View convertView,ViewGroup container){
		View venueItemView=convertView;
		if(venueItemView==null){
			venueItemView=mLayoutInflater.inflate(mVenueItemView,null);
		}
		Log.v("vivek","19");
		TextView venueHeading=(TextView) venueItemView.findViewById(R.id.venuename);
		TextView venueAddress=(TextView) venueItemView.findViewById(R.id.venueaddress);
		Venue venueItem=getItem(position);
		Log.v("vivek","20");
		
		venueHeading.setText(venueItem.getName());
		Log.v("vivek","21 "+venueItem.getName());
		venueAddress.setText(venueItem.getAddress());
		Log.v("vivek","22 " +venueItem.getAddress());
		return venueItemView;
	}

}
