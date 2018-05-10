/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pregel;

/**
 *
 * @author Sai, Ravi, Ishwari
 */
public class Message {
	int source_id;
	int destination_id;
	String value_type;
	double double_value;
	String str_value;
	
    public Message(int sid,int did,double value) {
    	this.source_id=sid;
    	this.destination_id=did;
    	this.value_type="Double";
    	this.double_value=value;
    	this.str_value="";
    	//this.Print();
    	
    }
    public Message(int sid,int did,String value) {
    	this.source_id=sid;
    	this.destination_id=did;
    	this.value_type="String";
    	this.double_value=-1;
    	this.str_value=value;
    }
}
