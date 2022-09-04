


import java.util.ArrayList;

public class Vertex {


	public int id;
	private ArrayList<Vertex> arr;//list of neighbors by reference
	public int dis;
	int[][] W;//table of weights


	//Contractor
	public Vertex(int id,int d,int[][] w) {
		this.id = id;
		this.dis= d;
		this.W = w;
		arr = new ArrayList<>();
	}


	public ArrayList<Vertex> getArr() {
		return arr;
	}


	public void setArr(ArrayList<Vertex> arr) {
		this.arr = arr;
	}
	public void setArrayList(Vertex v) {

		arr.add(v);

	}

	//The relax function
	public boolean relaxzion() {
		boolean flag = false;

		for (int i = 0; i < arr.size(); i++) {
			Vertex v = arr.get(i);
			int dis = v.getDis();
			if(this.dis != Integer.MAX_VALUE) {//if there is a value to check with and its not infinite

				v.setDis(Math.min(v.dis, this.dis + W[this.id][v.id]));
			}

			if(dis != v.getDis()) {//if change was accure
				flag =true;	
			}

		}

		return flag;
	}



	public int getDis() {
		return dis;
	}

	public void setDis(int dis) {
		this.dis = dis;

	}


}
