package in.nsoft.evmanagementsystem;

import java.text.ParseException;
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

import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class BookingStartActivity extends Activity implements AlertInterface{
	EditText txtRechargeDate;
	Spinner ddlStation,ddlFrom,ddlTo,ddlTypeOfCharge,ddlInitialCharge,ddlDesiredCharge;
	Button btnMap,btnProceed;
	DatabaseHelper db;
	Handler mainThreadHandler;
	CustomAlert cAlert;
	private int year;
	private int month;
	private int day;
	String stationId,FromId,ToId,TypOfChrgId,IniChrgId,DesiredChrgId;
	DDLAdapter aFrom,aTo,aTypeofChrg,aInichrg,aDeschrg;
	static ElectricVehicle VI;
	ActionBar bar;	
	ProgressDialog ringProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking_start);


		db = new DatabaseHelper(this);
		VI = new ElectricVehicle();
		mainThreadHandler = new Handler();

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



		txtRechargeDate = (EditText)findViewById(R.id.txtRechargeDate);
		ddlStation= (Spinner)findViewById(R.id.ddlStation);
		ddlFrom = (Spinner)findViewById(R.id.ddlFrom);
		ddlTo = (Spinner)findViewById(R.id.ddlTo);
		ddlTypeOfCharge = (Spinner)findViewById(R.id.ddlTypeOfCharge);
		ddlInitialCharge = (Spinner)findViewById(R.id.ddlInitialCharge);
		ddlDesiredCharge =(Spinner)findViewById(R.id.ddlDesiredCharge);
		btnMap = (Button) findViewById(R.id.btnMap);
		btnProceed = (Button) findViewById(R.id.btnProceed);


		stationId=FromId =ToId = TypOfChrgId=IniChrgId=DesiredChrgId = "0";

		try {
			
			
			ddlStation.setAdapter(db.getMasterData(4));
			ddlTypeOfCharge.setAdapter(db.getMasterData(5));
			ddlFrom.setAdapter(db.getEVTime());
			ddlTo.setAdapter(db.getEVTime());			
			ddlInitialCharge.setAdapter(db.getEVCharge());
			ddlDesiredCharge.setAdapter(db.getEVCharge());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		///////////Get Booking Station from STATION LOCATOR MAP .///////////////////
		String val = getIntent().getStringExtra("Key");			
		//String val=Id of Charging Station;
		if(val==null)
		{
			
		}
		else
		{
			try {
				ddlStation.setAdapter(db.getStationData(val));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		/////////////////////// End Station Locator MAP /////////////////////////////////////////

		txtRechargeDate.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {					
				try {	
					//For current Day Else Default Date -> 01-01-1900
					final Calendar c = Calendar.getInstance();
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);
					showDialog(0);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					CustomToast.makeText(BookingStartActivity.this, "Error in Calendar 1", Toast.LENGTH_SHORT);
					e.printStackTrace();
				}					
			}
		});

		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try
				{
					if(!LoginActivity.gpsTracker.isGPSConnected())
					{
						CustomToast.makeText(BookingStartActivity.this,
								"Please Enable GPS Location", Toast.LENGTH_LONG);
					}
					else
					{
						Intent i = new Intent(BookingStartActivity.this,MapTestActivity.class);//Redirect to Home Page startActivity(i);

						startActivity(i);
					}		
				}
				catch(Exception e)
				{
					Toast.makeText(BookingStartActivity.this,"Error in Location", Toast.LENGTH_LONG).show();
					return;
				}


			}
		});	

		ddlStation.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try

				{
					DDLItem k = (DDLItem)arg0.getItemAtPosition(arg2);
					stationId = k.getId();
				}
				catch(Exception e)
				{
					Log.d("", e.toString()); 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});	

		ddlFrom.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try

				{
					DDLItem k = (DDLItem)arg0.getItemAtPosition(arg2);
					FromId = k.getId();
					if(!FromId.equals("0"))
					{
						ddlTo.setAdapter(db.getEVTime());
					}

				}
				catch(Exception e)
				{
					Log.d("", e.toString()); 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});	

		ddlTo.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try

				{
					DDLItem k = (DDLItem)arg0.getItemAtPosition(arg2);
					ToId = k.getId();

				}
				catch(Exception e)
				{
					Log.d("", e.toString()); 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});	

		ddlTypeOfCharge.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try

				{
					DDLItem k = (DDLItem)arg0.getItemAtPosition(arg2);
					TypOfChrgId = k.getId();

				}
				catch(Exception e)
				{
					Log.d("", e.toString()); 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		ddlInitialCharge.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try

				{
					DDLItem k = (DDLItem)arg0.getItemAtPosition(arg2);
					IniChrgId = k.getId();
					if(!IniChrgId.equals("0"))
					{
						ddlDesiredCharge.setAdapter(db.getEVCharge());

					}

				}
				catch(Exception e)
				{
					Log.d("", e.toString()); 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});	
		ddlDesiredCharge.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try
				{
					DDLItem k = (DDLItem)arg0.getItemAtPosition(arg2);
					DesiredChrgId = k.getId();
				}
				catch(Exception e)
				{
					Log.d("", e.toString()); 
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});	
		////////////////////////////////////////////////////////////////////////
		btnProceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try
				{

					String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	

					if(ddlStation.getSelectedItem().toString().equals("--SELECT--"))
					{
						CustomToast.makeText(BookingStartActivity.this, "Please Select Station.", Toast.LENGTH_SHORT);
						return;
					}
					else if(txtRechargeDate.getText().toString().trim().length()== 0|| txtRechargeDate.getText().toString().trim().length()!=10)
					{
						CustomToast.makeText(BookingStartActivity.this, "Select Recharge Date.", Toast.LENGTH_SHORT);
						return;
					}
					else if(ddlFrom.getSelectedItem().toString().equals("--SELECT--"))
					{
						CustomToast.makeText(BookingStartActivity.this, "Please Select Recharge Time Availability From.", Toast.LENGTH_SHORT);
						return;
					}
					else if(ddlTo.getSelectedItem().toString().equals("--SELECT--"))
					{
						CustomToast.makeText(BookingStartActivity.this, "Please Select Recharge Time Availability To.", Toast.LENGTH_SHORT);
						return;
					}
					else if(Integer.parseInt(FromId) >= Integer.parseInt(ToId))
					{ 
						CustomToast.makeText(BookingStartActivity.this, "To Time should be greater than From Time.", Toast.LENGTH_SHORT);
						ddlTo.setAdapter(db.getEVTime());
						return;
					}
					else if(ddlTypeOfCharge.getSelectedItem().toString().equals("--SELECT--"))
					{
						CustomToast.makeText(BookingStartActivity.this, "Please Select Type Of Charge.", Toast.LENGTH_SHORT);
						return;
					}
					else if(ddlInitialCharge.getSelectedItem().toString().equals("--SELECT--"))
					{
						CustomToast.makeText(BookingStartActivity.this, "Please Select Initial Charge.", Toast.LENGTH_SHORT);
						return;
					}
					else if(ddlDesiredCharge.getSelectedItem().toString().equals("--SELECT--"))
					{
						CustomToast.makeText(BookingStartActivity.this, "Please Select Desired Charge.", Toast.LENGTH_SHORT);
						return;
					}
					else if(Integer.parseInt(IniChrgId) >= Integer.parseInt(DesiredChrgId))
					{ 
						CustomToast.makeText(BookingStartActivity.this, "Desired Charge should be greater than Inital Charge.", Toast.LENGTH_SHORT);
						ddlDesiredCharge.setAdapter(db.getEVCharge());
						return;
					}

					CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
					params.setContext(BookingStartActivity.this);					
					params.setMainHandler(mainThreadHandler);
					params.setMsg("Are you sure you want to obtained Charging Station Data?");
					params.setButtonOkText("NO");
					params.setButtonCancelText("YES");
					params.setTitle("Recharge Booking");
					params.setFunctionality(1);
					cAlert  = new CustomAlert(params);


				}
				catch(Exception e)
				{
					CustomToast.makeText(BookingStartActivity.this, "Failed to Save", Toast.LENGTH_SHORT);
				}




			}
		});
		///////////////////////////////////////////////////////////////////////
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.3  
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return OptionsMenu.navigate(item, BookingStartActivity.this);			
	}

	///////////////////////////////////////////////////
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			//05-05-2021
			String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			day = selectedDay;
			month = selectedMonth+1; // Month is 0 based, just add 1
			year = selectedYear;

			String mDay = "";
			String mMonth = "";
			String mYear = String.valueOf(year);

			if(day<10)
				mDay = "0" + String.valueOf(day);
			else
				mDay = String.valueOf(day);


			if(month<10)
				mMonth = "0" + String.valueOf(month);
			else
				mMonth = String.valueOf(month);        

			txtRechargeDate.setText(new StringBuilder().append(mDay).append("-").append(mMonth).append("-").append(mYear).append(" "));

			try {
				if(sdf.parse(txtRechargeDate.getText().toString()).before(sdf.parse(currentdate)))  //If Current Date is less then do not allow to generate Collection Receipts.
				{
					CustomToast.makeText(BookingStartActivity.this,"Invalid Recharge Date  " , Toast.LENGTH_LONG);
					txtRechargeDate.setText("");
					return;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	///////////////////////////////////////////////////

	@Override
	public void performAction(boolean alertResult, int functionality) {
		// TODO Auto-generated method stub

		if(functionality==0)
		{

		}		
		else if(!alertResult && functionality==1)
		{
			new Charging_InitialData().execute();	
		}
	}
	
	//////////////////////////ListView Load//////////////////////////////////
	private class Charging_InitialData extends AsyncTask<Void, Void, Void> {
		final ProgressDialog ringProgress =	ProgressDialog.show(BookingStartActivity.this, "Please wait..","Loading Data...",true);//Spinner
		String rValue = "";
		ArrayList<String> lststrComplainsStart;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpclt = new DefaultHttpClient();//object for HttpClient	
				HttpPost httpPost = new HttpPost(ConstantClass.IPAddress + "/Android_EVManagementSystem");//Live	
				List<NameValuePair> lvp = new ArrayList<NameValuePair>(1);	
				lvp.add(new BasicNameValuePair("flag", "6"));
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
					CustomToast.makeText(BookingStartActivity.this, "Charging Data Does Not Exist .", Toast.LENGTH_SHORT);


				}
				else if(lststrComplainsStart.get(0).toString().contains("ACK"))
				{

					try 
					{

						int val=db.insertchargeSolutionfromserver(lststrComplainsStart);								
						if(val>0)
						{  
							CustomToast.makeText(BookingStartActivity.this, "Complaint Data Obtained .", Toast.LENGTH_SHORT);
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
							Intent i = new Intent(BookingStartActivity.this,ChargingSolutionFirstActivity.class);
							startActivity(i);	
							

						}	
						else
						{
							ringProgress.setCancelable(false);
							ringProgress.dismiss();
							CustomToast.makeText(BookingStartActivity.this, "Data Not Available .", Toast.LENGTH_SHORT);	
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
					CustomToast.makeText(BookingStartActivity.this, "Error.", Toast.LENGTH_SHORT);
				}	
			}
			catch(Exception e)
			{
				ringProgress.setCancelable(false);
				ringProgress.dismiss();
				CustomToast.makeText(BookingStartActivity.this, "Error Occured.", Toast.LENGTH_SHORT);
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			ringProgress.setCancelable(false); 
			ringProgress.dismiss();	 
			CustomToast.makeText(BookingStartActivity.this,"Error Occured/No Response from server", Toast.LENGTH_LONG);

		}
	}


}
	