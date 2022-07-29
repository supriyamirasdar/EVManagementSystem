package in.nsoft.evmanagementsystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Global;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeActivity extends Activity implements AlertInterface {
	
	LinearLayout lyt1;
	Handler dh;
	int count;
	TextView lbl1,lbl2,lblSBBlinkText;
	GridView gridView;

	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter mCustomGridViewAdapter;
	ActionBar bar;
	static int ImageFlag;
	
	DatabaseHelper db = new DatabaseHelper(HomeActivity.this);
	ProgressDialog ringProgress;

	
	Bitmap profile,about,notif,booking,StattionTracker,QRCode;
	static String fCount,cCount= "0"; //17-05-2021

	static ArrayList<LatLong> lstMap;
	Handler mainThreadHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridhome);
		
		dh = new Handler();
		lyt1 = (LinearLayout) findViewById(R.id.lyt1);
		lbl1 = (TextView) findViewById(R.id.lbl1);	
		lbl2 = (TextView) findViewById(R.id.lbl2);	
		
		mainThreadHandler = new Handler();
		
		
		try
		{
			//#################**BLINKING ANIM**#########################

			Animation anim=new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(1000); //BLINKING duration
			anim.setStartOffset(100);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(Animation.INFINITE);
			lbl1.startAnimation(anim);
			//##### BLINKING ANIM END ########
			lbl1.setText("WELCOME :" + db.getUserDetails().getmUserName().trim());

		}
		catch(Exception e)
		{

		}
		try
		{

			new Thread(new Runnable() {
				@Override
				public void run() {

					while(true){
						try {

							dh.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									count = count + 1;
									if(LoginActivity.gpsTracker.isGPSConnected())
									{
										if(String.valueOf(LoginActivity.gpsTracker.latitude).equals("0.0"))
										{
											lbl2.setText("GPS Details Not Obtained");
											lbl2.setTextColor(getResources().getColor(R.color.red));

										}
										else
										{
											lbl2.setText("GPS Details Obtained");
											lbl2.setTextColor(getResources().getColor(R.color.appcolor));
										}
									}
									else
									{
										lbl2.setText("GPS Location Disabled");
										lbl2.setTextColor(getResources().getColor(R.color.red));
									}
									if(count%4 ==0)
										lyt1.setBackground(getResources().getDrawable(R.drawable.images1));
									if(count%4 ==1)
										lyt1.setBackground(getResources().getDrawable(R.drawable.images2));
									if(count%4 ==2)
										lyt1.setBackground(getResources().getDrawable(R.drawable.images5));
									if(count%4 ==3)
										lyt1.setBackground(getResources().getDrawable(R.drawable.images4));

								}
							});
							Thread.sleep(4000);

						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			}).start();
		}
		catch(Exception e)
		{
			Log.d("a","a");
		}

		profile = BitmapFactory.decodeResource(this.getResources(),	R.drawable.p);//profile1
		booking =  BitmapFactory.decodeResource(this.getResources(),R.drawable.b);//booking
		StattionTracker = BitmapFactory.decodeResource(this.getResources(),R.drawable.s);//booking
		about = BitmapFactory.decodeResource(this.getResources(),R.drawable.ab);//about1
		notif = BitmapFactory.decodeResource(this.getResources(), R.drawable.notif);//Others
		QRCode = BitmapFactory.decodeResource(this.getResources(), R.drawable.q);//Others
		
		gridArray.add(new Item(profile,notif, "Profile","0",false));		
		//gridArray.add(new Item(booking,notif, "Booking","0",false));
		//gridArray.add(new Item(StattionTracker,notif, "Station Tracker","0",false));
		gridArray.add(new Item(QRCode,notif, "QRCode Scan","0",false));
		gridArray.add(new Item(about,notif, "About","0",false));
		
		gridView = (GridView)findViewById(R.id.grdHomePage);
		mCustomGridViewAdapter = new CustomGridViewAdapter(HomeActivity.this, R.layout.row_grid, gridArray);
		gridView.setAdapter(mCustomGridViewAdapter);
		
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi") @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub   

				String Name = gridArray.get(arg2).Name;
				ImageFlag = 0;
				String CurDate = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().
						getTime());				
				try 
				{
					if(Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0)
					{
						CustomToast.makeText(HomeActivity.this,"Please Enable AutoDateTime Feature." , Toast.LENGTH_SHORT);
						return;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(Name.equals("Profile"))
				{
					try {	

						Intent i = new Intent(HomeActivity.this,MyProfileActivity.class);
						startActivity(i);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomeActivity.this, "MyProfile Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}	
				}
				else if(Name.equals("QRCode Scan"))
				{
					try {			
						Intent i = new Intent(HomeActivity.this,QRCodeActivity.class);
						startActivity(i);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomeActivity.this, "QRCode Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}	
				}
				else if(Name.equals("About"))
				{
					try {			
						Intent i = new Intent(HomeActivity.this,AboutAppActivity.class);
						startActivity(i);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomeActivity.this, "About Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}	
				}
				else if(Name.equals("Booking"))
				{
					try {	

						Intent i = new Intent(HomeActivity.this,BookingStartActivity.class);
						startActivity(i);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomeActivity.this, "Booking Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}	
				}
				else if(Name.equals("Station Tracker"))
				{
					try 
					{
						if ((new ConnectionDetector(HomeActivity.this)).isConnectedToInternet()) {

							final ProgressDialog ringProgress = ProgressDialog.show(HomeActivity.this, "Please wait..",	"Synchronizing Data...", true);// Spinner
							ringProgress.setCancelable(false);
							Thread thMaster = new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									// 1 MR LatLong
									try {
										HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
										HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
										
										List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
										lvp.add(new BasicNameValuePair("flag", "10"));
										lvp.add(new BasicNameValuePair("IMEINo", LoginActivity.IMEINumber));
										lvp.add(new BasicNameValuePair("MobileNo","0"));
										lvp.add(new BasicNameValuePair("Id1","0"));
										lvp.add(new BasicNameValuePair("Id2","0"));
										lvp.add(new BasicNameValuePair("Id3","0"));
										lvp.add(new BasicNameValuePair("ApprovalNo","0"));				
										lvp.add(new BasicNameValuePair("Param1","0"));
										lvp.add(new BasicNameValuePair("Param2","0"));
										lvp.add(new BasicNameValuePair("Param3","0"));
										lvp.add(new BasicNameValuePair("Param4","0"));
										lvp.add(new BasicNameValuePair("Param5","0"));
										lvp.add(new BasicNameValuePair("Remarks","0"));	
										
										httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
										httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");						

										HttpResponse res = httpclt.execute(httpPost);
										HttpEntity ent = res.getEntity();
										if (ent != null) 
										{
											String rValue = EntityUtils.toString(ent);
											ReadServerResponse rfsr = new ReadServerResponse();
											ArrayList<String> lststr = rfsr.Readfilename(rValue);
											lstMap = new ArrayList<LatLong>();
											if (lststr.size()> 0) 
											{
												try 
												
												{											
													for (String stringdata : lststr) 
													{
														String[] str = stringdata.toString().split(",");
														LatLong ltlng = new LatLong();
														ltlng.setmId(str[1].trim());
														ltlng.setmName(str[2].trim());
														ltlng.setmAddress(str[3].trim());
														ltlng.setmLatitude(str[4].trim());
														ltlng.setmLongitude(str[5].trim());
														ltlng.setmPower(str[6].trim());
														ltlng.setmCost(str[7].trim());
														lstMap.add(ltlng);
													}										
													mainThreadHandler.post(new Runnable() 
													{
														@Override
														public void run() {
															CustomToast.makeText(HomeActivity.this,"Location Details Obtained.",Toast.LENGTH_SHORT);
															ringProgress.setCancelable(false);
															ringProgress.dismiss();
															Intent i = new Intent(HomeActivity.this,StationTrackerMap2Activity.class);// Redirect to Home Page
															startActivity(i);
														}
													});
												} 
												catch (Exception e) 
												{
													Log.d("", e.toString());
												}
												
											}
										}
									} 
									catch (Exception e) {
										Log.d("SendtoServer", e.toString());
										mainThreadHandler.post(new Runnable() {

											@Override
											public void run() {
												CustomToast.makeText(HomeActivity.this,"Synchronization with Server failed for MR Performance.",Toast.LENGTH_SHORT);
												return;
											}
										});								
									}	
									ringProgress.dismiss();	
								}
							});// .start();
							thMaster.start();
						} 
						else 
						{
							CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters();
							params.setContext(HomeActivity.this);
							params.setMainHandler(mainThreadHandler);
							params.setMsg("Please check your data connection. If turned OFF Turn ON and SYNC to get Latest Updated Data. ");
							params.setButtonOkText("OK");
							params.setTitle("Message");
							params.setFunctionality(1);
							CustomAlert cAlert = new CustomAlert(params);
						}
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						Toast toast = Toast.makeText(HomeActivity.this,"MrTracking Error", Toast.LENGTH_SHORT);
						toast.show();
						e.printStackTrace();
					}
				}
				
		
			}
		});	
		
		
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
		return OptionsMenu.navigate(item, HomeActivity.this);			
	}
	@Override
	public void onBackPressed() 
	{
		CustomToast.makeText(HomeActivity.this, "Back Button Disabled in this Screen.", Toast.LENGTH_SHORT);
		return;
	}
	
	

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		
	};

}
