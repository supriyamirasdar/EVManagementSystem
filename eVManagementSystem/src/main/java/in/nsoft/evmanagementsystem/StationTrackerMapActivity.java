package in.nsoft.evmanagementsystem;

import java.io.BufferedReader;

import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.w3c.dom.UserDataHandler;

import android.R.color;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class StationTrackerMapActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {

	private static final String TAG = null;
	ArrayList<LatLng> mMarkerPoints;
	double mLatitude = 0;
	double mLongitude = 0;
	private ArrayList<MyMarker> mMark = new ArrayList<MyMarker>();
	private HashMap<Marker, MyMarker> mMarkerHashMap;
	private ArrayList<LatLong> alLatLng;

	Location location;
	LocationManager locationManager;
	/*SupportMapFragment mMap;*/
	private GoogleMap googleMap;
	DatabaseHelper db = new DatabaseHelper(this);

	MarkerOptions markOpt;
	LatLng latLng;


	@Override //manasa 15-1-15
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_tracker_map);

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();


		} else { // Google Play Services are available

			// Initializing 
			mMarkerPoints = new ArrayList<LatLng>();

			// Getting reference to SupportMapFragment of the activity_main
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

			// Getting Map for the SupportMapFragment
			fm.getMapAsync(this);

			/*googleMap.setTrafficEnabled(true);*/


			// Enable MyLocation Button in the Map
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			googleMap.setMyLocationEnabled(true);

			googleMap.setBuildingsEnabled(true);

			// Getting LocationManager object from System Service LOCATION_SERVICE
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location From GPS	       
			locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,  0, 0, this);	
			if(locationManager != null)
			{ 
				location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			if(location!=null){	        
				onLocationChanged(location);
			}

			locationManager.requestLocationUpdates(provider, 20000, 0, this);
			mMarkerHashMap=new HashMap<Marker,MyMarker>();
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.setIndoorEnabled(true);

			alLatLng=HomeActivity.lstMap;


			try{

				for(int i=0;i<alLatLng.size();i++)	
				{
					mMark.add(new MyMarker( Double.parseDouble(alLatLng.get(i).getmLatitude()),Double.parseDouble(alLatLng.get(i).getmLongitude()),alLatLng.get(i).getmName().trim()+"," + alLatLng.get(i).getmAddress()));
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(alLatLng.get(i).getmLatitude()),Double.parseDouble(alLatLng.get(i).getmLongitude())),12f));
				}


			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(getBaseContext(), "lat and long values from db", Toast.LENGTH_SHORT).show();
			}	

			try
			{




				setUpMarker();
				plotMarkers(mMark);	




				LatLng origin = new LatLng(mLatitude,mLongitude);



			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(getBaseContext(), "latLong parsed for direction", Toast.LENGTH_SHORT).show();
			}

			zoomBoundaries(mMark);
		}		
	}




	private void zoomBoundaries(ArrayList<MyMarker> markers) {
		try
		{

			LatLngBounds.Builder builder=new LatLngBounds.Builder();


			for(MyMarker MM: mMark)
			{

				LatLng latlng = new LatLng(MM.getmLatitude(),MM.getmLongitude());

				builder.include(latlng).build();


			}
			LatLngBounds bounds=builder.build();	
			CameraPosition.Builder builder1=CameraPosition.builder();
			builder1.zoom(5);
			builder1.bearing(155);

			CameraUpdate cu=CameraUpdateFactory.newLatLngZoom(latLng, 7);

			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,400,400,0));


			bounds.getCenter();
		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "latLong Zoom Exception", Toast.LENGTH_SHORT).show();
		}
	}






	private void plotMarkers(ArrayList<MyMarker> markers) {
		try{

			if(markers.size()>0)
			{
				try{

					for(MyMarker mMark:markers)
					{

						MarkerOptions markerOption=new MarkerOptions().position(new LatLng(mMark.getmLatitude(),mMark.getmLongitude()));
						//if(mMark.getmBillcount().equals("0"))
						{
							markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
						}
						/*else
						{
							markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
						}*/


						/*CameraUpdate cu=new LatLngZoom(markerOption.getPosition(),10F);*/
						Marker currentMarker=googleMap.addMarker(markerOption);
						mMarkerHashMap.put(currentMarker, mMark);
						googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
					}



				}
				catch(Exception e){
					e.printStackTrace();
				}

			}
		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "PlotMarkers", Toast.LENGTH_SHORT).show();
		}	
	}


	private void setUpMarker() {


		try
		{
			if(googleMap==null)
			{
				((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "unable to get google map", Toast.LENGTH_SHORT).show();
		}	

		if(googleMap!=null)
			googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() 
			{


				@Override
				public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
					marker.showInfoWindow();

					// TODO Auto-generated method stub
					return true;
				}
			});

		else
			Toast.makeText(getApplicationContext(), "Unable to create maps", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

	}

	public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

		public MarkerInfoWindowAdapter()
		{

		}

		@Override
		public View getInfoContents(Marker marker) {
			View v = null;
			try
			{
				v=getLayoutInflater().inflate(R.layout.infowindow_layout,null);
				MyMarker mMark=mMarkerHashMap.get(marker);

				TextView markerLabel=(TextView)v.findViewById(R.id.marker_label);
				markerLabel.setText(mMark.getMMRName());


			}

			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(getBaseContext(), "marker label infor", Toast.LENGTH_SHORT).show();
			}	
			return v;
		}

		@Override
		public View getInfoWindow(Marker arg0) {

			return null;
		}

	}


	private String getDirectionsUrl(LatLng origin,LatLng dest){

		String url ="";
		try{


			// Origin of route
			String str_origin = "origin="+origin.latitude+","+origin.longitude;

			// Destination of route
			String str_dest = "destination="+dest.latitude+","+dest.longitude;			

			// Sensor enabled
			String sensor = "sensor=false";			

			// Building the parameters to the web service
			String parameters = str_origin+"&"+str_dest+"&"+sensor;

			// Output format
			String output = "json";

			// Building the url to the web service
			url= "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;		



		}


		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "error on  marker", Toast.LENGTH_SHORT).show();
		}

		return url;
	}

	// A method to download json data from url *//*
	private String downloadUrl(String strUrl) throws IOException{
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url 
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url 
			urlConnection.connect();

			// Reading data from url 
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb  = new StringBuffer();

			String line = "";
			while( ( line = br.readLine())  != null){
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		}catch(Exception e){
			Log.d("Exception ", e.toString());
		}finally{
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}



	// A class to download data from Google Directions URL *//*
	private class DownloadTask extends AsyncTask<String, Void, String>{			

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}		
	}

	//A class to parse the Google Directions in JSON format *//*
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

		// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           

			try{
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);    
			}catch(Exception e){
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);					

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	

					points.add(position);

				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(6);
				lineOptions.color(Color.BLUE);	

			}
			if(lineOptions!=null){
				// Drawing polyline in the Google Map for the i-th route
				googleMap.addPolyline(lineOptions);	
				plotMarkers(mMark);
				zoomBoundaries(mMark);
			}
		}			
	}   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.station_tracker, menu);
		return true;
	}



	@Override //3-3-15
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){

		case R.id.menu_zoomout:	
			googleMap.animateCamera(CameraUpdateFactory.zoomOut());
			break;

		case R.id.menu_zoomin:	
			googleMap.animateCamera(CameraUpdateFactory.zoomIn());
			break;


		case R.id.menu_showtraffic:	
			googleMap.setTrafficEnabled(true);
			break;

		case R.id.menu_setNormal:	
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;


		case R.id.menu_setHybrid:	
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;

		case R.id.menu_Terrain:	
			googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;

		case R.id.menu_satelite:	
			googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;

		case R.id.menu_None:	
			googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;


		}
		return true;

	}


	private void drawMarker(LatLng point){
		mMarkerPoints.add(point);

		// Creating MarkerOptions
		MarkerOptions options = new MarkerOptions();

		// Setting the position of the marker
		options.position(point);



		if(mMarkerPoints.size()==1)
		{
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		}

		// Add new marker to the Google Map Android API V2*/
		googleMap.addMarker(options);


	}




	@Override
	public void onLocationChanged(Location location) {
		try{
			// Draw the marker, if destination location is not set
			if(mMarkerPoints.size() < 1){                                                                                  

				mLatitude = location.getLatitude();
				mLongitude = location.getLongitude();
				LatLng point = new LatLng(mLatitude, mLongitude);

				googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(3)); 


				drawMarker(point);			
			} 
		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "location changed", Toast.LENGTH_SHORT).show();
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
}