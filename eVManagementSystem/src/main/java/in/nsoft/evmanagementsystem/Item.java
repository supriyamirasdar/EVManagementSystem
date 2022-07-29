package in.nsoft.evmanagementsystem;


import android.graphics.Bitmap;

public class Item {

	Bitmap bitmap;
	String Name;
	String Name1;
	Bitmap bitmap1;
	Boolean isRequired;

	
	public Item(Bitmap bm,Bitmap bm1, String name,String name1,boolean isReq) {
		bitmap = bm;
		Name = name;
		Name1 = name1;
		bitmap1 = bm1;
		isRequired = isReq;
	}
	
}
