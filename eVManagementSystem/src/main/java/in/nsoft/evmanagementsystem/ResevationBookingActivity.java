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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResevationBookingActivity extends Activity implements AlertInterface{
	TextView txtBookingId,txtName,txtDate,txtTime,txtDuration,txtChargeType,txtFinalCharge,txtPrice;
	Button btnConfirm,btnReject;
	ArrayList<ElectricVehicle> alElectricData;
	static String addReasonId = "0";
	DatabaseHelper db= new DatabaseHelper(this);
	Handler dh;
	static String ChargeID = "";
	ProgressDialog ringProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resevation_booking);
		
		dh = new Handler();

		
		txtBookingId = (TextView)findViewById(R.id.txtBookingId);
		txtName = (TextView)findViewById(R.id.txtName);
		txtTime = (TextView)findViewById(R.id.txtTime);
		txtDate = (TextView)findViewById(R.id.txtDate);
		txtDuration = (TextView)findViewById(R.id.txtDuration);
		txtChargeType = (TextView)findViewById(R.id.txtChargeType);
		txtFinalCharge = (TextView)findViewById(R.id.txtFinalCharge);
		txtPrice = (TextView)findViewById(R.id.txtPrice);
		
		
		btnConfirm = (Button)findViewById(R.id.btnConfirm);
		btnReject = (Button)findViewById(R.id.btnReject);
		
		alElectricData = db.getReservationBookingDetails();
		
		
		try
		{
			ChargeID = alElectricData.get(0).getmId();
			txtBookingId.setText(alElectricData.get(0).getmId());
			txtName.setText(alElectricData.get(0).getmCustomerName());
			txtDate.setText(alElectricData.get(0).getmRechargeDate());
			txtTime.setText(alElectricData.get(0).getmTimeAvailabilityFrom());
			txtDuration.setText(alElectricData.get(0).getmDuration());
			txtChargeType.setText(alElectricData.get(0).getmTypeOfCharge());
			txtFinalCharge.setText(alElectricData.get(0).getmCharge());
			txtPrice.setText(alElectricData.get(0).getmPrice());
		}
		catch(Exception e)
		{
			Log.d("a", "a");
			CustomToast.makeText(ResevationBookingActivity.this,  e.toString(), Toast.LENGTH_SHORT);
		}
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(ResevationBookingActivity.this);					
				params.setMainHandler(dh);
				params.setMsg("Are you Sure You Want To Confirm ? ");
				params.setButtonOkText("No");
				params.setButtonCancelText("Yes");
				params.setTitle("Confirm");
				params.setFunctionality(1);
				CustomAlert cAlert  = new CustomAlert(params);
			}
		});
		btnReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(ResevationBookingActivity.this);					
					params.setMainHandler(dh);
					params.setMsg("Are you Sure You Want To Reject ? ");
					params.setButtonOkText("No");
					params.setButtonCancelText("Yes");
					params.setTitle("Reject");
					params.setFunctionality(2);
					CustomAlert cAlert  = new CustomAlert(params);

				} catch (Exception e) {
					// TODO: handle exception
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
		return OptionsMenu.navigate(item, ResevationBookingActivity.this);			
	}


	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub
		
		if(functionality==0)
		{

		}		
		else if(!alertResult && functionality==1)
		{
			new ReservationDataConfirm().execute();		
		}
		else if(!alertResult && functionality==2)
		{
			new ReservationDataReject().execute();	
		}
		
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private class ReservationDataConfirm extends AsyncTask<Void, Void, Void> {
		final ProgressDialog ringProgress =	ProgressDialog.show(ResevationBookingActivity.this, "Please wait..","Loading Data...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststrComplainsStart;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "8"));
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
					lststrComplainsStart =  rfsr.Readfilename(rValue);
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
			try
			{			
				if(lststrComplainsStart.get(0).toString().contains("NACK"))
				{
					ringProgress.setCancelable(false); 
					ringProgress.dismiss();
					CustomToast.makeText(ResevationBookingActivity.this, "Booking Data Does Not Exist .", Toast.LENGTH_SHORT);


				}
				else if(lststrComplainsStart.get(0).toString().contains("ACK"))
				{

					try 
					{

						int val=db.insertReservationChargeDetailsfromserver(lststrComplainsStart);								
						if(val>0)
						{  
							CustomToast.makeText(ResevationBookingActivity.this, "Booking Confirm Successfully. .", Toast.LENGTH_SHORT);
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
							Intent i = new Intent(ResevationBookingActivity.this,HomeActivity.class);
							startActivity(i);	
							

						}	
						else
						{
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
							CustomToast.makeText(ResevationBookingActivity.this, "Data Not Available .", Toast.LENGTH_SHORT);	
						}
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else
				{
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
					CustomToast.makeText(ResevationBookingActivity.this, "Error.", Toast.LENGTH_SHORT);
				}	
			}
			catch(Exception e)
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();
				CustomToast.makeText(ResevationBookingActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss();	 
			CustomToast.makeText(ResevationBookingActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}
	}

	/////////////////////////////
	private class ReservationDataReject extends AsyncTask<Void, Void, Void> {
		final ProgressDialog ringProgress1 =	ProgressDialog.show(ResevationBookingActivity.this, "Please wait..","Saving Data...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststrFeasabilityStart;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live		
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "9"));
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
					lststrFeasabilityStart =  rfsr.Readfilename(rValue);
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
			//ringProgress.setCancelable(false);
			//ringProgress.dismiss();
			try
			{			
				if(lststrFeasabilityStart.get(0).toString().contains("NACK"))
				{
					CustomToast.makeText(ResevationBookingActivity.this, "Application  Data Does Not Exist .", Toast.LENGTH_SHORT);
					ringProgress1.setCancelable(false); 
					ringProgress1.dismiss();
				}
				else if(lststrFeasabilityStart.get(0).toString().contains("ACK"))
				{
					CustomToast.makeText(ResevationBookingActivity.this, "Booking Rejected Successfully!.", Toast.LENGTH_SHORT);
					ringProgress1.setCancelable(false);
					ringProgress1.dismiss();
					Intent i = new Intent(ResevationBookingActivity.this,HomeActivity.class);
					startActivity(i);		
				}												
				else
				{
					ringProgress1.setCancelable(false);
					ringProgress1.dismiss();
					CustomToast.makeText(ResevationBookingActivity.this, "Error.", Toast.LENGTH_SHORT);

				}	
			}
			catch(Exception e)
			{
				ringProgress1.setCancelable(false);
				ringProgress1.dismiss();
				CustomToast.makeText(ResevationBookingActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress1.setCancelable(false); 
			ringProgress1.dismiss();	 
			CustomToast.makeText(ResevationBookingActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}
	}

}
