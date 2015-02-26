package database;

public class itemForGraph {

	public int type , account;
	public String firstDate , lastDate;
	
	public itemForGraph(int type , int account , String firstDate , String lastDate) {
		setAccount(account);
		setFirstDate(firstDate);
		setLastDate(lastDate);
		setType(type);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getAccount() {
		return account;
	}
	public void setAccount(int account) {
		this.account = account;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

}
