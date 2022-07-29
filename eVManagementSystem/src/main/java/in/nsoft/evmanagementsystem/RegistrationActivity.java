package in.nsoft.evmanagementsystem;



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

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	static String IMEINumber = "";
	EditText txtUserName, txtPassword,txtCPassword,txtPhoneNo,txtotp;
	static GPSTracker gpsTracker ;
	private boolean isInternetConnected = false;
	Thread th;
	static volatile boolean contTh = false;
	static volatile boolean GPSThread = false;
	DatabaseHelper db =new DatabaseHelper(this);
	Handler mainThreadHandler;
	Button btnRegister,btnConfirm;
	ArrayList<String> lststr;
	LinearLayout lytRegisterInfo,lytOtpInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);


		lytRegisterInfo = (LinearLayout)findViewById(R.id.lytRegisterInfo);
		txtUserName =(EditText)findViewById(R.id.txtUserName);
		txtPassword =(EditText)findViewById(R.id.txtPassword);
		txtCPassword =(EditText)findViewById(R.id.txtCPassword);
		txtPhoneNo =(EditText)findViewById(R.id.txtPhoneNo);
		btnRegister =(Button)findViewById(R.id.btnRegister);

		lytOtpInfo = (LinearLayout)findViewById(R.id.lytOtpInfo);
		txtotp = (EditText)findViewById(R.id.txtotp);
		btnConfirm = (Button)findViewById(R.id.btnConfirm);
		lytOtpInfo.setVisibility(View.GONE);

		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try
				{

					if(txtUserName.getText().toString().length()== 0)
					{

						CustomToast.makeText(RegistrationActivity.this, "Please enter the user name", Toast.LENGTH_SHORT);
						return;

					}
					else if(txtPassword.getText().toString().length() == 0)
					{

						CustomToast.makeText(RegistrationActivity.this, "Please enter the password", Toast.LENGTH_SHORT);
						return;

					}
					else if(txtCPassword.getText().toString().length()== 0)
					{

						CustomToast.makeText(RegistrationActivity.this, "Please enter the confirm password", Toast.LENGTH_SHORT);
						return;

					}
					else if(!txtCPassword.getText().toString().equals(txtPassword.getText().toString()))
					{

						CustomToast.makeText(RegistrationActivity.this, "Password Mismatch.", Toast.LENGTH_SHORT);
						txtCPassword.setText("");
						return;

					}
					else if(txtPhoneNo.getText().toString().trim().length()==0 || txtPhoneNo.getText().toString().trim().length()!=10)
					{
						CustomToast.makeText(RegistrationActivity.this, "Enter Phone No.", Toast.LENGTH_SHORT);
						return;
					}
					//07-04-2021
					else if(!(txtPhoneNo.getText().toString().startsWith("6") ||txtPhoneNo.getText().toString().startsWith("7") || txtPhoneNo.getText().toString().startsWith("8") ||txtPhoneNo.getText().toString().startsWith("9") ))
					{
						CustomToast.makeText(RegistrationActivity.this, "Enter Valid PhoneNo.", Toast.LENGTH_SHORT);
						txtPhoneNo.setText("");
						return;
					}

					if(new ConnectionDetector(RegistrationActivity.this).isConnectedToInternet())
					{
						new registerUser().execute();

					}
					else
					{
						CustomToast.makeText(RegistrationActivity.this, "Check internet connection.", Toast.LENGTH_SHORT);
					}					
				}
				catch(Exception e)
				{
					Toast toast = Toast.makeText(RegistrationActivity.this, "Reports Error", Toast.LENGTH_SHORT);
					toast.show();
					e.printStackTrace();
				}
			} 
		});

		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try
				{

					if(txtotp.getText().toString().length()== 0)
					{

						CustomToast.makeText(RegistrationActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT);
						return;

					}
					if(new ConnectionDetector(RegistrationActivity.this).isConnectedToInternet())
					{
						new otpVerify().execute();

					}
					else
					{
						CustomToast.makeText(RegistrationActivity.this, "Check internet connection.", Toast.LENGTH_SHORT);
					}					
				}
				catch(Exception e)
				{
					Toast toast = Toast.makeText(RegistrationActivity.this, "Reports Error", Toast.LENGTH_SHORT);
					toast.show();
					e.printStackTrace();
				}
			} 
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	private class registerUser extends AsyncTask<Void, Void, Void> 	
	{
		final ProgressDialog ringProgress = ProgressDialog.show(RegistrationActivity.this, "Please wait..", "User Registration...",true);
		String rValue = "";
		ArrayList<String> lststr;
		UserData ud;
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
					lvp.add(new BasicNameValuePair("flag", "2"));
					lvp.add(new BasicNameValuePair("IMEINo", LoginActivity.IMEINumber));
					lvp.add(new BasicNameValuePair("MobileNo","0"));//txtPhoneNo.getText().toString()
					lvp.add(new BasicNameValuePair("Id1","0"));//txtUserName.getText().toString()
					lvp.add(new BasicNameValuePair("Id2", "0"));//txtPassword.getText().toString()
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
					if(ent != null)
					{
						rValue = EntityUtils.toString(ent);
						ReadServerResponse rfsr = new ReadServerResponse();
						lststr =  rfsr.Readfilename(rValue);
					}

				}
				catch(Exception e)

				{
					CustomToast.makeText(RegistrationActivity.this, "Error-1.", Toast.LENGTH_SHORT);
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


				if(lststr.get(0).toString().contains("ACK"))
				{
					
					
					lytRegisterInfo.setVisibility(View.GONE);
					lytOtpInfo.setVisibility(View.VISIBLE);

				}
				else if(lststr.get(0).toString().contains("AREG"))
				{
					CustomToast.makeText(RegistrationActivity.this, "User Already Registered.", Toast.LENGTH_SHORT);
					Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);//Redirect to Pair Bluetooth
					startActivity(i);
					finish();
				}
				else if(lststr.get(0).toString().contains("NATH"))
				{
					CustomToast.makeText(RegistrationActivity.this, "Not Authorised.", Toast.LENGTH_SHORT);
				}
				else if(lststr.get(0).toString().contains("NREG"))
				{
					CustomToast.makeText(RegistrationActivity.this, "Not Registered.", Toast.LENGTH_SHORT);
				}
				else
				{					
					CustomToast.makeText(RegistrationActivity.this, "Registration Error .", Toast.LENGTH_SHORT);

				}
			} 
			catch (Exception e) 
			{
				CustomToast.makeText(RegistrationActivity.this, "Error-2.", Toast.LENGTH_SHORT);
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false);
			ringProgress.dismiss();
			CustomToast.makeText(RegistrationActivity.this, "Error-3.", Toast.LENGTH_SHORT);
		}
	}

	////////////////////////////////////////////////otpverify///////////////////////////////////////////////////
	private class otpVerify extends AsyncTask<Void, Void, Void> {
		final ProgressDialog ringProgress =	ProgressDialog.show(RegistrationActivity.this, "Please wait..","Verifying OTP...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststr;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "3"));
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
					CustomToast.makeText(RegistrationActivity.this, "OTP does not Exist .", Toast.LENGTH_SHORT);
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
				}
				else if(lststr.get(0).toString().contains("ACK"))
				{

					try 
					{

						ringProgress.setCancelable(false);
						ringProgress.dismiss();
						CustomToast.makeText(RegistrationActivity.this, "Registration Completed.", Toast.LENGTH_SHORT);
						Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
						startActivity(i); 							


					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}												
				else
				{
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
					CustomToast.makeText(RegistrationActivity.this, "Error.", Toast.LENGTH_SHORT);
				}	
			}
			catch(Exception e)
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();
				CustomToast.makeText(RegistrationActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}

		}


		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss(); 
			CustomToast.makeText(RegistrationActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}
	}
}
