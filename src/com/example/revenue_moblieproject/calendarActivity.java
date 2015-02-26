package com.example.revenue_moblieproject;

import java.util.Calendar;
import java.util.Locale;

import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemAccount;
import database.projectDB;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class calendarActivity extends Fragment {
	private Button prevMonthButton, nextMonthButton;
	private gridCellAdapterDay adapter;
	public GridView calendarView;
	private TextView currentMonthButton , income ,expense ,balance ,name ;
	
	private allPassData data;
	public Context _context;

	public calendarActivity() {}
	
	public calendarActivity(allPassData data) {
		data.setBeforePage(dictionary.page.calendarPage);
		this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_calender, container,
				false); // create fragment
		_context = rootView.getContext();
		
		Calendar _calendar = Calendar.getInstance(Locale.getDefault());
		if(data.getDay() == 0){ 
			data.setDay(1);
			data.setMonth(_calendar.get(Calendar.MONTH));
			data.setYear(_calendar.get(Calendar.YEAR));
		}
		
		calendarView = (GridView) rootView.findViewById(R.id.calendar); // get gridview (show day)
		currentMonthButton = (TextView) rootView.findViewById(R.id.currentMonth);
		prevMonthButton = (Button) rootView.findViewById(R.id.prevMonth);
		nextMonthButton = (Button) rootView.findViewById(R.id.nextMonth);
		name = (TextView) rootView.findViewById(R.id.calendarAcc);
		income = (TextView) rootView.findViewById(R.id.calendarIncome);
		expense = (TextView) rootView.findViewById(R.id.calendarExpense);
		balance = (TextView) rootView.findViewById(R.id.calendarBalance);
		
		setGotoButton();

		return rootView; //return this page to main fragment to show up this page
	}
	
	private float getMoneyInMonth(int type){
		float money = 0;
		String sql = "";
		String firstDate = newProgramActivity.getDate(1, data.getMonth(), data.getYear());
		String lastDate = newProgramActivity.getDate(
				gridCellAdapterDay.daysOfMonth[data.getMonth()], data.getMonth(), data.getYear());
		if(data.getAccount() == 0){
			sql = String.format("SELECT sum(%s) FROM %s where %s between '%s' and '%s' and %s = %s", 
				dbName.program.column.MONEY, dbName.program.TABLE , dbName.program.column.DATE , firstDate, lastDate,
				dbName.program.column.TYPE , type);
		}else{
			sql = String.format("SELECT sum(%s) FROM %s where  %s between '%s' and '%s' and %s = %s and %s = %s ", 
				dbName.program.column.MONEY, dbName.program.TABLE , dbName.program.column.DATE , firstDate, lastDate,
				dbName.program.column.TYPE , type , dbName.program.column.account , data.getAccount());
		}
		Log.d("test", sql);
		Cursor item = RevenueActivity.dbRevenue.rawQuery(sql, null);
		
		item.moveToFirst();
		if (item != null){
			money = item.getFloat(0);
		}
		item.close();
		return money;
	}
	
	public void setGotoButton() {
		float incomeShow = getMoneyInMonth(dictionary.programType.income);
		float expenseShow = getMoneyInMonth(dictionary.programType.expense);
		
		itemAccount account = null;
		if( data.getAccount() == 0)
			account = projectDB.getAllAccount(RevenueActivity.dbRevenue);
		else
			account = projectDB.getAccount(RevenueActivity.dbRevenue , data.getAccount());
		name.setText(account.getName());
		
		income.setText("INCOME: \n " + incomeShow);
		expense.setText("EXPENSE : \n " + expenseShow);
		balance.setText("BALANCE : \n " + (account.getBalance()));
		
		prevMonthButton.setOnClickListener(new calenderButton("prevMonth"));
		nextMonthButton.setOnClickListener(new calenderButton("nextMonth"));
		
		adapter = new gridCellAdapterDay(this, data); //create day cell
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter); //add day cell to gridview
		
		currentMonthButton.setText(gridCellAdapterDay.months[data.getMonth()] + " " + data.getYear());
	}
	
	
	//create action when click next / preview month
	public class calenderButton implements View.OnClickListener {
		private String type;
		
		public calenderButton(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View view) {
			int currentMonth = data.getMonth() ;
			int currentYear = data.getYear();
			int gotoMonth = 0 , gotoYear = currentYear;
			
			switch(type){
				case "prevMonth" : 
					gotoMonth = currentMonth - 1;
					if (currentMonth == 0) {
						gotoMonth = 11;
						gotoYear = currentYear - 1;
					} 
					
					break;
				case "nextMonth" : 
					gotoMonth = currentMonth + 1;
					if (currentMonth == 11) {
						gotoMonth = 0;
						gotoYear = currentYear + 1;
					} 
					break;
			}
			data.setDay(1);
			data.setMonth(gotoMonth);;
			data.setYear(gotoYear);
			
			setGotoButton();
		}
	}
}
