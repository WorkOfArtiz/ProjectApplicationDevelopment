package Assignment6;

import Assignment6.ClusterObjects.Cluster;
import Assignment6.ClusterObjects.ClusterRow;
import Assignment6.ClusteringMethods.AverageLinkage;
import Assignment6.ClusteringMethods.ClusterMethod;
import Assignment6.ClusteringMethods.CompleteLinkage;
import Assignment6.ClusteringMethods.SingleLinkage;
import Assignment6.Distance.DistanceMeasure;
import Assignment6.Distance.Euclidean;
import Assignment6.Distance.Manhattan;
import Assignment6.Distance.Pearson;
import Assignment6.Views.Cartesian;
import Assignment6.Views.CircularDendogram;
import Assignment6.Views.Dendogram;
import Assignment6.Views.View;

// Looks and feels
//import com.alee.laf.WebLookAndFeel;
//import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import ui.DrawUserInterface;
import ui.Event;
import ui.UIAuxiliaryMethods;
import ui.UserInterfaceFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Arthur on 2-6-14.
 */

public class Clustering {
    public static final int
            DRAWING_SIZE_X = 1000,
            DRAWING_SIZE_Y = 500;

    public static final String
            VERTEXOVERLAY_PATH = "sphere6.png",
            INTRO_BACKGROUND_PATH = "intro.jpg",
            BACKGROUND_PATH = "bg.jpg";

    // enum for views
    private final int
            CARTESTIAN = 0,
            DENDOGRAM = 1,
            CIRCULAR_DENDOGRAM = 2;

    // the improving of graphics D:
    public static final boolean GRAPHICS_IMPROVEMENTS = true;

    private View[] views;
    private View currentView;

    private BufferedImage intro;

    private Dataset d;
    private DrawUserInterface drawUI;

    public Clustering() {
        Locale.setDefault(Locale.US);

        if(GRAPHICS_IMPROVEMENTS){
            try {
                intro = ImageIO.read(new File(Clustering.INTRO_BACKGROUND_PATH));
            }
            catch (IOException e) { e.printStackTrace();
            }
        }
    }

    public void execute() {
        // first of all I hate the default looks, so Ill change them right away, Im working with dialogs here so I want the dialog lookfeel
        if(GRAPHICS_IMPROVEMENTS){setLookAndFeel(true);}

        DistanceMeasure DM = getDistanceMeasure();
        ClusterMethod CM = getClusterMethod(DM);

        d = Processing.processFile();

        printAssignment1();

        d.normalizeDataset();
        d.preselection();

        printAssignment2();

        ClusterRow totalClusters = new ClusterRow(d);

        printAssignment3(totalClusters);
        printAssignment4(totalClusters);

        Clusterer clusterer = new Clusterer(CM,totalClusters);

        views = createViews(totalClusters);
        drawUI.enableEventProcessing(true);

        eventHandler(clusterer);
    }

    private void eventHandler(Clusterer c) {
        Event e;

        // an extra to make a nice intro screen
        if(GRAPHICS_IMPROVEMENTS){
            // drawing intro screen
            drawUI.drawImage(intro, DRAWING_SIZE_X, DRAWING_SIZE_Y, 0, DRAWING_SIZE_Y);
            drawUI.showChanges();
        }
        else{
            // draw the first step already
            currentView.draw(c.getNextStep());
        }

        while (true) {
            e = drawUI.getEvent();

            if(isEventForwards(e)){
                currentView.draw(c.getNextStep());
            }

            if(isEventBackwards(e)){
                currentView.draw(c.getPreviousStep());
            }

            if(isEventChangeView(e)){
                currentView = getView(e.data);
                currentView.draw(c.getCurrentStep());
            }

            if(isEventChangeRectangle(e)){
                changeRect();
                currentView.draw(c.getCurrentStep());
            }

            if (e.name.equals("mouseover")) {
                drawUI.printf(e.data);
            }

            if (e.name.equals("mouseexit")) {
                drawUI.clearStatusBar();
            }
        }
    }

    private void setLookAndFeel(boolean dialogs){
        try {
            if(dialogs){
//                UIManager.setLookAndFeel(new WebLookAndFeel());
            }
            else{
//                UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEventForwards(Event e){
        return e.name.equals("other_key") && e.data.equals("Space")     ||
                e.name.equals("arrow") && e.data.equals("D")            ||
                e.name.equals("arrow") && e.data.equals("R");
    }

    private boolean isEventBackwards(Event e){
        return          e.name.equals("arrow") && e.data.equals("U")    ||
                        e.name.equals("arrow") && e.data.equals("L");
    }

    private boolean isEventChangeView(Event e){
        return
                e.name.equals("letter") && e.data.equals("c")       ||
                e.name.equals("letter") && e.data.equals("d")       ||
                e.name.equals("letter") && e.data.equals("e");
    }

    private boolean isEventChangeRectangle(Event e){
        return (currentView == views[CARTESTIAN]) && e.name.equals("letter") && e.data.equals("r");
    }

    private void changeRect(){
        ((Cartesian)currentView).changeRect();
    }

    private View getView(String name){
        if(name.equals("c")){
            return views[CARTESTIAN];
        }
        if(name.equals("d")){
            return views[DENDOGRAM];
        }
        else{

            return views[CIRCULAR_DENDOGRAM];
        }
    }

    private DistanceMeasure getDistanceMeasure() {
        // Asks the different kinds of distanceMeasurements
        String distanceMeasure = UIAuxiliaryMethods.askUserForChoice(
                "What distance measure do you want to use ?",
                "Manhattan",
                "Euclidean",
                "Pearson"
        );
        if (distanceMeasure.equals("Manhattan")) {
            return new Manhattan();
        }
        if (distanceMeasure.equals("Euclidean")) {
            return new Euclidean();
        } else {
            return new Pearson();
        }
    }

    private ClusterMethod getClusterMethod(DistanceMeasure DM) {
        String linkage = UIAuxiliaryMethods.askUserForChoice(
                "What linkage do you want to use ?",
                "Single linkage",
                "Complete linkage",
                "Average linkage"
        );

        if (linkage.equals("Single linkage")) {
            return new SingleLinkage(DM);
        }
        if (linkage.equals("Complete linkage")) {
            return new CompleteLinkage(DM);
        } else {
            return new AverageLinkage(DM);
        }
    }

    private View[] createViews(ClusterRow totalClusters){
        // Asks the different kinds of views
        String viewType = UIAuxiliaryMethods.askUserForChoice(
                "What kind of View do you want",
                "Cartesian",
                "Dendogram",
                "Circular Dendogram"
        );

        View[] temp = new View[3];

        // From here on Im working with graphics here so I want the graphic lookfeel
        if(GRAPHICS_IMPROVEMENTS){setLookAndFeel(false);}

        drawUI = UserInterfaceFactory.getDrawUI(DRAWING_SIZE_X, DRAWING_SIZE_Y);
        temp[CARTESTIAN]            = new Cartesian(drawUI, true);
        if (viewType.equals("Cartesian")) {
            currentView = temp[CARTESTIAN];
        }

        temp[DENDOGRAM]             = new Dendogram(drawUI,totalClusters);
        if(viewType.equals("Dendogram")){
            currentView = temp[DENDOGRAM];
        }

        temp[CIRCULAR_DENDOGRAM]    = new CircularDendogram(drawUI, totalClusters);
        if(viewType.equals("Circular Dendogram")){
            currentView = temp[CIRCULAR_DENDOGRAM];
        }

        return temp;
    }

    private void printAssignment1() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assignment 1 output:\n");

        sb.append(
                "The maximum value of the variable " +
                        d.getHeading(0) +
                        " is " +
                        d.getMaxOfRow(0)
        );
        sb.append("\n");

        System.out.println(sb.toString());
    }

    private void printAssignment2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assignment 2 output:\n");

        // print dataset as input
        sb.append("Variables after preselection:\n");
        for (String h : d.getHeadings()) {
            sb.append(h + ", ");
        }
        sb.append("\n");
        System.out.println(sb.toString());
    }

    private void printAssignment3(ClusterRow clusters) {
        StringBuilder sb = new StringBuilder();

        double max = Integer.MIN_VALUE;
        Cluster c = clusters.getCluster(0);

        UnitRow unitRow = c.getUnits();
        Unit u;

        for (int i = 0; i < c.getWidth(); i++) {
            u = unitRow.getUnit(i);

            for (int j = 0; j < u.getSize(); j++) {
                if (max < u.getRow(j)) {
                    max = u.getRow(j);
                }
            }
        }

        sb.append("Assignment 3 output:\n");
        sb.append("Maximum value of first cluster: " + max + "\n");

        System.out.println(sb.toString());
    }

    private void printAssignment4(ClusterRow clusters) {
        StringBuffer sb = new StringBuffer();
        DistanceMeasure[] DM = {new Euclidean(), new Manhattan(), new Pearson()};

        // Assignment 3 output
        // we use the first 2 clusters
        Cluster c1 = clusters.getCluster(0);
        Cluster c2 = clusters.getCluster(1);

        sb.append("Assignment 4 output:\n");
        sb.append(String.format("Euclidean + SingleLinkage:      %.6f\n", new SingleLinkage(DM[0]).calculateDistance(c1, c2)));
        sb.append(String.format("Euclidean + AverageLinkage:     %.6f\n", new AverageLinkage(DM[0]).calculateDistance(c1, c2)));
        sb.append(String.format("Euclidean + CompleteLinkage:    %.6f\n", new CompleteLinkage(DM[0]).calculateDistance(c1, c2)));
        sb.append(String.format("Manhattan + SingleLinkage:      %.6f\n", new SingleLinkage(DM[1]).calculateDistance(c1, c2)));
        sb.append(String.format("Manhattan + AverageLinkage:     %.6f\n", new AverageLinkage(DM[1]).calculateDistance(c1, c2)));
        sb.append(String.format("Manhattan + CompleteLinkage:    %.6f\n", new CompleteLinkage(DM[1]).calculateDistance(c1, c2)));
        sb.append(String.format("Pearson + SingleLinkage:        %.6f\n", new SingleLinkage(DM[2]).calculateDistance(c1, c2)));
        sb.append(String.format("Pearson + AverageLinkage:       %.6f\n", new AverageLinkage(DM[2]).calculateDistance(c1, c2)));
        sb.append(String.format("Pearson + CompleteLinkage:      %.6f\n\n", new CompleteLinkage(DM[2]).calculateDistance(c1, c2)));

        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        new Clustering().execute();
    }
}
