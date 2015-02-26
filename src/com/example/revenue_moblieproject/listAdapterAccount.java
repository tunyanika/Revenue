package com.example.revenue_moblieproject;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import database.allPassData;
import database.dbName;
import database.itemAccount;

public class listAdapterAccount extends ArrayAdapter<itemAccount> {
	private Context _context;
	private final List<itemAccount> list;
	private TextView name,type,balance;
	private ImageView picture;
	private SQLiteDatabase db;
	private allPassData data;

	public listAdapterAccount(Context context , int textViewResourceId , 
			List<itemAccount> item , SQLiteDatabase db , allPassData data) {
		super(context, textViewResourceId , item);
		_context = context;
		list = item;
		this.db = db;
		this.data = data;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) { // make cell grid here
		View row = convertView;
		if (row == null) { // row => now in x grid 
			LayoutInflater inflater = (LayoutInflater) _context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gridcell_account, null);
		}

		name = (TextView) row.findViewById(R.id.accountNameView);
		name.setText(list.get(index).getName());
		
		// choose picture name
		String accountType = "";
		try {
			String sql = String.format("select %s from %s where %s = %s",
					dbName.accountType.column.NAME, dbName.accountType.TABLE,
					dbName.accountType.column.ID, list.get(index).getType());
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			accountType = cursor.getString(cursor
					.getColumnIndex(dbName.accountType.column.NAME));
		} catch (SQLException e) {
			Log.d("test", "error : " + e);
		}

		type = (TextView) row.findViewById(R.id.accountTypeView);
		type.setText(accountType);
		
		balance = (TextView) row.findViewById(R.id.accountBalanceView);
		balance.setText(list.get(index).getBalance()+"");
		
		picture = (ImageView) row.findViewById(R.id.isChoose);
		picture.setTag(list.get(index).getId());
		Log.d("test", "id now = " + list.get(index).getId() + " account defult " 
				+ data.getAccount() + " so : " + (list.get(index).getId() == data.getAccount()));
		if(list.get(index).getId() == data.getAccount())
			picture.setBackgroundResource(R.drawable.check);
		else
			picture.setBackground(null);
		
		return row;
	}

}
