package com.example.revenue_moblieproject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemAccount;
import database.itemProgram;
import database.projectDB;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class oneDayActivity extends Fragment {
	private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	
	private Context _context;
	private ListView viewProgram;
	private Boolean isLeapYear = false;
	private int day , month , year;
	private allPassData data;
	private SQLiteDatabase dbRevenue;
	private oneDayActivity oneDayActivity;
	
	private Button prevDay,nextDay,currentDay,viewAccount;
	private ImageButton newProgram;
	private TextView balance;

	public oneDayActivity(allPassData data) {
		data.setBeforePage(dictionary.page.inOneDayPage);
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.data = data;
		this.day = data.getDay();
		this.month = data.getMonth();
		this.year = data.getYear();
		GregorianCalendar _date = new GregorianCalendar(year, month, day);
		isLeapYear = _date.isLeapYear(year);
		oneDayActivity = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		data.setBeforePage(dictionary.page.inOneDayPage);
		View rootView = inflater.inflate(R.layout.view_one_day, container,
				false); // create fragment
		_context = rootView.getContext();
		
		Log.d("test" , data.getDay() + " " + data.getMonth() + " " + data.getYear());
		
		currentDay = (Button) rootView.findViewById(R.id.currentDay);
		currentDay.setText(day + " " + months[month] + " " + year);
		currentDay.setOnClickListener(new inOneDayButton("currentDay"));
		
		prevDay = (Button) rootView.findViewById(R.id.prevDay);
		prevDay.setText("< " + getPrevDay());
		prevDay.setOnClickListener(new inOneDayButton("prevDay"));
		
		nextDay = (Button) rootView.findViewById(R.id.nextDay);
		nextDay.setText(getNextDay() + " >");
		nextDay.setOnClickListener(new inOneDayButton("nextDay"));
		
		viewProgram = (ListView) rootView.findViewById(R.id.viewProgram);
		
		newProgram = (ImageButton) rootView.findViewById(R.id.newProgram);
		newProgram.setOnClickListener(new inOneDayButton("newProgram"));
		
		itemAccount account = null;
		if (data.getAccount() == 0)
			account = projectDB.getAllAccount(dbRevenue);
		else
			account = projectDB.getAccount(dbRevenue, data.getAccount());
		
		viewAccount = (Button) rootView.findViewById(R.id.viewAccount);;
		viewAccount.setOnClickListener(new inOneDayButton("viewAccount"));
		String accountSetName = "";
		if(account.getName().length() <= 30)
			accountSetName = account.getName();
		else{
			accountSetName = account.getName().substring(0, 25) + "...";
		}
		viewAccount.setText(accountSetName);
		
		balance = (TextView) rootView.findViewById(R.id.balance);
		balance.setText(account.getBalance()+"");
		
		setListView();
		
		return rootView;
	}
	
	private void setListView() {
		String sql = "";
		if(data.getAccount() == 0){
			sql = String.format("SELECT * FROM %s where %s = '%s'", 
					dbName.program.TABLE , dbName.program.column.DATE , 
					newProgramActivity.getDate(data.getDay(),data.getMonth(),data.getYear()));
		}else{
			sql = String.format("SELECT * FROM %s where %s = '%s' and (%s = %s or %s = %s)", 
					dbName.program.TABLE , dbName.program.column.DATE , 
					newProgramActivity.getDate(data.getDay(),data.getMonth(),data.getYear()) , dbName.program.column.account
					,data.getAccount() , dbName.program.column.accTranTo , data.getAccount());			
		}
		Cursor item = RevenueActivity.dbRevenue.rawQuery(sql, null);

		ArrayList<itemProgram> allProgram = new ArrayList<itemProgram>();
		item.moveToFirst();

		while (!item.isAfterLast()) {
			itemProgram row = projectDB.getProgram(dbRevenue, 
					item.getInt(item.getColumnIndex(dbName.program.column.ID)));
			allProgram.add(row);
			item.moveToNext();
		}
		item.close();
		listAdapterProgram adapter = new listAdapterProgram(_context,
				R.layout.gridcell_category, allProgram);
		viewProgram.setAdapter(adapter);
		
		viewProgram.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemProgram program = (itemProgram) viewProgram.getAdapter().getItem(position);
				RevenueActivity mainActivity = (RevenueActivity) oneDayActivity.getActivity();
				mainActivity.chooseActivity(dictionary.page.newProgramPage , data, program);
			}
	    });
	}

	private int getPrevDay(){ // use local variable because this is current day 
		int prevDay = day - 1; // normal case
		if(prevDay == 0){ // current day is first day of month case
			prevDay = daysOfMonth[month == 0 ? 11 : month-1];
			if(isLeapYear && (month-1)==1) // 29 Feb case
				prevDay++;
		}
		return prevDay;
	}
	
	private int getNextDay(){ // use local variable because this is current day 
		int prevDay = day+1;
		if(prevDay > daysOfMonth[month]){
			if(!(isLeapYear && month==1 && prevDay == 29))
				prevDay = 1;
		};
		return prevDay;
	}

	// create action when click next / preview month
	public class inOneDayButton implements View.OnClickListener {
		private String button;

		public inOneDayButton(String button) {
			this.button = button;
		}

		@Override
		public void onClick(View view) {
			RevenueActivity mainActivity = (RevenueActivity) oneDayActivity
					.getActivity();
			switch (button) {
			case "currentDay":
				mainActivity.chooseActivity(dictionary.page.calendarPage, data);
				break;
			case "prevDay":
				int prevDay = getPrevDay(),
				prevMonth = month,
				prevYear = year;
				if (prevDay > day) { // last day of month 31 > 1 , 30 > 1 , 29 > 1 , 28 > 1
					prevMonth--;
					if (prevMonth == -1){ // last month of year
						prevMonth = 11;
						prevYear--;
					}
				}
				data.setDay(prevDay);
				data.setMonth(prevMonth);
				data.setYear(prevYear);
				mainActivity.chooseActivity(dictionary.page.inOneDayPage,data);
				break;
			case "nextDay":
				int nextDay = getNextDay(),
				nextMonth = month,
				nextYear = year;
				if (nextDay == 1) { // first day of month
					nextMonth++;
					if (nextMonth == 12){ // first month of year
						nextMonth = 0;
						nextYear++;
					}
				}
				data.setDay(nextDay);
				data.setMonth(nextMonth);
				data.setYear(nextYear);
				mainActivity.chooseActivity(dictionary.page.inOneDayPage , data);
				break;
			case "newProgram":
				mainActivity.chooseActivity(dictionary.page.newProgramPage , data);
				break;
			case "viewAccount":
				//save previous page to stack
				RevenueActivity.goBackStack.add(data.getBeforePage());
				mainActivity.chooseActivity(dictionary.page.accountPage , data);
				break;
			}
		}

	}

}
