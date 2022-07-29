package in.nsoft.evmanagementsystem;
/**
 * 
 * @author Nitish
 * @Desc  Schema For database
 * @Started  14-01-2016
 */
public interface Schema 
{
	//14-01-2016 
	public static final String TABLE_UserDetails = "UserDetails";	
	public static final String COL_Id_UserDetails = "UserDetails.Id";//TEXT
	public static final String COL_UserName_UserDetails = "UserDetails.UserName";//TEXT
	public static final String COL_UserCode_UserDetails = "UserDetails.UserCode";//TEXT
	public static final String COL_Designation_UserDetails ="UserDetails.Designation";//TEXT
	public static final String COL_EmailId_UserDetails ="UserDetails.EmailId";//TEXT
	public static final String COL_MobileNo_UserDetails = "UserDetails.MobileNo";//TEXT

	//14-01-2016 
	public static final String TABLE_Users = "Users";
	public static final String COL_UID_USERS = "Users.UID";//TEXT
	public static final String COL_IMEINO_USERS = "Users.IMEINO";//TEXT
	public static final String COL_USERNAME_USERS = "Users.username";//TEXT
	public static final String COL_PASSWORD_USERS = "Users.password";//TEXT
	public static final String COL_SYNCDATE = "Users.SyncDate";//TEXT
	public static final String COL_IsRegistered = "Users.IsRegistered";//TEXT
	public static final String COL_Latitude_USERS = "Users.Latitude";//TEXT
	public static final String COL_Longitude_USERS = "Users.Longitude";//TEXT
	public static final String COL_MobileNo_USERS = "Users.MobileNo";//TEXT
	
	
	public static final String TABLE_RechargeStationMaster ="RechargeStationMaster";
	public static final String COL_ID_RechargeStationMaster ="RechargeStationMaster.ID";
	public static final String COL_Name_RechargeStationMaster ="RechargeStationMaster.Name";
	public static final String COL_Desc_RechargeStationMaster ="RechargeStationMaster.Desc";

	
	public static final String TABLE_TypeOfChargingMaster ="TypeOfChargingMaster";
	public static final String COL_ID_TypeOfChargingMaster ="TypeOfChargingMaster.ID";
	public static final String COL_Name_TypeOfChargingMaster ="TypeOfChargingMaster.Name";
	public static final String COL_Desc_TypeOfChargingMaster ="TypeOfChargingMaster.Desc";
	
	
	
	public static final String TABLE_ChargingSolution = "ChargingSolution";
	public static final String COL_Id_RechargeBooking = "ChargingSolution.ChargeId";
	public static final String COL_Date_RechargeBooking = "ChargingSolution.Date";
	public static final String COL_Duration_RechargeBooking = "ChargingSolution.Duration";
	public static final String COL_Charge_RechargeBooking = "ChargingSolution.Charge";
	public static final String COL_Price_RechargeBooking = "ChargingSolution.Price";
	
	
	public static final String TABLE_ReservationChargeDetails = "ReservationChargeDetails";
	public static final String COL_Id_ReservationChargeDetails = "ReservationChargeDetails.Id";
	public static final String COL_Name_ReservationChargeDetails = "ReservationChargeDetails.Name";
	public static final String COL_Date_ReservationChargeDetails = "ReservationChargeDetails.Date";
	public static final String COL_Time_ReservationChargeDetails = "ReservationChargeDetails.Time";
	public static final String COL_Duration_ReservationChargeDetails = "ReservationChargeDetails.Duration";
	public static final String COL_ChargeType_ReservationChargeDetails = "ReservationChargeDetails.ChargeType";
	public static final String COL_FinalCharge_ReservationChargeDetails = "ReservationChargeDetails.FinalCharge";
	public static final String COL_Price_ReservationChargeDetails= "ReservationChargeDetails.Price";
	
	
}
