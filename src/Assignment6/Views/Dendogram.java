package Assignment6.Views;

import Assignment6.ClusterObjects.Cluster;
import Assignment6.ClusterObjects.ClusterRow;
import Assignment6.ClusterObjects.Leaf;
import Assignment6.ClusterObjects.Node;
import Assignment6.Clustering;
import Assignment6.ColourGradients;
import Assignment6.Unit;
import Assignment6.UnitRow;
import ui.Colour;
import ui.DrawUserInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Arthur on 6-6-14.
 */
public class Dendogram implements View {
    DrawUserInterface DrawUI;
    // dimensions of the screen
    private int width, height;
    // dimensions of drawing
    int startX, endX,startY,endY;
    // size of a vertex and size of a single depth unit
    int vertexSize, depthSize;
    // total number of vertices
    int numberOfUnits;
    // a counter to see what unit were drawing atm
    int nthUnit;

    // overlying vertexOverlay
    BufferedImage vertexOverlay;
    BufferedImage background;


    public Dendogram(DrawUserInterface DrawUI, ClusterRow CR) {
        this.DrawUI = DrawUI;
        // I couldn't use drawUI.getHeight due to some weird flaw, it returned me numbers larger than the actual height
        this.width  = Clustering.DRAWING_SIZE_X;
        this.height = Clustering.DRAWING_SIZE_Y;

        this.numberOfUnits = CR.getNumberOfUnits();

        this.startX = width/20;
        this.startY = height/20;
        this.endX = width - width/10; // it needs extra room for the text
        this.endY = height - height/40;

        this.vertexSize = (endY-startY) / numberOfUnits;
        // somehow it looks shitty if vertexSize is odd so
        this.vertexSize -= (1-vertexSize%2);

        if(Clustering.GRAPHICS_IMPROVEMENTS){
            try {
                vertexOverlay   = ImageIO.read(new File(Clustering.VERTEXOVERLAY_PATH));
                background      = ImageIO.read(new File(Clustering.BACKGROUND_PATH));
            } catch (IOException e) { e.printStackTrace();}
        }

        this.depthSize = (endX-startX)/(numberOfUnits - CR.getClusterGoal() + 1);
    }

    @Override
    public void draw(ClusterRow clusters) {
        DrawUI.clear();

        if(Clustering.GRAPHICS_IMPROVEMENTS){
            // drawing the background
            DrawUI.drawImage(background, width, height, 0,height);
        }

        // setting the counter of units drawn back to 0
        nthUnit = 0;

        // drawing all clusters
        for (int i = 0; i < clusters.getNumberOfClusters(); i++) {
            draw(clusters.getCluster(i), 0);
        }

        DrawUI.showChanges();
    }

    private int draw(Cluster cluster, int parentDepth) {
        if (cluster.hasChildren()) {
            return drawNode((Node)cluster,parentDepth);
        }
        else {
            return drawLeaf((Leaf)cluster,parentDepth);
        }
    }

    private int drawNode(Node node, int parentDepth){
        Cluster child1 =  node.getChild1();
        Cluster child2 =  node.getChild2();


        int y1 = draw(child1, node.getDepth());
        int y2 = draw(child2, node.getDepth());

        // now the vertical line
        int x = depthToX(node.getDepth());
        DrawUI.drawLine(x, y1, x, y2, ColourGradients.WHITE);

        // now for the horizontal line
        int x1 = depthToX(parentDepth);
        int x2 = depthToX(node.getDepth());
        int y = (y1 + y2) / 2;

        if(Clustering.GRAPHICS_IMPROVEMENTS){
            DrawUI.drawLine(x1, y, x2, y, ColourGradients.WHITE);

            // the dot of course
            DrawUI.drawCircle(x, y, vertexSize *3 / 4, vertexSize * 3 /4, node.getColour(), true);
            DrawUI.drawImage(vertexOverlay, (vertexSize * 3 / 4)+1, (vertexSize * 3 / 4)+1, x - (vertexSize * 3 / 8), y + (vertexSize * 3 / 8));
        }
        else{
            DrawUI.drawLine(x1, y, x2, y, ColourGradients.BLACK);
        }

        return y;
    }

    private int drawLeaf(Leaf leaf, int parentDepth){
        UnitRow unitRow = leaf.getUnits();
        int y = drawUnit(unitRow.getUnit(0), leaf.getColour(), parentDepth);
        nthUnit++;
        return y;
    }

    private int drawUnit(Unit u, Colour c, int parentDepth) {
        int x = endX;
        int y = startY +  nthUnit * (endY-startY)/numberOfUnits ;
        String label = "-" + u.getLabel();

        if(Clustering.GRAPHICS_IMPROVEMENTS){
        // drawing the inherited line
        DrawUI.drawLine(depthToX(parentDepth), y, x - vertexSize / 2, y, ColourGradients.WHITE);

        // drawing the vertex and setting the hotspot
        DrawUI.drawCircle(x, y, vertexSize, vertexSize, c, true);
        DrawUI.drawImage(vertexOverlay, vertexSize, vertexSize, x - vertexSize / 2, y + vertexSize / 2);
        DrawUI.setCircleHotspot(x, y, vertexSize, vertexSize, u.getLabel());

        // drawing the label
        DrawUI.drawText(x + vertexSize, y - vertexSize / 5, label, ColourGradients.WHITE);
        }
        else{
            // drawing the inherited line
            DrawUI.drawLine(depthToX(parentDepth), y, x - vertexSize / 2, y, ColourGradients.BLACK);

            // drawing the vertex and setting the hotspot
            DrawUI.drawCircle(x, y, vertexSize, vertexSize, c, true);

            // drawing the label
            DrawUI.drawText(x + vertexSize, y - vertexSize / 5, label, ColourGradients.BLACK);

        }
        return y;
    }

    private int depthToX(int x) {
        // if the level is 0 I want it to start at the beginning, otherwise it will be calculated from the right
        if (x == 0) { return startX; }
        return endX - depthSize * (x + 1);
    }
}
