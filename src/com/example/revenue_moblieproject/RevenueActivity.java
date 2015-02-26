package com.example.revenue_moblieproject;

import java.util.ArrayList;
import java.util.List;

import database.allPassData;
import database.dictionary;
import database.itemAccount;
import database.itemCategory;
import database.itemForGraph;
import database.itemProgram;
import database.projectDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class RevenueActivity extends Activity {
	private ImageButton goToCalendar , goToAccount , goToGrahp , goToCategory ,about;
	
	public static SQLiteDatabase dbRevenue;
	public static List<Integer> goBackStack = new ArrayList<>(); 
	public projectDB myDB;
	private allPassData data = new allPassData(0, 0 , 0 , 0, dictionary.page.calendarPage);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE); //not show title bar
		
		//create database
		myDB = new projectDB(this);
		dbRevenue = myDB.getWritableDatabase();
		//myDB.onUpgrade(dbRevenue, 0, 1);
		
		setContentView(R.layout.main_layout);
		
		goToCalendar = (ImageButton) findViewById(R.id.menuCalendar);
		goToCalendar.setOnClickListener(new menuBarButton("calendar"));
		
		goToAccount = (ImageButton) findViewById(R.id.menuBookBank); 
		goToAccount.setOnClickListener(new menuBarButton("account"));
		
		goToCategory = (ImageButton) findViewById(R.id.menuCategory); 
		goToCategory.setOnClickListener(new menuBarButton("category"));
		
		goToGrahp = (ImageButton) findViewById(R.id.menuGraph);
		goToGrahp.setOnClickListener(new menuBarButton("graph"));
		
		about = (ImageButton) findViewById(R.id.menuAbout);
		about.setOnClickListener(new menuBarButton("about"));
		
		chooseActivity(dictionary.page.calendarPage ,data);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case 1:
			builder.setTitle("Developer");
			builder.setMessage("Yanika Inkham\n09-5950-9926")
			.setCancelable(false)
			.setNegativeButton("OK", null);
			break;

		}
		dialog = builder.create();

		return dialog;
	}
	
	public void chooseActivity(int page , allPassData data){
		Fragment fragment = null;
		switch (page) {
		case dictionary.page.calendarPage:
			fragment = new calendarActivity(data);
			break;
		case dictionary.page.categoryPage:
			fragment = new categoryActivity(data);
			break;
		case dictionary.page.inOneDayPage:
			fragment = new oneDayActivity(data);
			break;
		case dictionary.page.accountPage:
			fragment = new accountActivity(data);
			break;
		case dictionary.page.newProgramPage:
			fragment = new newProgramActivity(data);
			break;
		case dictionary.page.newAccountPage:
			fragment = new newAccountActivity(data);
			break;
		case dictionary.page.newCategoryPage:
			fragment = new newCategoryActivity(data);
			break;
		case dictionary.page.graphPage:
			fragment = new graphActivity(data);
			break;
		default:
			break;
		}
		try{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		}catch(Exception e){
			Log.d("test", "error : " + e);
		}
	}
	
	public void chooseActivity(int page , allPassData data , itemProgram newProgramData){
		Fragment fragment = null;
		switch (page) {
		case dictionary.page.categoryPage:
			fragment = new categoryActivity(data , newProgramData);
			break;
		case dictionary.page.newProgramPage:
			fragment = new newProgramActivity(data , newProgramData);
			break;
		case dictionary.page.accountPage:
			fragment = new accountActivity(data , newProgramData);
			break;
		default:
			break;
		}
		try{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		}catch(Exception e){
			Log.d("test", "error : " + e);
		}
	}
	
	public void chooseActivity(int page , allPassData data , itemForGraph graphData ){
		Fragment fragment = null;
		switch (page) {
		case dictionary.page.graphPage:
			fragment = new graphActivity(data , graphData);
			break;
		case dictionary.page.accountPage:
			fragment = new accountActivity(data , graphData);
			break;
		default:
			break;
		}
		try{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		}catch(Exception e){
			Log.d("test", "error : " + e);
		}
	}
	//553890
	public void chooseActivity(int page , allPassData data , itemProgram newProgramData  , int type){
		Fragment fragment = null;
		switch (page) {
		case dictionary.page.pickaDate:
			fragment = new pickADate(data , newProgramData , type);
			break;
		default:
			break;
		}
		try{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		}catch(Exception e){
			Log.d("test", "error : " + e);
		}
	}
	
	public void chooseActivity(int page , allPassData data , itemForGraph graphData  , int type){
		Fragment fragment = null;
		switch (page) {
		case dictionary.page.pickaDate:
			fragment = new pickADate(data , graphData , type);
			break;
		default:
			break;
		}
		try{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		}catch(Exception e){
			Log.d("test", "error : " + e);
		}
	}

	public void chooseActivity(int page , allPassData data, itemCategory categoryData) {
		Fragment fragment = null;
		switch (page) {
			case dictionary.page.categoryPage:
				fragment = new categoryActivity(data , categoryData);
				break;
			case dictionary.page.newCategoryPage:
				fragment = new newCategoryActivity(data , categoryData);
				break;
		}
		try{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		}catch(Exception e){
			Log.d("test", "error : " + e);
		}
	}

	public void chooseActivity(int page, allPassData data, itemAccount account) {
		Fragment fragment = null;
		switch (page) {
			case dictionary.page.newAccountPage:
				fragment = new newAccountActivity(data, account);
				break;
		}
		try{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		}catch(Exception e){
			Log.d("test", "error : " + e);
		}
	}

	public class menuBarButton implements View.OnClickListener {
		private String button;

		public menuBarButton(String button) {
			this.button = button;
		}

		@Override
		public void onClick(View view) {
			switch (button) {
			case "calendar":
				//save previous page to stack
				goBackStack.add(data.getBeforePage());
				chooseActivity(dictionary.page.calendarPage , data);
				break;
			case "account":
				goBackStack.add(data.getBeforePage());
				chooseActivity(dictionary.page.accountPage , data);
				break;
			case "graph":
				goBackStack.add(data.getBeforePage());
				chooseActivity(dictionary.page.graphPage , data);
				break;
			case "category":
				goBackStack.add(data.getBeforePage());
				chooseActivity(dictionary.page.categoryPage , data);
				break;
			case "about":
				showDialog(1);
				break;
			}
		}

	}
	
}
