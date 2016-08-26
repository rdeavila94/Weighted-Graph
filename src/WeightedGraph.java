
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;



public class WeightedGraph {
	
    public int getSize() {
  	  return mVertices.size();
    }
    
   class WeightedNode {
      int mIndex;
      private List<WeightedEdge> mNeighbors = new ArrayList<WeightedEdge>();
      
      WeightedNode(int index) {
         mIndex = index;
      }
      
      public String toString() {
    	  for (WeightedEdge a: mNeighbors) {
    		  if (a.mFirst == this) {
    			  System.out.print("(" + a.mSecond.mIndex + ", " + a.mWeight + ")");
    		  }
    		  else {
    			  System.out.print("(" + a.mFirst.mIndex + ", " + a.mWeight + ")");
    		  }
    	  }
    	  return "";
      }
   }

   private class WeightedEdge implements Comparable {

      private WeightedNode mFirst, mSecond;
      private double mWeight;

      WeightedEdge(WeightedNode first, WeightedNode second, double weight) {
         mFirst = first;
         mSecond = second;
         mWeight = weight;
      }

      public int compareTo(Object o) {
         WeightedEdge e = (WeightedEdge) o;
         return Double.compare(mWeight, e.mWeight);
      }
      
      public WeightedNode getFirst() {
    	  return mFirst;
      }
   }

   private List<WeightedNode> mVertices = new ArrayList<WeightedNode>();

   public WeightedGraph(int numberOfVertices) {
      for (int i = 0; i < numberOfVertices; i++) {
         mVertices.add(new WeightedNode(i));
      }
   }

   public void addEdge(int firstVertex, int secondVertex, double weight) {
      WeightedNode first = mVertices.get(firstVertex);
      WeightedNode second = mVertices.get(secondVertex);
      
      WeightedEdge edge = new WeightedEdge(first, second, weight);
      first.mNeighbors.add(edge);
      second.mNeighbors.add(edge);
   }

   /**
    * Prints the graph to the console. Each vertex is printed on its own line,
    * starting with the vertex's number (its index in the mVertices list), then
    * a colon, then a sequence of pairs for each edge incident to the vertex.
    * For each edge, print the number of the vertex at the opposite end of the
    * edge, as well as the edge's weight.
    *
    * Example: in a graph with three vertices (0, 1, and 2), with edges from 0
    * to 1 of weight 10, and from 0 to 2 of weight 20, printGraph() should print
    *
    * Vertex 0: (1, 10), (2, 20) Vertex 1: (0, 10) Vertex 2: (0, 20)
    */
   public void printGraph() {
	   for (WeightedNode a: mVertices) {
		   System.out.print("Vertex " + a.mIndex + ": ");
		   System.out.println(a);
	   }
   }

   /**
    * Applies Prim's algorithm to build and return a minimum spanning tree for
    * the graph. Start by constructing a new WeightedGraph with the same number
    * of vertices as this graph. Then apply Prim's algorithm. Each time an edge
    * is selected by Prim's, add an equivalent edge to the other graph. When
    * complete, return the new graph, which is the minimum spanning tree.
    *
    * @return an UnweightedGraph representing this graph's minimum spanning
    * tree.
    */
   	public WeightedGraph getMinimumSpanningTree() {
	   WeightedGraph g = new WeightedGraph(mVertices.size());
	   HashSet <WeightedNode> s = new HashSet <WeightedNode>();
	   PriorityQueue <WeightedEdge> p = new PriorityQueue<WeightedEdge>();
	  // for (WeightedNode a: mVertices)
		   p.addAll(mVertices.get(0).mNeighbors);
	   WeightedNode tempNode = mVertices.get(0);
	   s.add(tempNode);
	   WeightedEdge tempEdge;
	   while (s.size() < mVertices.size()) {
		   tempEdge = p.poll();
		   if (!(s.contains(tempEdge.mSecond)) && s.contains(tempEdge.mFirst)) {
			   s.add(tempEdge.mSecond);
			   g.addEdge(tempEdge.mFirst.mIndex, tempEdge.mSecond.mIndex, tempEdge.mWeight);
			   p.addAll(tempEdge.mSecond.mNeighbors);
		   }
		   else if (!(s.contains(tempEdge.mFirst)) && s.contains(tempEdge.mSecond)) {
			   s.add(tempEdge.mFirst);
			   g.addEdge(tempEdge.mSecond.mIndex,  tempEdge.mFirst.mIndex,  tempEdge.mWeight);
			   p.addAll(tempEdge.mFirst.mNeighbors);
		   }
	   }
	   
      return g;
   }

   /**
    * Applies Dijkstra's algorithm to compute the shortest paths from a source
    * vertex to all other vertices in the graph. Returns an array of path
    * lengths; each value in the array gives the length of the shortest path
    * from the source vertex to the corresponding vertex in the array.
    */
   public double[] getShortestPathsFrom(int source) {
      // TODO: apply Dijkstra's algorithm and return the distances array.
      
      // This queue is used to select the vertex with the smallest "d" value
      // so far.
      // Each time a "d" value is changed by the algorithm, the corresponding
      // DijkstraDistance object needs to be removed and then re-added to
      // the queue so its position updates.
      PriorityQueue<DijkstraDistance> vertexQueue = 
       new PriorityQueue<DijkstraDistance>();
      
      // Initialization: set the distance of the source node to 0, and all
      // others to infinity. Add all distances to the vertex queue.
      DijkstraDistance[] distances = new DijkstraDistance[mVertices.size()];
      distances[source] = new DijkstraDistance(source, 0);
      for (int i = 0; i < distances.length; i++) {
         if (i != source)
            distances[i] = new DijkstraDistance(i, Integer.MAX_VALUE);
         
         vertexQueue.add(distances[i]);
      }
      double [] answer = new double [mVertices.size()];
      while (vertexQueue.size() > 0) {
         DijkstraDistance temp = vertexQueue.poll();
         for (WeightedEdge a: mVertices.get(temp.mVertex).mNeighbors) {
        	 WeightedNode from, to;
        	 if (a.mFirst == mVertices.get(temp.mVertex)) {
        		 from = a.mFirst;
        		 to = a.mSecond;
        	 }
        	 else {
        		 from = a.mSecond;
        		 to = a.mFirst;
        	 }
        	 Double weight = distances[from.mIndex].mCurrentDistance;
        	 Double weight2 = weight + a.mWeight;
        	 if (weight2 <distances[to.mIndex].mCurrentDistance) {
        		 distances[to.mIndex].mCurrentDistance = weight2;
        	 }
         }
         
         
      }
      for (int i = 0; i < answer.length; i++) {
    	  answer[i] = distances[i].mCurrentDistance; 
      }
      
      return answer;
   }

   class DijkstraDistance implements Comparable <DijkstraDistance> {

      int mVertex;
      double mCurrentDistance;

      DijkstraDistance(int vertex, double distance) {
         mVertex = vertex;
         mCurrentDistance = distance;
      }

      public int compareTo(DijkstraDistance other) {
        if (mCurrentDistance > other.mCurrentDistance)
        	return 1;
        else if (mCurrentDistance < other.mCurrentDistance)
        	return -1;
        else
        	return 0;
      }
      
   }
   
   public static void main(String[] args) {
	   Scanner sc = new Scanner(System.in);
	   Scanner fileReader = null;
	   File file;
	   String convenientString;
	   boolean finished = false;
	   while (!finished) {
		   try {
			  System.out.println("Enter the name of a file");
			  convenientString = sc.next();
			  file = new File(convenientString);
			  fileReader = new Scanner (file);
			  finished = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("File not found");
			}
	   }
	   WeightedGraph g = new WeightedGraph(fileReader.nextInt());
	   int vertex = -1;
	   Scanner anotherReader = new Scanner (fileReader.nextLine());
	   while (fileReader.hasNext()) {
		    while (anotherReader.hasNext()) {
			   g.addEdge(vertex, anotherReader.nextInt(), anotherReader.nextDouble());
		   }
		   vertex++;
		   anotherReader = new Scanner (fileReader.nextLine());
	   }
	   g.addEdge(vertex, anotherReader.nextInt(), anotherReader.nextDouble());
	   boolean cont = true;
	   while (cont) {
		   System.out.println("1) Print Graph 2) Print minimum spanning tree 3) Print shortest paths");
		   switch (sc.nextInt()) {
		   case 1: g.printGraph();
		   break;
		   
		   case 2: g.getMinimumSpanningTree().printGraph();
		   break;
		   
		   case 3: System.out.println("Select a vertex from 0 to " + (g.getSize() - 1));
		   int input = sc.nextInt();
		   if (input >= g.getSize()) {
			   System.out.println("Not a valid vertex");
			   break;
		   }
		   double[] distances = g.getShortestPathsFrom(input);
		   for (int i = 0; i < distances.length; i++) {
			   System.out.println("Distance from " + input + " to " + i + ": " + distances[i]);
		       }
		   break;
		   default: cont = false;
		   break;
		      
		   }
	   }
	   anotherReader.close();
	   sc.close();
   }
}
