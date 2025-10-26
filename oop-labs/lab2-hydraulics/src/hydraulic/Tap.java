package hydraulic;

/**
 * Represents a tap that can interrupt the flow.
 * 
 * The status of the tap is defined by the method
 * {@link #setOpen(boolean) setOpen()}.
 */

public class Tap extends Element {
	boolean open = false;

	/**
	 * Constructor
	 * @param name name of the tap element
	 */
	public Tap(String name) {
		super(name);
	}
	public boolean getState(){
		return open;
	}

	@Override
	public void connect(Element elem) {
		// does nothing by default
		connected=elem;
		if (elem != null){
			elem.setInput(this);
		}
	}

	@Override
	public Element getOutput(){
		return connected;
	}

	/**
	 * Set whether the tap is open or not. The status is used during the simulation.
	 *
	 * @param open opening status of the tap
	 */
	public void setOpen(boolean open){
		// TODO: to be implemented
		this.open=open;
		if (open==false){
			setFlow(0);
		}
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
		if (open) {
			observer.notifyFlow("Tap", getName(), getFlow(), getFlow());
			if (enableMaxFlowCheck && getFlow() > getMaxFlow()) {
				observer.notifyFlowError("Tap", getName(),getFlow(),getMaxFlow()); 
			}
		} else {
			observer.notifyFlow("Tap", getName(), getFlow(), 0);
		}
		getOutput().simulate(observer,enableMaxFlowCheck);
	}

	@Override 
	public boolean delElement() {
		if (getInput() instanceof Split){
			Split split = (Split) getInput();
			for(int i = 0; i < split.getOutputs().length; i++) {
				if (split.getOutputs()[i] == this) {
					split.getOutputs()[i].connect(getOutput(), i); //this should be null
					getOutput().connect(getInput());
					return true;
				}
			}
		}
		else {
			if (getInput() != null) {
				getInput().connect(getOutput()); 
				getOutput().setInput(getInput());
				return true;
		}
	}
	return false;		
	}
	
	
}
