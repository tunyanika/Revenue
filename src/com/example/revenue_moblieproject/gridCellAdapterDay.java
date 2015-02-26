package com.example.revenue_moblieproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import database.allPassData;
import database.dictionary;
import database.projectDB;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class gridCellAdapterDay extends BaseAdapter implements OnClickListener {
	private LinearLayout gridcell;
	private TextView day , income , expense;
	
	private calendarActivity calendarActivity;
	private final List<String> list;
	private allPassData data; 
	
	public final static String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	public final static int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	//private final String[] weekdays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

	public gridCellAdapterDay(calendarActivity calendarActivity,allPassData data) {
		this.calendarActivity = calendarActivity;
		list = new ArrayList<String>();
		this.data = data;
		printCalendar(data.getMonth(), data.getYear());
	}

	public void printCalendar(int currentMonth , int currentYear) { //get day in month and add to list
		Log.d("test" , "===========================in print=======================");
		int allDayinPrevMonth, prevMonth, prevYear, nextMonth, nextYear, allDayinMonth;
		
		GregorianCalendar firstDate = new GregorianCalendar(currentYear, currentMonth, 1); // get first of month in this year
		
		prevMonth = currentMonth - 1; // in normal case not at first or last month
		nextMonth = currentMonth + 1;
		prevYear = currentYear;
		nextYear = currentYear;
		if (currentMonth == 11) {
			nextMonth = 0;
			nextYear = currentYear + 1;
		} else if (currentMonth == 0) {
			prevMonth = 11;
			prevYear = currentYear - 1;
		}
		allDayinMonth = daysOfMonth[currentMonth];
		allDayinPrevMonth = daysOfMonth[prevMonth];
		
		int firstDayWeek = firstDate.get(Calendar.DAY_OF_WEEK) - 1; // get Day(sunday = 0 ,monday = 1,...)

		if (firstDate.isLeapYear(firstDate.get(Calendar.YEAR))) { // if this feb have 29 days
			if(currentMonth == 1)
				allDayinMonth++;
			else if (currentMonth == 2)
				allDayinPrevMonth++;
		}

		// scrap from last month (ex. 28,29,30,1,2,3,4)
		for (int i = 1; i <= firstDayWeek; i++) {
			list.add(String.valueOf((allDayinPrevMonth - firstDayWeek)+ i)+ "-" + prevMonth + "-"+ prevYear+"-GREY");
			// day and color ex. 2-January-2014 
		}

		Calendar calendar = Calendar.getInstance(Locale.getDefault()); // get this day
		int thisDay = calendar.get(Calendar.DAY_OF_MONTH); // today(num) index start at 1 (day 1 = 1 , day 2 = 2)
		int thisMonth = calendar.get(Calendar.MONTH);
		int thisYear = calendar.get(Calendar.YEAR);
		
		// all day in this month
		for (int i = 1; i <= allDayinMonth; i++) {
			if (i == thisDay && thisMonth == currentMonth && thisYear == currentYear)  // if this day color change
				list.add(String.valueOf(i)+ "-" + currentMonth + "-"+ currentYear+"-BLUE");
			 else 
				list.add(String.valueOf(i)+ "-" + currentMonth + "-"+ currentYear+"-WRITE");
		}

		// scrap for next month
		for (int i = 1; i <= list.size() % 7; i++) {
			list.add(String.valueOf(i)+ "-" + nextMonth + "-"+ nextYear+"-GREY");
		}
	}

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
	public View getView(int index, View convertView, ViewGroup parent) { // make cell grid here
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) calendarActivity._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gridcell_day, parent,false);
		}

		// Get a reference to the Day grid cell
		gridcell = (LinearLayout) row.findViewById(R.id.dayGridCell);
		gridcell.setOnClickListener(this);
		
		day = (TextView) row.findViewById(R.id.numDay);
		income = (TextView) row.findViewById(R.id.incomeDay);
		expense = (TextView) row.findViewById(R.id.expenseDay);

		String[] oneDay = list.get(index).split("-"); // data of day
		int currentDay = Integer.parseInt(oneDay[0]); // number
		int currentMonth = Integer.parseInt(oneDay[1]); // mount
		int currentYear = Integer.parseInt(oneDay[2]); // year
		String color = oneDay[3]; // color

		// Set the Day GridCell
		gridcell.setTag(currentDay + "-" + currentMonth + "-" + currentYear);
		day.setText(currentDay + "");
		switch (color) {
		case "BLUE": // today
			day.setTextColor(Color.BLACK);
			gridcell.setBackgroundColor(Color.parseColor("#fa8a11"));
			break;
		case "GREY": // not this month 
			day.setTextColor(Color.GRAY);
			break;
		default: // in this month
			day.setTextColor(Color.WHITE);
			break;
		}
		income.setText(projectDB.getMoneyinDay(RevenueActivity.dbRevenue, 
				newProgramActivity.getDate(currentDay, currentMonth, currentYear), dictionary.programType.income , data.getAccount()));
		expense.setText(projectDB.getMoneyinDay(RevenueActivity.dbRevenue, 
				newProgramActivity.getDate(currentDay, currentMonth, currentYear), dictionary.programType.expense , data.getAccount()));
		
		return row;
	}

	@Override
	public void onClick(View button) {
		String[] oneDay = button.getTag().toString().split("-"); // get tag (set in get view line:132)
		data.setDay(Integer.parseInt(oneDay[0])); // number
		data.setMonth(Integer.parseInt(oneDay[1])); // mount
		data.setYear(Integer.parseInt(oneDay[2])); // year
		
		RevenueActivity mainActivity = (RevenueActivity) calendarActivity.getActivity();
		mainActivity.chooseActivity(dictionary.page.inOneDayPage,data);
	}
}
