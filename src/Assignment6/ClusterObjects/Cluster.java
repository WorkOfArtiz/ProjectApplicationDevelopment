package Assignment6.ClusterObjects;


import Assignment6.UnitRow;
import ui.Colour;

public interface Cluster {
    // Returns or Sets the colour that the cluster is going to be coloured in
    Colour getColour();

    void setColour(Colour c);

    /**
     * Returns the depth of the cluster.
     *
     * @return the depth of the cluster.
     */
    int getDepth();

    /**
     * Returns the width of the cluster.
     *
     * @return the width of the cluster.
     */
    int getWidth();

    /**
     * Returns all units contained in this cluster.
     *
     * @return all units contained in this cluster.
     */
    UnitRow getUnits();

    /**
     * Returns whether this cluster has children.
     * I.e. is this a node or a leaf?
     *
     * @return true: if this cluster has children. <br>
     * false: if this cluster has no children.
     */
    boolean hasChildren();
}
