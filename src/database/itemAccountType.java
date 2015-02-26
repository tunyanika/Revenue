package database;

public class itemAccountType {
	public String name;
	public int id ;

	public itemAccountType(int id , String name) {
		setId(id);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
