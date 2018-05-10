package pregel;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sai, Ravi, Ishwari
 */
public class Distance {
	
	static int num_workers = 4;
	static int num_vertices = 4;
	static List<Worker> workers = new ArrayList<Worker>();
	static List<Vertex> vertices = new ArrayList<Vertex>();
	static int[][] edges = new int[4][4];

	public static void main(String[] args){
		
		for(int i =0; i < 4; i++) {
			for(int j=0; j< 4; j++)
			edges[i][j] = 100;
		}
		for(int i=0; i < num_workers; i++){
			workers.add(new Worker(i));
		}
		
		for(int i=0; i < num_vertices; i++){
			vertices.add(new distanceVertex(i,i>0?100:0,num_vertices,edges));
		}
		create_edges(vertices);
		distance_pregel(vertices);
	}

	public static void create_edges(List<Vertex> vertices){
		for(int i=0;i<num_vertices;i++) {
			vertices.get(0).addAdjacent(vertices.get(1));
			edges[0][1] = 5;
			vertices.get(0).addAdjacent(vertices.get(2));
			edges[0][2] = 2;
			vertices.get(1).addAdjacent(vertices.get(2));
			edges[1][2] = 4;
			vertices.get(2).addAdjacent(vertices.get(0));
			edges[2][0] = 1;
			vertices.get(3).addAdjacent(vertices.get(2)); 
			edges[3][2] = 6;
		}
	}

	public static void distance_pregel(List<Vertex> vertices){
		Pregel pregel_object = new Pregel(vertices,workers);
		pregel_object.run();
		pregel_object.display();
	}	
}	
class distanceVertex extends Vertex{
	int no_of_vertices;
	int edges[][];
	public distanceVertex(int i, int d, int v,int[][] edges) {
		super(i,d,v);
		this.no_of_vertices=v;
		this.edges = edges;
	}
	public void update() {
		
		if (this.superstep<4) {
			
			for(int i=0;i<this.incoming_messages.size();i++) {
				double value=incoming_messages.get(0).double_value + edges[incoming_messages.get(0).source_id][incoming_messages.get(0).destination_id];
				if(this.value > value) {
				this.value = value;
				incoming_messages.remove(0);
				}
			}
			
			//System.out.println("Vertex:"+this.vertex_id+" Size:"+this.adjacent_vertices.size() +" Msgs:"+this.incoming_messages.size()+" Value:"+this.value+" PR:"+outgoing_pagerank);
			for(int i=0;i<this.adjacent_vertices.size();i++) {
				Message m=new Message(this.vertex_id,adjacent_vertices.get(i).vertex_id,this.value);
				this.outgoing_messages.add(m);
			}
			
		}
		else {
			this.active=false;
			
		}
			
	}
}
	


