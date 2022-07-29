package in.nsoft.evmanagementsystem;

public class MyMarker {

	private Double mLatitude;
	private Double mLongitude;
	/* private String mbillcount; */
	private String mMrName;

	public MyMarker(Double latitude, Double longitude, String mrName) {

		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.setMMRName(mrName);

	}

	public Double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(Double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public Double getmLongitude() {
		return mLongitude;
	}

	public void setmlongitude(Double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getMMRName() {
		return mMrName;
	}

	public void setMMRName(String mMrName) {
		this.mMrName = mMrName;
	}
}
