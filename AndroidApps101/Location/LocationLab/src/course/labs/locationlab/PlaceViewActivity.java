package course.labs.locationlab;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceViewActivity extends ListActivity implements LocationListener {
	private static final long FIVE_MINS = 5 * 60 * 1000;

	private static String TAG = "Lab-Location";

	private Location mLastLocationReading;
	private PlaceViewAdapter mAdapter;

	// default minimum time between new readings
	private long mMinTime = 5000;

	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f;

	private LocationManager mLocationManager;
	private LocationListener mLocationListener;

	// A fake location provider used for testing
	private MockLocationProvider mMockLocationProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // Set up the app's user interface
        // This class is a ListActivity, so it has its own ListView
        // ListView's adapter should be a PlaceViewAdapter

		mAdapter = new PlaceViewAdapter(getApplicationContext());
		ListView listView = getListView();
		listView.setFooterDividersEnabled(true);
		
        // add a footerView to the ListView
        // You can use footer_view.xml to define the footer
		LayoutInflater inflater = this.getLayoutInflater();
		TextView footerView = (TextView)inflater.inflate(R.layout.footer_view, null);
		listView.addFooterView(footerView);
		
        // When the footerView's onClick() method is called, it must issue the
        // following log call
        // log("Entered footerView.OnClickListener.onClick()");
        
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered footerView.OnClickListener.onClick()");

		        // footerView must respond to user clicks.
		        // Must handle 3 cases:
				if (mLastLocationReading != null)
				{
					if (!mAdapter.intersects(mLastLocationReading))
					{
				        // 1) The current location is new - download new Place Badge. Issue the
				        // following log call:
				        log("Starting Place Download");
				        PlaceDownloaderTask task = new PlaceDownloaderTask(PlaceViewActivity.this);
				        task.execute(mLastLocationReading);
					}
					else
					{
				        // 2) The current location has been seen before - issue Toast message.
				        Toast.makeText(getApplicationContext(), "You alread have the location badge", Toast.LENGTH_LONG).show();
				        // Issue the following log call:
				        log("You already have this location badge");
					}
				}
				else
				{
			        // 3) There is no current location - response is up to you. The best
			        // solution is to disable the footerView until you have a location.
			        // Issue the following log call:
			        log("Location data is not available");
				}
				
			}
		});
		
		listView.setAdapter(mAdapter);
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
			finish();
		
		mLocationListener = this;
	}

	@Override
	protected void onResume() {
		super.onResume();

		mMockLocationProvider = new MockLocationProvider(
				LocationManager.NETWORK_PROVIDER, this);

        // Check NETWORK_PROVIDER for an existing location reading.
        // Only keep this last reading if it is fresh - less than 5 minutes old.

		// Determine whether initial reading is
		// "good enough"

	    // register to receive location updates from NETWORK_PROVIDER
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, mMinTime, mMinDistance,
				mLocationListener);

		Location loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc != null)
		{
			if (mLastLocationReading == null 
					|| age(loc) < FIVE_MINS
						&& age(loc) < age(mLastLocationReading))
		    {
	    		mLastLocationReading = loc;
		    }
		}
	}

	@Override
	protected void onPause() {

		mMockLocationProvider.shutdown();

		// unregister for location updates
		mLocationManager.removeUpdates(mLocationListener);
		
		super.onPause();
	}

	// Callback method used by PlaceDownloaderTask
	public void addNewPlace(PlaceRecord place) {

		log("Entered addNewPlace()");
		mAdapter.add(place);

	}

	@Override
	public void onLocationChanged(Location location) {

        // Cases to consider
        // 1) If there is no last location, keep the current location.
        // 2) If the current location is older than the last location, ignore
        // the current location
        // 3) If the current location is newer than the last locations, keep the
        // current location.

		if (null == mLastLocationReading
				|| age(location) < age(mLastLocationReading)) 
		{
			mLastLocationReading = location;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// not implemented
	}

	@Override
	public void onProviderEnabled(String provider) {
		// not implemented
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// not implemented
	}

	private long age(Location location) {
		return System.currentTimeMillis() - location.getTime();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.print_badges:
			ArrayList<PlaceRecord> currData = mAdapter.getList();
			for (int i = 0; i < currData.size(); i++) {
				log(currData.get(i).toString());
			}
			return true;
		case R.id.delete_badges:
			mAdapter.removeAllViews();
			return true;
		case R.id.place_one:
			mMockLocationProvider.pushLocation(37.422, -122.084);
			return true;
		case R.id.place_invalid:
			mMockLocationProvider.pushLocation(0, 0);
			return true;
		case R.id.place_two:
			mMockLocationProvider.pushLocation(38.996667, -76.9275);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}
