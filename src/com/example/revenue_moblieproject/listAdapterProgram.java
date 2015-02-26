package com.example.revenue_moblieproject;

import java.util.List;

import database.dictionary;
import database.itemProgram;
import database.projectDB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class listAdapterProgram extends ArrayAdapter<itemProgram> {
	private ImageView picture;
	private TextView name ,account ,money;
	
	private Context _context;
	private final List<itemProgram> list;
	private SQLiteDatabase dbRevenue;

	public listAdapterProgram(Context context , int textViewResourceId , List<itemProgram> item) {
		super(context, textViewResourceId , item);
		this.list = item;
		_context = context;
		this.dbRevenue = RevenueActivity.dbRevenue;
	}
	
	@Override
	public View getView(int index, View convertView, ViewGroup parent) { // make cell grid here
		View row = convertView;
		if (row == null) { // row => now in x grid 
			LayoutInflater inflater = (LayoutInflater) _context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gridcell_program, null);
		}
		itemProgram data = list.get(index);

		// choose picture name
		String img = projectDB.getPictureFromCategory(dbRevenue, data.getCategory());
		picture = (ImageView) row.findViewById(R.id.programPicture); //set picture category 
		picture.setImageResource(getImageId(_context,img));
		
		name = (TextView) row.findViewById(R.id.programName);
		name.setText(list.get(index).getName());
		
		account = (TextView) row.findViewById(R.id.programAccount);
		money = (TextView) row.findViewById(R.id.programMoney);
		
		String accountName = projectDB.getAccount(dbRevenue, data.getAccount()).getName(); 
		
		switch (data.getType()) {
		case dictionary.programType.transfer:
			account.setText(accountName + " -> " + 
					projectDB.getAccount(dbRevenue, data.getAccTran()).getName());
			money.setText(""+data.getMoney());
			money.setTextColor(Color.GRAY);
			picture.setImageResource(R.drawable.btn_trans_select);
			break;
		case dictionary.programType.expense:
			account.setText(accountName);
			money.setText("-"+data.getMoney());
			money.setTextColor(Color.RED);
			break;
		case dictionary.programType.income:
			account.setText(accountName);
			money.setText("+"+data.getMoney());
			money.setTextColor(Color.GREEN);
			break;
		}
		
		return row;
	}

	public static int getImageId(Context context, String imageName) { // get id of image
	    return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
	}

}
