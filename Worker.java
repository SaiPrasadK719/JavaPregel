/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pregel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Sai, Ravi, Ishwari
 * 
 */

public class Worker extends Thread{
	int worker_id;
	List<Worker> worker_threads = new ArrayList<Worker>();
	
	
	int thread_completed=0;
	/* 0 -> Initial 
	 * 1 -> Started Processing
	 * 2 -> Processing Done -- Waiting for Barrier Sync
	 * 3 -> Transfer Messages
	 */
	
	List<Vertex> vertices = new ArrayList<Vertex>();
	
	BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	boolean halt=false;
	
	
	public Worker(int worker_id) {
		this.worker_id=worker_id;
		
	}
	
	public void addWorkers(List<Worker> input_workers) {
		for(int i=0;i<input_workers.size();i++) {
    		Worker instance = input_workers.get(i);
    		worker_threads.add(instance);
		}
	}
	
	public int getHashValue(int vertex_id) {
    	return vertex_id%(worker_threads.size());
    }
	
	public void addVertex(Vertex vertex) {
		this.vertices.add(vertex);
	}

	public boolean isActive() {
		for(int i=0;i<vertices.size();i++) {
			if (vertices.get(i).isActive())
				return true;
		}
		return false;
	}
	
	public void displayOutput() {
		for(int i=0;i<vertices.size();i++) {
			System.out.println("Vertex "+vertices.get(i).vertex_id +" Value: "+vertices.get(i).value);
		}
		
	}
	
	
	public void run() {
		queue.clear();
		while(halt==false) {
			//System.out.println("Running"+worker_id+" "+thread_completed);
			if (thread_completed==1) {
				
				Message msg;
				while ((msg = queue.poll()) != null) {
						for(int i=0;i<vertices.size();i++) {
							if (vertices.get(i).vertex_id==msg.destination_id) {
								vertices.get(i).incoming_messages.add(msg);
							}
						}
			    }
				
				
				//System.out.println("Queue size after "+worker_id+ " - "+queue.size());
				
				for(int i=0;i<vertices.size();i++) {
					vertices.get(i).superstep+=1;
					vertices.get(i).update();
				}
				
				
				thread_completed=2;
			}
			
			
			else if (thread_completed==3) {
				Message msg;
				for(int i=0;i<vertices.size();i++) {
					for(int j=0;j<vertices.get(i).outgoing_messages.size();j++) {
						msg=vertices.get(i).outgoing_messages.get(j);
						
						int msg_worker_id=getHashValue(msg.destination_id);
						try {
							worker_threads.get(msg_worker_id).queue.put(msg);
						}
						catch (Exception e) {
							
						}
						
					}
					vertices.get(i).outgoing_messages.clear();
				}
				thread_completed=0;
			}
		}
	}
	public void startProcess() {
		thread_completed=1;
    }
	
	public void transferMessages() {
		thread_completed=3;
    }
    
	public boolean isDone() {
		return thread_completed==2;
	}
	public boolean isTransferCompleted() {
		return thread_completed==0;
	}
	
	
}
