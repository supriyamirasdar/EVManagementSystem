package in.nsoft.evmanagementsystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	DatabaseHelper db = new DatabaseHelper(this);
	static String IMEINumber = "";
	EditText txtUserName, txtPassword;
	TextView txtRegister;
	static String username,password = "";

	Thread th;
	static volatile boolean contTh = false;
	static volatile boolean GPSThread = false;

	static GPSTracker gpsTracker;
	private Handler handler;
	ActionBar bar;	
	ProgressDialog ringProgress;	
	Button btnSignIn;
	TextView lblSBBlinkText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		handler = new android.os.Handler();
		//ringProgress  = new ProgressDialog(this);
		if(gpsTracker == null)
			gpsTracker = new GPSTracker(LoginActivity.this);

		txtUserName = (EditText) findViewById(R.id.txtUserName);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtRegister = (TextView) findViewById(R.id.txtRegister);
		lblSBBlinkText = (TextView)findViewById(R.id.lblSBBlinkText);
		txtRegister.setPaintFlags(txtRegister.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		btnSignIn = (Button)findViewById(R.id.btnSignIn);

		
		bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(ConstantClass.ActionBarColor)));


		if(ConstantClass.IPAddress.equals("http://123.201.131.113:8112/EVManagementSystem.asmx"))
		{
			lblSBBlinkText.setText(ConstantClass.mTest);
		}
		else
		{
			lblSBBlinkText.setText(ConstantClass.mLive);
		}

		//##################################***Get Mobile Details***################################ 
		try
		{
			IMEINumber = CommonFunction.getDeviceNo(LoginActivity.this);
		}
		catch(Exception e)
		{

		}
		//##################################*******************######################################
		//##################################***At First Time Installation***################################
		SharedPreferences shpre = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isfirstRun = shpre.getBoolean("FirstRun", true);
		if(isfirstRun)//If it True, then first time installation of App.
		{
			InputStream myInput =null;
			try
			{
				db.VerifyUser("", "");
			}
			catch (Exception e)
			{  
				Log.d("btnTest", e.toString());
			}
			try
			{
				myInput = this.getAssets().open(ConstantClass.dbname);					
				OutputStream myOutput = new FileOutputStream(ConstantClass.rootDBPathWithDB);
				if(!new File(ConstantClass.rootDBPath).exists())
				{
					new File(ConstantClass.rootDBPath).mkdirs();
				}

				byte[] buffer = new byte[1024];
				int length;
				while((length = myInput.read(buffer))>0)
				{
					myOutput.write(buffer, 0, length);					
				}
				myOutput.flush();
				myOutput.close();				
				myInput.close();/**/				
				SharedPreferences.Editor edt = shpre.edit();
				edt.putBoolean("FirstRun", false);
				edt.commit();                  				


			}
			catch (Exception e)
			{
				Log.d("", e.toString());
			}
			finally
			{
				if(myInput != null)
				{
					try {
						myInput.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}/**/
				}
			}
		}	
		else//Other Not First Time 
		{

		}
		db.DropCreateTables();
		//////////////////////////////////////////////////Version ///////////////////////////////////////////////
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////

		/*if(!contTh)
			threadforSendtoserver();
		 */
		Intent i = new Intent(LoginActivity.this,HomeActivity.class);
		startActivity(i); 
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO Auto-generated method stub
				username= txtUserName.getText().toString();
				password =txtPassword.getText().toString();					
				if(username.trim().equals(""))
				{
					CustomToast.makeText(LoginActivity.this, "Enter User Name", Toast.LENGTH_SHORT);					
					return;
				}
				else if (password.trim().equals(""))
				{
					CustomToast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT);					
					return;
				}
				try
				{
					if((new ConnectionDetector(LoginActivity.this)).isConnectedToInternet())
					{
						if(LoginActivity.gpsTracker.isGPSConnected())
						{
							if((new ConnectionDetector(LoginActivity.this)).isConnectedToInternet())
							{
								ringProgress = ProgressDialog.show(LoginActivity.this, "Please wait..", "Loading...",true);
								new VersionCheckSync().execute();
							}
							else
							{	
								CustomToast.makeText(LoginActivity.this,"Please Enable Data Connection",
										Toast.LENGTH_SHORT);
							}
						} 
						else
						{
							CustomToast.makeText(LoginActivity.this,"Please Enable GPS Location.",
									Toast.LENGTH_SHORT);
							
						}
					}
					else
					{
						CustomToast.makeText(LoginActivity.this, "Check internet connection.", Toast.LENGTH_SHORT);
					}

				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					CustomToast.makeText(LoginActivity.this, "error Occured", Toast.LENGTH_SHORT);
					e.printStackTrace();
				}
			}
		});

		txtRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try
				{

					Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);//Redirect to Pair Bluetooth
					startActivity(i);

				} 
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					CustomToast.makeText(LoginActivity.this, "error Occured", Toast.LENGTH_SHORT);
					e.printStackTrace();
				}

			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, LoginActivity.this);			
	}




	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		return;
	}
	public  void performAction(boolean alertResult,int functionality) {

	}

	private class VersionCheckSync extends AsyncTask<Void, Void, Void> {

		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();// object for
				// HttpClient
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/VersionCheckEVManagementSystem");// HttpPost method uri
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("IMEINO", LoginActivity.IMEINumber));
				lvp.add(new BasicNameValuePair("Version",ConstantClass.AppVersion));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");

				HttpResponse res = httpclt.execute(httpPost);// execute post
				// method

				HttpEntity ent = res.getEntity();
				if (ent != null) {
					rValue = EntityUtils.toString(ent);
				}
			} catch (Exception e) {
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			if (rValue.equals("ACK")) // ACK received
			{
				CustomToast.makeText(LoginActivity.this, "Version Match",Toast.LENGTH_SHORT);
				btnSignIn.setVisibility(View.VISIBLE);
				ringProgress.setCancelable(false);
				ringProgress.dismiss();	
				new verifyUser().execute();
			} 

			else
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();	
				if (rValue.equals("NACK"))// NACK received
				{
					CustomToast.makeText(LoginActivity.this,"Version Mismatch.Download latest app..",Toast.LENGTH_SHORT);
					btnSignIn.setVisibility(View.GONE);
					new GetAPKFile().execute();
				} else {
					CustomToast.makeText(LoginActivity.this,
							"Version Check Failed", Toast.LENGTH_SHORT);
					return;
				}
			}
		}
		@Override
		protected void onCancelled() 
		{
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();			 
			CustomToast.makeText(LoginActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);
		}
	}

	private class GetAPKFile extends AsyncTask<Void, Void, Void> {
		//final ProgressDialog ringProgress =	ProgressDialog.show(LoginActivity.this, "Please wait..",	"Downloading Latest APP...",true);
		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String root = Environment.getExternalStorageDirectory()
						.getPath();// get the sd Card Path
				File f = new File(root + "/EVManagementSystem/");
				if (!f.exists()) {
					f.mkdir();
				}
				OutputStream myOutput = new FileOutputStream(f
						+ "/EVManagementSystem.apk");
				HttpClient httpclt = new DefaultHttpClient();// object for
				// HttpClient
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress
						+ "/GetApkFileEVManagementSystem");// HttpPost method uri
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);
				lvp.add(new BasicNameValuePair("IMEINO", LoginActivity.IMEINumber));
				lvp.add(new BasicNameValuePair("Version",ConstantClass.AppVersion));
				httpPost.setEntity((new UrlEncodedFormEntity(lvp)));
				httpPost.setHeader("Content-Type",	"application/x-www-form-urlencoded");
				HttpResponse res = httpclt.execute(httpPost);
				HttpEntity ent = res.getEntity();
				if (ent != null) {
					String rValue = EntityUtils.toString(ent);
					ReadServerResponse rfsr = new ReadServerResponse();
					FileDownload fd = rfsr.Read(rValue);
					int length = Integer.valueOf(fd.getMfileLength());
					byte[] buffer = new byte[1024];
					byte[] sd = fd.getmBytes().getBytes("UTF-8");
					byte[] sd1 = Base64.decode(sd, Base64.DEFAULT);
					if (length == sd1.length) {
						ByteArrayInputStream ipStream = new ByteArrayInputStream(
								sd1);
						while ((length = ipStream.read(buffer)) > 0) {
							myOutput.write(buffer, 0, length);
						}
						myOutput.flush();
						myOutput.close();
						// Auto Installation of an APK
						String vsName = Environment.getExternalStorageDirectory().getAbsolutePath()	+ "/EVManagementSystem/";
						File file = new File(vsName, "EVManagementSystem.apk");

						if(android.os.Build.VERSION.SDK_INT >= 29){
							//Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
							//intent.setData(Uri.fromFile(file));
							//intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							//startActivity(intent);   

							Uri urifromFile = FileProvider.getUriForFile(LoginActivity.this,"in.nsoft.evmanagementsystem.fileProvider", file);
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(urifromFile, "application/vnd.android.package-archive");
							intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							startActivity(intent);



						}else{
							Intent intent = new Intent(Intent.ACTION_VIEW);
							//output file is the apk downloaded earlier
							intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
						// End Auto Installation of an APK

					} else {
						cancel(true);
					}
				}
			} catch (Exception e) {
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ringProgress.setCancelable(false); 
			ringProgress.dismiss();
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss();
			CustomToast.makeText(LoginActivity.this,"App could not be downloaded", Toast.LENGTH_SHORT);
		}
	}
	/////////////////////////////////////Verify User/////////////////////////////////////////////////
	private class verifyUser extends AsyncTask<Void, Void, Void> 	
	{
		final ProgressDialog ringProgress = ProgressDialog.show(LoginActivity.this, "Please wait..", "App and User Validation...",true);
		String rValue = "";
		ArrayList<String> lststr;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try
			{
				try
				{
					HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
					HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
					List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
					lvp.add(new BasicNameValuePair("flag", "1"));
					lvp.add(new BasicNameValuePair("IMEINo", LoginActivity.IMEINumber));
					lvp.add(new BasicNameValuePair("MobileNo","0"));
					lvp.add(new BasicNameValuePair("Id1","0"));//username
					lvp.add(new BasicNameValuePair("Id2","0"));//password
					lvp.add(new BasicNameValuePair("Id3","0"));//ConstantClass.AppVersion
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
					if(ent != null)
					{
						rValue = EntityUtils.toString(ent);
						ReadServerResponse rfsr = new ReadServerResponse();
						lststr =  rfsr.Readfilename(rValue);


					}

				}
				catch(Exception e)
				{
					CustomToast.makeText(LoginActivity.this, "Error-1.", Toast.LENGTH_SHORT);
				}
			}
			catch (Exception e)
			{
				cancel(true);		
				e.printStackTrace();
			}
			return null;	
		}		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			try {

				if(lststr.get(0).toString().contains("NACK"))
				{					
					CustomToast.makeText(LoginActivity.this, "Profile does not Exist .", Toast.LENGTH_SHORT);
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
				}
				else if(lststr.get(0).toString().contains("ACK"))
				{
					/*CustomToast.makeText(LoginActivity.this, "User Validation Completed.", Toast.LENGTH_SHORT);
					//Intent i = new Intent(LoginActivity.this, GasStationActivity.class);//Redirect to Pair Bluetooth
					//startActivity(i);
					new  RechargeStationMaster().execute();*/
					try 
					{
						int ret=  db.insertUserDetails(lststr.get(0).toString());
						if (ret > 0)
						{						
							new  RechargeStationMaster().execute();

						}
						else
						{
							CustomToast.makeText(LoginActivity.this, "Could Not Sync.", Toast.LENGTH_SHORT);
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				else if(lststr.get(0).toString().contains("VM"))
				{

					CustomToast.makeText(LoginActivity.this, "App Version Mismatch.", Toast.LENGTH_SHORT);
				}
				else if(lststr.get(0).toString().contains("NATH"))
				{
					CustomToast.makeText(LoginActivity.this, "Not Authorised.", Toast.LENGTH_SHORT);
				}
				else if(lststr.get(0).toString().contains("NREG"))
				{
					CustomToast.makeText(LoginActivity.this, "Not Registered.", Toast.LENGTH_SHORT);
					txtUserName.setText("");
					txtPassword.setText("");
				}
				else
				{					
					CustomToast.makeText(LoginActivity.this, "Verification Error .", Toast.LENGTH_SHORT);

				}
			} 
			catch (Exception e) 
			{
				CustomToast.makeText(LoginActivity.this, "Error-2.", Toast.LENGTH_SHORT);
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(LoginActivity.this, "Error-3.", Toast.LENGTH_SHORT);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////

	/////////////////////Profile///////////////////////////////////////
	/*	private class StaffValidation extends AsyncTask<Void, Void, Void> {
		final ProgressDialog ringProgress =	ProgressDialog.show(LoginActivity.this, "Please wait..","Loading  Data...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "1"));
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
				if (ent != null) {
					rValue = EntityUtils.toString(ent);
					ReadServerResponse rfsr = new ReadServerResponse();
					lststr =  rfsr.Readfilename(rValue);
				}
			} catch (Exception e) {
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {


			try
			{
				if(lststr.get(0).toString().contains("NACK"))
				{					
					CustomToast.makeText(LoginActivity.this, "Profile does not Exist .", Toast.LENGTH_SHORT);
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
				}
				else if(lststr.get(0).toString().contains("ACK"))
				{

					try 
					{
						int ret=  db.insertUserDetails(lststr.get(0).toString());
						if (ret > 0)
						{						
							new  RechargeStationMaster().execute();

						}
						else
						{
							CustomToast.makeText(LoginActivity.this, "Could Not Sync.", Toast.LENGTH_SHORT);
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}												
				else
				{
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
					CustomToast.makeText(LoginActivity.this, "Error.", Toast.LENGTH_SHORT);
				}	
			}
			catch(Exception e)
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();
				CustomToast.makeText(LoginActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}

		}


		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss(); 
			CustomToast.makeText(LoginActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}

	}*/
	/////////////////////////////////////////
	private class RechargeStationMaster extends AsyncTask<Void, Void, Void> {
		//final ProgressDialog ringProgress1 =	ProgressDialog.show(LoginActivity.this, "Please wait..","Loading  Data...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "4"));
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
				if (ent != null) {
					rValue = EntityUtils.toString(ent);
					ReadServerResponse rfsr = new ReadServerResponse();
					lststr =  rfsr.Readfilename(rValue);
				}
			} catch (Exception e) {
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {


			try
			{
				if(lststr.get(0).toString().contains("NACK"))
				{					
					CustomToast.makeText(LoginActivity.this, "Profile does not Exist .", Toast.LENGTH_SHORT);
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
				}
				else if(lststr.get(0).toString().contains("ACK"))
				{

					try 
					{
						int ret=  db.insertMasterData(lststr,4);
						if (ret > 0)
						{						
							new  TypeOfChargingMaster().execute();

						}
						else
						{
							CustomToast.makeText(LoginActivity.this, "Could Not Sync.", Toast.LENGTH_SHORT);
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}												
				else
				{
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
					CustomToast.makeText(LoginActivity.this, "Error.", Toast.LENGTH_SHORT);
				}	
			}
			catch(Exception e)
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();
				CustomToast.makeText(LoginActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}

		}


		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss(); 
			CustomToast.makeText(LoginActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}

	}




	private class TypeOfChargingMaster extends AsyncTask<Void, Void, Void> {
		//final ProgressDialog ringProgress1 =	ProgressDialog.show(LoginActivity.this, "Please wait..","Loading  Data...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "5"));
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
				if (ent != null) {
					rValue = EntityUtils.toString(ent);
					ReadServerResponse rfsr = new ReadServerResponse();
					lststr =  rfsr.Readfilename(rValue);
				}
			} catch (Exception e) {
				cancel(true);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {


			try
			{
				if(lststr.get(0).toString().contains("NACK"))
				{					
					CustomToast.makeText(LoginActivity.this, "Type Of Charging does not Exist .", Toast.LENGTH_SHORT);
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
				}
				else if(lststr.get(0).toString().contains("ACK"))
				{

					try 
					{
						int ret=  db.insertMasterData(lststr,5);
						if (ret > 0)
						{						
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
							CustomToast.makeText(LoginActivity.this, "Data Sync Successfull.", Toast.LENGTH_SHORT);
							Intent i = new Intent(LoginActivity.this,HomeActivity.class);
							startActivity(i); 							

						}
						else
						{
							CustomToast.makeText(LoginActivity.this, "Could Not Sync.", Toast.LENGTH_SHORT);

						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}												
				else
				{
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
					CustomToast.makeText(LoginActivity.this, "Error.", Toast.LENGTH_SHORT);
				}	
			}
			catch(Exception e)
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();
				CustomToast.makeText(LoginActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}

		}


		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss(); 
			CustomToast.makeText(LoginActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}
	}
}
