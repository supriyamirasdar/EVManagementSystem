package in.nsoft.evmanagementsystem;


import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyProfileActivity extends Activity {

	TextView txtId,txtName,txtDesignation,txtEmailId,txtMobile,lblprofileheader;	
	DatabaseHelper db=new DatabaseHelper(this);
	UserDetails user=new UserDetails();	
	ActionBar bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		
		
		txtId=(TextView)findViewById(R.id.txtId);
		txtName=(TextView)findViewById(R.id.txtName);
		
		txtMobile=(TextView)findViewById(R.id.txtMobile);
		txtDesignation=(TextView)findViewById(R.id.txtDesignation);
		txtEmailId=(TextView)findViewById(R.id.txtEmailId);
	    lblprofileheader=(TextView)findViewById(R.id.lblprofileheader);
		
		bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(ConstantClass.ActionBarColor)));
	
		try 
		{
			user=db.getUserDetails();
			txtId.setText(user.getmID().toString());
			txtName.setText(user.getmUserName().toString());			
			txtDesignation.setText(user.getmDesignation().toString());
			txtEmailId.setText(user.getmEmailId().toString());
			txtMobile.setText(user.getmMobileNo().toString());	
			
			
		} 
		catch (Exception e)
		{			
			e.printStackTrace();
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
		return OptionsMenu.navigate(item, MyProfileActivity.this);			
	}

}
