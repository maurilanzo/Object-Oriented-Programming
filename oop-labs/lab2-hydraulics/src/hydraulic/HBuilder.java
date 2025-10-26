package hydraulic;

public class HBuilder {
    public static final int MAX_DEPTH = 10;
    private HSystem system;
    private Element latest;
    private int outputIndex = 0;
    private Element[] parent = new Element[MAX_DEPTH];
    private int[] count = new int[MAX_DEPTH];
    private int topParent = -1;
    private boolean splitOutput = false;

    private void pushParent(Element e){
        parent[++topParent] = e;
        count[topParent] = outputIndex;
        outputIndex = 0;
    }

    private Element popParent(){
        outputIndex = count[topParent];
        return parent[topParent--];
    }

    private Element currentParent(){
        return parent[topParent];
    }

    public HBuilder(){
        this.system = new HSystem();
    }

    public HBuilder addSource(String name) {
        Source src = new Source(name);
        system.addElement(src);
        latest = src;
        return this;
    }

    public HBuilder withFlow(double flow) {
        if(latest instanceof Source src){
            src.setFlow(flow);
        }
        return this;
    }

    public HSystem complete() {
        return system;
    }

    private void link(Element e){
        system.addElement(e);
        if(splitOutput){
            currentParent().connect(e, outputIndex);
        }else{
            latest.connect(e);
        }
        latest = e;
        splitOutput = false;
    }
    public HBuilder linkToTap(String name) {
        Tap t = new Tap(name);
        link(t);
        return this;
    }

    public HBuilder linkToSink(String name) {
        Sink s = new Sink(name);
        link(s);
        return this;
    }

    public HBuilder linkToSplit(String name) {
        Split t = new Split(name);
        link(t);
        return this;
    }

    public HBuilder then() {
        splitOutput = true;
        outputIndex ++;
        return this;
    }

    public HBuilder withOutputs() {
        pushParent(latest);
        splitOutput = true;
        return this;     
    }

    public HBuilder open() {
        if(latest instanceof Tap tap){
            tap.setOpen(true);
        }
        return this;
    }

    public HBuilder closed() {
        if(latest instanceof Tap tap){
            tap.setOpen(false);
        }
        return this;
    }

    public HBuilder linkToMultisplit(String name, int numOutput) {
        Multisplit t = new Multisplit(name,numOutput);
        outputIndex = 0;
        link(t);
        return this;
    }

    public HBuilder withPropotions(double[] props) {
        if(latest instanceof Multisplit ms){
            ms.setProportions(props);
        }
        return this;
    }

    public HBuilder done() {
        popParent();
        return this;
    }

    public HBuilder maxFlow(double max) {
        latest.setMaxFlow(max);
        return this;
    }

}
