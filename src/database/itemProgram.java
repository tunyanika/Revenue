package database;

public class itemProgram {

	public String name, date, note;
	public int id, type, category, account, accTran;
	public float money;

	public itemProgram() {
	}

	public itemProgram(int id, String name, float money, int type, String date,
			String note, int category, 
			int account, int accTran) {
		setId(id);
		setName(name);
		setMoney(money);
		setType(type);
		setDate(date);
		setNote(note);
		setCategory(category);
		setAccount(account);
		setAccTran(accTran);
	}


	public int getAccTran() {
		return accTran;
	}

	public void setAccTran(int accTran) {
		this.accTran = accTran;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public boolean equals(itemProgram other) {
	    if(!this.getName().equals(other.getName()))
	    	return false;
	    if(!(this.getType() == other.getType()))
	    	return false;
	    if(!(this.getMoney() == other.getMoney()))
	    	return false;
	    if(!(this.getAccount() == other.getAccount()))
	    	return false;
	    if(!(this.getAccTran() == other.getAccTran()))
	    	return false;
	    if(!(this.getCategory() == other.getCategory()))
	    	return false;
	    if(!this.getDate().equals(other.getDate()))
	    	return false;
	    if(!this.getNote().equals(other.getNote()))
		    return false;
	    return true;
	}
}
