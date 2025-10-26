package hydraulic;

/**
 * Represents the sink, i.e. the terminal element of a system
 *
 */
public class Sink extends Element {

	/**
	 * Constructor
	 * @param name name of the sink element
	 */
	public Sink(String name) {
		super(name);
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
			observer.notifyFlowError("Sink", getName(),getFlow(),getMaxFlow()); 
		}
		observer.notifyFlow("Sink", getName(), getFlow(), observer.NO_FLOW);
			}
	
	

	@Override 
	public boolean delElement() {
		if (getInput() instanceof Split){
			Split split = (Split) getInput();
			for(int i = 0; i < split.getOutputs().length; i++) {
				if (split.getOutputs()[i] == this) {
					split.connect(getOutput(), i); //this should be null
					return true;
				}
			}
		}
		else {
			if (getInput() != null) {
				getInput().connect(null); 
				return true;
		}
	}
	return false;		
	}

}
	

