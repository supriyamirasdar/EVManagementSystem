package in.nsoft.evmanagementsystem;

/**
 * 
 * @author Nitish
 * @Desc  This class will contain all static variables used in Project
 * @Started  14-01-2016
 */                       
 public class ConstantClass 
{	                             

	public final static String AppVersion = "1.0";//File Version

	//Test
	//public final static String IPAddress = "http://123.201.131.113:8112/EVManagementSystem.asmx";//Testing IP Address 		
	public final static String IPAddress = "http://123.201.131.113:8112/EVManagementSystem.asmx";
	
	//Live		
	//public final static String IPAddress =  "https://gprs-webservices.nsoft.in/BengalGas.asmx";//Live IP Address	
	//public final static String IPAddress =  "http://124.153.117.121:8185/BescomCustomer.asmx";//Live IP Address		
	public final static String rootDBPath = "/data/data/in.nsoft.evmanagementsystem/databases";
	public final static String rootDBPathWithDB = "/data/data/in.nsoft.evmanagementsystem/databases/EVManagementSystem.dat";
	public final static String dbname = "EVManagementSystem.dat";	

	public final static String ActionBarColor = "#006400";	
	public final static String White = "#FFFFFF";	             


	
	public final static String mTest = "Test  AppVersion " + AppVersion;
	public final static String mLive = "Live  AppVersion " + AppVersion;


}
