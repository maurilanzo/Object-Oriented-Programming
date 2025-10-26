package hydraulic;

/**
 * Represents a split element, a.k.a. T element
 * 
 * During the simulation each downstream element will
 * receive a stream that is half the input stream of the split.
 */

public class Split extends Element {

	/**
	 * Constructor
	 * @param name name of the split element
	 */
	public Split(String name) {
		super(name);
	}

	@Override
	public void connect(Element elem, int index){
		splitConnections[index]=elem;
		if (elem != null){
			elem.setFlow(getFlow()/2);
			elem.setInput(this);
		}
	}

	@Override
	public Element [] getOutputs(){
		return splitConnections;
	}
	@Override
	public void setMaxFlow(double maxFlow) {
		// does nothing by default
		this.maxFlow=maxFlow;
	}
	public double getMaxFlow(){
		return maxFlow;
	}

	@Override
	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {
		// does nothing by default
		if (enableMaxFlowCheck && getFlow() > getMaxFlow()) {
			observer.notifyFlowError("Split", getName(),getFlow(),getMaxFlow()); 
		}
		observer.notifyFlow("Split", getName(), getFlow(), getFlow()/2, getFlow()/2);
		for (Element elem : splitConnections) {
			elem.simulate(observer,enableMaxFlowCheck);
		}	
}
	@Override 
	public boolean delElement() {
		int cont=0;
		for (int i = 0; i < getOutputs().length; i++) {
			if (getOutputs()[i] != null) {
				cont++;
			}
		}
		if(cont ==1){
			for (int i = 0; i < getOutputs().length; i++) {
				if (getOutputs()[i] != null) {
					getInput().connect(getOutputs()[i]);
					getOutputs()[i].setInput(getInput());
					return true;
				}
			}	
				
		}
		else if (cont == 0) {
			if (getInput() != null) {
				getInput().connect(null); 
				return true;
			}
		}
		
		
		return false;		
	}
}

