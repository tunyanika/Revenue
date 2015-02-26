package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class projectDB extends SQLiteOpenHelper {
	private static final String DB_NAME = "Revenue"; // db name  
	private static final int DATABASE_VERSION = 1; //Database Version
	
	private final String[] accountType = {"Other" , "Cash" , "Credit Card" , "Debit Card" , "Saving"};
	
	private final String[] categoryExpense = {"Other" , "Clothing" , "Eating Out" , "Education" , 
			"Entertainment" , "Gift" , "Health & Fitness" , "Home Repair" , "Household" , "Medical" , 
			"Pets" , "Rent" ,"Transport" ,  "Travel" , "Utinities"};
	
	private final String[] categorIncome = {"Other" , "Bonus" , "Salary" , "Savings Deposit"};
	
	private final String[] allPicture = {"baby_icon" , "backpack_icon" , "bag_icon" , "ball_icon", 
			"balloon_icon" , "beverage_icon" , "boar_icon" , "book_icon" , "breastmilk_icon" , "bulb_icon",
			"bus_icon" , "cake_icon" , "camera_icon" , "car_icon" , "cat_icon" , "coffee_icon" , "crown_icon",
			"dog_icon" , "dollarbill_icon" , "dumbbell_icon" , "fastfood_icon" , "film_icon" , "fuel_icon",
			"gift_icon" , "globe_icon" , "graduate_icon" , "green_other_icon" , "heart_icon" , "home_icon", "home_repair_icon" , 
			"joystick_icon" , "moneybag_icon" , "musicnote_icon" , "noodle_icon" , "notebook_icon" , "nurse_icon",
			"painter_icon" , "pc_icon" , "phone_icon" , "piggybang_icon" , "plane_icon" , "red_other_icon" , "rent_icon" ,
			"rice_icon" , "shirt_icon" , "shoes_icon" , "spoon_icon" , "star_icon" , "tap_icon" , "tooth_icon",
			"tv_icon" , "vacation_icon" , "wrench_icon"};
	
	private final String[] allColor = {"#fad7d7" , "#6cc1e6" , "#e04f60" , "#4c8f46", "#ed7a68" , "#e67d22" , 
			"#de6e5f" , "#d2ecfa" , "#e36d6f" , "#e0695c" , "#d93916" , "#25b3b3" , "#e65039" , "#e6c120" , 
			"#9859b3" , "#70c6e6" , "#f58c22" , "#302725" , "#ffd61f" , "#bd3422" , "#354052" , "#de746d" , 
			"#d1401b" , "#ff96e1" , "#4aba4a" , "#63bfa1" , "#66ff33" , "#e6222f" , "#83d6b3" , "#5297b3" , 
			"#c5d6c5" , "#6dbcc2" , "#2828a1" , "#eb3713" , "#2f61cc" , "#6a9dcc" , "#facc4d" , "#c9bda7" , 
			"#ff6f21" , "#edc44a" , "#80dbf2" , "#bd0000" , "#d78cfa" , "#abd9d6" , "#f07160" , "#6ac2eb" , 
			"#c2329c" , "#442b63" , "#373742" , "#2a81b8" , "#ebba50" , "#29a2ab" , "#9b34cf"};
	
	public projectDB(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		//create picture table 
		db.execSQL("CREATE TABLE " + dbName.picture.TABLE + " ("
				+ dbName.picture.column.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
				+ dbName.picture.column.NAME +" TEXT NOT NULL, "
				+ dbName.picture.column.COLOR +" TEXT NOT NULL);");
		
		//create category table
		db.execSQL("CREATE TABLE " + dbName.category.TABLE + " ("
				+ dbName.category.column.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
				+ dbName.category.column.NAME + " TEXT NOT NULL, "
				+ dbName.category.column.TYPE + " INTEGER NOT NULL, "
				+ dbName.category.column.PICTURE + " INTEGER NOT NULL DEFAULT 1 "
				+ "REFERENCES " + dbName.picture.TABLE 
				+ "(" + dbName.picture.column.ID + ") ON DELETE SET DEFAULT, "
				+ dbName.category.column.SUBTYPE + " INTEGER "
				+ "REFERENCES " + dbName.category.TABLE 
				+ "(" + dbName.category.column.ID + ") ON DELETE SET NULL );");
		
		//create account type table
		db.execSQL("CREATE TABLE " + dbName.accountType.TABLE + " ("
				+ dbName.accountType.column.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
				+ dbName.accountType.column.NAME +" TEXT NOT NULL UNIQUE );");
		
		//create account table
		db.execSQL("CREATE TABLE " + dbName.account.TABLE + " ("
				+ dbName.account.column.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
				+ dbName.account.column.NAME + " TEXT NOT NULL UNIQUE, "
				+ dbName.account.column.BALANCE + " REAL NOT NULL, "
				+ dbName.account.column.TYPE + " INTEGER NOT NULL "
				+ "REFERENCES " + dbName.accountType.TABLE 
				+ "(" + dbName.accountType.column.ID + ") );");
		
		//create alert table
		db.execSQL("CREATE TABLE " + dbName.alert.TABLE + " ("
				+ dbName.alert.column.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
				+ dbName.alert.column.DATE + " TEXT NOT NULL, "
				+ dbName.alert.column.TIME + " TEXT NOT NULL, "
				+ dbName.alert.column.NOTE + " TEXT, "
				+ dbName.alert.column.ISON + " INTEGER NOT NULL DEFAULT 1); "); // SQLITE DONSN'T HAVE BOOLEAN SO 0 = FALSE , 1 = TRUE
		
		//create program table
		db.execSQL("CREATE TABLE " + dbName.program.TABLE + " ("
				+ dbName.program.column.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ dbName.program.column.NAME + " TEXT NOT NULL,"
				+ dbName.program.column.MONEY + " REAL NOT NULL,"
				+ dbName.program.column.TYPE + " INTEGER NOT NULL," // TYPE IN 3 => INCOME , EXPENSE , TRANSFROM
				+ dbName.program.column.DATE + " TEXT NOT NULL,"  // SQLITE DON'T HAVE DATE AND TIME SO SAVE AS STRING IN SAME FORMAT
				+ dbName.program.column.NOTE + " TEXT,"
				//SET CATEGORY TO FOREIGNKEY
				+ dbName.program.column.category + " INTEGER NOT NULL DEFAULT 1 "
				+ "REFERENCES " + dbName.category.TABLE 
				+ "(" + dbName.category.column.ID + ") ON DELETE SET DEFAULT ON UPDATE CASCADE, "
				//SET ACCOUNT TO FOREIGNKEY				
				+ dbName.program.column.account + " INTEGER NOT NULL "
				+ "REFERENCES " + dbName.account.TABLE 
				+ "(" + dbName.account.column.ID + ") ON DELETE CASCADE ON UPDATE CASCADE, "
				+ dbName.program.column.accTranTo + " INTEGER "
				+ "REFERENCES " + dbName.account.TABLE 
				+ "(" + dbName.account.column.ID + ") ON DELETE CASCADE ON UPDATE CASCADE);");
		
		String insertData ;
		
		//insert picture(directory) to Picture
		for (int i = 0 ; i < allPicture.length ; i++){
			try{
				insertData = String.format("INSERT INTO %s ( %s , %s ) VALUES ('%s' , '%s'); " 
						, dbName.picture.TABLE , dbName.picture.column.NAME , 
						dbName.picture.column.COLOR , allPicture[i] , allColor[i]);
				db.execSQL(insertData); 
			}catch(SQLException e){
				Log.d("test" , "insert error : " + e);
			}
		}
		
		//insert category
		for (String category : categoryExpense){
			try{
				insertData = String.format("INSERT INTO %s ( %s , %s , %s ) VALUES ('%s' , %s , %s ); " 
						, dbName.category.TABLE , dbName.category.column.NAME , dbName.category.column.TYPE
						, dbName.category.column.PICTURE  , category , dictionary.programType.expense 
						, getDefaultExpensePicture(category));
				db.execSQL(insertData); 
			}catch(SQLException e){
				Log.d("test" , "insert error : " + e);
			}
		}
		for (String category : categorIncome){
			try{
				insertData = String.format("INSERT INTO %s ( %s , %s , %s ) VALUES ('%s' , %s , %s ); " 
						, dbName.category.TABLE , dbName.category.column.NAME , dbName.category.column.TYPE
						, dbName.category.column.PICTURE  , category , dictionary.programType.income 
						, getDefaultIncomePicture(category));
				db.execSQL(insertData); 
			}catch(SQLException e){
				Log.d("test" , "insert error : " + e);
			}
		}
			
		//insert type to Account Type
		for (String type : accountType){
			try{
				insertData = String.format("INSERT INTO %s (%s) VALUES ('%s') ;" 
						, dbName.accountType.TABLE , dbName.accountType.column.NAME , type);
				db.execSQL(insertData); 
			}catch(SQLException e){
				Log.d("test" , "insert error : " + e);
			}
		}
		
		// insert default account
		try{
			insertData = String.format("INSERT INTO %s ( %s , %s , %s ) VALUES ('Default' , 0 , 1); " 
					, dbName.account.TABLE , dbName.account.column.NAME , 
					dbName.account.column.BALANCE , dbName.account.column.TYPE);
			db.execSQL(insertData); 
		}catch(SQLException e){
			Log.d("test" , "insert error : " + e);
		}
		
		Log.d("test" , "table craeted");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + dbName.program.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + dbName.alert.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + dbName.account.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + dbName.accountType.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + dbName.category.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + dbName.picture.TABLE);
		onCreate(db);
	}
	
	private int getDefaultExpensePicture(String category){
		switch(category){
		case "Clothing" :  return 45;
		case "Eating Out" : return 47;
		case "Education" : return 26;
		case "Entertainment" : return 33;
		case "Gift" : return 24;
		case "Health & Fitness" : return 20;
		case "Home Repair" : return 30;
		case "Household" : return 29;
		case "Medical" : return 36;
		case "Pets" : return 18;
		case "Rent" : return 43;
		case "Transport" : return 11;
		case "Travel" : return 52;
		case "Utinities" : return 49;
		default : return 42;
		}
	}
	
	private int getDefaultIncomePicture(String category){
		switch(category){
		case "Bonus" :  return 32;
		case "Salary" : return 19;
		case "Savings Deposit" : return 40;
		default : return 27;
		}
	}
	
	public static void updateAccount(SQLiteDatabase db , itemAccount account) {
		ContentValues values = new ContentValues(); 
		values.put(dbName.account.column.NAME, account.getName());
		values.put(dbName.account.column.BALANCE, account.getBalance() );
		values.put(dbName.account.column.TYPE, account.getType());
		
		db.update(dbName.account.TABLE, values, " " + dbName.account.column.ID + " = ? ", 
				new String[] { String.valueOf(account.getId()) } );
	}
	
	public static itemAccount getAccount(SQLiteDatabase db , int idAcc) {
		itemAccount account = new itemAccount();
		String sql = String.format("SELECT * FROM %s where %s = %s", dbName.account.TABLE , 
				dbName.account.column.ID , idAcc);
		Cursor item = db.rawQuery(sql, null);
		
		if (item != null && item.moveToFirst()){			
			account.setId(idAcc);
			account.setName(item.getString(item.getColumnIndex(dbName.account.column.NAME)));
			account.setBalance(item.getFloat(item.getColumnIndex(dbName.account.column.BALANCE)));
			account.setType(item.getInt(item.getColumnIndex(dbName.account.column.TYPE)));
		}
		item.close();
		return account;
	}
	
	public static itemAccount getAllAccount(SQLiteDatabase db) {
		itemAccount account = new itemAccount();
		String sql = String.format("SELECT sum(%s) FROM %s ", dbName.account.column.BALANCE, dbName.account.TABLE);
		Cursor item = db.rawQuery(sql, null);
		
		if (item != null){
			item.moveToFirst();
			
			account.setId(0);
			account.setName("All Account");
			account.setBalance(item.getFloat(0));
			account.setType(1);
		}
		item.close();
		return account;
	}
	
	public static itemProgram getProgram(SQLiteDatabase db , int idP) {
		itemProgram program = new itemProgram();
		String sql = String.format("SELECT * FROM %s where %s = %s", dbName.program.TABLE , 
				dbName.program.column.ID , idP);
		Cursor item = db.rawQuery(sql, null);
		
		if (item != null){
			item.moveToFirst();
			program.setId(idP);
			program.setName(item.getString(item.getColumnIndex(dbName.program.column.NAME)));
			program.setMoney(item.getFloat(item.getColumnIndex(dbName.program.column.MONEY)));
			program.setType(item.getInt(item.getColumnIndex(dbName.program.column.TYPE)));
			program.setAccount(item.getInt(item.getColumnIndex(dbName.program.column.account)));
			program.setAccTran(item.getInt(item.getColumnIndex(dbName.program.column.accTranTo)));
			program.setCategory(item.getInt(item.getColumnIndex(dbName.program.column.category)));
			program.setDate(item.getString(item.getColumnIndex(dbName.program.column.DATE)));
			program.setNote(item.getString(item.getColumnIndex(dbName.program.column.NOTE)));
		}
		item.close();
		return program;
	}
	
	public static String getMoneyinDay(SQLiteDatabase db , String date , int type , int account) {
		int money = 0;
		String sql = "";
		if(account == 0){
			sql = String.format("SELECT sum(%s) FROM %s where %s = '%s' and %s = %s", 
				dbName.program.column.MONEY, dbName.program.TABLE , dbName.program.column.DATE , date,
				dbName.program.column.TYPE , type);
		}else{
			sql = String.format("SELECT sum(%s) FROM %s where %s = '%s' and %s = %s and %s = %s ", 
					dbName.program.column.MONEY, dbName.program.TABLE , dbName.program.column.DATE , date,
					dbName.program.column.TYPE , type , dbName.program.column.account , account);
		}
		Cursor item = db.rawQuery(sql, null);
		
		item.moveToFirst();
		if (item != null){
			money = (int) item.getFloat(0);
		}
		item.close();
		return money+"";
	}
	
	public static String getPictureFromCategory(SQLiteDatabase db , int idCat) {
		String sql = String.format("select %s from %s where %s = %s", dbName.category.column.PICTURE ,
				dbName.category.TABLE , dbName.category.column.ID , idCat);
		Cursor item = db.rawQuery(sql, null);
		
		int idPic = 0;
		if (item != null){
			item.moveToFirst();
			idPic = item.getInt(0);
		}
		item.close();
		return getPicture(db,idPic);
	}
	
	public static String getCategoryName(SQLiteDatabase db , int idCat) {
		String sql = String.format("select %s from %s where %s = %s", dbName.category.column.NAME ,
				dbName.category.TABLE , dbName.category.column.ID , idCat);
		Cursor item = db.rawQuery(sql, null);
		
		String name = "";
		if (item != null){
			item.moveToFirst();
			name = item.getString(0);
		}
		item.close();
		return name;
	}
	
	public static String getPicture(SQLiteDatabase db , int idPic) {
		String picture = "";
		String sql = String.format("select %s from %s where %s = %s", dbName.picture.column.NAME ,
				dbName.picture.TABLE , dbName.picture.column.ID , idPic);
		Cursor item = db.rawQuery(sql, null);
		
		if (item != null){
			item.moveToFirst();
			picture = item.getString(item.getColumnIndex(dbName.picture.column.NAME));
		}
		item.close();
		return picture;
	}
	
	public static String getColor(SQLiteDatabase db , int idCat) {
		String sql = String.format("select %s from %s where %s = %s", dbName.category.column.PICTURE ,
				dbName.category.TABLE , dbName.category.column.ID , idCat);
		Cursor item = db.rawQuery(sql, null);
		
		int idPic = 0;
		if (item != null && item.moveToFirst()){
			idPic = item.getInt(0);
		}
		item.close();
		
		String color = "";
		sql = String.format("select %s from %s where %s = %s", dbName.picture.column.COLOR ,
				dbName.picture.TABLE , dbName.picture.column.ID , idPic);
		Cursor itemPic = db.rawQuery(sql, null);
		
		if (itemPic != null && itemPic.moveToFirst()){
			color = itemPic.getString(0);
		}
		itemPic.close();
		return color;
	}

	public static itemCategory getCategory(SQLiteDatabase db , int idCat){
		String sql = String.format("select * from %s where %s = %s", 
				dbName.category.TABLE , dbName.category.column.ID , idCat);
		Cursor item = db.rawQuery(sql, null);
		itemCategory cat = null;
		if (item != null){
			item.moveToFirst();
			cat = new itemCategory(
					// item.getColumnIndex(columnName) to get index attribute
					item.getInt(item.getColumnIndex(dbName.category.column.ID)),
					item.getString(item.getColumnIndex(dbName.category.column.NAME)),
					item.getInt(item.getColumnIndex(dbName.category.column.TYPE)),
					item.getInt(item.getColumnIndex(dbName.category.column.PICTURE)),
					item.getInt(item.getColumnIndex(dbName.category.column.SUBTYPE)));
		}
		item.close();
		return cat;
	}
}
