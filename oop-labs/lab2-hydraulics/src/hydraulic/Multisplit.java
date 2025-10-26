package hydraulic;

/**
 * Represents a multisplit element, an extension of the Split that allows many outputs
 * 
 * During the simulation each downstream element will
 * receive a stream that is determined by the proportions.
 */

public class Multisplit extends Split {

	/**
	 * Constructor
	 * @param name the name of the multi-split element
	 * @param numOutput the number of outputs
	 */
	private int numOutput;
	private double [] proportions;

	
	protected Element [] multisplitConnections; 
	public Multisplit(String name, int numOutput) {
		super(name);
		multisplitConnections = new Element [numOutput]; 
		this.numOutput=numOutput;
	}

	@Override
	public void connect(Element elem, int index){
		multisplitConnections[index]=elem;
		if (elem != null){
			elem.setInput(this);
		}
	}

	@Override
	public Element [] getOutputs(){
		return multisplitConnections;
	}
	public double [] getPropotions(){
		return proportions;
	}

	
	/**
	 * Define the proportion of the output flows w.r.t. the input flow.
	 * 
	 * The sum of the proportions should be 1.0 and 
	 * the number of proportions should be equals to the number of outputs.
	 * Otherwise a check would detect an error.
	 * 
	 * @param proportions the proportions of flow for each output
	 */
	public void setProportions(double... proportions) {
		// TODO: to be implemented
		this.proportions=proportions;
		if (proportions.length==numOutput){
			for(int i=0;i<numOutput;i++){

				if(multisplitConnections[i]!=null) multisplitConnections[i].setFlow(proportions[i]*getFlow());
			}
		}
		else {
			System.err.println("error");
		}

	}

	@Override
	public void setMaxFlow(double maxFlow) {
		// does nothing by default
		this.maxFlow=maxFlow;
	}

	@Override
	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {

		// does nothing by default
		if (enableMaxFlowCheck && getFlow() > getMaxFlow()) {
			observer.notifyFlowError("Multisplit", getName(),getFlow(),getMaxFlow()); 
		}
		double [] outputFlows = new double[numOutput];
		for (int i=0;i<multisplitConnections.length;i++){
			outputFlows[i]=proportions[i]*getFlow();
		}
		observer.notifyFlow("Multisplit", getName(), getFlow(), outputFlows);
		for (int i=0;i<numOutput;i++){
			multisplitConnections[i].simulate(observer,enableMaxFlowCheck);
		}
}

	
}
