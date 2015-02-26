package database;

public class itemAccount {
	public String name;
	public int id  , type;
	public float balance;
	
	public itemAccount(){}

	public itemAccount(int id , String name ,float balance , int type) {
		setId(id);
		setName(name);
		setBalance(balance);
		setType(type);

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
	
	public boolean equals(itemAccount other) {
	    if(!this.getName().equals(other.getName()))
	    	return false;
	    if(!(this.getType() == other.getType()))
	    	return false;
	    return true;
	}

}
