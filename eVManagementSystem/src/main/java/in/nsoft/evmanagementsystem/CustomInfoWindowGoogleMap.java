package in.nsoft.evmanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap  implements GoogleMap.InfoWindowAdapter {

	private Context context;

	public CustomInfoWindowGoogleMap(Context ctx){
		context = ctx;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public View getInfoContents(Marker marker) {
		View view = ((Activity)context).getLayoutInflater()
				.inflate(R.layout.infowindowlayout, null);

		TextView lbl1 = (TextView) view.findViewById(R.id.lbl1);
		TextView lbl2 = (TextView) view.findViewById(R.id.lbl2);
		TextView lbl3 = (TextView) view.findViewById(R.id.lbl3);

		lbl1.setText(marker.getTitle());
		lbl2.setText(marker.getSnippet());

		lbl3.setText(marker.getId());

		

		return view;
	}
}