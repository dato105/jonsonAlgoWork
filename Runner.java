import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;




public class Runner {

	public static void main(String[] args) {

		//initialize function w of each E
		int[][] W = new int[6][6];

		W[0][1] = -2;  
		W[1][2] = -1;
		W[2][0] = 4;
		W[2][3] = 2;
		W[2][4] = -3;
		W[5][3]= 1;
		W[5][4] = -4;
		
		System.out.println("Graph before the Johnson algorithm:");
		for (int i = 0; i < W.length; i++) {
			for (int j = 0; j < W.length; j++) {
				System.out.print(W[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		pairsThroughVer(W,2);
		
		


	}

	public static void pairsThroughVer(int[][] W, int vertex ) {
		int count = 0;

		boolean flag = jonson(W);

		if(flag) {//If there are no negative circuits
			for (int i = 0; i < W.length; i++) {
				for (int j = 0; j < W.length; j++) {
					//I do not consider a vertex trajectory for itself or a pair that v is included in
					if(i != vertex && vertex != j && i!=j)
						if(W[i][vertex] + W[vertex][j] == W[i][j] )
							count++;
				}

			}
			System.out.println("result:");
			System.out.println("Through vertex 2 pass " + count + " pairs.");
		}



	}

	public static boolean jonson(int[][] W) {

		int size = W.length;
		int [][]W2 = new int[size+1][size+1];

		//create new Graph G' with s 
		for (int i = 1; i < W2.length; i++) {
			for (int j = 1; j < W2[0].length; j++) {
				W2[i][j] = W[i-1][j-1];
			}
		}

		//initialize data structures 
		ArrayList<Vertex> arr = new ArrayList<Vertex>();
		/*Graph- ArrayList of Vertex and
		 *  each Vertex has list of neighbors
		 */
		for (int i = 0; i < W2.length; i++) {

			arr.add(new Vertex(i,Integer.MAX_VALUE,W2));

		}

		//initialize the vertex that contect to all vertex
		for (int i = 1; i < W2.length; i++) {
			arr.get(0).setArrayList(arr.get(i));
		}
		


		/*set each neighbor to his vertex in the vertex
		 *  arraylist send reference not create new to update the original
		 */
		for (int i = 1; i < W2.length; i++) {
			for (int j = 1; j < W2[0].length; j++) {

				if(W2[i][j] != 0) {
					arr.get(i).setArrayList(arr.get(j));
				}
			}
		}
		arr.get(0).setDis(0);//set to 0 to to make him root

		boolean flag = false;

		flag = 	belmanFord(arr, 0);//activate Bellman-Ford and send to him data structures
		if(flag) {
			System.out.println("There is a negative circle in the graph");//true
			return false;
		}


		int []h = new int[size];
		for (int i = 0; i < h.length; i++) {
			h[i] = arr.get(i+1).getDis();

		}

		int dist[][] = new int [W.length][W.length];
		//w* - now update the weight to be positive
		for (int i = 0; i < W.length; i++) {
			for (int j = 0; j < W[0].length; j++) {
				if(W[i][j] != 0) {
					W[i][j] = W[i][j] + h[i] - h[j];

				}
				else {
					W[i][j] = Integer.MAX_VALUE;
				}
				dist[i][j] = Integer.MAX_VALUE;
			}
		}


		for (int i = 0; i < dist.length; i++) {
			dijkstra(W,i,arr,dist) ;
		}


		System.out.println("Graph after the Johnson algorithm:");
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[0].length; j++) {
				
				if(dist[i][j] != Integer.MAX_VALUE ) {
					dist[i][j] = dist[i][j] + h[j] - h[i] ;
					System.out.print(dist[i][j] + " ");
				}
				else {
					dist[i][j] = 0;
					System.out.print(dist[i][j] + " ");
				}
				
			}
			System.out.println();
		}
		System.out.println();
		return true;



	}

	public static boolean belmanFord(ArrayList<Vertex> arr, int g) {
		boolean flag = false;
		for (int i = 0; i < arr.size(); i++) {

			for (int j = 0; j < arr.size(); j++) {
				int k = (g + j)%9;
				/*I decide that the order of checking is 
				 * from index g and circle the list back to g not include
				 */

				boolean temp = arr.get(k).relaxzion();//return boolean if in round |V| some value was change

				if( i == arr.size()-1 && temp) {
					return true;


				}
			}
			//example if you want to print the changing distance

			//			System.out.println("Round " + (i+1)+ ":");
			//			for (int j = 0; j < arr.size(); j++) {
			//				System.out.println("Distance V-" + (j) + ": " + arr.get(j).getDis());
			//			}
			//			System.out.println("-----------------");

		}
		return false;//if there is no change in round |V|
	}

	public static void dijkstra(int[][]W,int s,ArrayList<Vertex> arr,int [][]dist) {
		Set<Integer> visited = new HashSet<Integer>();
		PriorityQueue<Node> pqueue = new PriorityQueue<Node>(W.length, new Node()); 

		for (int i = 0; i < W.length; i++) 
			dist[s][i] = Integer.MAX_VALUE; 

		// first add source vertex to PriorityQueue 
		pqueue.add(new Node(s, 0)); 

		// Distance to the source from itself is 0 
		dist[s][s] = 0;

		while(!pqueue.isEmpty() ) {

			// u is removed from PriorityQueue and has min distance  
			int u = pqueue.remove().node; 

			// add node to finalized list (visited)
			visited.add(u); 

			int edgeDistance = -1; 
			int newDistance = -1; 

			ArrayList<Vertex> adj_list = arr.get(u+1).getArr();

			for (int i = 0; i < adj_list.size(); i++) { 

				Node v = new Node(adj_list.get(i).id-1, W[u][adj_list.get(i).id-1]); 

				//System.out.println(W[u][adj_list.get(i).id-1]+ "   "+ u);
				//  proceed only if current node is not in 'visited'
				if (!visited.contains(v.node)) { 
					edgeDistance = v.cost; 
					newDistance = dist[s][u] + edgeDistance; 

					// compare distances 
					if (newDistance < dist[s][v.node]) 
						dist[s][v.node] = newDistance; 

					// Add the current vertex to the PriorityQueue 
					pqueue.add(new Node(v.node, dist[s][v.node])); 
				} 

			} 



		}



	}


}
