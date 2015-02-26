package com.example.revenue_moblieproject;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemAccount;
import database.itemForGraph;
import database.itemProgram;
import database.projectDB;

public class accountActivity extends Fragment {
	private SQLiteDatabase dbRevenue;
	private Button back, newAccount;
	private ListView viewAccount;

	private Context _context;
	private accountActivity accountActivity;
	private allPassData data;
	private int actionType = 1; 
	private itemProgram newProgramData;
	private itemForGraph graphData;

	public accountActivity(allPassData data) {
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.data = data;
		accountActivity = this;
	}

	public accountActivity(allPassData data, itemProgram newProgramData) {
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.data = data;
		accountActivity = this;
		actionType = 0;
		this.newProgramData = newProgramData;
	}

	public accountActivity(allPassData data, itemForGraph graphData) {
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.data = data;
		accountActivity = this;
		actionType = 2;
		this.graphData = graphData;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		data.setBeforePage(dictionary.page.accountPage);
		View rootView = inflater.inflate(R.layout.view_account, container,false);
		_context = rootView.getContext();
		
		back = (Button) rootView.findViewById(R.id.backToBefore);
		back.setOnClickListener(new accountButton("back"));
		
		newAccount = (Button) rootView.findViewById(R.id.newAccount);
		newAccount.setOnClickListener(new accountButton("newAccount"));
		if(newProgramData != null)
			newAccount.setVisibility(View.GONE);
		viewAccount = (ListView) rootView.findViewById(R.id.viewAccount);
		showAccount();
		return rootView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
			super.onSaveInstanceState(outState);
			outState.putInt("account", 1);
			outState.putInt("category", 1);
			Log.d("test" , "save in activity");
	}

	private void showAccount(){
		String sql = String.format("SELECT * FROM %s", dbName.account.TABLE);
		ArrayList<itemAccount> allAccount = getAllAccount(sql);
		
		listAdapterAccount adapter = new listAdapterAccount(_context, R.layout.gridcell_account, 
				allAccount,dbRevenue ,data);
		viewAccount.setAdapter(adapter);
		
		viewAccount.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemAccount account = (itemAccount) viewAccount.getAdapter().getItem(position);
				RevenueActivity mainActivity = (RevenueActivity) accountActivity.getActivity();
				switch (actionType) {
				case 0:
					if(newProgramData.getAccTran() == -1)
						newProgramData.setAccTran(account.getId());
					else
						newProgramData.setAccount(account.getId());
					mainActivity.chooseActivity(dictionary.page.newProgramPage , data , newProgramData);
					break;
				case 1:
					if(account.getId() != 0)
						mainActivity.chooseActivity(dictionary.page.newAccountPage , data , account);
					else{
						data.setAccount(0);
						showAccount();
					}
					break;
				case 2:graphData.setAccount(account.getId());
					mainActivity.chooseActivity(dictionary.page.graphPage , data , graphData);
					break;
				}
			}
	    });
	}
	
	private ArrayList<itemAccount> getAllAccount(String sql){
		Cursor item = dbRevenue.rawQuery(sql, null);
		ArrayList<itemAccount> allAccount = new ArrayList<itemAccount>();
		item.moveToFirst();
		
		while (!item.isAfterLast()) {
			int id = item.getInt(item.getColumnIndex(dbName.account.column.ID));
			itemAccount row = projectDB.getAccount(dbRevenue, id);
			allAccount.add(row);
			item.moveToNext();
		}
		item.close();
		if(newProgramData == null)
			allAccount.add(projectDB.getAllAccount(dbRevenue));
		
		return allAccount;
	}

	// create action when click back / preview new account
	public class accountButton implements View.OnClickListener {
		private String type;

		public accountButton(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View view) {
			RevenueActivity mainActivity = (RevenueActivity) accountActivity.getActivity();
			switch (type) {
			case "back":
				int lastIndexOfGeBack = RevenueActivity.goBackStack.size()-1;
				if(newProgramData != null)
					mainActivity.chooseActivity(
							dictionary.page.newProgramPage , data , newProgramData);
				else if (graphData != null)
					mainActivity.chooseActivity(
							dictionary.page.graphPage , data , graphData);
				else{
					mainActivity.chooseActivity(
							RevenueActivity.goBackStack.get(lastIndexOfGeBack) , data);
					RevenueActivity.goBackStack.remove(lastIndexOfGeBack);
				}
				break;
			case "newAccount":
				mainActivity.chooseActivity(dictionary.page.newAccountPage , data);
				break;
			}
		}
	}
}
