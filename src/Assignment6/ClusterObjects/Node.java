package Assignment6.ClusterObjects;

import Assignment6.ColourGradients;
import Assignment6.UnitRow;
import ui.Colour;

public class Node implements Cluster {
    private int depth;

    private Colour c;

    private UnitRow units;

    private Cluster child1;
    private Cluster child2;

    public Node(Cluster a, Cluster b) {
        child1 = a;
        child2 = b;

        // activate this for average color merging
        setColour(ColourGradients.average(a.getColour(), b.getColour() ));
        // activate this for random color merging
        //setColour(ColourGradients.genColour());

        units = new UnitRow();

        UnitRow temp = a.getUnits();

        for (int i = 0; i < a.getWidth(); i++) {
            units.addUnit(temp.getUnit(i));
        }

        temp = b.getUnits();
        for (int i = 0; i < b.getWidth(); i++) {
            units.addUnit(temp.getUnit(i));
        }

        depth = Math.max(a.getDepth(), b.getDepth()) + 1;
    }

    // this is a copy constructor, whenever clusterer wants to generate a new clusterRow the old objects can't be changed
    // this means that objects need to be cloned, I do this by giving them as argument for the same class
    public Node(Node n){
        c = ColourGradients.duplicateColor(n.getColour());

        Cluster a,b;
        // the first cluster is a node
        if(n.getChild1().hasChildren()){
            a = new Node((Node)n.getChild1());
        }
        else{ // the first cluster is a Leaf
            a = new Leaf((Leaf)n.getChild1());
        }

        // the second cluster is a node
        if(n.getChild2().hasChildren()){
            b = new Node((Node)n.getChild2());
        }
        else{ // the second cluster is a Leaf
            b = new Leaf((Leaf)n.getChild2());
        }

        // normal constructor
        units = new UnitRow();

        UnitRow temp = a.getUnits();

        for (int i = 0; i < a.getWidth(); i++) {
            units.addUnit(temp.getUnit(i));
        }

        temp = b.getUnits();
        for (int i = 0; i < b.getWidth(); i++) {
            units.addUnit(temp.getUnit(i));
        }

        depth = Math.max(a.getDepth(), b.getDepth()) + 1;

        child1 = a;
        child2 = b;
    }

    @Override
    public Colour getColour() {
        return c;
    }

    @Override
    public void setColour(Colour c) {
        this.child1.setColour(c);
        this.child2.setColour(c);
        this.c = c;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getWidth() {
        return units.getSize();
    }

    @Override
    public UnitRow getUnits() {
        return units;
    }


    @Override
    public boolean hasChildren() {
        return true;
    }

    public Cluster getChild1() {
        return child1;
    }

    public Cluster getChild2() {
        return child2;
    }
}
