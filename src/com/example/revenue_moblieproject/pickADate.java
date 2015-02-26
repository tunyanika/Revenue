package com.example.revenue_moblieproject;

import database.allPassData;
import database.dictionary;
import database.itemForGraph;
import database.itemProgram;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

public class pickADate extends Fragment{
	private Button cancel , set;
	private DatePicker date;
	
	private allPassData data;
	private itemForGraph graphData;
	private itemProgram newProgramData;
	private pickADate pickADate;
	private int dataType = 0;
	
	public pickADate(allPassData data , itemForGraph graphData , int type) { // type = 1 =>from , normal , type = 2 =>to , alert
		this.graphData = graphData;
		this.data = data;
		this.pickADate = this;
		dataType = type;
	}

	public pickADate(allPassData data, itemProgram newProgramData, int type) {
		this.newProgramData = newProgramData;
		this.data = data;
		this.pickADate = this;
		dataType = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pick_a_date, container,
				false);

		//back , account , btnChartType , dateTo , dateFrom
		cancel = (Button) rootView.findViewById(R.id.backToBeforeDate);
		cancel.setOnClickListener(new pickDateButton("cencel"));
		
		set = (Button) rootView.findViewById(R.id.setDate);
		set.setOnClickListener(new pickDateButton("set"));
		int day = 0 , month = 0 , year = 0;
		if(graphData != null){
			if (dataType == 1){
				String[] splite = graphData.getFirstDate().split("-");
				year = Integer.parseInt(splite[0]);
				month = Integer.parseInt(splite[1]);
				day = Integer.parseInt(splite[2]);
			}else if (dataType == 2){
				String[] splite = graphData.getLastDate().split("-");
				year = Integer.parseInt(splite[0]);
				month = Integer.parseInt(splite[1]);
				day = Integer.parseInt(splite[2]);
			}
		}else if (newProgramData != null){
			String[] splite = newProgramData.getDate().split("-");
			year = Integer.parseInt(splite[0]);
			month = Integer.parseInt(splite[1]);
			day = Integer.parseInt(splite[2]);
		}
		
		date = (DatePicker) rootView.findViewById(R.id.getDate);
		date.init(year, month, day, null);
        
		return rootView;
	}
	
	public class pickDateButton implements View.OnClickListener {
		private String type;

		public pickDateButton(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View view) {
			RevenueActivity mainActivity = (RevenueActivity) pickADate.getActivity();
			switch (type) {
			case "cencel":
				if(graphData != null)
					mainActivity.chooseActivity(dictionary.page.graphPage , data , graphData);
				else if (newProgramData != null)
					mainActivity.chooseActivity(dictionary.page.newProgramPage , data , newProgramData);
				break;
			case "set":
				if(graphData != null){
					if (dataType == 1)
						graphData.setFirstDate(date.getYear() + "-" + date.getMonth() + "-" + date.getDayOfMonth());
					else if (dataType == 2)
						graphData.setLastDate(date.getYear() + "-" + date.getMonth() + "-" + date.getDayOfMonth());
					mainActivity.chooseActivity(dictionary.page.graphPage , data , graphData);
				}else if (newProgramData != null){
					newProgramData.setDate(newProgramActivity.getDate(date.getDayOfMonth(),date.getMonth(),date.getYear()));
					mainActivity.chooseActivity(dictionary.page.newProgramPage , data , newProgramData);
				}
				break;
			}
		}
	}
	
}
