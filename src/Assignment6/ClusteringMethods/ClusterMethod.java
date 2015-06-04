package Assignment6.ClusteringMethods;

import Assignment6.ClusterObjects.Cluster;


public interface ClusterMethod {

    /**
     * Calculates the distance between two numberOfClusters based on a
     * cluster method and a distance measure.
     *
     * @param cluster1 The first cluster from which the distance is calculated.
     * @param cluster2 The second cluster to which the distance is calculated.
     * @return The distance between cluster1 and cluster2.
     */
    double calculateDistance(Cluster cluster1, Cluster cluster2);

}
