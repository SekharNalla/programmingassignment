package com.sekhar.assignment;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class GalleryView extends FragmentActivity {

	public static final String IMAGE_TITLE = "image_title";
	public static final String IMAGE_PATH = "image_path";
	public static final String IMAGE_LATITUDE = "image_latitude";
	public static final String IMAGE_LONGITUDE = "image_longitude";
	public static final String KEY_IMAGE_URI = "uri's";
	private Context context;
	private Cursor cursor;
	private ImageCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		context = this;

		GridView gridview = (GridView) findViewById(R.id.gridview);
		DBAdapter.getInstance(context).open();
		cursor = DBAdapter.getInstance(this).getAllRows();
		adapter = new ImageCursorAdapter(context, cursor);

		//empty view for gridview
		View view = getLayoutInflater().inflate(R.layout.listview_empty, null,false);
		TextView text = (TextView) view.findViewById(R.id.textview_ifempty);
		text.setTextSize(20);
		addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		gridview.setEmptyView(view);

		gridview.setAdapter(adapter);
		
		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				cursor.moveToPosition(position);
				File file = new File(cursor.getString(DBAdapter.COL_IMAGE_PATH));
				Uri uri = Uri.fromFile(file);
				Log.i("URI", uri.toString());
				
				Bundle bundle = new Bundle();
				ArrayList<Uri> urilist = new ArrayList<Uri>();
				urilist.add(uri);
				bundle.putParcelableArrayList(KEY_IMAGE_URI, urilist);
			
				ImageOptionsDialog dialog = new ImageOptionsDialog();
				dialog.setArguments(bundle);
				dialog.setCancelable(true);
				dialog.show(getSupportFragmentManager(), "image dialog");
			
				return true;
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				cursor.moveToPosition(position);
				String image_path = cursor.getString(DBAdapter.COL_IMAGE_PATH);
				String image_title = cursor.getString(DBAdapter.COL_CITY_NAME);
				String latitude = cursor.getString(DBAdapter.COL_LATITUDE);
				String longitude = cursor.getString(DBAdapter.COL_LATITUDE);
				
				Intent intent = new Intent(getBaseContext(), DisplayImage.class);
				intent.putExtra(IMAGE_TITLE, image_title);
				intent.putExtra(IMAGE_PATH, image_path);
				intent.putExtra(IMAGE_LATITUDE, latitude);
				intent.putExtra(IMAGE_LONGITUDE, longitude);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("GalleryView", "OnResume");
		DBAdapter.getInstance(this).open();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("GalleryView", "OnPause");
		DBAdapter.getInstance(context).close();
	}
}