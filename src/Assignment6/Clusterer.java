package Assignment6;

import Assignment6.ClusterObjects.Cluster;
import Assignment6.ClusterObjects.ClusterRow;
import Assignment6.ClusteringMethods.ClusterMethod;

/**
 * Created by Arthur on 3-6-14.
 */
public class Clusterer{
    private ClusterMethod clusterMethod;
    private ClusterRow[] history;
    private int clusterSteps;
    private int atStep = -1; // i will begin by calling getNextStep so it will be set to 0 the first time around

    public Clusterer(ClusterMethod CM,ClusterRow original) {
        clusterSteps = original.getNumberOfClusters() - original.getClusterGoal() + 1;
        history = new ClusterRow[clusterSteps];
        history[0] = original;

        this.clusterMethod = CM;
    }

    public int getClusterSteps(){return clusterSteps;}

    public ClusterRow getNextStep(){
        atStep += (atStep < (getClusterSteps()-1))?1:0;
        return getStep(atStep);
    }

    public ClusterRow getPreviousStep(){
        atStep -= (atStep > 0)?1:0;
        return getStep(atStep);
    }

    public ClusterRow getCurrentStep(){
        atStep = (atStep < 0)?0:atStep;
        return getStep(atStep);
    }

    private ClusterRow getStep(int i){
        if (i >= history.length || i < 0){
            System.out.println("CLUSTERER ERROR: INDEX INCORRECT");
            System.out.printf("i:%d history size: %d", i, history.length);
            System.exit(-1);
        }

        if (history[i] == null){
            calculateStep(i);
        }

        return history[i];
    }

    private void calculateStep(int i){
        if(history[i-1] == null){calculateStep(i-1);}

        ClusterRow previousStep = getStep(i-1);
        ClusterRow currentStep = new ClusterRow(previousStep);

        double minDist = Double.MAX_VALUE;
        double tempDist;
        Cluster minRow = null;
        Cluster minCol = null;

        for (int column = 0; column < currentStep.getNumberOfClusters(); column++) {
            for (int row = 0; row < column; row++) {

                tempDist = clusterMethod.calculateDistance(currentStep.getCluster(column), currentStep.getCluster(row));
                if (minDist > tempDist) {

                    minDist = tempDist;
                    minRow = currentStep.getCluster(row);
                    minCol = currentStep.getCluster(column);

                }
            }
        }

        currentStep.merge(minRow, minCol);
        history[i] = currentStep;
    }

}
