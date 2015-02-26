package com.example.revenue_moblieproject;

import java.util.List;

import database.itemCategory;
import database.projectDB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class listAdapterCategory extends ArrayAdapter<itemCategory> {
	private Context _context;
	private final List<itemCategory> list;
	private TextView name;
	private ImageView picture , edit;
	private SQLiteDatabase dbRevenue;
	
	public listAdapterCategory(Context context , int textViewResourceId , 
			List<itemCategory> item) {
		super(context, textViewResourceId , item);
		_context = context;
		list = item;
		this.dbRevenue = RevenueActivity.dbRevenue;
	}
	
	@Override
	public View getView(int index, View convertView, ViewGroup parent) { // make cell grid here
		View row = convertView;
		if (row == null) { // row => now in x grid 
			LayoutInflater inflater = (LayoutInflater) _context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gridcell_category, null);
		}

		name = (TextView) row.findViewById(R.id.nameCategory); // set name category
		name.setText(list.get(index).getName());
		
		// choose picture name
		String img = projectDB.getPicture(dbRevenue, list.get(index).getPicture());
		
		picture = (ImageView) row.findViewById(R.id.pictureCategory); //set picture category 
		picture.setImageResource(getImageId(_context,img));
		if(list.get(index).getSubType() != 0 ){
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(35, 35);
			layout.setMargins(40, 0, 0, 0);
			picture.setLayoutParams(layout);
		}else{
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(35, 35);
			layout.setMargins(3, 0, 0, 0);
			picture.setLayoutParams(layout);
		}
			
		
		edit = (ImageView) row.findViewById(R.id.categoryEdit);
		if (list.get(index).getId() >= 20)
			edit.setBackgroundResource(R.drawable.can_edit);
		else
			edit.setBackground(null);
		
		return row;
	}

	public static int getImageId(Context context, String imageName) { // get id of image
	    return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
	}

//	public BitmapDrawable getImageNewSize(int imageID){
//		Bitmap originPicture = BitmapFactory.decodeResource(get,
//		           imageID);
//		
//		int width = originPicture.getWidth();
//		int height = originPicture.getHeight();
//		int newWidth = 50;
//		int newHeight = 50;
//
//		// calculate the scale - in this case = 0.4f
//		float scaleWidth = ((float) newWidth) / width;
//		float scaleHeight = ((float) newHeight) / height;
//
//		// create a matrix for the manipulation
//		Matrix matrix = new Matrix();
//		// resize the bit map
//		matrix.postScale(scaleWidth, scaleHeight);
//
//		// recreate the new Bitmap
//		Bitmap resizedBitmap = Bitmap.createBitmap(originPicture, 0, 0, width,
//				height, matrix, true);
//
//		// make a Drawable from Bitmap to allow to set the BitMap
//		// to the ImageView, ImageButton or what ever
//		BitmapDrawable newSizePicture = new BitmapDrawable(resizedBitmap);
//		return newSizePicture;
//	}

}
