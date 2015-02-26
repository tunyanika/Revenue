package com.example.revenue_moblieproject;

import java.util.List;

import database.itemPicture;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class gridCellAdapterPuture extends BaseAdapter {
	private Context _context;
	private final List<itemPicture> list;
	private ImageView picture;

	public gridCellAdapterPuture(newCategoryActivity activity ,List<itemPicture> list) {
		this._context = activity._context;
		this.list = list;
	}

	@Override
	public int getCount() { // how many grid that will make
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gridcell_picture_category, parent,false);
		}
		
		picture = (ImageView) row.findViewById(R.id.pictureCategory);
		picture.setImageResource(listAdapterCategory.getImageId(_context, list.get(position).getName()));

		return row;
	}

}
