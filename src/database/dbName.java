package database;

public class dbName {
	
	public dbName() {
	}

	public class picture {
		public static final String TABLE = "picture";
		public class column {
			public static final String ID = "pic_id";
			public static final String NAME = "pic_name";
			public static final String COLOR = "pic_color";
		}
	}
	
	public class category {
		public static final String TABLE = "category";
		public class column {
			public static final String ID = "c_id";
			public static final String NAME = "c_name";
			public static final String TYPE = "c_type";
			public static final String PICTURE = "c_picture";
			public static final String SUBTYPE = "subType";
		}
	}
	
	public class accountType {
		public static final String TABLE = "accountType";
		public class column {
			public static final String ID = "t_id";
			public static final String NAME = "t_name";
		}
	}
	
	public class account {
		public static final String TABLE = "account";
		public class column {
			public static final String ID = "a_id";
			public static final String NAME = "a_name";
			public static final String BALANCE = "a_balance";
			public static final String TYPE = "accType";
		}
	}
	
	public class alert {
		public static final String TABLE = "alert";
		public class column {
			public static final String ID = "s_id";
			public static final String DATE = "s_date";
			public static final String TIME = "s_time";
			public static final String ISON = "s_ison";
			public static final String NOTE = "s_note";
		}
	}
	
	public class program {
		public static final String TABLE = "program";
		public class column {
			public static final String ID = "p_id";
			public static final String NAME = "p_name";
			public static final String MONEY = "p_money";
			public static final String TYPE = "p_type";
			public static final String DATE = "p_date";
			public static final String NOTE = "p_note";
			public static final String category = "category";
			public static final String account = "account"; //tranfrom
			public static final String accTranTo = "account_transfer"; // transto
		}
	}
	
}
