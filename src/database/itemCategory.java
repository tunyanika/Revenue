package database;

public class itemCategory {
	public String name;
	public int id , type , picture , subType;
	
	public itemCategory(int id , String name , int type , int picture , int subType) {
		setId(id);
		setName(name);
		setPicture(picture);
		setSubType(subType);
		setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public int getPicture() {
		return picture;
	}

	public void setPicture(int picture) {
		this.picture = picture;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}
	
	public boolean equals(itemCategory other) {
	    if(!this.getName().equals(other.getName()))
	    	return false;
	    if(!(this.getType() == other.getType()))
	    	return false;
	    if(!(this.getSubType() == other.getSubType()))
	    	return false;
	    if(!(this.getPicture() == other.getPicture()))
	    	return false;
	    return true;
	}

}
