package com.example.revenue_moblieproject;

import java.util.ArrayList;

import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemCategory;
import database.itemProgram;
import database.projectDB;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class categoryActivity extends Fragment {
	private ListView categoryViewList;
	private Context _context;
	private Button back, newCategoty;
	private categoryActivity categoryActivity;
	private allPassData data;
	private itemProgram newProgramData;
	private itemCategory categoryData;
	private int type = 10;

	public categoryActivity(allPassData data) {
		data.setBeforePage(dictionary.page.categoryPage);
		categoryActivity = this;
		this.data = data;
	}

	public categoryActivity(allPassData data, itemProgram newProgramData) {
		data.setBeforePage(dictionary.page.newProgramPage);
		this.newProgramData = newProgramData;
		categoryActivity = this;
		this.data = data;
		type = newProgramData.getType();
	}
	
	public categoryActivity(allPassData data, itemCategory categoryData) {
		data.setBeforePage(dictionary.page.newCategoryPage);
		this.categoryData = categoryData;
		categoryActivity = this;
		this.data = data;
		type = categoryData.getType();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_category, container,
				false); // create fragment
		_context = rootView.getContext();
		data.setBeforePage(dictionary.page.categoryPage);
		
		back = (Button) rootView.findViewById(R.id.cancelChooseCategory);
		back.setOnClickListener(new categorytButton("back"));
		
		newCategoty = (Button) rootView.findViewById(R.id.newCategory);
		if(categoryData != null || newProgramData != null)
			newCategoty.setVisibility(View.GONE);
		else
			newCategoty.setOnClickListener(new categorytButton("newCategoty"));
		
		categoryViewList = (ListView) rootView.findViewById(R.id.viewCategory);
		setListView();
		
		return rootView;
	}

	private void setListView() {
		String sql = "";
		switch (type) {
		case dictionary.programType.expense:
			sql = String.format("SELECT * FROM %s where %s = %s and %s is null order by lower(%s)",
					dbName.category.TABLE, dbName.category.column.TYPE, dictionary.programType.expense 
					, dbName.category.column.SUBTYPE ,dbName.category.column.NAME);
			break;
		case dictionary.programType.income:
			sql = String.format("SELECT * FROM %s where %s = %s and %s is null order by lower(%s)",
					dbName.category.TABLE, dbName.category.column.TYPE, dictionary.programType.income
					, dbName.category.column.SUBTYPE , dbName.category.column.NAME);
			break;
		default:
			sql = String.format("SELECT * FROM %s where %s is null order by lower(%s)", dbName.category.TABLE
					, dbName.category.column.SUBTYPE , dbName.category.column.NAME);
			break;
		}
		Cursor item = RevenueActivity.dbRevenue.rawQuery(sql, null);

		ArrayList<itemCategory> allCategory = new ArrayList<itemCategory>();
		item.moveToFirst();

		while (!item.isAfterLast()) {
			int id = item.getInt(item.getColumnIndex(dbName.category.column.ID)); 
			itemCategory row = projectDB.getCategory(RevenueActivity.dbRevenue, id);
			allCategory.add(row);

			//sub type
			sql = String.format("SELECT * FROM %s where %s = %s order by lower(%s)", dbName.category.TABLE
					, dbName.category.column.SUBTYPE , id , dbName.category.column.NAME);
			Cursor itemSub = RevenueActivity.dbRevenue.rawQuery(sql, null);
			if(itemSub != null && itemSub.moveToFirst()){
				while(!itemSub.isAfterLast()){
					int idSub = itemSub.getInt(itemSub.getColumnIndex(dbName.category.column.ID)); 
					itemCategory rowSub = projectDB.getCategory(RevenueActivity.dbRevenue, idSub);
					allCategory.add(rowSub);
					itemSub.moveToNext();
				}
			}
			itemSub.close();
			item.moveToNext();
		}
		item.close();
		listAdapterCategory adapter = new listAdapterCategory(_context,
				R.layout.gridcell_category, allCategory);
		categoryViewList.setAdapter(adapter);

		categoryViewList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RevenueActivity mainActivity = (RevenueActivity) categoryActivity.getActivity();
				itemCategory category = (itemCategory) categoryViewList.getAdapter().getItem(position);
				if (newProgramData != null){
					newProgramData.setCategory(category.getId());
					mainActivity.chooseActivity(dictionary.page.newProgramPage,data, newProgramData);
				}else if (categoryData != null){
					categoryData.setSubType(category.getId());
					mainActivity.chooseActivity(dictionary.page.newCategoryPage,data, categoryData);
				}else{
					mainActivity.chooseActivity(dictionary.page.newCategoryPage,data, category);
				}
			}
		});
	}

	// create action when click back / preview new account
	public class categorytButton implements View.OnClickListener {
		private String type;

		public categorytButton(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View view) {
			RevenueActivity mainActivity = (RevenueActivity) categoryActivity
					.getActivity();
			switch (type) {
			case "back":
				int lastIndexOfGeBack = RevenueActivity.goBackStack.size()-1;
				if (newProgramData != null)
					mainActivity.chooseActivity(dictionary.page.newProgramPage,data, newProgramData);
				else if (categoryData != null)
					mainActivity.chooseActivity(dictionary.page.newCategoryPage,data, categoryData);
				else{
					mainActivity.chooseActivity(RevenueActivity.goBackStack.get(lastIndexOfGeBack), data);
					RevenueActivity.goBackStack.remove(lastIndexOfGeBack);
				}
				break;
			case "newCategoty":
				mainActivity.chooseActivity(dictionary.page.newCategoryPage,data);
				break;
			}
		}
	}
}
