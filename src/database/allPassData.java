package database;

public class allPassData {
	public int account , day , month , year , beforePage;

	public allPassData(int account , int day , int month , int year , int beforePage) {
		setAccount(account);
		setDay(day);
		setMonth(month);
		setYear(year);
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getBeforePage() {
		return beforePage;
	}

	public void setBeforePage(int beforePage) {
		this.beforePage = beforePage;
	}
	
}
