package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {

		//start with the walking
		TNode walkHead = new TNode();
		walkHead.setLocation(0);
		TNode curr = walkHead;

		for(int i =0; i<locations.length; i++){
			curr.setNext(new TNode(locations[i], null, null));
			curr = curr.getNext();
		}

		TNode busHead = new TNode();
		busHead.setLocation(0);
		busHead.setDown(walkHead);
		curr = busHead;
		TNode currWalk = walkHead;
		for(int i = 0; i < busStops.length; i++){
			//grab the corresponding walk node
			while(currWalk.getLocation() != busStops[i]){
				currWalk = currWalk.getNext();
			}
			curr.setNext(new TNode(busStops[i], null, currWalk));
			curr = curr.getNext();
		}

		TNode trainHead = new TNode();
		trainHead.setLocation(0);
		trainHead.setDown(busHead);
		curr = trainHead;
		TNode currBus = busHead;

		for(int i =0; i < trainStations.length; i++){
			//grab the corresponding bus node
			while(currBus.getLocation() != trainStations[i]){
				currBus = currBus.getNext();
			}
			curr.setNext(new TNode(trainStations[i], null, currBus));
			curr = curr.getNext();
		}
		trainZero = trainHead;

	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
		TNode curr = trainZero;
		TNode prev = null;

		//if the first node is the node to be deleted
		if(curr.getLocation() == station){
			trainZero = curr.getNext();
		}

		//otherwise, search for the node to be deleted
		while(curr!=null && curr.getLocation()!= station){
			prev = curr;
			curr = curr.getNext();
		}
		
		//if the node was not found, we do nothing
		if (curr == null)return;

		//now that we found the node, need to change the pointers and remove the node
		prev.setNext(curr.getNext());
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		//first make sure there is a corresponding walking location and find the previous value as well
		TNode walkNode = trainZero.getDown().getDown();
		while(walkNode != null && walkNode.getLocation() != busStop){
			walkNode = walkNode.getNext();
		}
		//do nothing if the walk node was not found
		if (walkNode == null) return;

		TNode busNode = trainZero.getDown();
		TNode busNodePrev = null;
		while(busNode!=null && busNode.getLocation() < busStop){
			busNodePrev = busNode;
			busNode = busNode.getNext();
		}
		//now that we have found the location

		//if we are not adding to the end of the list 
		if(busNode!=null){
			busNodePrev.setNext(new TNode(busStop, busNode.getNext(), walkNode));}
		
		else{
		busNodePrev.setNext((new TNode(busStop, null, walkNode)));
		}
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {

		ArrayList<TNode> path = new ArrayList<TNode>();
		//lets use backtracking to find the best path
		//we will also use the properties of our linked list structure that all the train stations are included in the bus stops and so on

			TNode curr = trainZero;
			TNode prev = null;
			while(curr.getLocation() <= destination){
				path.add(curr);
				prev = curr;
				curr = curr.getNext();
				
				if(curr == null || curr.getLocation() > destination) {
					curr = prev;
					break;
				}

			}


			if(curr.getLocation() == destination){
				return path;
			}

			//passed the destination, so go down to the busses
			curr = curr.getDown();
			prev = null;
			while(curr.getLocation() <= destination){
				path.add(curr);
				prev = curr;
				curr = curr.getNext();

				if(curr == null || curr.getLocation() > destination) {
					curr = prev;
					break;
				}
			}

			if(curr.getLocation() == destination){
				return path;
			}

			//passed the destination, so go down to the walking stops
			curr = curr.getDown();
			prev = null;
			while(curr.getLocation() <= destination){
				path.add(curr);
				prev = curr;
				curr = curr.getNext();

				if(curr == null || curr.getLocation() > destination) {
					curr = prev;
					break;
				}
			}

	    return path;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

		//lets check how many layers we have(in case of scooters)
		TNode curr = trainZero;
		int count = 0;
		while(curr!=null){
			count++;
			curr = curr.getDown();
		}


	    return null;
	}

	private TNode findScooterNode(int destination){
		TNode prev = null;
		TNode curr = trainZero.getDown().getDown();
		while(curr.getLocation() != destination){
			prev = curr;
			curr = curr.getNext();
			if (curr ==null){
				return prev;
			}		
		}
		return curr;
	}

	private TNode findWalkNode(int destination){
		TNode curr = trainZero.getDown().getDown().getDown();
		TNode prev = null;
		while(curr.getLocation() != destination){
			prev = curr;			
			curr = curr.getNext();
			if(curr == null) return prev;
		}
		return curr;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {

		//create a new node for the start of the scooters list
		TNode startScooters = new TNode();

		//it starts off with 0 just like the rest of methods of transport
		startScooters.setLocation(0);

		//need a pointer node
		TNode curr = startScooters;

		//set the down of the start of the scooters to the corresponding walk node
		startScooters.setDown(trainZero.getDown().getDown());

		//first set up the list for the scooters
		for(int i = 0; i<scooterStops.length; i++){
			TNode newNode = new TNode();
			newNode.setLocation(scooterStops[i]);
			
			curr.setNext(newNode);
			curr = curr.getNext();
		}


		//set the downs from the bus nodes
		curr = trainZero.getDown();
		curr.setDown(startScooters);
		curr = curr.getNext();
		while(curr != null){
			curr.setDown(findScooterNode(curr.getLocation()));
			curr = curr.getNext();
		}
		
		//set the downs from the scooter nodes
		curr = startScooters;
		while(curr !=null){
			curr.setDown(findWalkNode(curr.getLocation()));
			curr = curr.getNext();
		}


	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
