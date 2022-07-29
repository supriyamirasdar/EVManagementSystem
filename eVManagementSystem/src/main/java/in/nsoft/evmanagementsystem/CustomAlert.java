//Nitish 9/01/2015
package in.nsoft.evmanagementsystem;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

public final class CustomAlert {	
	
	private  Context mContext;
	private boolean ispos;
	private Handler mdh;	
	private int mfunctionality;
	final static String TAG = "�n.nsoft.materialreport.CustomAlert";
	
	public CustomAlert(CustomAlertParameters obj)
	{		
		mContext = obj.context;
		mdh = obj.mainHandler;
		mfunctionality=obj.functionality;
		try
		{		
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setCancelable(false);
			builder.setMessage(obj.msg);
			builder.setTitle(obj.title);				
			builder.setPositiveButton(obj.buttonOkText, new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub	
						ispos = true;
						if(mdh!=null)
						{
							mdh.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									((AlertInterface)mContext).performAction(ispos,mfunctionality);
									
								}
							});
						}
				}
			});
			builder.setNegativeButton(obj.buttonCancelText, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {				
					
						ispos = false;				
						mdh.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								((AlertInterface)mContext).performAction(ispos,mfunctionality);
								
							}
						});			
				}
			});
			builder.create();
			builder.show();	
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("",e.toString());
			e.printStackTrace();
		}	
	}	
	public final static class CustomAlertParameters
	{
		
		private Context context;
		private Handler mainHandler;
		private int functionality;
		private String msg,title,buttonOkText,buttonCancelText;	
		
		public Context getContext() {
			return context;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public void setContext(Context context) {
			this.context = context;
		}
		public Handler getMainHandler() {
			return mainHandler;
		}
		public void setMainHandler(Handler mainHandler) {
			this.mainHandler = mainHandler;
		}
		public int getFunctionality() {
			return functionality;
		}
		public void setFunctionality(int functionality) {
			this.functionality = functionality;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getButtonOkText() {
			return buttonOkText;
		}
		public void setButtonOkText(String buttonOkText) {
			this.buttonOkText = buttonOkText;
		}
		public String getButtonCancelText() {
			return buttonCancelText;
		}
		public void setButtonCancelText(String buttonCancelText) {
			this.buttonCancelText = buttonCancelText;
		}
	}
}

