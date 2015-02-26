package database;

import java.util.List;

public class itemGraph {
	public float money;
	public int category;
	
	public itemGraph(int category , float money) {
		setCategory(category);
		setMoney(money);
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public static int findGraph(List<itemGraph> graph , int idCat){
		for(int i = 0 ; i < graph.size() ; i++){
			if(graph.get(i).getCategory() == idCat)
				return i;
		}
		return -1;
	}
	
	public static float getAllMoney(List<itemGraph> graph){
		float money = 0;
		for(int i = 0 ; i < graph.size() ; i++){
			money += graph.get(i).getMoney();
		}
		return money;
	}
}
