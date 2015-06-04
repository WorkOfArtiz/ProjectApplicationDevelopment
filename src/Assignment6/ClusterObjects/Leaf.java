package Assignment6.ClusterObjects;

import Assignment6.ColourGradients;
import Assignment6.Unit;
import Assignment6.UnitRow;
import ui.Colour;

public class Leaf implements Cluster {
    public Colour c;
    private UnitRow units;

    public Leaf(Unit u, Colour c) {
        units = new UnitRow();
        units.addUnit(u);

        this.c = c;
    }

    public Leaf(Leaf l){
        Colour colourL = l.getColour();
        c = ColourGradients.duplicateColor(colourL);

        units = new UnitRow();
        units.addUnit( l.getUnits().getUnit(0) );
    }

    @Override
    public Colour getColour() {
        return c;
    }

    @Override
    public void setColour(Colour c) {
        this.c = c;
    }

    @Override
    public int getDepth() {
        return 1;
    }

    public int getWidth() {return 1;}

    @Override
    public UnitRow getUnits() {
        return units;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }
}
