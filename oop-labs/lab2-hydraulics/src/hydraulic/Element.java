package hydraulic;


/**
 * Represents the generic abstract element of an hydraulics system.
 * It is the base class for all elements.
 *
 * Any element can be connect to a downstream element
 * using the method {@link #connect(Element) connect()}.
 * 
 * The class is abstract since it is not intended to be instantiated,
 * though all methods are defined to make subclass implementation easier.
 */
public abstract class Element {
	protected String name;
	protected double flow;
	protected Element input;	
	protected double maxFlow;
	
	/**
	 * getter method for the name of the element
	 * 
	 * @return the name of the element
	 */
	public Element(String name){
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Connects this element to a given element.
	 * The given element will be connected downstream of this element
	 * 
	 * In case of element with multiple outputs this method operates on the first one,
	 * it is equivalent to calling {@code connect(elem,0)}. 
	 * 
	 * @param elem the element that will be placed downstream
	 */
	protected Element connected;
	public void connect(Element elem) {
		// does nothing by default
	}
	
	public void setInput(Element elem){
		input = elem;
	}

	public Element getInput(){
		return input;
	}
	/**
	 * Connects a specific output of this element to a given element.
	 * The given element will be connected downstream of this element
	 * 
	 * @param elem the element that will be placed downstream
	 * @param index the output index that will be used for the connection
	 */

	protected Element [] splitConnections = new Element [2]; 
	public void connect(Element elem, int index){
		// does nothing by default

	}
	
	/**
	 * Retrieves the single element connected downstream of this element
	 * 
	 * @return downstream element
	 */
	public Element getOutput(){
		return null;
	}

	/**
	 * Retrieves the elements connected downstream of this element
	 * 
	 * @return downstream element
	 */

	
	public Element[] getOutputs(){
		return null;
	}

	
	public void setFlow(double flow) {
		this.flow = flow;
	}
	
	public double getFlow() {
		return flow;
	}

	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck){
		// does nothing by default
	}

	public boolean delElement() {
		// does nothing by default
		return false;
	}


	
	/**
	 * Defines the maximum input flow acceptable for this element
	 * 
	 * @param maxFlow maximum allowed input flow
	 */
	public void setMaxFlow(double maxFlow) {
		// does nothing by default
	}

	public double getMaxFlow(){
		return maxFlow;
	}
	

	protected static String pad(String current, String down){
		int n = current.length();
		final String fmt = "\n%"+n+"s";
		return current + down.replace("\n", fmt.formatted("") );
	}

	@Override
	public String toString(){
		String res = "[%s] ".formatted(getName());
		Element[] out = getOutputs();
		if( out != null){
			StringBuilder buffer = new StringBuilder();
			for(int i=0; i<out.length; ++i) {
				if(i>0) buffer.append("\n");
				if (out[i] == null) buffer.append("+-> *");
				else buffer.append(pad("+-> ", out[i].toString()));
			}
			res = pad(res,buffer.toString());
		}
		return res;
	}


}
