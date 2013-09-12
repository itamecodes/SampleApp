package com.vivek.phunwaresampleapp.app;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.phunwaresampleapp.loaders.VenueLoader;
import com.vivek.phunwaresampleapp.R;
import com.vivek.phunwaresampleapp.adapters.VenueAdapter;
import com.vivek.phunwaresampleapp.utils.DataReceivedSignal;
import com.vivek.phunwaresampleapp.utils.EventBus;
import com.vivek.phunwaresampleapp.utils.Venue;

/**
 * A list fragment representing a list of Venues. This fragments communicates with its activity
 * by using an EventBus.
 * It uses Async Loader to load data from the network.
 * 
 * @author vivek
 */
public class VenueListFragment extends ListFragment implements LoaderCallbacks<List<Venue>>{

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	
	/**
	 * This is the custom adapter which extends an array adapter and is responsible for displaying the list
	 */
	private VenueAdapter mVenueAdapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public VenueListFragment() {
	}

	/**
	 * We are not doing anything in the onCreate method here except calling the super
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/*
	 * Once the view has been created we initialize the adapter. This is sort of late initialization
	 * just to ensure that everything is ready before we initialize the adapter.It is argued that this should be done
	 * in the onCreate method.
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
		mVenueAdapter=new VenueAdapter(getActivity(),R.layout.venueitem);
		setListAdapter(mVenueAdapter);
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	/**
	 * We dont do anything in the on Attach activity too as we are not using interfaces , rather we are relying on
	 * the eventbus to do the heavy lifting for us
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * When any list item is selected we take the venue details which have been clicked and send it to the
	 * containing activity via a bus
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		Venue venueClicked=mVenueAdapter.getItem(position);
		EventBus.getInstance().post(venueClicked);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	/**
	 * the first in the lifecycle of the loader where we create the venue loader and it starts talking to 
	 * the network for us
	 */
	@Override
	public Loader<List<Venue>> onCreateLoader(int loaderId,
			Bundle bundle) {
		switch(loaderId){
		case 0:{
			return new VenueLoader(getActivity());
		}
		}
		return null;
	}

	
	/**
	 * Once the loader has done downloading the data , it sends it back here .The data is taken and put into the adapter
	 * which lays it out in the listview
	 */
	@Override
	public void onLoadFinished(Loader<List<Venue>> venueLoader,
		List<Venue> venueList) {
		mVenueAdapter.clear();
		//mVenueAdapter.addAll(venueList);->we could have used this to add to the array list but does not
		//work on 2.2 and 2.3
		for(Venue venue:venueList){
			mVenueAdapter.add(venue);
		}
		//send a signal to the main activity to remove the progress dialog
		DataReceivedSignal dataReceived=new DataReceivedSignal();
		EventBus.getInstance().post(dataReceived);
	}

	@Override
	public void onLoaderReset(Loader<List<Venue>> venueLoader) {
		
	}
	/**
	 *Register the event bus here 
	 */
	@Override
	public void onResume(){
		super.onResume();
		EventBus.getInstance().register(this);
	}

	/**
	 * Unregister the event bus
	 */
	@Override
	public void onPause(){
		EventBus.getInstance().unregister(this);
		super.onPause();
	}
}
