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
public class Vertex {
	int vertex_id;
	double value;
	int number_of_vertices;
	boolean active;
	List<Vertex> adjacent_vertices = new ArrayList<Vertex>();
	List<Message> incoming_messages = new ArrayList<Message>();
	List<Message> outgoing_messages = new ArrayList<Message>();
	
	int superstep;
    public Vertex(int vertex_id) {
    	this.vertex_id=vertex_id;
    	this.active=true;
    	this.superstep=0; 
    }
    public Vertex(int vertex_id, double value, int count) {
    	this.vertex_id=vertex_id;
    	this.active=true;
    	this.superstep=0;
    	this.number_of_vertices=count;
    	this.value=value;
    	
    }
    public boolean isActive()
    {
    	return this.active;
    }
    public void update() {
    	
    }
    public void addAdjacent(Vertex v) {
    	this.adjacent_vertices.add(v);
    }

}