package com.example.revenue_moblieproject;

import database.allPassData;
import database.dbName;
import database.dictionary;
import database.itemAccount;
import database.itemProgram;
import database.projectDB;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class newProgramActivity extends Fragment{

	private Button cancel , save , back , delete , category , account , accountForm , accountTo ,programDate ;
	private EditText programName , programMoney , programNote ;
	private ImageButton typeIncome , typeExpense , typeTransfer ;
	private LinearLayout notTransfer , selectTransfer;
	private TextView programNameView;
	
	private SQLiteDatabase dbRevenue;
	private  newProgramActivity activity ;
	private int day, month , year;
	private itemProgram newProgramData;
	private allPassData data;
	private itemAccount accountData , accountTranTo;
	
	public newProgramActivity(allPassData data) {
		this.data = data;
		this.day = data.getDay();
		this.month = data.getMonth();
		this.year = data.getYear();
		this.dbRevenue = RevenueActivity.dbRevenue;
		activity = this;
		newProgramData = new itemProgram(0,"-",0,dictionary.programType.expense,
				"","", 1,data.getAccount(),0);
	}
	
	public newProgramActivity(allPassData data , itemProgram newProgramData) {
		this.data = data;
		this.day = data.getDay();
		this.month = data.getMonth();
		this.year = data.getYear();
		this.dbRevenue = RevenueActivity.dbRevenue;
		activity = this;
		this.newProgramData = newProgramData;
		if(this.newProgramData.getAccTran() == -1)
			this.newProgramData.setAccTran(projectDB.getProgram(dbRevenue, this.newProgramData.getId()).getAccTran());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_new_program, container, false);
		
		//menu top
		cancel = (Button) rootView.findViewById(R.id.cancelProgram);
		save = (Button) rootView.findViewById(R.id.addNewProgram);
		back = (Button) rootView.findViewById(R.id.backProgram);
		delete = (Button) rootView.findViewById(R.id.deleteProgram);
		programNameView = (TextView) rootView.findViewById(R.id.programNameView);
		
		programName = (EditText) rootView.findViewById(R.id.newProgramName);
		if(!newProgramData.getName().equals("-"))
			programName.setText(newProgramData.getName());;
		
		//about money
		typeIncome = (ImageButton) rootView.findViewById(R.id.incomeButton);
		typeExpense = (ImageButton) rootView.findViewById(R.id.expenseButton);
		typeTransfer = (ImageButton) rootView.findViewById(R.id.transferButton);
		programMoney = (EditText) rootView.findViewById(R.id.moneyThatUse);
		if(newProgramData.getMoney() != 0)
			programMoney.setText(newProgramData.getMoney()+"");
		
		//about category
		category = (Button) rootView.findViewById(R.id.categoryChoose);
		category.setText(projectDB.getCategoryName(dbRevenue, newProgramData.getCategory()));
		
		//account normal
		notTransfer = (LinearLayout) rootView.findViewById(R.id.notTransfer);
		account = (Button) rootView.findViewById(R.id.accountChoose);
		
		//account when transfer
		selectTransfer = (LinearLayout) rootView.findViewById(R.id.selectTransfer);
		accountForm = (Button) rootView.findViewById(R.id.accountChooseFrom);
		accountTo = (Button) rootView.findViewById(R.id.accountChooseTo);
		
		//date
		programDate = (Button) rootView.findViewById(R.id.newProgramDate);
		
		programNote = (EditText) rootView.findViewById(R.id.programNote);
		programNote.setText(newProgramData.getNote());
		
		setButtonClick();
		
		return rootView;
	}

	public class newProgramButton implements View.OnClickListener {
		private String button;
		
		public newProgramButton(String button){
			this.button = button;
		}
		
		@Override
		public void onClick(View arg0) {
			RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
			setData();
			switch (button) {
			case "cancel":
				showDialog(1);
				break;
			case "back":
				itemProgram oldPro = projectDB.getProgram(dbRevenue, newProgramData.getId());
				if(!newProgramData.equals(oldPro))
					showDialog(4);
				else
					mainActivity.chooseActivity(dictionary.page.inOneDayPage , data);
				break;
			case "save":
				try{
					newProgramData.setMoney(Float.parseFloat(programMoney.getText().toString()));
				}catch(NumberFormatException e){
					Log.d("test", "no money set to 0");
				}finally{
					if (newProgramData.getMoney() == 0){
						showDialog(2);
					}else if(accountData == null || (newProgramData.getType() == dictionary.programType.transfer 
							&& newProgramData.getAccTran() < 1 )){
						showDialog(3);
					}else{		
						upDateAccount(newProgramData.getType());
						addNewProgram();
						mainActivity.chooseActivity(dictionary.page.inOneDayPage, data);
					}
				}
				break;
			case "clickIncome":
				newProgramData.setCategory(16);
				setIsTrans(dictionary.programType.income);
				break;
			case "clickExpense":
				newProgramData.setCategory(1);
				setIsTrans(dictionary.programType.expense);
				break;
			case "clickTransfer":
				setIsTrans(dictionary.programType.transfer);
				break;
			case "category":
				mainActivity.chooseActivity(dictionary.page.categoryPage,data,newProgramData);
				break;
			case "account":
				mainActivity.chooseActivity(dictionary.page.accountPage,data,newProgramData);
				break;
			case "accountTo":
				newProgramData.setAccTran(-1);
				mainActivity.chooseActivity(dictionary.page.accountPage,data,newProgramData);
				break;
			case "date":
				mainActivity.chooseActivity(dictionary.page.pickaDate , data , newProgramData , 1);
				break;
			case "delete":
				showDialog(5);
				break;
			}
		}
	}
	
	private void setButtonClick(){
		if(newProgramData.getId() == 0){ // fisrt time can do
			cancel.setOnClickListener(new newProgramButton("cancel"));
			save.setOnClickListener(new newProgramButton("save"));
			back.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
			
			typeIncome.setOnClickListener(new newProgramButton("clickIncome"));
			typeExpense.setOnClickListener(new newProgramButton("clickExpense"));
			typeTransfer.setOnClickListener(new newProgramButton("clickTransfer"));
		}else{
			cancel.setVisibility(View.GONE);
			save.setVisibility(View.GONE);
			back.setOnClickListener(new newProgramButton("back"));
			delete.setOnClickListener(new newProgramButton("delete"));
		}
		
		if(newProgramData.getId() != 0)
			programNameView.setText("View Program");
		
		programDate.setOnClickListener(new newProgramButton("date"));
		
		category.setOnClickListener(new newProgramButton("category"));
		account.setOnClickListener(new newProgramButton("account"));
		
		accountForm.setOnClickListener(new newProgramButton("account"));
		accountTo.setOnClickListener(new newProgramButton("accountTo"));
		
		if(newProgramData.getDate().equals("")){
			newProgramData.setDate(getDate(day, month, year));
			programDate.setText(graphActivity.getDate(newProgramData.getDate()));
		}else{
			programDate.setText(graphActivity.getDate(newProgramData.getDate()));
		}
		setIsTrans(newProgramData.getType());
		
	}
	
	public void showDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		switch (id) {
		case 1:
			builder.setMessage("Are u sure that want to cancel this program ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
									mainActivity.chooseActivity(dictionary.page.inOneDayPage , data);
								}
							}).setNegativeButton("No", null);
			break;
		case 2:
			builder.setMessage("Amount is Required.")
					.setCancelable(false);
			builder.setNegativeButton("OK", null);
			break;
		case 3:
			builder.setMessage("Account is Required.")
					.setCancelable(false);
			builder.setNegativeButton("OK", null);
			break;
		case 4:
			builder.setMessage("Do u want to save this program ?")
					.setCancelable(false)
					.setPositiveButton("Save",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
									upDateAccount(newProgramData.getType());
									updateProgram();
									mainActivity.chooseActivity(dictionary.page.inOneDayPage , data);
								}
							})
					.setNegativeButton("No", 		
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
									mainActivity.chooseActivity(dictionary.page.inOneDayPage , data);
								}
							});
			break;
		case 5:
			builder.setMessage("Are u sure that want to delete this program ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									upDateAccountWhenDelete();
									deleteProgram();
									RevenueActivity mainActivity = (RevenueActivity) activity.getActivity();
									mainActivity.chooseActivity(dictionary.page.inOneDayPage , data);
								}
							})
					.setNegativeButton("No", null);
			break;
		default:
			break;
		}
		builder.show();
	}
	
	private void setIsTrans(int type){ // set linearlayout to show or hind when change programtype
		if(newProgramData.getAccount() != 0)
			accountData = projectDB.getAccount(dbRevenue, newProgramData.getAccount());
		
		if(type == dictionary.programType.transfer){
			typeIncome.setImageResource(R.drawable.btn_income_noselect);
			typeExpense.setImageResource(R.drawable.btn_expense_noselect); 
			typeTransfer.setImageResource(R.drawable.btn_trans_select);
			
			if(accountData == null)
				accountForm.setText("Not Select");
			else
				accountForm.setText(accountData.getName());
			if(newProgramData.getAccTran() > 0){
				accountTranTo = projectDB.getAccount(dbRevenue, newProgramData.getAccTran());
				accountTo.setText(accountTranTo.getName());
			}
			
			setSize(true,selectTransfer); // set size is zero = false => show
			setSize(false,notTransfer);
		}else{
			if(type == dictionary.programType.expense){
				typeIncome.setImageResource(R.drawable.btn_income_noselect);
				typeExpense.setImageResource(R.drawable.btn_expense_select);
			}else{
				typeIncome.setImageResource(R.drawable.btn_income_select);
				typeExpense.setImageResource(R.drawable.btn_expense_noselect);
			}
			typeTransfer.setImageResource(R.drawable.btn_trans_noselect);
			
			if(accountData == null)
				account.setText("Not Select");
			else
				account.setText(accountData.getName());
			
			setSize(false,selectTransfer); // set size is zero = true => not show
			setSize(true,notTransfer);
		}
		newProgramData.setType(type);
		category.setText(projectDB.getCategoryName(dbRevenue, newProgramData.getCategory()));
	}
	
	private void setSize(boolean isShow , LinearLayout layout){
		if(isShow){
//			layout.setLayoutParams(new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.MATCH_PARENT,
//					LinearLayout.LayoutParams.WRAP_CONTENT));
			layout.setVisibility(View.VISIBLE);
		}else{
//			layout.setLayoutParams(new LinearLayout.LayoutParams(0 , 0));
			layout.setVisibility(View.GONE);
		}
	}	
	
	private void setData(){
		try{
			newProgramData.setMoney(Float.parseFloat(programMoney.getText().toString()));
		}catch(NumberFormatException e){
			Log.d("test","no money set to 0");
		}finally{
			String name = programName.getText().toString();
			newProgramData.setName(name.equals("") ? "-" : name);
			newProgramData.setNote(programNote.getText().toString());
		}
	}
	
	private void upDateAccount(int type){ //update account when change
		float amount = accountData.getBalance();
		float money = newProgramData.getMoney();
		if(newProgramData.getId() == 0){ // save first time
			switch (type) {
			case dictionary.programType.expense:
				accountData.setBalance(amount-money);
				break;
			case dictionary.programType.income:
				accountData.setBalance(amount+money);
				break;
			case dictionary.programType.transfer:
				float amountTo = accountTranTo.getBalance();
				accountData.setBalance(amount-money);
				accountTranTo.setBalance(amountTo+money);
				projectDB.updateAccount(dbRevenue, accountTranTo);
				break;
			}
			projectDB.updateAccount(dbRevenue, accountData);
		}else{ // update to
			itemProgram oldProgramData = projectDB.getProgram(dbRevenue, newProgramData.getId());
			Log.d("test", "old data money = " + oldProgramData.getMoney() + " now money = " + money + " balance = " +accountData.getBalance());
			itemAccount oldAccData = projectDB.getAccount(dbRevenue, oldProgramData.getAccount());
			
			
			switch (type) {
			case dictionary.programType.expense:
				if(accountData.getId() != oldAccData.getId()){ // change account
					oldAccData.setBalance(oldAccData.getBalance() + oldProgramData.getMoney());
					accountData.setBalance(amount - money);
				}else
					accountData.setBalance(amount + oldProgramData.getMoney() - money);
				break;
			
			case dictionary.programType.income:
				if(accountData.getId() != oldAccData.getId()){ // change account
					oldAccData.setBalance(oldAccData.getBalance() - oldProgramData.getMoney());
					accountData.setBalance(amount + money);
				}else
					accountData.setBalance(amount - oldProgramData.getMoney() + money);
				break;
			
			case dictionary.programType.transfer:
				float amountTo = accountTranTo.getBalance();
				itemAccount oldAccTo = projectDB.getAccount(dbRevenue, oldProgramData.getAccTran());
				if(accountData.getId() != oldAccData.getId()){ // change account from
					oldAccData.setBalance(oldAccData.getBalance() + oldProgramData.getMoney());
					accountData.setBalance(amount - money);
				}else
					accountData.setBalance(amount + oldProgramData.getMoney() - money);
				
				if(accountTranTo.getId() != oldAccTo.getId()){
					oldAccTo.setBalance(oldAccTo.getBalance() - oldProgramData.getMoney());
					accountTranTo.setBalance(amountTo + money);
				}else
					accountTranTo.setBalance(amountTo - oldProgramData.getMoney() + money);
				
				projectDB.updateAccount(dbRevenue, oldAccTo);
				projectDB.updateAccount(dbRevenue, accountTranTo);
				break;
			}
			projectDB.updateAccount(dbRevenue, oldAccData);
			projectDB.updateAccount(dbRevenue, accountData);
		}
	}

	private void upDateAccountWhenDelete(){ //update account when change
		itemProgram programData = projectDB.getProgram(dbRevenue, newProgramData.getId());
		itemAccount accData = projectDB.getAccount(dbRevenue, programData.getAccount());
			
		switch (programData.getType()) {
		case dictionary.programType.expense:
			accData.setBalance(accData.getBalance() + programData.getMoney());
			break;
		case dictionary.programType.income:
			accData.setBalance(accData.getBalance() - programData.getMoney());
			break;
		case dictionary.programType.transfer:
			itemAccount accToData = projectDB.getAccount(dbRevenue, programData.getAccTran());
			accData.setBalance(accData.getBalance() + programData.getMoney());
			accToData.setBalance(accToData.getBalance() - programData.getMoney());
			projectDB.updateAccount(dbRevenue, accToData);
			break;
		}
		projectDB.updateAccount(dbRevenue, accData);
	}
	
	private void addNewProgram(){ //insert new program 
		ContentValues values = getProgramValues();
		
		dbRevenue.insert(dbName.program.TABLE, null, values);
	}	
	
	private void updateProgram(){ //insert new program 
		ContentValues values = getProgramValues();
		
		dbRevenue.update(dbName.program.TABLE, values, " " + dbName.program.column.ID + " = ? ", 
				new String[] { String.valueOf(newProgramData.getId()) });
	}
	
	private void deleteProgram(){	
		dbRevenue.delete(dbName.program.TABLE, " " + dbName.program.column.ID + " = ? ", 
				new String[] { String.valueOf(newProgramData.getId()) });
	}
	
	private ContentValues getProgramValues(){
		ContentValues values = new ContentValues(); //have to put in context
		values.put(dbName.program.column.NAME, newProgramData.getName()); // column name , data
		values.put(dbName.program.column.MONEY, newProgramData.getMoney() );
		values.put(dbName.program.column.TYPE, newProgramData.getType());
		values.put(dbName.program.column.DATE, newProgramData.getDate());
		values.put(dbName.program.column.NOTE, newProgramData.getNote());
		values.put(dbName.program.column.category, newProgramData.getCategory());
		values.put(dbName.program.column.account, newProgramData.getAccount());
		if(newProgramData.getAccTran() > 0)
			values.put(dbName.program.column.accTranTo, newProgramData.getAccTran());
		
		return values;
	}
	
	public static String getDate(int day , int month , int year) {
		String date = year+"-";
		date += (month < 10 ?  "0"+month+"-" : month+"-");
		date += (day < 10 ?  "0"+day : day);
		return date;
	}
	
	public String getTime(int hour , int min) {
		String timeResult = "";
				
		timeResult += (hour < 10 ?  "0"+hour+":" : hour+":");
		timeResult += (min < 10 ?  "0"+min : min);
		
		return timeResult;
	}
	
}
