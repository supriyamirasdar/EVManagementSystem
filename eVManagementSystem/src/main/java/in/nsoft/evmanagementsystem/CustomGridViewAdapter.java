package in.nsoft.evmanagementsystem;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomGridViewAdapter extends ArrayAdapter<Item>{
	Context ctx;
	int layoutResourceId;
	ArrayList<Item> data = new ArrayList<Item>();

	public CustomGridViewAdapter(Context context, int layoutResId, ArrayList<Item> data1)
	{
		super(context, layoutResId, data1);
		ctx = context;
		layoutResourceId = layoutResId;
		data = data1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		RecordHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new RecordHolder();
			holder.txtTitle = (TextView)row.findViewById(R.id.item_text);
			holder.imageItem = (ImageView)row.findViewById(R.id.item_image);	
			holder.lytnot = (LinearLayout)row.findViewById(R.id.lyt_not);
			holder.txtnot = (TextView)row.findViewById(R.id.item_not);
			row.setTag(holder);			
		}
		else
		{
			holder = (RecordHolder)row.getTag();
		}
		Item item = data.get(position);
		
		holder.txtTitle.setText(item.Name);
		holder.imageItem.setImageBitmap(item.bitmap);
		
		if(!item.isRequired)
		{
			holder.lytnot.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.lytnot.setVisibility(View.VISIBLE);
			holder.lytnot.setBackground(new BitmapDrawable(item.bitmap1));
			holder.txtnot.setText(item.Name1);
		}
		return row;
	}
	
	static class RecordHolder
	{
		TextView txtTitle;
		ImageView imageItem;
		LinearLayout lytnot;
		TextView txtnot;
	}
}
