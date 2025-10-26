package hydraulic;

/**
 * Main class that acts as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystem {

// R1
	/**
	 * Adds a new element to the system
	 * 
	 * @param elem the new element to be added to the system
	 */
	int num =0;
	Element[] elements = new Element[100];
	public void addElement(Element elem){
		//TODO: to be implemented
		elements[num]=elem;
		num++;
	}

	/**
	 * returns the number of element currently present in the system
	 * 
	 * @return count of elements
	 */
	public int size() {
        // TODO: to be implemented
		int cont =0;
		for (Element elem:elements){
			if (elem != null) cont++;
		}
		return cont;
    }

	/**
	 * returns the element added so far to the system
	 * 
	 * @return an array of elements whose length is equal to 
	 * 							the number of added elements
	 */

	public Element[] getElements(){
		//TODO: to be implemented
		int length=size();
		Element[] elementsReturn = new Element [length];
		int cont =0;
		for (int i=0;i<100 && cont <length;i++){
			if (elements[i]!=null){
			elementsReturn[cont]=elements[i];
			cont++;
			}
		}
		return elementsReturn;
	}

// R4
	/**
	 * starts the simulation of the system
	 * 
	 * The notification about the simulations are sent
	 * to an observer object
	 * 
	 * Before starting simulation the parameters of the
	 * elements of the system must be defined
	 * 
	 * @param observer the observer receiving notifications
	 */
	public void simulate(SimulationObserver observer){
		//TODO: to be implemented
		simulate(observer, false);

	}


// R6
	/**
	 * Deletes a previously added element 
	 * with the given name from the system
	 */
	public boolean deleteElement(String name) {
		elements = getElements();
		int i=0;
		//TODO: to be implemented
		for(i=0;i<size();i++){
			if (elements[i] != null && elements[i].getName()==name){
				if (elements[i].delElement()){
					elements[i]=null;
					return true;
					
				}

			}
		}

		

		return false;
	}

// R7
	/**
	 * starts the simulation of the system; if {@code enableMaxFlowCheck} is {@code true},
	 * checks also the elements maximum flows against the input flow
	 * 
	 * If {@code enableMaxFlowCheck} is {@code false}  a normals simulation as
	 * the method {@link #simulate(SimulationObserver)} is performed
	 * 
	 * Before performing a checked simulation the max flows of the elements in thes
	 * system must be defined.
	 */
	public void propagateFlow(Element element){
		if (element instanceof Sink) return;
		else if (element instanceof Multisplit){
			Multisplit multi =(Multisplit)element;
			for (int i=0; i<multi.getPropotions().length;i++){
				multi.getOutputs()[i].setFlow(multi.getPropotions()[i]*multi.getFlow());
				propagateFlow(multi.getOutputs()[i]);
			} 
		}
		else if (element instanceof Split){
			element.getOutputs()[0].setFlow(element.getFlow()/2);
			element.getOutputs()[1].setFlow(element.getFlow()/2);
			propagateFlow(element.getOutputs()[0]);
			propagateFlow(element.getOutputs()[1]);
		}
		else if (element instanceof Tap){
			Tap tap = (Tap)element;
			if (tap.getState()){
				element.getOutput().setFlow(element.getFlow());
				propagateFlow(element.getOutput());
			}
			else {
				element.getOutput().setFlow(0);
				propagateFlow(element.getOutput());
			}
		}
		else {
			element.getOutput().setFlow(element.getFlow());
			propagateFlow(element.getOutput());
		}
		
	}

	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {
		for (Element elem: elements){
			if (elem instanceof Source){
				propagateFlow(elem);
				elem.simulate(observer,enableMaxFlowCheck);
				break;
			}
		}

		//TODO: to be implemented
		
	}

// R8
	/**
	 * creates a new builder that can be used to create a 
	 * hydraulic system through a fluent API 
	 * 
	 * @return the builder object
	 */
    public static HBuilder build() {
		return new HBuilder();
    }
}
