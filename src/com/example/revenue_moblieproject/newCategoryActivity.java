package com.example.revenue_moblieproject;

import java.util.ArrayList;

import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemCategory;
import database.itemPicture;
import database.projectDB;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class newCategoryActivity extends Fragment {
	private Button cancel, save, back, delete, subType;
	private EditText name;
	private ImageButton income, expense;
	private ImageView icon;
	private GridView gridView;
	private TextView categoryViewName;

	public Context _context;
	private allPassData data;
	private itemCategory category;
	private SQLiteDatabase dbRevenue;
	private newCategoryActivity activity;

	public newCategoryActivity(allPassData data) {
		this.data = data;
		category = new itemCategory(0, "", dictionary.programType.expense, 0, 0);
		this.dbRevenue = RevenueActivity.dbRevenue;
		activity = this;
	}

	public newCategoryActivity(allPassData data, itemCategory categoryData) {
		this.data = data;
		category = categoryData;
		this.dbRevenue = RevenueActivity.dbRevenue;
		activity = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("test", "in new category page");
		View rootView = inflater.inflate(R.layout.view_new_category, container, false); // create fragment
		_context = rootView.getContext();

		cancel = (Button) rootView.findViewById(R.id.cancelCategory);
		save = (Button) rootView.findViewById(R.id.addNewCategory);
		back = (Button) rootView.findViewById(R.id.backCategory);
		delete = (Button) rootView.findViewById(R.id.deleteCategory);
		
		categoryViewName = (TextView) rootView.findViewById(R.id.categoryViewName);

		name = (EditText) rootView.findViewById(R.id.newCategoryName);

		income = (ImageButton) rootView.findViewById(R.id.incomeCategory);
		expense = (ImageButton) rootView.findViewById(R.id.expenseCategory);
		subType = (Button) rootView.findViewById(R.id.selectCategorySubtype);

		icon = (ImageView) rootView.findViewById(R.id.iconCategory);

		gridView = (GridView) rootView.findViewById(R.id.pictureView);

		setButton();
		
		return rootView;
	}

	private void setListView() { // show all icon
		String sql = String.format("SELECT * FROM %s", dbName.picture.TABLE);
		Cursor item = dbRevenue.rawQuery(sql, null);

		ArrayList<itemPicture> allPicture = new ArrayList<itemPicture>();
		item.moveToFirst();

		while (!item.isAfterLast()) {
			itemPicture row = new itemPicture(item.getInt(item.getColumnIndex(dbName.picture.column.ID)),
					item.getString(item.getColumnIndex(dbName.picture.column.NAME)),
					item.getString(item.getColumnIndex(dbName.picture.column.COLOR)));
			allPicture.add(row);
			item.moveToNext();
		}
		item.close();

		final gridCellAdapterPuture adapter = new gridCellAdapterPuture(this,
				allPicture);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemPicture picture = (itemPicture) gridView.getAdapter().getItem(position);
				category.setPicture(picture.getId());
				icon.setImageResource(listAdapterCategory.getImageId(_context,
						projectDB.getPicture(dbRevenue, category.getPicture())));
			}
		});
	}

	private void setButton(){ // set onclick to button
		//header button
		if(category.getId() == 0){
			cancel.setOnClickListener(new newCategoryButton("cancel"));
			save.setOnClickListener(new newCategoryButton("save"));
			back.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
		}else{
			cancel.setVisibility(View.GONE);
			save.setVisibility(View.GONE);
			back.setOnClickListener(new newCategoryButton("back"));
			if(category.getId() >=20)
				delete.setOnClickListener(new newCategoryButton("delete"));
			else
				delete.setVisibility(View.GONE);
			
			categoryViewName.setText("View Category");
		}
		
		
		// have to data
		name.setText(category.getName());
		setTypeCategory(category.getType());
		if (category.getSubType() != 0)
			subType.setText(projectDB.getCategoryName(dbRevenue,category.getSubType()));
		if (category.getPicture() != 0)
			icon.setImageResource(listAdapterCategory.getImageId(_context,
					projectDB.getPicture(dbRevenue, category.getPicture())));
		
		// not default Category can edit
		if(category.getId() == 0 || category.getId() >=20){
			income.setOnClickListener(new newCategoryButton("income"));
			expense.setOnClickListener(new newCategoryButton("expense"));

			subType.setOnClickListener(new newCategoryButton("subType"));
			setListView();
		}else{
			name.setFocusable(false);
			name.setClickable(false);

		}
		
	}
	
	public class newCategoryButton implements View.OnClickListener {
		private String type;

		public newCategoryButton(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View view) {
			RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
			category.setName(name.getText().toString());
			switch (type) {
			case "cancel":
				showDialog(1);
				break;
			case "save":
				if (category.getName().equals(""))
					showDialog(2);
				else if (category.getPicture() == 0)
					showDialog(3);
				else {
					insertNewCategory();
					mainActivity.chooseActivity(dictionary.page.categoryPage,data);
				}
				break;
			case "subType":
				mainActivity.chooseActivity(dictionary.page.categoryPage, data,category);
				break;
			case "income":
				category.setSubType(0);
				subType.setText("None");
				setTypeCategory(dictionary.programType.income);
				break;
			case "expense":
				category.setSubType(0);
				subType.setText("None");
				setTypeCategory(dictionary.programType.expense);
				break;
			case "back":
				itemCategory oldCat = projectDB.getCategory(dbRevenue, category.getId());
				if (category.getId() >=20 && !category.equals(oldCat))
					showDialog(4);
				else
					mainActivity.chooseActivity(dictionary.page.categoryPage, data);
				break;
			case "delete":
				showDialog(5);
				break;
			}
		}
	}

	private void setTypeCategory(int type) {
		if (type == dictionary.programType.income) {
			income.setImageResource(R.drawable.btn_categoey_income);
			expense.setImageResource(R.drawable.btn_categoey_expense_no);
		} else {
			income.setImageResource(R.drawable.btn_categoey_income_no);
			expense.setImageResource(R.drawable.btn_categoey_expense);
		}
		category.setType(type);
	}

	public void showDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		switch (id) {
		case 1:
			builder.setMessage("Are u sure that want to cancel this category ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
									mainActivity.chooseActivity(dictionary.page.categoryPage, data);
								}
							}).setNegativeButton("No", null);
			break;
		case 2:
			builder.setMessage("Name is Required.").setCancelable(false);
			builder.setNegativeButton("OK", null);
			break;
		case 3:
			builder.setMessage("Icon is Required.").setCancelable(false);
			builder.setNegativeButton("OK", null);
			break;
		case 4:
			builder.setMessage("Do u want to save Category ?")
			.setCancelable(false)
			.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							updateCategory();
							RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
							mainActivity.chooseActivity(dictionary.page.categoryPage,data);
						}
					})
			.setNegativeButton("No", 
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
							mainActivity.chooseActivity(dictionary.page.categoryPage,data);
						}
			});
			break;
		case 5:
			builder.setMessage("Are u sure that want to delete this category ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									deleteCategory();
									RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
									mainActivity.chooseActivity(dictionary.page.categoryPage, data);
								}
							}).setNegativeButton("No", null);
			break;
		default:
			break;
		}
		builder.show();
	}

	private void insertNewCategory() {
		dbRevenue.insert(dbName.category.TABLE, null, getCategoryValue());
	}

	private void updateCategory() {
		dbRevenue.update(dbName.category.TABLE, getCategoryValue(), " "
				+ dbName.category.column.ID + " = ? ",
				new String[] { String.valueOf(category.getId()) });
	}

	private ContentValues getCategoryValue() {
		ContentValues values = new ContentValues(); // have to put in context
		values.put(dbName.category.column.NAME, category.getName());
		values.put(dbName.category.column.TYPE, category.getType());
		values.put(dbName.category.column.PICTURE, category.getPicture());
		if (category.getSubType() != 0)
			values.put(dbName.category.column.SUBTYPE, category.getSubType());

		return values;
	}
	
	private void deleteCategory() {
		dbRevenue.delete(dbName.category.TABLE, " " + dbName.category.column.ID + " = ? ",
				new String[] { String.valueOf(category.getId()) });
	}
	
}
