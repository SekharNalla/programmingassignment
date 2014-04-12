package com.sekhar.assignment;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

public class DisplayImage extends ActionBarActivity {

	private String image_path;
	private String image_title;
	private String image_latitude;
	private String image_longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.displayimage);
	
		Intent intent = getIntent();
		image_path = intent.getStringExtra(GalleryView.IMAGE_PATH);
		image_title = intent.getStringExtra(GalleryView.IMAGE_TITLE);
		image_latitude = intent.getStringExtra(GalleryView.IMAGE_LATITUDE);
		image_longitude = intent.getStringExtra(GalleryView.IMAGE_LONGITUDE);
		
		AQuery aquery = new AQuery(this);

		ImageView image = (ImageView) findViewById(R.id.displayimage);
		TextView text = (TextView) findViewById(R.id.image_title);

		File imgFile = new File(image_path);

		if (imgFile.exists()) {
			// using an external library called Aquery for asynchronous image loading
			aquery.id(image).image(imgFile, 1024);
			text.setText(image_title + "\n" + "latitude: " + image_latitude + "\nlongitude: " + image_longitude);
		} else {
			Toast.makeText(this, "There was an error displaying the Image! Please try again", Toast.LENGTH_SHORT).show();
		}
	}
}
