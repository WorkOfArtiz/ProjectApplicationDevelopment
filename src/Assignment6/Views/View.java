package Assignment6.Views;

import Assignment6.ClusterObjects.ClusterRow;

public interface View {
    /**
     * Draws a visual representation of the numberOfClusters.
     *
     * @param clusters The numberOfClusters of which a visualisation is to be drawn.
     */
    public void draw(ClusterRow clusters);
}
