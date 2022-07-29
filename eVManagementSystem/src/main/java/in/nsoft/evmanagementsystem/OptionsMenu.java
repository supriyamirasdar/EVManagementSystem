package in.nsoft.evmanagementsystem;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

//Created Nitish 26/02/2014	
public class OptionsMenu  {	

	//Based on Menu Item Clicked move to Logout or Print Order or Take Order Screen.
	public static boolean navigate(MenuItem item, Context context)
	{	
		//Remove BillingObject and CollectionObject
		//BillingObject.Remove();
		DatabaseHelper mDb = new DatabaseHelper(context); //Nitish 12-05-2014
		//CollectionObject.Remove();
		switch(item.getItemId())
		{
		//Logout Move to LogIn Screen
		case R.id.menuLogout:								
			Intent logout = new Intent(context,LoginActivity.class);
			context.startActivity(logout);
			((Activity)context).finish();
			break;	
			
		case R.id.menuHome:								
			Intent home = new Intent(context,HomeActivity.class);
			context.startActivity(home);
			((Activity)context).finish();
			break;	
		case R.id.menuAbout:								
			Intent about = new Intent(context,AboutAppActivity.class);
			context.startActivity(about);					
			break;
	
		
		default: break;
		}	
		return true;
	}	

}



