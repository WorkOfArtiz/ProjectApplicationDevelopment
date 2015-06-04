package Assignment6.ClusterObjects;

import Assignment6.*;

public class ClusterRow {
    private Cluster[] clusters;
    private String xAxesHeading, yAxesHeading;

    private int numberOfClusters = 0;
    private int clusterGoal;

    // Initialise with a dataSet
    public ClusterRow(Dataset d) {
        clusters = new Cluster[d.getNumberOfEntries()];

        xAxesHeading = d.getHeading(0);
        yAxesHeading = d.getHeading(1);

        for (int i = 0; i < d.getNumberOfEntries(); i++) {
            if(Clustering.GRAPHICS_IMPROVEMENTS){
                clusters[i] = new Leaf(d.getEntry(i), ColourGradients.genColour(i, d.getNumberOfEntries()));
            }
            else{
                clusters[i] = new Leaf(d.getEntry(i), ColourGradients.genColour());
            }
        }

        numberOfClusters = d.getNumberOfEntries();
        clusterGoal = d.getNumberOfClusters();
    }

    // copy constructor (clone alternative)
    public ClusterRow(ClusterRow clusterRow){

        clusters = new Cluster[clusterRow.getNumberOfClusters()];

        xAxesHeading = clusterRow.getXHeading();
        yAxesHeading = clusterRow.getYHeading();
        clusterGoal = clusterRow.getClusterGoal();

        Cluster c;
        for(int i=0;i<clusterRow.getNumberOfClusters();i++){
            c = clusterRow.getCluster(i);
            if(c.hasChildren()){
                add(new Node((Node)c));
            }
            else
            {
                add( new Leaf((Leaf)c));
            }
        }
    }

    public String getXHeading() {
        return xAxesHeading;
    }

    public String getYHeading() {
        return yAxesHeading;
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    public int getNumberOfUnits(){
        int sum = 0;
        for(int i=0;i<numberOfClusters;i++){
            sum += clusters[i].getWidth();
        }
        return sum;
    }

    public int getClusterGoal() {
        return clusterGoal;
    }

    public Cluster getCluster(int index) {
        return clusters[index];
    }

    public void merge(Cluster c1, Cluster c2) {
        Cluster merged = new Node(c1, c2);
        remove(c1,c2);
        add(merged);
    }

    private void remove(Cluster c1, Cluster c2) {
        Cluster[] replacement = new Cluster[clusters.length-1];
        int counter = 0;

        for (int i = 0; i < numberOfClusters; i++) {
            if (counter == numberOfClusters) {
                System.out.println("NOTING CHANGED");
                // the cluster c wasn't in this array, dont change anything
                return;
            }

            if (c1 == clusters[i] || c2 == clusters[i]) {
                // I dont want the reference to c1 and c2 in there
            } else {
                replacement[counter++] = clusters[i];
            }
        }

        numberOfClusters -= 2;
        clusters = replacement;
    }

    public void add(Cluster a) {
        if (numberOfClusters >= clusters.length) {
            System.out.println("ERROR: TOO MANY CLUSTERS; LIMIT REACHED");
            System.out.println(numberOfClusters);
            System.exit(-1);
        }

        clusters[numberOfClusters++] = a;
    }


    public void dump(){
        Cluster c;
        for(int i=0;i<numberOfClusters;i++){
            c = clusters[i];
            String preText = "-";

            if(c.hasChildren()){
                dumpNode(preText, (Node) c);
            }
            else{
                dumpLeaf( preText,(Leaf) c);
            }
        }
    }

    public void dumpLeaf(String preText,Leaf l){
        System.out.println(preText+"Leaf");
    }

    public void dumpNode(String preText,Node n){
        System.out.println(preText+"Node");
        preText += "-";

        if(n.getChild1().hasChildren()){
            dumpNode(preText, (Node) n.getChild1());
        }
        else{
            dumpLeaf(preText,(Leaf) n.getChild1());
        }

        if(n.getChild2().hasChildren()){
            dumpNode(preText, (Node) n.getChild2());
        }
        else{
            dumpLeaf(preText,(Leaf) n.getChild2());
        }

    }

}
