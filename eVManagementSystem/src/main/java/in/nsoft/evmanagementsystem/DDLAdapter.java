package in.nsoft.evmanagementsystem;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public final class DDLAdapter extends ArrayAdapter<DDLItem> {
	private Context mCntx;
private ArrayList<DDLItem> mList;

	public DDLAdapter(Context context,ArrayList<DDLItem> list) {
		
		
		super(context, R.layout.genericddl,list);
		// TODO Auto-generated constructor stub
		mCntx=context;
		mList=list;		
		}
	
@Override
public View getDropDownView(int position, View convertView,
ViewGroup parent) {
// TODO Auto-generated method stub
return getCustomView(position, convertView, parent);
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
return getCustomView(position, convertView, parent);
}

public View getCustomView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
//return super.getView(position, convertView, parent);

LayoutInflater inflater=((Activity)mCntx).getLayoutInflater();
View row=inflater.inflate(R.layout.genericddl, parent, false);
TextView label=(TextView)row.findViewById(R.id.ddlText);
label.setText(GetValue(position));

return row;
}	
public void ClearList()
{
	if(mList!=null)
	mList.clear();
}

public void AddItem(String id,String value)	
{
	DDLItem item= new DDLItem( id,value);
	mList.add(item);
}
public String GetId(int index)
{
	return mList.get(index).getId();
}
public String GetValue(int index)
{
	return mList.get(index).getValue();
}
}
