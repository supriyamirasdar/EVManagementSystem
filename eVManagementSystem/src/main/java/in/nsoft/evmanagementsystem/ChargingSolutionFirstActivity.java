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
import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChargingSolutionFirstActivity extends Activity implements AlertInterface{

	ListView lstCharge;
	ReportAdapter adapter;
	DatabaseHelper db = new DatabaseHelper(this);	
	ProgressDialog ringProgress;
	Handler dh; 
	static String ChargeID = "";
	ActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charging_solution_first);

		dh = new Handler();
	


		try {

			bar=getActionBar();
			//bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ConstantClass.ActionBarColor)));	


			bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ConstantClass.ActionBarColor)));
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			bar.setDisplayShowTitleEnabled(true);
			bar.setIcon(color.transparent);	


		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		ArrayList<ElectricVehicle> divlist=db.getChargingSolution(); 
		adapter = new ReportAdapter(divlist);	
		lstCharge = (ListView)findViewById(R.id.lstCharge);
		lstCharge.setAdapter(adapter);	
		


		lstCharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {			
			@Override
			public void onItemClick(AdapterView<?> alist, View
					arg1, int pos,
					long arg3) {	
				try
				{
					ElectricVehicle k = (ElectricVehicle)  alist.getItemAtPosition(pos);
					ChargeID  = k.getmId();					

					if(new ConnectionDetector(ChargingSolutionFirstActivity.this).isConnectedToInternet())
					{
						ringProgress =	ProgressDialog.show(ChargingSolutionFirstActivity.this, "Please wait..","Loading Data...",true);//Spinner
						new Charge_Details().execute();	
					}
					else
					{
						CustomToast.makeText(ChargingSolutionFirstActivity.this, "Kindly Enable Mobile Internet Connection.", Toast.LENGTH_SHORT);
					}

				}

				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		});	
	}
	
	///////////////////////////
	//Class for List View
	private class ReportAdapter extends ArrayAdapter<ElectricVehicle>
	{
		ElectricVehicle olist;
		public ReportAdapter(ArrayList<ElectricVehicle> divList) {		
			super(ChargingSolutionFirstActivity.this, R.layout.list_chargingsolution, divList);		

		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			try
			{		
				if (convertView == null) {
					convertView = ChargingSolutionFirstActivity.this.getLayoutInflater().inflate(R.layout.list_chargingsolution, null);					
				}	

				olist =new ElectricVehicle();
				olist  = getItem(position);


				// Set List View Parameters
				TextView lblName,txtlist1,txtlist2,txtlist3,txtlist4;

				lblName = (TextView)convertView.findViewById(R.id.lblName);
				txtlist1 = (TextView)convertView.findViewById(R.id.txtlist1);
				txtlist2 = (TextView)convertView.findViewById(R.id.txtlist2);
				txtlist3 = (TextView)convertView.findViewById(R.id.txtlist3);
				txtlist4 = (TextView)convertView.findViewById(R.id.txtlist4);
				
				lblName.setText(olist.getmId());
				txtlist1.setText(olist.getmRechargeDate());
				txtlist2.setText(olist.getmDuration());
				txtlist3.setText(olist.getmCharge());
				txtlist4.setText(olist.getmPrice());

			}		
			catch(Exception e)
			{
				Toast.makeText(ChargingSolutionFirstActivity.this, "Error View " , Toast.LENGTH_LONG).show();			
			}
			return convertView;
		}
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
		return OptionsMenu.navigate(item, ChargingSolutionFirstActivity.this);			
	}
	//////////////////////////////
	////////Data for Feasability_InitialData
	private class Charge_Details extends AsyncTask<Void, Void, Void> {
		//final ProgressDialog ringProgress1 =	ProgressDialog.show(FeasabilityApprovalFirstActivity.this, "Please wait..","Loading Data...",true);//Spinner
		String rValue = "";
		ArrayList<String> alstr;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "7"));
				lvp.add(new BasicNameValuePair("IMEINo", LoginActivity.IMEINumber));
				lvp.add(new BasicNameValuePair("MobileNo","0"));
				lvp.add(new BasicNameValuePair("Id1",ChargeID));
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
					alstr =  rfsr.Readfilename(rValue);
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
				if(alstr.get(0).toString().contains("NACK"))
				{
					ringProgress.setCancelable(false);
					ringProgress.dismiss();
					CustomToast.makeText(ChargingSolutionFirstActivity.this, "Complaint Data Does Not Exist .", Toast.LENGTH_SHORT);

				}
				else if(alstr.get(0).toString().contains("ACK"))
				{
					try 
					{
						int ret=  db.insertReservationChargeDetailsfromserver(alstr);					
						if (ret > 0)
						{
							CustomToast.makeText(ChargingSolutionFirstActivity.this, "Complaint Data Obtained .", Toast.LENGTH_SHORT);
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
							Intent i = new Intent(ChargingSolutionFirstActivity.this,ResevationBookingActivity.class);
							startActivity(i);
						}
						else
						{

							CustomToast.makeText(ChargingSolutionFirstActivity.this, "Could Not Sync.", Toast.LENGTH_SHORT);
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
					CustomToast.makeText(ChargingSolutionFirstActivity.this, "Error.", Toast.LENGTH_SHORT);

				}	
			}
			catch(Exception e)
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();
				CustomToast.makeText(ChargingSolutionFirstActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss();	 
			CustomToast.makeText(ChargingSolutionFirstActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}
	}



	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub

	}

}

