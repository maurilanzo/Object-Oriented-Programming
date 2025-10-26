package hydraulic;

/**
 * Represents a source of water, i.e. the initial element for the simulation.
 *
 * Lo status of the source is defined through the method
 * {@link #setFlow(double) setFlow()}.
 */
public class Source extends Element {
	/**
	 * constructor
	 * @param name name of the source element
	 */
	public Source(String name) {
		super(name);
	}

	@Override
	public void connect(Element elem) {
		// does nothing by default
		elem.setFlow(getFlow());
		connected=elem;
		if (elem != null){
			elem.setInput(this);
		}
	}
	@Override
	public Element getOutput(){
		return connected;
	}

	@Override
	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {
		// does nothing by default
		observer.notifyFlow("Source", getName(), observer.NO_FLOW, getFlow());
		getOutput().simulate(observer,enableMaxFlowCheck);
			}

	@Override 
	public boolean delElement() {
		if (getOutput() != null) {
			getOutput().setInput(null); //this should be null
			connect(null);
			return true; 
		}
		return false;		
	}
		

	


	/**
	 * Define the flow of the source to be used during the simulation
	 *
	 * @param flow flow of the source (in cubic meters per hour)
	 */
}

