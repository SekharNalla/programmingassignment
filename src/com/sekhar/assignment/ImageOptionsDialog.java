package com.sekhar.assignment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ImageOptionsDialog extends DialogFragment {

	private String[] options = {"Share"};
	private Uri uri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		uri = (Uri) bundle.getParcelableArrayList(GalleryView.KEY_IMAGE_URI).get(0);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose an option").setItems(options,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent shareIntent = new Intent();
							shareIntent.setAction(Intent.ACTION_SEND);
							shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
							shareIntent.setType("image/jpeg");
							startActivity(Intent.createChooser(shareIntent,"Share"));
						} 
					}
				});
		return builder.create();
	}

}