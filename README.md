# JavaPregel

*Project Members:*
- Sai Prasad Kousika    
- Ravi Shankar Karanam   
- Ishwari Abhijit Patil  


We have implemented Pregel framework and also implemented the solutions of PageRank, Bipartite and Dijkistra single-source distance problems on this pregel framework.

We have a class 'Vertex' to hold all the properties of vertex in our pregel graphs. It has 'Vertex_id' - unique id for each vertex, 'number_of_vertices' to track the count of vertices, boolean 'active' to represent the state of vertex, 'adjacent_vertices' to store the vertices to which this vertex is connected to, 'incoming_messages' to store the incoming messages from other vertices in previous superstep, 'outgoing_messages' to store the outgoing messages to sent to other vertices in next superstep, 'superstep' to keep a track of the present superstep iterator count.

We have a class 'Message' to hold all the properties of messages. It has 'double_value' to hold the value of the message variable. The source_id and destination_id has the vertex ids of the source and destination vertices.


Worker class is implemented as extending Threads class.


Code flow :

1. Page rank code starts by creating worker, vertex instances and the edges along those vertices. We are initializing the vertex values with '1/no.of vertices'.
2. A Pregel object is created, the vertices are mapped to the workers in the constructor function of pregel. Then run() is called on that instance.
3. The Run() function in pregel checks if the vertices are in active state and call superstep(), which then calls startprocess() on all workers, this starts threads on all workers.
4. Threads will start executing Run() in the worker class. Here execution happens in two phases. In the first phase, we will poll the queue of each worker and loop through the vertices in that worker, if the vertexid matches with the destinationid in the message, then that message will be copied onto the incoming_messages list of that vertex. Then, update function of pagerank is called to update the values at each vertex based on the incoming messages.
5. The second phase starts only after all the processing is done (Barrier synchronization is taken care). Here the transfer of messages will be done. We will place the updated values onto the queues of the corresponding workers along with the destination id of the vertex. The next superstep starts only after all the messages are transferred.
6. The above code from 3rd step is executed in loop till the state of vertices become inactive. This inactivation is done at the update function of the Pagerank algorithm once the number of supersteps exceeds a particular threshold.


The Dijkistra single-source distance problem has the same flow as above, except that the update function to change the node values at each superstep is changed accordingly.



Any queries in code:
mailto:saiprasad@cse.iitb.ac.in
