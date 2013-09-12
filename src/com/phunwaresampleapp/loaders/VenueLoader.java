package com.phunwaresampleapp.loaders;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vivek.phunwaresampleapp.utils.Venue;
import com.vivek.phunwaresampleapp.utils.WrappedAsyncTaskLoader;

public class VenueLoader extends WrappedAsyncTaskLoader<List<Venue>> {
	
	
	private String mVenueJson;
	private static final String AMAZON_URL="https://s3.amazonaws.com/jon-hancock-phunware/nflapi-static.json";
	private StringBuilder mStringBuilder=new StringBuilder();
	private List<Venue> mVenueList=new ArrayList<Venue>();
	
	public VenueLoader(Context context) {
		super(context);
		Log.v("vivek","15");
	}

	@Override
	public List<Venue> loadInBackground() {
		Log.v("vivek","16");
		HttpURLConnection urlConnection=null;
		try{
			urlConnection=(HttpURLConnection) new URL(AMAZON_URL).openConnection();
			urlConnection.setRequestProperty("Accept", "application/json");
			int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
			urlConnection.addRequestProperty("Cache-Control", "max-stale=" + maxStale);
			InputStream inputStream=new BufferedInputStream(urlConnection.getInputStream());
			BufferedReader bufReader=new BufferedReader(new InputStreamReader(inputStream));
			String line=null;
			while((line=bufReader.readLine())!=null){
				mStringBuilder.append(line);
			}
			Log.v("vivek","17");
			mVenueJson=mStringBuilder.toString();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create();
			Type listType=new TypeToken<List<Venue>>(){}.getType();
			mVenueList=(List<Venue>)gson.fromJson(mVenueJson, listType);
			Log.v("testing","  "+mVenueList.get(0).getName());
		}catch(MalformedURLException malUrlEx){
			
		}catch(FileNotFoundException fileNotFoundEx){
			
		}catch(IOException ioException){
			
		}finally{
			urlConnection.disconnect();
		}
		return mVenueList;
	}
	
	private static void longInfo(String str) {
	    if(str.length() > 4000) {
	        Log.i("vivek",""+str.substring(0, 4000));
	        longInfo(str.substring(4000));
	    } else
	        Log.i("vivek",""+str);
	}

}
