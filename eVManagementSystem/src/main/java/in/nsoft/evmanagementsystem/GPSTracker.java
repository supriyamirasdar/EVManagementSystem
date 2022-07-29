package in.nsoft.evmanagementsystem;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public  class GPSTracker extends Service implements LocationListener
{
	Context mContext;
	boolean isGPSEnabled=false;
	boolean isNetworkEnabled=false;
	static boolean canGetLocation=false;
	Location location;
	double latitude;
	double longitude;
	private static final long MIN_TIME_BT_UPDATES = 0;//1 minute
	private static final long MIN_DISTANCE_CHANGE_UPDATES = 0;//10 metters
	protected LocationManager locationmanager;
	public static boolean isgot = false;
	private static final String TAG = GPSTracker.class.getName();
	//protected GPSDetails mGPSDetails;

	public GPSTracker(Context context)
	{
		this.mContext = context;
		//mGPSDetails = new GPSDetails();
		getLocation();
	}
	public boolean isGPSConnected()
	{
		boolean isEnabled = false;
		try
		{

			locationmanager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if(isGPSEnabled)
			{
				isEnabled = true;
				
			}
		}
		catch (Exception e) 
		{

			Log.d(TAG, e.toString());
		}
		return isEnabled;
	}
	public Location getLocation()
	{
		try 
		{
			locationmanager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if(!isGPSEnabled && !isNetworkEnabled)
			{

			}
			else
			{
				this.canGetLocation = true;
				if(isGPSEnabled)
				{
					if(location == null)
					{
						locationmanager.requestLocationUpdates(locationmanager.GPS_PROVIDER, MIN_DISTANCE_CHANGE_UPDATES, MIN_TIME_BT_UPDATES, this);
						Log.d("Network","Network");
						if(locationmanager != null)
						{ 
							location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							updateGPSCoordinates();
						}
					}
				}
				else
				{
					CustomToast.makeText(mContext, "GPS Disabled. Please turn on GPS.", Toast.LENGTH_LONG);
				}
			}

		}
		catch (Exception e) {
			int i=10;
			Log.d(TAG, e.toString());
		}
		return location;
	}

	public void updateGPSCoordinates()
	{
		try
		{
			if(location !=null)
			{
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				/*mGPSDetails.setmLatitude(String.valueOf(location.getLatitude()));
			mGPSDetails.setmLongitude(String.valueOf(location.getLongitude()));
			mGPSDetails.setmTimeStamp(CommonFunction.GetCurrentDateTime());*/
			}
		}
		catch (Exception e) 
		{

			Log.d(TAG, e.toString());
		}
	}	

	public void stopUsingGPS()
	{
		try
		{
			if(locationmanager != null)
			{
				locationmanager.removeUpdates(GPSTracker.this);
			}
		}
		catch (Exception e) 
		{

			Log.d(TAG, e.toString());
		}
	}

	public double getLatitude()
	{
		try
		{
			if(location !=null)
			{
				latitude=location.getLatitude();
			}
		}
		catch (Exception e) 
		{

			Log.d(TAG, e.toString());
		}
		return latitude;
	}
	public double getLongitude()
	{
		try
		{
			if(location !=null)
			{
				longitude=location.getLongitude();
			}
		}
		catch (Exception e) 
		{

			Log.d(TAG, e.toString());
		}
		return longitude;
	}
	public static boolean canGetLocation()
	{
		return canGetLocation;
	}



	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		//  updateGPSCoordinates();
		try
		{
			isgot = true;
			latitude = arg0.getLatitude();
			longitude = arg0.getLongitude();
		}
		catch (Exception e) 
		{

			Log.d(TAG, e.toString());
		}
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		CustomToast.makeText(mContext, "GPS Disabled", Toast.LENGTH_LONG);
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		CustomToast.makeText(mContext, "GPS Enabled", Toast.LENGTH_LONG);
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
