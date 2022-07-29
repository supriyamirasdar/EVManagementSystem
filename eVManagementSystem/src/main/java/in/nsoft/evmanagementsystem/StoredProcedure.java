package in.nsoft.evmanagementsystem;

import in.nsoft.evmanagementsystem.QueryParameters;

public class StoredProcedure implements Schema {

	public static QueryParameters GetUser(String id,String pwd)
	{	
	QueryParameters qParam=new QueryParameters();
	qParam.setSql("select "+ COL_USERNAME_USERS +" from "+TABLE_Users+ " where "+COL_USERNAME_USERS+ " = ? and  " +COL_PASSWORD_USERS+ " = ?");
	qParam.setSelectionArgs(new String[]{id+"",pwd+""});
	return qParam;	
	}
	public static QueryParameters getUserData()
	{
		QueryParameters qParam=new QueryParameters();
		qParam.setSql("select "+ COL_USERNAME_USERS + ","+ COL_PASSWORD_USERS + "," +COL_SYNCDATE + "," +COL_IsRegistered + "," +COL_MobileNo_USERS +"," +COL_Latitude_USERS + "," +COL_Longitude_USERS +" from "+TABLE_Users);
		return qParam;	
	}
	public static QueryParameters getUserDetails()
	{	
		QueryParameters qParam=new QueryParameters();
	    qParam.setSql(" select " + COL_Id_UserDetails +  " ," +
	    		" " + COL_UserName_UserDetails + " , " + COL_UserCode_UserDetails +
			" , " +	COL_Designation_UserDetails	+ " , " +  COL_EmailId_UserDetails + " , " +  COL_MobileNo_UserDetails +" from " + TABLE_UserDetails);


	return qParam;
	}

	public static QueryParameters getMasterData(int flag)
	{	
		QueryParameters qParam=new QueryParameters();

		if(flag == 4)
			qParam.setSql(" select " + COL_ID_RechargeStationMaster + " as [Id] , " + COL_Name_RechargeStationMaster  + " as [Name] , " + COL_Desc_RechargeStationMaster +" as [Desc] from " + TABLE_RechargeStationMaster );
		else if(flag == 5)
			qParam.setSql(" select " + COL_ID_TypeOfChargingMaster + " as [Id] , " + COL_Name_TypeOfChargingMaster  + " as [Name] , " + COL_Desc_TypeOfChargingMaster +" as [Desc]  from " + TABLE_TypeOfChargingMaster );

		return qParam;	

	}		
	public static QueryParameters getStationData(String id)
	{	
		QueryParameters qParam=new QueryParameters();
		qParam.setSql(" select " + COL_ID_RechargeStationMaster + " as [Id] , " + COL_Name_RechargeStationMaster  + " as [Name] , " + COL_Desc_RechargeStationMaster +" as [Desc] from " + TABLE_RechargeStationMaster  + " Where Id = ?");
		qParam.setSelectionArgs(new String[]{id});
		return qParam;	

	}	

	public static QueryParameters getChargingSolution()
	{	
		QueryParameters qParam=new QueryParameters();
		qParam.setSql(" Select " + COL_Id_RechargeBooking + "," + COL_Date_RechargeBooking+ " , " + COL_Duration_RechargeBooking + " , " + COL_Charge_RechargeBooking + " , " +COL_Price_RechargeBooking
				+ " from " + TABLE_ChargingSolution);
		return qParam;	
	}

	public static QueryParameters getReservationBookingDetails()
	{	
		QueryParameters qParam=new QueryParameters();
		qParam.setSql(" Select " + COL_Id_ReservationChargeDetails+ " , " + COL_Name_ReservationChargeDetails + "," + COL_Date_ReservationChargeDetails + " , " + COL_Time_ReservationChargeDetails + " , " +COL_Duration_ReservationChargeDetails 
				+ " , " + COL_ChargeType_ReservationChargeDetails + "," + COL_FinalCharge_ReservationChargeDetails + "," + COL_Price_ReservationChargeDetails  
				+ " from " + TABLE_ReservationChargeDetails);
		return qParam;	
	}

}
