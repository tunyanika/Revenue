package com.example.revenue_moblieproject;

import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemAccount;
import database.itemAccountType;
import database.projectDB;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class newAccountActivity extends Fragment{
	private Button cancel , save , back, delete, selectType , setAccount;
	private EditText accountName , accountBalance ;
	private TextView accountNameView;
	
	private newAccountActivity newAccountActivity;
	private SQLiteDatabase dbRevenue;
	private allPassData data;
	private itemAccount account;
	
	public newAccountActivity(allPassData data) {
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.data = data;
		newAccountActivity = this;
		account = new itemAccount(0, "", 0, 1);
	}
	
	public newAccountActivity(allPassData data , itemAccount account) {
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.data = data;
		newAccountActivity = this;
		this.account = account;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_new_account, container, false);
		
		cancel = (Button) rootView.findViewById(R.id.cancelAccount);		
		back = (Button) rootView.findViewById(R.id.backAccount);
		save = (Button) rootView.findViewById(R.id.addNewAccount);
		delete = (Button) rootView.findViewById(R.id.deleteAccount);
		
		accountNameView = (TextView) rootView.findViewById(R.id.accountNameView);
		if(account.getId() != 0)
			accountNameView.setText("View Account");
		
		accountName = (EditText) rootView.findViewById(R.id.accountNameText);
		if(!account.getName().equals(""))
			accountName.setText(account.getName());
		
		accountBalance = (EditText) rootView.findViewById(R.id.accountBalanceText);
		if(!(account.getId() == 0 && account.getBalance() == 0))
			accountBalance.setText(account.getBalance()+"");
		
		selectType = (Button) rootView.findViewById(R.id.selectAccountType);
		
		setAccount = (Button) rootView.findViewById(R.id.selectAccountToDefult);
		
		setButton();
		return rootView;
	}
	
	//create popup builder
	public void showDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		switch (id) {
		case 1:
			builder.setMessage("Are u sure tha want to cancel this account ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									RevenueActivity mainActivity = (RevenueActivity) newAccountActivity.getActivity();
									mainActivity.chooseActivity(dictionary.page.accountPage,data);
								}
							}).setNegativeButton("No", null);
			break;
		case 2:
			builder.setTitle("choose type of account");
			String sql = String.format("SELECT %s as _id , %s FROM %s" , dbName.accountType.column.ID
					, dbName.accountType.column.NAME , dbName.accountType.TABLE);
			final Cursor allitem = dbRevenue.rawQuery(sql, null);
			
			int type = account.getType() - 1;
			
			builder.setSingleChoiceItems(allitem, type , dbName.accountType.column.NAME, 
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.dismiss(); // Close dialog
							allitem.moveToPosition(item);
							itemAccountType select = new itemAccountType(
									allitem.getInt(allitem.getColumnIndex("_id")),
									allitem.getString(allitem.getColumnIndex(dbName.accountType.column.NAME))); 
							selectType.setText(select.getName());
							account.setType(select.getId());
						}
					});
			break;
		case 3:
			builder.setMessage("u didn't add name to this account")
					.setCancelable(true)
					.setPositiveButton("OK", null);
			break;
		case 4:
			builder.setMessage("u didn't add balance to this account.\nDo u want to set balance to 0 ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							addNewAccount();
							RevenueActivity mainActivity = (RevenueActivity) newAccountActivity.getActivity();
							mainActivity.chooseActivity(dictionary.page.accountPage ,data);
						}
					})
					.setNegativeButton("No", null);
			break;
		case 5:
			builder.setMessage("Are u sure tha want to delete this account ? \n"
					+ "If u delete this account data will be lost")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									deleteAccount();
									if(account.getId() == data.getAccount())
										data.setAccount(0);
									RevenueActivity mainActivity = (RevenueActivity) newAccountActivity.getActivity();
									mainActivity.chooseActivity(dictionary.page.accountPage,data);
								}
							}).setNegativeButton("No", null);
			break;
		case 6:
			builder.setMessage("Do u want to save Account ?")
			.setCancelable(false)
			.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							updateAccount();
							RevenueActivity mainActivity = (RevenueActivity) newAccountActivity.getActivity();
							mainActivity.chooseActivity(dictionary.page.accountPage,data);
						}
					})
			.setNegativeButton("No", 
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							RevenueActivity mainActivity = (RevenueActivity) newAccountActivity.getActivity();
							mainActivity.chooseActivity(dictionary.page.accountPage,data);
						}
			});
			break;
		default:
			break;
		}
		builder.show();
	}
	
	private void setData(){
		account.setName(accountName.getText().toString());
		try{
			account.setBalance(Float.parseFloat(accountBalance.getText().toString()));
		}catch(NumberFormatException e){
			Log.d("test", "account balance = 0");
		}
	}
	
	private void setButton(){
		selectType.setOnClickListener(new newAccountButton("select type"));
		
		if(account.getId() == 0){
			cancel.setOnClickListener(new newAccountButton("cancel"));
			save.setOnClickListener(new newAccountButton("save"));
			back.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
			setAccount.setVisibility(View.GONE);
		}else{
			cancel.setVisibility(View.GONE);
			save.setVisibility(View.GONE);
			back.setOnClickListener(new newAccountButton("back"));
			delete.setOnClickListener(new newAccountButton("delete"));
			setAccount.setOnClickListener(new newAccountButton("set"));
			
			accountBalance.setFocusable(false);
			accountBalance.setClickable(false);
		}
		
		Cursor cursor = null;
		try {
			String sql = String.format("select %s from %s where %s = %s", dbName.accountType.column.NAME, 
					dbName.accountType.TABLE, dbName.accountType.column.ID, account.getType());
			cursor = dbRevenue.rawQuery(sql, null);
			cursor.moveToFirst();
			selectType.setText(cursor.getString(cursor.getColumnIndex(dbName.accountType.column.NAME)));
		} catch (SQLException e) {
			Log.d("test", "error : " + e);
		}finally{
			cursor.close();
		}
	}
	
	private void addNewAccount(){ //insert data 
		ContentValues values = getDataAccount();
		dbRevenue.insert(dbName.account.TABLE, null, values);
	}
	
	private void updateAccount(){
		ContentValues values = getDataAccount();
		dbRevenue.update(dbName.account.TABLE, values, " " + dbName.account.column.ID + " = ? ", 
				new String[] { String.valueOf(account.getId()) });
	}
	
	private void deleteAccount(){
		dbRevenue.delete(dbName.account.TABLE, " " + dbName.account.column.ID + " = ? ", 
				new String[] { String.valueOf(account.getId()) });
	}
	
	private ContentValues getDataAccount(){
		ContentValues values = new ContentValues(); //have to put in context
		values.put(dbName.account.column.NAME, account.getName()); // column name , data
		values.put(dbName.account.column.BALANCE, account.getBalance() );
		values.put(dbName.account.column.TYPE, account.getType());
		
		return values;
	}

	public class newAccountButton implements View.OnClickListener {
		private String button;
		
		public newAccountButton(String button){
			this.button = button;
		}
		
		@Override
		public void onClick(View arg0) {
			RevenueActivity mainActivity = (RevenueActivity) newAccountActivity.getActivity();
			setData();
			switch (button) {
			case "cancel":
				if(account.getId() == 0){
					showDialog(1);
				}
				break;
			case "save":				
				if(account.getName().equals("")){
					showDialog(3);
				}else if (account.getBalance() == 0){
					showDialog(4);
				}else{
					addNewAccount();
					mainActivity.chooseActivity(dictionary.page.accountPage ,data);
				}
				break;
			case "back":
				itemAccount oldAcc = projectDB.getAccount(dbRevenue, account.getId());
				if(!oldAcc.equals(account))
					showDialog(6);
				else
					mainActivity.chooseActivity(dictionary.page.accountPage,data);
				break;
			case "select type":
				showDialog(2);
				break;
			case "delete":
				showDialog(5);
				break;
			case "set" :
				data.setAccount(account.getId());
				Toast.makeText(newAccountActivity.getActivity(),
						"Set " + account.getName() + " to default account.", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
	
}
