package pregel;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sai, Ravi, Ishwari
 */
public class Bipartite {
static int num_workers = 4;
static int num_vertices = 4;
static List<Worker> workers = new ArrayList<Worker>();
static List<Vertex> vertices = new ArrayList<Vertex>();
static int[][] edges = new int[4][4];
public static void main(String[] args){
		
		for(int i=0; i < num_workers; i++){
			workers.add(new Worker(i));
		}
		for(int i=0; i < num_vertices; i++){
			vertices.add(new BipartiteVertex(i,i%2==0?0:1,num_vertices));
		}
		create_edges(vertices);
		checkVertices(vertices);
		distance_pregel(vertices);
		showPairWith(vertices);
	}
	
	public static void create_edges(List<Vertex> vertices){
		
			vertices.get(0).addAdjacent(vertices.get(1));
			vertices.get(1).addAdjacent(vertices.get(0));
			
			vertices.get(0).addAdjacent(vertices.get(3));
			vertices.get(3).addAdjacent(vertices.get(0));
			
			vertices.get(1).addAdjacent(vertices.get(2));
			vertices.get(2).addAdjacent(vertices.get(1));
		
	}
	public static void checkVertices(List<Vertex>vertices) {
		
		for(int i=0;i<num_vertices;i++) {
			BipartiteVertex v=(BipartiteVertex) vertices.get(i);
			v.pairDone=(vertices.get(i).adjacent_vertices.size()==0);
			
		}
	}
	
	public static void distance_pregel(List<Vertex> vertices){
		Pregel pregel_object = new Pregel(vertices,workers);
		pregel_object.run();
		
	}
	public static void showPairWith(List<Vertex>vertices) {
		int count=0;
		System.out.println("\n--------------\n");
		for(int i=0;i<num_vertices;i++) {
			BipartiteVertex v=(BipartiteVertex) vertices.get(i);
			if (v.pairWith!=-1) {
				System.out.println("Vertex "+i+" is paired with "+v.pairWith);
				count++;
			}
			else
			System.out.println("Vertex "+i+" is not paired");
		}
		System.out.println("Maximum Edges "+count/2);
	}
}
class BipartiteVertex extends Vertex{
	int no_of_vertices;
	
	int side; 
	boolean pairDone=false;
	int pairWith=-1;
	public BipartiteVertex(int i, int d, int v) {
		super(i,d,v);
		this.no_of_vertices=v;
	}
	
	
	public void update() {
	if (this.superstep<5) {
			if (this.superstep%4==0) {
				
				// All left sides broadcast messages to right - request
				for(int i=0;value==0 && pairDone==false && i<adjacent_vertices.size();i++) {
					Message msg=new Message(vertex_id,adjacent_vertices.get(i).vertex_id,adjacent_vertices.size());
					this.outgoing_messages.add(msg);
					System.out.println("Requested "+vertex_id + " "+adjacent_vertices.get(i).vertex_id );
				}
			}
			
			else if (superstep%4==1) {
				// All right sides broadcast messages to right - grant
				int min=-1;
				double minIndegree=100;
				for(int i=0;value==1 && pairDone==false && i<this.incoming_messages.size();i++) {
					Message m = this.incoming_messages.get(i);
					if (m.double_value<minIndegree) {
						min=m.source_id;
						minIndegree=m.double_value;
					}
				}
				if (min!=-1) {
					Message msg=new Message(vertex_id,min,adjacent_vertices.size());
					this.outgoing_messages.add(msg);
					System.out.println("Granted "+vertex_id + " "+min);
				}

			}
			else if (superstep%4==2) {
				
				int min=-1;
				double minIndegree=100;
				for(int i=0;value==0 && pairDone==false && i<this.incoming_messages.size();i++) {
					Message m = this.incoming_messages.get(i);
					if (m.double_value<minIndegree) {
						min=m.source_id;
						minIndegree=m.double_value;
					}
				}
				if (min!=-1) {
					Message msg=new Message(vertex_id,min,adjacent_vertices.size());
					this.outgoing_messages.add(msg);
					this.pairDone=true;
					this.pairWith=min;
					System.out.println("Accepted "+vertex_id +" "+min);
				}
				
				
			}
			else {
				int min=-1;
				double minIndegree=100;
				for(int i=0;value==1 && pairDone==false && i<this.incoming_messages.size();i++) {
					Message m = this.incoming_messages.get(i);
					if (m.double_value<minIndegree) {
						min=m.source_id;
						minIndegree=m.double_value;
					}
				}
				if (min!=-1) {
					Message msg=new Message(vertex_id,min,adjacent_vertices.size());
					this.outgoing_messages.add(msg);
					this.pairDone=true;
					this.pairWith=min;
					System.out.println("Paired "+vertex_id+" "+min);
				}
			}
			this.superstep++;
	}
	else {
		this.active=false;
	}
	}
}