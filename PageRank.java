package pregel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sai, Ravi, Ishwari
 */
public class PageRank {
	static int num_workers = 4;
	static int num_vertices = 4;
	static List<Worker> workers = new ArrayList<Worker>();
	static List<Vertex> vertices = new ArrayList<Vertex>();
	
	public static void main(String[] args){
		for(int i=0; i < num_workers; i++){
			workers.add(new Worker(i));
		}
		for(int i=0; i < num_vertices; i++){
			vertices.add(new PageRankVertex(i,1.0/num_vertices,num_vertices));
		}
		create_edges(vertices);
		pagerank_pregel();
	}
	
	public static void create_edges(List<Vertex> vertices){
		for(int i=0;i<num_vertices;i++) {
			vertices.get(0).addAdjacent(vertices.get(1));
			vertices.get(0).addAdjacent(vertices.get(2));
			vertices.get(0).addAdjacent(vertices.get(3));
			vertices.get(1).addAdjacent(vertices.get(2));
			vertices.get(1).addAdjacent(vertices.get(3));
			vertices.get(2).addAdjacent(vertices.get(3));
		}
		
	}


	public static void pagerank_pregel(){
		
		Pregel pregel_object = new Pregel(vertices, workers);
		pregel_object.run();
		System.out.println("\n--------------\n");
		pregel_object.display();
		
	}	

}


class PageRankVertex extends Vertex{
	
	public PageRankVertex(int vertex_id, double value, int count) {
		super(vertex_id,value,count);
	}

	public void update() {
		
		if (this.superstep<3) {
			double old_value=value;
			this.value =0.15/number_of_vertices;
			
			for(int i=0;i<this.incoming_messages.size();i++) {
				double value=incoming_messages.get(0).double_value;
				this.value += 0.85*value;
				incoming_messages.remove(0);
			}
			double outgoing_pagerank=0;
			if (this.adjacent_vertices.size()!=0) {
				outgoing_pagerank = this.value/this.adjacent_vertices.size();
			}
			
			if (old_value!=value){
				for(int i=0;i<this.adjacent_vertices.size();i++) {
					Message m=new Message(this.vertex_id,adjacent_vertices.get(i).vertex_id,outgoing_pagerank);
					this.outgoing_messages.add(m);
				}
			}
			this.superstep++;
		}
		else {
			this.active=false;
			
		}
		
	}
}
