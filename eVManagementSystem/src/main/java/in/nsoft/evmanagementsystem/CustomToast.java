//Nitish 9/01/2015
package in.nsoft.evmanagementsystem;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast   {
		
	public static void makeText(Context context, String text,int duration)
	{
		LayoutInflater inflator = ((Activity)context).getLayoutInflater();
		View toastRoot = inflator.inflate(R.layout.generictoast,null);		
		Toast toast = new Toast(context);				
		toast.setView(toastRoot); 				
		toast.setDuration(duration);		
		TextView txtToast = (TextView)toastRoot.findViewById(R.id.txtToast);
		txtToast.setText(text);				
		toast.show();
	}	
}
