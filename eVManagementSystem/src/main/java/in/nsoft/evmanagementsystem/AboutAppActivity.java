package in.nsoft.evmanagementsystem;
// Nitish 19-01-2016

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AboutAppActivity extends Activity {

	private TextView txtAppVersion,txtDevNo, txtBattery;
	private static final String TAG = AboutAppActivity.class.getName();
	ActionBar bar;
	Button btnDownloadMainDB;
	
	File file;
	Handler mainThreadHandler;  
	InputStream ipStream;
	OutputStream myOutput;
	int count = 0, Total = 0;
	DatabaseHelper db = new DatabaseHelper(this);
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_app);
		mainThreadHandler = new Handler();
		bar=getActionBar();

		
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.appcolor))));		
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		bar.setDisplayShowTitleEnabled(true);
		//bar.setIcon(color.transparent);

		txtAppVersion = (TextView)findViewById(R.id.txtAppVersion);
		txtDevNo = (TextView)findViewById(R.id.txtDevNo);
		txtBattery = (TextView)findViewById(R.id.txtBattery); 
		
		btnDownloadMainDB = (Button)findViewById(R.id.btnDownloadMainDB);

		try
		{
			PackageManager mgr= this.getPackageManager();
			PackageInfo pkgInfo = mgr.getPackageInfo(this.getPackageName(), 0);			
			txtAppVersion.setText(pkgInfo.versionName);			
			txtDevNo.setText(LoginActivity.IMEINumber);
			txtBattery.setText(BatteryLevel()+"%");
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		
		btnDownloadMainDB.setOnClickListener(new OnClickListener() {//btnDownloadMainDB 

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try
				{
					//Pull Out DataBase from Device
					//Added 06-09-2019 Also add in Database helper
					if (android.os.Build.VERSION.SDK_INT >= 28) {                    
	                    file = new File(db.getReadableDatabase().getPath());
				    }
					else
					{
						file = new File(ConstantClass.rootDBPathWithDB);//Get DB Path
					}
					
					if(!file.exists())
					{
						CustomToast.makeText(AboutAppActivity.this, "Database file not found...", Toast.LENGTH_SHORT);
						return;	
						
					}		
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							try
							{
								mainThreadHandler.post(new Runnable() {									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										btnDownloadMainDB.setVisibility(ProgressBar.INVISIBLE);									
										
									}
								});
								String root = Environment.getExternalStorageDirectory().getPath();//Get External Storage Path 
								String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());					
								File filepath;
								String filePh = root+"/EVManagementSystem";
								filepath = new File(filePh);
								if(!filepath.exists())
								{
									filepath.mkdir();
								}//Not Exists Create Directory
								filePh = filePh+"/"+timeStamp;
								filepath = new File(filePh);//External Storage Path + additional Folder
								if(!filepath.exists())
								{
									filepath.mkdir();
								}//Not Exists Create Directory
								if(filepath.exists())//If Directory Exists
								{
							
									
									ipStream = new FileInputStream(file);//get Input Stream of DataBase Path
									myOutput = new FileOutputStream(filePh+"/EVManagementSystem.dat");//Get OutputStream of New Directory Path					
									byte[] buffer = new byte[1024];//Variable
									int length;//Variable
									Total = ipStream.available();
									
									
									while((length = ipStream.read(buffer))>0)
									{
										myOutput.write(buffer, 0, length);//write byte by byte to OutputStream Directory
										count = count + length;

										
									}	

								}/**/								
							}
							catch(Exception e)
							{
								Log.d(TAG, e.toString());
							}
							finally
							{
								try {
									myOutput.flush();
									myOutput.close();//Close OutputStream
									ipStream.close();//Close InputStream
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}//flush OutputStream
							}
							mainThreadHandler.post(new Runnable() {									
								@Override
								public void run() { 
									
									// TODO Auto-generated method stub
									btnDownloadMainDB.setVisibility(ProgressBar.VISIBLE);									
									
									CustomToast.makeText(AboutAppActivity.this, "Download completed.", Toast.LENGTH_SHORT);
								}
							});
						}
					}).start();

				}
				catch(Exception e)
				{
					Log.d(TAG, e.toString());
				}				
			}
		});//END btnDownloadMainDB 
	}
	//Tamilselvan on 13-05-2014
	/**
	 * Get Battery Percent
	 * @return
	 */
	public String BatteryLevel()
	{
		float battery = 0;
		try
		{
			Intent batteryLevel = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			float level = batteryLevel.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			float scale = batteryLevel.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			if(level == -1 || scale == -1)
			{
				battery = 50.0f;
			}
			else
			{
				battery = (level/scale)* 100.0f;
			} 
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
		String Level = String.valueOf(new BigDecimal(battery).setScale(0, BigDecimal.ROUND_HALF_EVEN));
		return Level;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, AboutAppActivity.this);			
	}
}
