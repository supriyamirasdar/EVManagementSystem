package in.nsoft.evmanagementsystem;

/*import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;*/


import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StationTrackerMap2Activity extends android.support.v4.app.FragmentActivity implements OnMapReadyCallback {
	private ViewGroup infoWindow;
	private TextView txttitle,txtDescription;

	private Button buttonDirection,buttonBooking;
	private StationTracker2_OnInfoWindowItemTouchListener infoButtonListenerDirection,infoButtonListenerBooking;
	GoogleMap map;
	Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_tracker_map2);

		//final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
		final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
		//map = mapFragment.getMapAsync(this);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);



		// MapWrapperLayout initialization
		// 39 - default marker height
		// 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
		mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 40));

		// We want to reuse the info window for all the markers,
		// so let's create only one class member instance

		ctx = StationTrackerMap2Activity.this;
		infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window_test2, null);
		txttitle = (TextView)infoWindow.findViewById(R.id.txttitle);


		txtDescription = (TextView)infoWindow.findViewById(R.id.txtDescription);
		buttonDirection = (Button)infoWindow.findViewById(R.id.buttonDirection);
		buttonBooking = (Button)infoWindow.findViewById(R.id.buttonBooking);

		

		map.setInfoWindowAdapter(new InfoWindowAdapter() {
			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				// Setting up the infoWindow with current's marker info

				String snippetData[] = marker.getSnippet().split(",");

				txttitle.setText(marker.getTitle().trim());

				txtDescription.setText("Power: " + snippetData[1].trim() + " Cost:" + snippetData[2].trim());		

				infoButtonListenerDirection.setMarker(marker);
				infoButtonListenerBooking.setMarker(marker);
				// We must call this to set the current marker and infoWindow references
				// to the MapWrapperLayout

				mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);

				return infoWindow;
			}
		});

		// Let's add a couple of markers

		/*map.addMarker(new MarkerOptions()
		.title("Charge and Drive Station ,Vidyapeetha")
		.snippet("1,24KW,20Rs per Kw")
		.position(new LatLng(12.935219, 77.56)));

		map.addMarker(new MarkerOptions()
		.title("Bescom Charging Station ,Katriguppe")
		.snippet("2,24KW,19Rs per Kw")
		.position(new LatLng(12.93, 77.54)));

		map.addMarker(new MarkerOptions()
		.title("Ather Grid Charging Point,Deve Gowda Petrol Pump")
		.snippet("3,22KW,19Rs per Kw")
		.position(new LatLng(12.922,77.56)));

		map.addMarker(new MarkerOptions()
		.title("Mahindra Electric Charging Point,BSK 2nd Stage")
		.snippet("4,24KW,20Rs per Kw")
		.position(new LatLng(12.9248,77.565)));*/


		ArrayList<LatLong> alLatLng=HomeActivity.lstMap;


		try{

			for(int i=0;i<alLatLng.size();i++)	
			{
				map.addMarker(new MarkerOptions()
				.title(alLatLng.get(i).getmName() + "," +alLatLng.get(i).getmAddress())
				.snippet(alLatLng.get(i).getmId() + "," +alLatLng.get(i).getmPower() + "," +alLatLng.get(i).getmCost())
				.position(new LatLng(Double.parseDouble(alLatLng.get(i).getmLatitude()),Double.parseDouble(alLatLng.get(i).getmLongitude()))));

			}


			
		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "Error in Lat Long Values", Toast.LENGTH_SHORT).show();
		}	


		LatLng CENTER = new LatLng(12.93, 77.54);
		map.moveCamera(CameraUpdateFactory.newLatLng(CENTER));
		final int zoom = 12;
		map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 1500, null);
		
		
		///////////////////////////////////BUTTON CONTROLS ///////////////////////////////////
		// Setting custom OnTouchListener which deals with the pressed state
		// so it shows up
		
		///////////////////////Direction Button ///////////////////////////
		infoButtonListenerDirection = new StationTracker2_OnInfoWindowItemTouchListener(buttonDirection,
				getResources().getDrawable(R.drawable.round_but_green_sel), //btn_default_normal_holo_light
				getResources().getDrawable(R.drawable.round_but_red_sel)) //btn_default_pressed_holo_light
		{
			@Override
			protected void onClickConfirmed(View v, Marker marker) {
				// Here we can perform some action triggered after clicking the button

				Toast.makeText(StationTrackerMap2Activity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();

				try
				{
					String sLatitude = String.valueOf(marker.getPosition().latitude);
					String sLongitude = String.valueOf(marker.getPosition().longitude);
					

					String strUri = "http://maps.google.com/maps?q=loc:" + sLatitude + "," + sLongitude + " (" + marker.getTitle() + ")";
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
					startActivity(intent);

				}
				catch(Exception e)
				{
					Toast.makeText(StationTrackerMap2Activity.this,
							"Error in Location", Toast.LENGTH_LONG).show();

					return;
				}

			}
		};
		buttonDirection.setOnTouchListener(infoButtonListenerDirection);
		///////////////////////End Direction Button ///////////////////////////

		
		///////////////////////Booking Button ///////////////////////////
		infoButtonListenerBooking = new StationTracker2_OnInfoWindowItemTouchListener(buttonBooking,
				getResources().getDrawable(R.drawable.round_but_green_sel), //btn_default_normal_holo_light
				getResources().getDrawable(R.drawable.round_but_red_sel)) //btn_default_pressed_holo_light
		{
			@Override
			protected void onClickConfirmed(View v, Marker marker) {
				// Here we can perform some action triggered after clicking the button

				Toast.makeText(StationTrackerMap2Activity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();

				try
				{
					String sId = String.valueOf(marker.getSnippet().split(",")[0].trim());
					
					Intent i = new Intent(StationTrackerMap2Activity.this, BookingStartActivity.class);
					i.putExtra("Key",sId);
					startActivity(i);

				

				}
				catch(Exception e)
				{
					Toast.makeText(StationTrackerMap2Activity.this,
							"Error in Booking", Toast.LENGTH_LONG).show();

					return;
				}

			}
		};
		buttonBooking.setOnTouchListener(infoButtonListenerBooking);
		///////////////////////End Booking Button ///////////////////////////

		

		
		///////////////////////////////////END BUTTON CONTROLS ///////////////////////////////////
		
		
		



	}



	public static int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dp * scale + 0.5f);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

	}
}


