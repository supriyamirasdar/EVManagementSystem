package in.nsoft.evmanagementsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * 
 * @author MANASA
 * @Desc Google Map
 * @date 03-03-2015
 */
public class MapTestActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener  {

	private GoogleMap mMap;
	private static final String TAG = null;
	ArrayList<LatLng> mMarkerPoints;
	//double mLatitude = 0;
	//double mLongitude = 0;


	double sLatitude = 0;
	double sLongitude = 0;

	Location location;
	LocationManager locationManager;
	/* SupportMapFragment mMap; */
	// private GoogleMap googleMap;


	MarkerOptions markOpt;
	LatLng latLng;
	int status;


	GoogleMap gmap;
	Geocoder geoCoder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_test);
		// Obtain the SupportMapFragment and get notified when the map is ready
		// to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());


		/*try {
			alLatLng = db.GetCurrentMrStatusLatLong(mrsingleton.getStaffID()
					.trim());
		} catch (Exception e) {
			Log.d("", e.toString());
		}*/




	}



	/**
	 * Manipulates the map once available. This callback is triggered when the
	 * map is ready to be used. This is where we can add markers or lines, add
	 * listeners or move the camera. In this case, we just add a marker near
	 * Sydney, Australia. If Google Play services is not installed on the
	 * device, the user will be prompted to install it inside the
	 * SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		try {
			getLocation();
			mMap.getUiSettings().setCompassEnabled(true);


			//sLatitude = 12.970000;
			//sLongitude = 77.6;		


			mMap.addMarker(new MarkerOptions().position(new LatLng(sLatitude,sLongitude))
					.title(	"PICK LOCATION:" + getLocationAddress(sLatitude,sLongitude))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup))
					);

			mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
					.title("MY LOCATION:" + getLocationAddress(location.getLatitude(),location.getLongitude()))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
					);



			mMap.getProjection();
			mMap.getFocusedBuilding();


			LatLng CENTER = new LatLng(location.getLatitude(),location.getLongitude());
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER));
			final int zoom = 11;
			mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 1500, null);

			// setUpMarker(); 
			//plotMarkers(mMark);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "latLong parsed for direction",
					Toast.LENGTH_SHORT).show();
		}	


		mMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				sLatitude = arg0.latitude;
				sLongitude = arg0.longitude;
				try {
					mMap.clear();
					mMap.getUiSettings().setCompassEnabled(true);

					mMap.addMarker(new MarkerOptions().position(new LatLng(sLatitude,sLongitude))
							.title(	"PICK LOCATION:" + getLocationAddress(sLatitude,sLongitude))
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup))
							);

					mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
							.title("MY LOCATION:" + getLocationAddress(location.getLatitude(),location.getLongitude()))
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
							);

				


					LatLng CENTER = new LatLng(location.getLatitude(),location.getLongitude());
					mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER));
					final int zoom = 11;
					mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 1500, null);

					// setUpMarker(); 
					//plotMarkers(mMark);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getBaseContext(), "latLong parsed for direction",
							Toast.LENGTH_SHORT).show();
				}
			}
		});


	}	



	@Override
	public void onLocationChanged(Location location) {
		try {
		mMap.clear();
		mMap.getUiSettings().setCompassEnabled(true);
		
		mMap.addMarker(new MarkerOptions().position(new LatLng(sLatitude,sLongitude))
				.title(	"PICK LOCATION:" + getLocationAddress(sLatitude,sLongitude))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup))
				);

		mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
				.title("MY LOCATION:" + getLocationAddress(location.getLatitude(),location.getLongitude()))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
				);

		//mMap.getProjection();
		//mMap.getFocusedBuilding();

		//Toast.makeText(getBaseContext(), "LocationChanged",
		//		Toast.LENGTH_SHORT).show();

		
			LatLng CENTER = new LatLng(location.getLatitude(),location.getLongitude());
			mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER));
			final int zoom = 11;
			mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 1500, null);

			// setUpMarker(); 
			//plotMarkers(mMark);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "latLong parsed for direction",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public Location getLocation()
	{
		try 
		{
			locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
			boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if(!isGPSEnabled && !isNetworkEnabled)
			{

			}
			else
			{

				if(isGPSEnabled)
				{
					if(location == null)
					{
						locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 10000, 0, this);
						Log.d("Network","Network");
						if(locationManager != null)
						{ 
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							//mLatitude = location.getLatitude();
							//mLongitude = location.getLongitude();
						}
					}
				}
				else
				{
					CustomToast.makeText(this, "GPS Disabled. Please turn on GPS.", Toast.LENGTH_LONG);
				}
			}

		}
		catch (Exception e) {
			int i=10;
			Log.d(TAG, e.toString());
		}
		return location;
	}


	public String getLocationAddress(double latitude,double longitude)
	{
		String paddress = "";
		try
		{
			
				geoCoder = new Geocoder(this, Locale.ENGLISH);
				// StringBuilder b= null;
				

				List<Address> addresses = geoCoder.getFromLocation(latitude,
						longitude, 1);

				if (addresses != null) {
					Address returnedAddress = addresses.get(0);
					// b= new StringBuilder();				

					paddress = returnedAddress.getAddressLine(0)+ " " + returnedAddress.getAddressLine(1) + " " + returnedAddress.getAddressLine(2);

				}
			
		}
		catch(Exception e)
		{
			
		}
		
		return paddress;
		
	}

}
