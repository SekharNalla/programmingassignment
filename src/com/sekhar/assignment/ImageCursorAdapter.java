package com.sekhar.assignment;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class ImageCursorAdapter extends CursorAdapter {

	AQuery aquery;
	private LayoutInflater mInflater;
	
	public ImageCursorAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.NO_SELECTION);
		aquery = new AQuery(context);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		String image_path = cursor.getString(DBAdapter.COL_IMAGE_PATH);
		String image_location = cursor.getString(DBAdapter.COL_CITY_NAME);
		File imgFile = new  File(image_path);
		ImageView image = null;
		
		if(imgFile.exists()){

			image = (ImageView) view.findViewById(R.id.image);

			//set the content description of image to latitude and longitude
			image.setContentDescription(cursor.getString(DBAdapter.COL_LATITUDE) + "," + cursor.getString(DBAdapter.COL_LONGITUDE));
			
			//using an external library called Aquery for asynchronous image loading
			aquery.id(image).image(imgFile, 200);
			TextView text = (TextView) view.findViewById(R.id.image_title);
			text.setText(image_location);
		} else {
			aquery.id(image).image(R.drawable.icon);
			TextView text = (TextView) view.findViewById(R.id.image_title);
			text.setText("Error Loading Image");
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.image_in_gallery, parent, false);
	}

}
