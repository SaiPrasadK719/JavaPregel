/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pregel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sai, Ravi, Ishwari
 */
public class Pregel {
    static int no_of_vertices;
    static int no_of_workers;
    
    List<Worker> workers = new ArrayList<Worker>();
    
    
    public int getHashValue(int vertex_id) {
    	return vertex_id%no_of_workers;
    }
    
    public Pregel(List<Vertex> input_vertices,List<Worker> input_workers) {
    	no_of_vertices=input_vertices.size();
    	no_of_workers=input_workers.size();
    	System.out.println("Pregel Started");
    	
    	for(int i=0;i<no_of_workers;i++) {
    		workers.add(input_workers.get(i));
    		input_workers.get(i).addWorkers(input_workers);
    	}
    	
    	for(int i=0;i<no_of_vertices;i++) {
    		Vertex vertex_instance = input_vertices.get(i);
    		int worker_id = getHashValue(vertex_instance.vertex_id);
    		input_workers.get(worker_id).addVertex(vertex_instance);
    		input_workers.get(worker_id).start();
    	}
    	
    }
    
    
    public void display() {
    	for(int i=0;i<no_of_workers;i++) {
			workers.get(i).displayOutput();
	}
    }
    
	public void superstep(){
    	
		for(int i=0;i<no_of_workers;i++) {
    			workers.get(i).startProcess();
    	}
		System.out.println("All threads started");
		
		
		
		for(int i=0;i<no_of_workers;i++) {
				while(workers.get(i).isDone()==false);
				System.out.println("Process "+i+" completed");
    	}
		
		
		
		System.out.println("Barrier Sync");
		
		
		for(int i=0;i<no_of_workers;i++) {
			workers.get(i).transferMessages();
		}
		
		System.out.println("All transfers started");
		
		for(int i=0;i<no_of_workers;i++) {
				while(workers.get(i).isTransferCompleted()==false);				
				System.out.println("Transfer "+i+" completed");
    	}
		System.out.println("\n");
		
    }
	
	
    public boolean isActive() {
    	for(int i=0;i<no_of_workers;i++) {
    		if (workers.get(i).isActive()==true)
    			return true;
    	}
    	return false;
    }
    
    
    public void run(){
    	while(this.isActive()==true) {
    		this.superstep();	
    	}
    	
    	for(int i=0;i<workers.size();i++) {
    		workers.get(i).halt=true;
    		
    		try {
    			workers.get(i).join();
    		}
    		catch(Exception e ) {
    		}
    	}
    }
    
    
}
