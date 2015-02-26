package com.example.revenue_moblieproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemCategory;
import database.itemForGraph;
import database.itemGraph;
import database.projectDB;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class graphActivity extends Fragment {
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
    private Button back , account , dateTo , dateFrom;
    private ImageButton chartType;
	
    private Context _contecx;
	private SQLiteDatabase dbRevenue;
	private allPassData data;
	private graphActivity graphActivity ;
	private itemForGraph graphData;
    
	public graphActivity(allPassData data) {
		this.graphActivity = this;
		this.data = data;
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.graphData = new itemForGraph(dictionary.programType.expense, data.getAccount(), "", "");
	}
	
	public graphActivity(allPassData data , itemForGraph graphData) {
		this.graphActivity = this;
		this.data = data;
		this.dbRevenue = RevenueActivity.dbRevenue;
		this.graphData = graphData;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		data.setBeforePage(dictionary.page.graphPage);
		View rootView = inflater.inflate(R.layout.view_graph, container,
				false);
		_contecx  = rootView.getContext();

		//back , account , btnChartType , dateTo , dateFrom
		back = (Button) rootView.findViewById(R.id.backGraph);
		back.setOnClickListener(new graphtButton("back"));
		account = (Button) rootView.findViewById(R.id.chartAccount);
		account.setOnClickListener(new graphtButton("account"));
		if(graphData.getAccount() == 0)
			account.setText(projectDB.getAllAccount(dbRevenue).getName());
		else
			account.setText(projectDB.getAccount(dbRevenue, graphData.getAccount()).getName());
		
		chartType = (ImageButton) rootView.findViewById(R.id.chartType);
		chartType.setOnClickListener(new graphtButton("type"));
		if(graphData.getType() == dictionary.programType.income)
			chartType.setImageResource(R.drawable.btn_graph_income);
		
		dateFrom = (Button) rootView.findViewById(R.id.grarpDateFrom);
		dateFrom.setOnClickListener(new graphtButton("dateFrom"));
		if(!graphData.getFirstDate().equals(""))
			dateFrom.setText(getDate(graphData.getFirstDate()));
		
		dateTo = (Button) rootView.findViewById(R.id.grarpDateTo);
		dateTo.setOnClickListener(new graphtButton("dateTo"));
		if(!graphData.getLastDate().equals(""))
			dateTo.setText(getDate(graphData.getLastDate()));
		
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setStartAngle(90);

        List<itemGraph> realGraph = getData();
        float money = itemGraph.getAllMoney(realGraph);
        for (int i = 0; i < realGraph.size() ; i++) {
        	int idCat = realGraph.get(i).getCategory();
            mSeries.add(projectDB.getCategoryName(dbRevenue, idCat) + " " + 
            		String.format("%.2f" ,realGraph.get(i).getMoney() / money * 100) +
            		"% ( " + realGraph.get(i).getMoney() + " )", realGraph.get(i).getMoney());
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(Color.parseColor(projectDB.getColor(dbRevenue, idCat)));
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }
        
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.chart);
        mChartView = ChartFactory.getPieChartView(_contecx, mSeries, mRenderer);
        layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
		return rootView;
	}
	
	private List<itemGraph> getData(){
		List<itemGraph> realGraph = new ArrayList<itemGraph>();
		List<itemGraph> allGraph = getAllGraph();
		for(int i = 0 ; i < allGraph.size() ; i++){
			itemCategory category = projectDB.getCategory(dbRevenue, allGraph.get(i).getCategory());
			if(category.getSubType() != 0){
				int inxChange = itemGraph.findGraph(realGraph , category.getSubType());
				if (inxChange == -1){
					itemGraph item = new itemGraph(category.getSubType(), allGraph.get(i).getMoney());
					realGraph.add(item);
				}else{
					itemGraph item = new itemGraph(realGraph.get(inxChange).getCategory(), 
							realGraph.get(inxChange).getMoney() + allGraph.get(i).getMoney());
					realGraph.set(inxChange, item);
				}
			}else{
				int inxChange = itemGraph.findGraph(realGraph , category.getId());
				if (inxChange == -1){
					itemGraph item = new itemGraph(allGraph.get(i).getCategory(), allGraph.get(i).getMoney());
					realGraph.add(item);
				}else{
					itemGraph item = new itemGraph(realGraph.get(inxChange).getCategory(), 
							realGraph.get(inxChange).getMoney() + allGraph.get(i).getMoney());
					realGraph.set(inxChange, item);
				}
			}
		}
		return realGraph;
	}

	private List<itemGraph> getAllGraph(){
		if (graphData.getFirstDate().equals("")){
			Calendar _calendar = Calendar.getInstance(Locale.getDefault());
			graphData.setFirstDate(newProgramActivity.getDate(1, _calendar.get(Calendar.MONTH), _calendar.get(Calendar.YEAR)));
			graphData.setLastDate(newProgramActivity.getDate(gridCellAdapterDay.daysOfMonth[_calendar.get(Calendar.MONTH)],
					_calendar.get(Calendar.MONTH), _calendar.get(Calendar.YEAR)));
		}
		dateFrom.setText(getDate(graphData.getFirstDate()));
		dateTo.setText(getDate(graphData.getLastDate()));
		
		String sql = "" ;
		List<itemGraph> allGraph = new ArrayList<itemGraph>();
		if(graphData.getAccount() == 0){
			sql = String.format("SELECT * FROM (SELECT sum(%s) as sum , %s as category "
					+ "FROM %s where %s = %s and %s between '%s' and '%s' group by %s) as mytable WHERE sum != 0;", 
					dbName.program.column.MONEY , dbName.program.column.category , dbName.program.TABLE ,
					dbName.program.column.TYPE , graphData.getType() , dbName.program.column.DATE , graphData.getFirstDate() , 
					graphData.getLastDate() , dbName.program.column.category);
		}else{
			sql = String.format("SELECT * FROM (SELECT sum(%s) as sum , %s as category "
					+ "FROM %s where %s = %s and %s = %s and %s between '%s' and '%s' group by %s) as mytable WHERE sum != 0;", 
					dbName.program.column.MONEY , dbName.program.column.category , dbName.program.TABLE ,
					dbName.program.column.account , graphData.getAccount() , dbName.program.column.TYPE , graphData.getType() , 
					dbName.program.column.DATE , graphData.getFirstDate() , graphData.getLastDate() , 
					dbName.program.column.category);
		}
		Log.d("test" , sql);
		
		Cursor item = dbRevenue.rawQuery(sql, null);
		item.moveToFirst();

		while (!item.isAfterLast()) {
			itemGraph row = new itemGraph(item.getInt(item.getColumnIndex("category")), 
					item.getFloat(item.getColumnIndex("sum")));
			allGraph.add(row);
			item.moveToNext();
		}
		item.close();
		
		return allGraph;
	}
	
	public static String getDate(String date){
		String[] dateSprite = date.split("-");
		int day = Integer.parseInt(dateSprite[2]);
		int month = Integer.parseInt(dateSprite[1]);
		int year = Integer.parseInt(dateSprite[0]);
		
		String result = day + " " + gridCellAdapterDay.months[month] + " " + year;
		return result;
	}
	
 	public class graphtButton implements View.OnClickListener {
		private String type;

		public graphtButton(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View view) {
			RevenueActivity mainActivity = (RevenueActivity) graphActivity.getActivity();
			switch (type) {
			case "back":
				int lastIndexOfGeBack = RevenueActivity.goBackStack.size()-1;
				mainActivity.chooseActivity(
						RevenueActivity.goBackStack.get(lastIndexOfGeBack), data);
				RevenueActivity.goBackStack.remove(lastIndexOfGeBack);
				break;
			case "account":
				mainActivity.chooseActivity(dictionary.page.accountPage , data , graphData);
				break;
			case "type" :
				if (graphData.getType() == dictionary.programType.income){
					graphData.setType(dictionary.programType.expense);
				}else{
					graphData.setType(dictionary.programType.income);
				}
				mainActivity.chooseActivity(dictionary.page.graphPage , data , graphData);
				break;
			case "dateFrom" :
				mainActivity.chooseActivity(dictionary.page.pickaDate , data , graphData , 1);
				break;
			case "dateTo" :
				mainActivity.chooseActivity(dictionary.page.pickaDate , data , graphData , 2);
				break;
			}
		}
	}
}
