package in.nsoft.evmanagementsystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class CommonFunction {

	private static final String TAG = CommonFunction.class.getName();

	
	public static String getCurrentTime()
	{
		String currentTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		return currentTime;
	}
	public static String getCurrentDate()
	{
		String currentTime = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		return currentTime;
	}
	public static String getFirstDateOfMonth()
	{
		String currentMonthYear = new SimpleDateFormat("MM-yyyy").format(Calendar.getInstance().getTime());
		return ("01-" + currentMonthYear) ;
	}
	public static String DateConvert(String date)//dd-MM-yyyy
	{
		String sp[] = date.split("-");
		String NewFormat = sp[2] + "-" + sp[1] + "-" + sp[0];
		return NewFormat;//yyyy-MM-dd
	}
	public static String GetStringforExpo(Double s)
	{
		NumberFormat fo = new DecimalFormat("#0.00");
		String k = fo.format(s);
		try
		{
			if(k.contains(",")) //EX:k =  3,22
			{
				k=k.replace("," , "."); //k = 3.22
			}
		}
		catch(Exception e)
		{

		}
		return k;
	}
	public static String getDeviceNo(Context context)
	{
		String DeviceId = "";
		try
		{
			
			if(Build.VERSION.SDK_INT >= 29)	
			{
				DeviceId = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
				if(DeviceId.length()>15)
					DeviceId = DeviceId.substring(DeviceId.length()-15, DeviceId.length());
				
			}
			
			else
			{
				TelephonyManager mgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				DeviceId = mgr.getDeviceId();
				
			}			
			
			
		}
		catch(Exception e)
		{
			Log.d("", "");		
		}
		return DeviceId;

	}
	// supriya 11-03-2021
	public static String getPhotoStream(String PhotoName,String dmaFolder)
	{
		FileInputStream myInput = null;
		//OutputStream myOutput = null;
		byte[] bufferFinal = null;
		String encImage = "";
		try
		{
			//imei_date_time
			//123456789012345_11032021_103020
			//String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
			// SplashScreenActivity.IMEINumber +"_SURVEYID_" +db.getMaxIdSurvey()+ "_" +currentdatetime + ".jpg";
			String folderdate = "";
			if(PhotoName != "")
			{
				String[] getSplitStr = PhotoName.split("_");
				if(getSplitStr.length == 4)
				{
					folderdate = getSplitStr[3].substring(0, 2) + "-" + getSplitStr[3].substring(2, 4) + "-" + getSplitStr[3].substring(4, 8);
				}				
			}
			/*String root = Environment.getExternalStorageDirectory().getPath();//get the sd Card Path			
			File f = new File(root+"/BengalGasDMA/"+folderdate+"/Photos", PhotoName);
			myInput = new FileInputStream(f);
			Bitmap bn = BitmapFactory.decodeStream(myInput);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Bitmap bm = Bitmap.createScaledBitmap(bn, 250, 250, false); //08-06-2016
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] b = baos.toByteArray();
			encImage = Base64.encodeToString(b, Base64.DEFAULT);*/
			
			//Added 31-08-2018
			String root = Environment.getExternalStorageDirectory().getPath();//get the sd Card Path		
			String filepath = "";
			filepath = root+"/EVManagementSystem/"+folderdate+ "/" + dmaFolder +"/Photos";

			File f = new File(filepath, PhotoName);
			//End 31-08-2018
			myInput = new FileInputStream(f);
			Bitmap bn = BitmapFactory.decodeStream(myInput);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//Bitmap bm = Bitmap.createScaledBitmap(bn, 400, 200, false); 
			//Added 06-09-2019
			Bitmap bm = null;
			if(f.length() > 6000000) //1 Kb = 1000 -> 6000 kb = 6000000
			{
				bm = Bitmap.createScaledBitmap(bn,(int)bn.getWidth()/8, (int)bn.getHeight()/8, false); 				
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				
			}
			else if(f.length() > 4000000) //1 Kb = 1000 -> 4000 kb = 4000000
			{
				bm = Bitmap.createScaledBitmap(bn,(int)bn.getWidth()/6, (int)bn.getHeight()/6, false); 				
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				
			}			
			else if(f.length() > 2000000) //1 Kb = 1000 -> 2000 kb = 2000000
			{
				bm = Bitmap.createScaledBitmap(bn,(int)bn.getWidth()/4, (int)bn.getHeight()/4, false); 				
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				
			}
			else if(f.length() > 1000000) //1 Kb = 1000 -> 1000 kb = 1000000
			{
				bm = Bitmap.createScaledBitmap(bn,(int)bn.getWidth()/3, (int)bn.getHeight()/3, false); 				
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				
			}
			else if(f.length() > 500000) //1 Kb = 1000 -> 1000 kb = 1000000
			{
				bm = Bitmap.createScaledBitmap(bn,(int)bn.getWidth()/2, (int)bn.getHeight()/2, false); 				
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				
			}
			else
			{
				bm = Bitmap.createScaledBitmap(bn, bn.getWidth(),  bn.getHeight(), false); 
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			}
			//End 06-09-2019
			
			//Bitmap bm = Bitmap.createScaledBitmap(bn, bn.getWidth(),  bn.getHeight(), false); 
			//Bitmap bm = resize(bn,300,200);
			//bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] b = baos.toByteArray();
			encImage = Base64.encodeToString(b, Base64.DEFAULT);
			
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
			bufferFinal = null;
			if(myInput != null)
			{
				try {
					myInput.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return encImage;
	}
	
}
