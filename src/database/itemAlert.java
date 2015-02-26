package database;

public class itemAlert {
	public int id , ison;
	public String date, time , note;

	public itemAlert(int id , String date , String time , String note , int ison) {
		setDate(date);
		setId(id);
		setIson(ison);
		setNote(note);
		setTime(time);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIson() {
		return ison;
	}

	public void setIson(int ison) {
		this.ison = ison;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
