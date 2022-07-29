package in.nsoft.evmanagementsystem;


import com.google.zxing.client.android.IntentIntegrator;
import com.google.zxing.client.android.IntentResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QRCodeActivity extends Activity  {

	
	Handler dh = new Handler();
	CustomAlert cAlert ;
	static String qrcodeValue = "";
	EditText txtFetchurl;
	Button btnProceed,btnScan;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode); 

		new IntentIntegrator(QRCodeActivity.this).initiateScan();
		//buttonQRCodeScan = (Button)findViewById(R.id.buttonQRCodeScan);

		/*buttonQRCodeScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Nitish 25-01-2016
				new IntentIntegrator(QRCodeActivity.this).initiateScan();

			}
		});	*/
		txtFetchurl =(EditText)findViewById(R.id.txtFetchurl);
		btnProceed =(Button)findViewById(R.id.btnProceed);
		btnScan = (Button)findViewById(R.id.btnScan);
		btnScan.setVisibility(View.GONE);
		
		btnProceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QRCodeActivity.this,OnlineWebViewActivity.class);
				startActivity(i);
			}
		});
		
		btnScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new IntentIntegrator(QRCodeActivity.this).initiateScan();
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
				resultCode, data);
		if (result != null) {
			qrcodeValue = result.getContents();
			if (qrcodeValue == null) {
				Log.d("QRCodeActivity", "Cancelled Scan");
				//Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
				CustomToast.makeText(QRCodeActivity.this," Scanning Cancelled." , Toast.LENGTH_SHORT);
				Intent i =new Intent(QRCodeActivity.this,HomeActivity.class);
				startActivity(i);
			} else {

				txtFetchurl.setText(qrcodeValue);
				txtFetchurl.setEnabled(false);
				btnScan.setVisibility(View.VISIBLE);





				/*			Log.d("QRCodeActivity", "Scanned");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(qrcodeValue);
				builder.setTitle("Scanning Result");
				builder.setCancelable(false);
				builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int arg1) {
	                   // onClick Function // for scan again
	                	new IntentIntegrator(QRCodeActivity.this).initiateScan();
	                }
	            });
				builder.setNegativeButton("finish", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						finish();
					}
				});
			   builder.create();
			   builder.show();*/


				/*	CustomAlert.CustomAlertParameters params = new CustomAlert.CustomAlertParameters(); 
				params.setContext(QRCodeActivity.this);					
				params.setMainHandler(dh);
				params.setMsg(result.getContents());
				params.setButtonOkText("No");
				params.setButtonCancelText("Yes");
				params.setTitle("Scanning Result");
				params.setFunctionality(1);
				cAlert  = new CustomAlert(params);	*/





				//Toast.makeText(this, "Scanned: " + result.getContents(),Toast.LENGTH_LONG).show();
				//Intent i =new Intent(QRCodeActivity.this,HomeActivity.class);
				//startActivity(i);

			}
		} else {
			// This is important, otherwise the result will not be passed to the
			// fragment
			super.onActivityResult(requestCode, resultCode, data);
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
		return OptionsMenu.navigate(item, QRCodeActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}











