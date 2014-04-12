package com.sekhar.assignment;

import java.io.File;
import java.io.IOException;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class MapView extends FragmentActivity {

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		DBAdapter.getInstance(this).open();
		final Cursor cursor = DBAdapter.getInstance(this).getAllRows();
		final int count = cursor.getCount();
		Log.i("MapView: Cursor count:", "" + count);
		
		// use cursor to work with current item
			new Thread(new Runnable() {
				@Override
				public void run() {
					
					
					for(int i = 0; i < count; i++) {
					// TODO Auto-generated method stub
					try {
						File f = new File(cursor.getString(DBAdapter.COL_IMAGE_PATH));
						//Using a library Picasso developed by onesquare to get the bitmaps asynchronously from file's
						final Bitmap bitmap = Picasso.with(getBaseContext()).load(f).resize(50, 50).centerCrop().get();

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								
								// TODO Auto-generated method stub
								map.addMarker(new MarkerOptions()
										.position(
												new LatLng(Double.parseDouble(cursor.getString(DBAdapter.COL_LATITUDE)),
														   Double.parseDouble(cursor.getString(DBAdapter.COL_LONGITUDE))))
										.title(cursor.getString(DBAdapter.COL_CITY_NAME))
										.icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
								  cursor.moveToNext();
							}
						});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
			}).start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DBAdapter.getInstance(this).close();
	}
	
}