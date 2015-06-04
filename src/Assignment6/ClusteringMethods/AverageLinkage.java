package Assignment6.ClusteringMethods;

import Assignment6.ClusterObjects.Cluster;
import Assignment6.Distance.DistanceMeasure;
import Assignment6.UnitRow;

/**
 * Created by Arthur on 3-6-14.
 */
public class AverageLinkage implements ClusterMethod {
    private DistanceMeasure distanceMeasure;

    public AverageLinkage(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public double calculateDistance(Cluster cluster1, Cluster cluster2) {
        double sum = 0;
        UnitRow unitsCluster1 = cluster1.getUnits();
        UnitRow unitsCluster2 = cluster2.getUnits();

        for (int c1 = 0; c1 < cluster1.getWidth(); c1++) {
            for (int c2 = 0; c2 < cluster2.getWidth(); c2++) {
                sum += distanceMeasure.calculateDistance(unitsCluster1.getUnit(c1), unitsCluster2.getUnit(c2));
            }
        }

        return sum / (cluster1.getWidth() * cluster2.getWidth());
    }
}
