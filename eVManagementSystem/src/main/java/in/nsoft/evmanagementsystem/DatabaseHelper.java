package in.nsoft.evmanagementsystem;
/**
 * 
 * @author Nitish
 * @Desc  This class will contain  functions for accessing database.
 * @Started  14-01-2016
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper implements Schema {
	private static final String DB_NAME = "EVManagementSystem.dat";
	private static final int VERSION = 1;//1,2
	private Context mcntx;
	private Cursor cr;	
	public DatabaseHelper(Context context) {
		super(context,DB_NAME,null,VERSION);
		mcntx=context;
		// TODO Auto-generated constructor stub		
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@Override //Added 29-11-2019
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		if (android.os.Build.VERSION.SDK_INT >= 28)
			db.disableWriteAheadLogging();
	}
	public boolean VerifyUser(String id,String pwd) throws Exception
	{

		cr=null ;
		boolean result=false;
		try {
			QueryParameters qParam=StoredProcedure.GetUser(id, pwd);
			cr=getReadableDatabase().rawQuery(qParam.getSql(),qParam.getSelectionArgs());

			if(cr.getCount()==1)
			{
				result= true;
				cr.moveToFirst();

			}
		} 
		catch (Exception e) {
			// TODO: handle exception
			throw e;   

		}
		finally
		{
			if(cr!=null)
				cr.close();

		}
		return result;
	}
	/////////////////////////////////////////////////
	
/////////////////////////////Registration///////////////////////////////////
	public void DropCreateTables()
	{
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();		

		sldb.execSQL("CREATE TABLE IF NOT EXISTS Users(UID INTEGER PRIMARY KEY,IMEINo TEXT,username TEXT,password TEXT, SyncDate TEXT,Latitude TEXT,Longitude TEXT,IsRegistered INTEGER,MobileNo TEXT);");
	}
	public UserData getUserData() throws Exception
	{
		UserData rb = new UserData();	
		Cursor crRep=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.getUserData();
			crRep = getReadableDatabase().rawQuery(qParam.getSql(), null);	
			if(crRep!=null)
			{
				if(crRep.getCount() > 0)//Cursor IF Start
				{							
					crRep.moveToFirst();												

					rb.setmUserName(crRep.getString(crRep.getColumnIndex("username")));
					rb.setmPassword(crRep.getString(crRep.getColumnIndex("password")));
					rb.setmSyncDate(crRep.getString(crRep.getColumnIndex("SyncDate")));
					rb.setmRegistered(crRep.getInt(crRep.getColumnIndex("IsRegistered")));
					rb.setmLatitude(crRep.getString(crRep.getColumnIndex("Latitude")));
					rb.setmLongitude(crRep.getString(crRep.getColumnIndex("Longitude")));
					rb.setmMobileNo(crRep.getString(crRep.getColumnIndex("MobileNo")));



				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally
		{
			if(crRep!=null)
			{
				crRep.close();
			}
		}
		return rb;
	}

	public int UpdateUserData(UserData ud,int flag)
	{
		int upStatus = 0;
		try
		{
			String currentdate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
			ContentValues valuesUpdateGPStbl = new ContentValues();
			if(flag==1) //Registration
			{
				valuesUpdateGPStbl.put("username", ud.getmUserName());
				valuesUpdateGPStbl.put("password", ud.getmPassword());
				valuesUpdateGPStbl.put("MobileNo", ud.getmMobileNo());
				valuesUpdateGPStbl.put("IsRegistered", "1");
				valuesUpdateGPStbl.put("IMEINO", LoginActivity.IMEINumber);
			}
			else //Sync
			{
				valuesUpdateGPStbl.put("SyncDate", currentdate);				
				valuesUpdateGPStbl.put("Latitude", ud.getmLatitude());
				valuesUpdateGPStbl.put("Longitude", ud.getmLongitude());
			}
			upStatus = getWritableDatabase().update("Users", valuesUpdateGPStbl, null,null);

		}
		catch(Exception e)
		{
			Log.d("", e.toString());
		}
		return upStatus;
	}
	
	////////////////////////////////////////////////////////////////





	

	public int insertUserDetails(String stringdata)
	{
		int rtvalue = 0;		
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();
		String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
		try                             

		{
			sldb.beginTransaction();
			sldb.execSQL("DROP TABLE IF EXISTS " + TABLE_UserDetails);
			sldb.execSQL("CREATE TABLE UserDetails(Id TEXT,UserName TEXT, UserCode TEXT,Designation TEXT,EmailId TEXT, MobileNo TEXT);");

			try {

				ContentValues valuesInsertMaintbl = new ContentValues();
				String[] str=	stringdata.toString().split(",");
				valuesInsertMaintbl.put("Id", str[1].trim());						
				valuesInsertMaintbl.put("UserName", str[2].trim());
				valuesInsertMaintbl.put("UserCode", str[3].trim());
				valuesInsertMaintbl.put("Designation", str[4].trim());						
				valuesInsertMaintbl.put("EmailId",str[5].trim());						
				valuesInsertMaintbl.put("MobileNo",str[6].trim());

				long insertResult = sldb.insert("UserDetails", null, valuesInsertMaintbl);					
				if(insertResult <= 0)
				{	
					sldb.endTransaction();
					throw new Exception("Insertion failed for UserDetails");
				}

			}
			catch (Exception e)
			{
				Log.d("", e.toString());
			}
			sldb.setTransactionSuccessful();
			rtvalue = 1;

		}
		catch(Exception e)
		{			
			Log.d("", e.toString());
			rtvalue = 0;		

		}	
		finally         
		{
			sldb.endTransaction();                                     
		}	

		return rtvalue;
	}
	public UserDetails getUserDetails() 
	{
		UserDetails u = new  UserDetails();

		Cursor crRep1=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.getUserDetails();
			crRep1=getReadableDatabase().rawQuery(qParam.getSql(),qParam.getSelectionArgs());	

			if(crRep1!=null)
			{
				for(int i=1; i<=crRep1.getCount();++i)
				{

					if(u==null)
					{
						u=new UserDetails();
					}
					if(i==1)				
						crRep1.moveToFirst();				
					else
						crRep1.moveToNext();

					u.setmID(crRep1.getString(crRep1.getColumnIndex(COL_Id_UserDetails)));
					u.setmUserName(crRep1.getString(crRep1.getColumnIndex(COL_UserName_UserDetails)));
					u.setmUserCode(crRep1.getString(crRep1.getColumnIndex(COL_UserCode_UserDetails)));
					u.setmDesignation(crRep1.getString(crRep1.getColumnIndex(COL_Designation_UserDetails)));
					u.setmEmailId(crRep1.getString(crRep1.getColumnIndex(COL_EmailId_UserDetails)));
					u.setmMobileNo(crRep1.getString(crRep1.getColumnIndex(COL_MobileNo_UserDetails)));


				}
			}
		} 
		catch (Exception e) 
		{
			Log.d("", "");
		}
		finally
		{
			if(crRep1!=null)
			{
				crRep1.close();
			}
		}
		return u;
	}	

	public DDLAdapter getMasterData(int flag) throws Exception
	{
		DDLAdapter conList = null;
		Cursor cr1 =null ;
		try
		{
			
			
			if(conList==null)
			{
				conList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());				
			}

			QueryParameters qParam = StoredProcedure.getMasterData(flag);
			cr1 = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			for(int i=1; i<=cr1.getCount();++i)
			{

				if(i==1)
					cr1.moveToFirst();
				else
					cr1.moveToNext();				
				conList.AddItem(cr1.getString(cr1.getColumnIndex("Id")), cr1.getString(cr1.getColumnIndex("Name")));

			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.d("", "");
		}

		finally
		{
			if(cr1!=null)
			{
				cr1.close();
			}
		}
		return conList;
	}
	public DDLAdapter getStationData(String id) throws Exception
	{
		DDLAdapter conList = null;
		Cursor cr1 =null ;
		try
		{
			
			
			if(conList==null)
			{
				conList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());				
			}

			QueryParameters qParam = StoredProcedure.getStationData(id);
			cr1 = getReadableDatabase().rawQuery(qParam.getSql(), qParam.getSelectionArgs());	
			for(int i=1; i<=cr1.getCount();++i)
			{

				if(i==1)
					cr1.moveToFirst();
				else
					cr1.moveToNext();				
				conList.AddItem(cr1.getString(cr1.getColumnIndex("Id")), cr1.getString(cr1.getColumnIndex("Name")));

			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.d("", "");
		}

		finally
		{
			if(cr1!=null)
			{
				cr1.close();
			}
		}
		return conList;
	}
	public DDLAdapter getEVTime() throws Exception
	{
		DDLAdapter conList = null;
		Cursor cr1 =null ;
		try
		{

			conList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			conList.AddItem("0", "--SELECT--");
			conList.AddItem("1", "1:00");
			conList.AddItem("2", "2:00");
			conList.AddItem("3", "3:00");
			conList.AddItem("4", "4:00");
			conList.AddItem("5", "5:00");
			conList.AddItem("6", "6:00");
			conList.AddItem("7", "7:00");
			conList.AddItem("8", "8:00");
			conList.AddItem("9", "9:00");
			conList.AddItem("10", "10:00");
			conList.AddItem("11", "11:00");
			conList.AddItem("12", "12:00");
			conList.AddItem("13", "13:00");
			conList.AddItem("14", "14:00");
			conList.AddItem("15", "15:00");
			conList.AddItem("16", "16:00");
			conList.AddItem("17", "17:00");
			conList.AddItem("18", "18:00");
			conList.AddItem("19", "19:00");
			conList.AddItem("20", "20:00");
			conList.AddItem("21", "21:00");
			conList.AddItem("22", "22:00");
			conList.AddItem("23", "23:00");
			conList.AddItem("24", "24:00");



		} catch (Exception e) {
			// TODO: handle exception
			Log.d("", "");
		}

		finally
		{
			if(cr1!=null)
			{
				cr1.close();
			}
		}
		return conList;
	}

	public DDLAdapter getEVCharge() throws Exception
	{
		DDLAdapter conList = null;
		Cursor cr1 =null ;
		try
		{

			conList=new DDLAdapter(mcntx,new ArrayList<DDLItem>());
			conList.AddItem("0", "--SELECT--");
			conList.AddItem("1", "10%");
			conList.AddItem("2", "20%");
			conList.AddItem("3", "30%");
			conList.AddItem("4", "40%");
			conList.AddItem("5", "50%");
			conList.AddItem("6", "60%");
			conList.AddItem("7", "70%");
			conList.AddItem("8", "80%");
			conList.AddItem("9", "90%");
			conList.AddItem("10", "100%");

		} catch (Exception e) {
			// TODO: handle exception
			Log.d("", "");
		}

		finally
		{
			if(cr1!=null)
			{
				cr1.close();
			}
		}
		return conList;
	}

	public int insertMasterData(ArrayList<String> lststr,int flag) throws Exception
	{		
		int rtvalue = 0;
		DatabaseHelper dh1 = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb1 = dh1.getWritableDatabase();		
		sldb1.beginTransaction();
		try
		{	
			if (lststr.size() > 0)
			{

				if(flag ==4)
				{
					sldb1.execSQL("DROP TABLE If Exists RechargeStationMaster;");
					sldb1.execSQL("CREATE TABLE IF NOT EXISTS RechargeStationMaster(Id TEXT,Name TEXT,Desc TEXT);");
				}
				else if(flag ==5)
				{
					sldb1.execSQL("DROP TABLE If Exists TypeOfChargingMaster;");
					sldb1.execSQL("CREATE TABLE IF NOT EXISTS TypeOfChargingMaster(Id TEXT,Name TEXT,Desc TEXT);");
				}				
				for (int i=0; i< lststr.size() ; i++)
				{					
					String str[] = lststr.get(i).split(",");

					ContentValues valuesInsertMaintbl = new ContentValues();
					valuesInsertMaintbl.put("Id", str[1].trim());
					valuesInsertMaintbl.put("Name",str[2].trim());
					valuesInsertMaintbl.put("Desc",str[3].trim());

					long insertResult = 0;			

					if(flag ==4)
					{

						insertResult = sldb1.insert("RechargeStationMaster", null, valuesInsertMaintbl);
					}
					else if(flag ==5)
					{

						insertResult = sldb1.insert("TypeOfChargingMaster", null, valuesInsertMaintbl);
					}					
					if(insertResult <= 0)
					{	
						sldb1.setTransactionSuccessful();			
						sldb1.endTransaction();
						throw new Exception("Insertion failed for Master Table");
					}						
					else 
						rtvalue = 1;

				}
			}	
			sldb1.setTransactionSuccessful();				

		}
		catch(Exception e)
		{
			Log.d("", e.toString());
			rtvalue = 0;			
		}	
		finally
		{
			sldb1.endTransaction();
		}
		return rtvalue;		
	}

	////////////////////////////////////////////////////////////////////
	public int	insertchargeSolutionfromserver(ArrayList<String> lststr)
	{
		int rtvalue = 0;		
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();		
		String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
		try                              
		{
			sldb.beginTransaction();
			sldb.execSQL("DROP TABLE If Exists ChargingSolution;");
			sldb.execSQL("CREATE TABLE IF NOT EXISTS ChargingSolution(Uid INTEGER PRIMARY KEY,ChargeId TEXT,Date TEXT, Duration TEXT ,Charge TEXT,Price TEXT);");

			if(lststr.size()>0)
			{
				try {
					for (String stringdata : lststr) 
					{
						ContentValues valuesInsertMaintbl = new ContentValues();
						String[] str=	stringdata.toString().split(",");	
						valuesInsertMaintbl.put("ChargeId", str[1].trim());	
						valuesInsertMaintbl.put("Date", str[2].trim());					
						valuesInsertMaintbl.put("Duration", str[3].trim());
						valuesInsertMaintbl.put("Charge", str[4].trim());
						valuesInsertMaintbl.put("Price", str[5].trim());




						long insertResult = sldb.insert("ChargingSolution", null, valuesInsertMaintbl);					
						if(insertResult <= 0)
						{	
							sldb.endTransaction();
							throw new Exception("Insertion failed for ChargingSolution");
						}
					}
				}
				catch (Exception e)
				{
					Log.d("", e.toString());
				}
				sldb.setTransactionSuccessful();
				rtvalue = 1;
			}			
		}
		catch(Exception e)
		{			
			Log.d("", e.toString());
			rtvalue = 0;
		}	
		finally         
		{
			sldb.endTransaction();                                     
		}	

		return rtvalue;
	}
	/////////////////////////////////////////////////////////////////////////////
	public ArrayList<ElectricVehicle> getChargingSolution()
	{
		ArrayList<ElectricVehicle> mList = new ArrayList<ElectricVehicle>();
		Cursor crRep1=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.getChargingSolution();
			crRep1 = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crRep1!=null)
			{
				for(int i=1; i<=crRep1.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ElectricVehicle>();
					}
					if(i==1)				
						crRep1.moveToFirst();				

					else
						crRep1.moveToNext();

					ElectricVehicle md = new ElectricVehicle();

					md.setmId(crRep1.getString(crRep1.getColumnIndex("ChargeId")));
					md.setmRechargeDate(crRep1.getString(crRep1.getColumnIndex("Date")));
					md.setmDuration(crRep1.getString(crRep1.getColumnIndex("Duration")));
					md.setmCharge(crRep1.getString(crRep1.getColumnIndex("Charge")));
					md.setmPrice(crRep1.getString(crRep1.getColumnIndex("Price")));
					mList.add(md);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.d("", "");
		}
		finally
		{
			if(crRep1!=null)
			{
				crRep1.close();
			}
		}
		return mList;
	}

	public int	insertReservationChargeDetailsfromserver(ArrayList<String> lststr)
	{
		int rtvalue = 0;		
		DatabaseHelper dh = new DatabaseHelper(mcntx);
		SQLiteDatabase sldb = dh.getWritableDatabase();		
		String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
		try                              
		{
			sldb.beginTransaction();
			sldb.execSQL("DROP TABLE If Exists ReservationChargeDetails;");
			sldb.execSQL("CREATE TABLE IF NOT EXISTS ReservationChargeDetails(Id TEXT,Name TEXT ,Date TEXT,MobileNo TEXT,Time TEXT,Duration TEXT,ChargeType TEXT,FinalCharge TEXT,Price TEXT);");

			if(lststr.size()>0)
			{
				try {
					for (String stringdata : lststr) 
					{
						ContentValues valuesInsertMaintbl = new ContentValues();
						String[] str=	stringdata.toString().split(",");						
						valuesInsertMaintbl.put("Id", str[1].trim());					
						valuesInsertMaintbl.put("Name", str[2].trim());
						valuesInsertMaintbl.put("Date", str[3].trim());
						valuesInsertMaintbl.put("Time", str[4].trim());								
						valuesInsertMaintbl.put("Duration", str[5].trim());
						valuesInsertMaintbl.put("ChargeType", str[6].trim());
						valuesInsertMaintbl.put("FinalCharge", str[7].trim());								
						valuesInsertMaintbl.put("Price", str[8].trim());

						long insertResult = sldb.insert("ReservationChargeDetails", null, valuesInsertMaintbl);					
						if(insertResult <= 0)
						{	
							sldb.endTransaction();
							throw new Exception("Insertion failed for ReservationChargeDetails");
						}
					}
				}
				catch (Exception e)
				{
					Log.d("", e.toString());
				}
				sldb.setTransactionSuccessful();
				rtvalue = 1;
			}			
		}
		catch(Exception e)
		{			
			Log.d("", e.toString());
			rtvalue = 0;
		}	
		finally         
		{
			sldb.endTransaction();                                     
		}	

		return rtvalue;
	}
	public ArrayList<ElectricVehicle> getReservationBookingDetails()
	{
		ArrayList<ElectricVehicle> mList = new ArrayList<ElectricVehicle>();
		Cursor crRep1=null ;
		try
		{
			QueryParameters qParam = StoredProcedure.getReservationBookingDetails();
			crRep1 = getReadableDatabase().rawQuery(qParam.getSql(), null);
			if(crRep1!=null)
			{
				for(int i=1; i<=crRep1.getCount();++i)
				{

					if(mList==null)
					{
						mList=new ArrayList<ElectricVehicle>();
					}
					if(i==1)				
						crRep1.moveToFirst();				
					else
						crRep1.moveToNext();

					ElectricVehicle md = new ElectricVehicle();


					md.setmId(crRep1.getString(crRep1.getColumnIndex("Id")));
					md.setmCustomerName(crRep1.getString(crRep1.getColumnIndex("Name")));
					md.setmRechargeDate(crRep1.getString(crRep1.getColumnIndex("Date")));
					md.setmTimeAvailabilityFrom(crRep1.getString(crRep1.getColumnIndex("Time")));
					md.setmDuration(crRep1.getString(crRep1.getColumnIndex("Duration")));
					md.setmTypeOfCharge(crRep1.getString(crRep1.getColumnIndex("ChargeType")));
					md.setmCharge(crRep1.getString(crRep1.getColumnIndex("FinalCharge")));
					md.setmPrice(crRep1.getString(crRep1.getColumnIndex("Price")));


					mList.add(md);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.d("", "");
		}
		finally
		{
			if(crRep1!=null)
			{
				crRep1.close();
			}
		}
		return mList;
	}	
	///////////////////////////////////////////////////////////////////////////////////////////


}
