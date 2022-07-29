package in.nsoft.evmanagementsystem;
//supriya 06-04-2021

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OnlineWebViewActivity extends Activity {
	WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_webview);
		webview = (WebView)findViewById(R.id.webview);
		
		if(new ConnectionDetector(OnlineWebViewActivity.this).isConnectedToInternet())
		{	
			final ProgressDialog pd = ProgressDialog.show(this, "", "Loading...",true);
			pd.setCancelable(false);

			
			webview.requestFocus();
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setLoadsImagesAutomatically(true);
			webview.getSettings().setBuiltInZoomControls(true);
			webview.getSettings().setDisplayZoomControls(false);
			webview.getSettings().setDomStorageEnabled(true); 
			webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			webview.setEnabled(true);
			webview.getSettings().setSavePassword(true);
			
	
			webview.setWebViewClient(new WebViewClient());
			webview.loadUrl(QRCodeActivity.qrcodeValue);
			//webview.loadUrl("http://192.168.4.3:9090/BENGAL_GAS/ConsumerInitialPayment.jsp");
		

			webview.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});
			webview.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					if (progress < 100) {
						pd.show();
					}
					if (progress == 100) {
						pd.dismiss();
					}
				}
			});
		}
		else
		{
			CustomToast.makeText(OnlineWebViewActivity.this, "Kindly Enable internet connection.", Toast.LENGTH_SHORT);
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
		return OptionsMenu.navigate(item, OnlineWebViewActivity.this);			
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
