package Assignment6.Views;

import Assignment6.ClusterObjects.Cluster;
import Assignment6.ClusterObjects.ClusterRow;
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

// This class is made because I saw a circular dendogram on the internet, and I thought "yes I should totally make this"
// so here it is, my masterpiece of coding, not really but I don't think anyone else was that enthusiastic...

public class CircularDendogram implements View{
    DrawUserInterface DrawUI;
    // dimensions of the screen
    private int width, height;
    // dimensions of drawing
    int centerX, centerY;
    // size of the radius of the circle, size of a vertex and size of a single depth unit
    int radius,vertexSize, depthSize;
    // total number of vertices
    int numberOfUnits;

    // a counter to see what unit were drawing atm
    // I know its not pretty to do it globally, sorry for that
    int nthUnit;

    // vertexOverlay to overlay on the vertices
    BufferedImage vertexOverlay;
    BufferedImage background;


    public CircularDendogram(DrawUserInterface DrawUI, ClusterRow CR) {
        this.DrawUI = DrawUI;
        // I couldn't use drawUI.getHeight due to some weird flaw, it returned me numbers larger than the actual height
        this.width  = Clustering.DRAWING_SIZE_X;
        this.height = Clustering.DRAWING_SIZE_Y;

        this.centerX = width/2;
        this.centerY = height/2;

        this.numberOfUnits = CR.getNumberOfClusters();

        this.vertexSize = height / (numberOfUnits);

        this.radius = height/2 - vertexSize; // with some padding

        this.depthSize = radius / (numberOfUnits-CR.getClusterGoal());

        this.vertexOverlay =  null;

        if(Clustering.GRAPHICS_IMPROVEMENTS){
            try {
                vertexOverlay   = ImageIO.read(new File(Clustering.VERTEXOVERLAY_PATH));
                background      = ImageIO.read(new File(Clustering.BACKGROUND_PATH));
            }
            catch (IOException e) { e.printStackTrace();}
        }
    }

    @Override
    public void draw(ClusterRow clusters) {
        DrawUI.clear();
        if(Clustering.GRAPHICS_IMPROVEMENTS){
            DrawUI.drawImage(background, width, height, 0, height);
        }

        // drawUI.drawCircle(centerX, centerY, depthTo(numberOfUnits-1), depthTo(numberOfUnits-1), WHITE, false);
        DrawUI.setCircleHotspot(centerX, centerY, vertexSize, vertexSize, "the root");

        nthUnit = 0;
        Cluster c;

        for (int i = 0; i < clusters.getNumberOfClusters(); i++) {
            c = clusters.getCluster(i);
            draw(c, numberOfUnits - clusters.getClusterGoal());
        }
        //drawUI.drawCircle(centerX,centerY,100,100,WHITE,true);
        DrawUI.showChanges();
    }

    private double draw(Cluster c, int parentDepth) {
        if (c.hasChildren()) {
            // its a node, return the degree at which this cluster will be
            return drawNode(c,parentDepth);
        }
        else {
            // its a leaf, return the degree at which this cluster will be
            return drawLeaf(c,parentDepth);
        }
    }

    private double drawNode(Cluster c, int parentDepth){
        // children of the node
        Cluster child1 = ((Node)c).getChild1();
        Cluster child2 = ((Node)c).getChild2();

        double degree1 = draw(child1, c.getDepth());
        double degree2 = draw(child2, c.getDepth());

        Colour colour;
        if(Clustering.GRAPHICS_IMPROVEMENTS){colour = ColourGradients.WHITE;}
        else{colour = ColourGradients.BLACK;}

        // now the arc between 2 clusters
        drawArc(centerX,centerY,depthTo(c.getDepth()),degree1,degree2,colour);

        // degree of this cluster
        double degree = (degree1+degree2)/2;

        // now for the line to the center
        int fromX   = centerX     + (int)( depthTo(c.getDepth()) * Math.sin(degree));
        int fromY   = centerY    + (int)( depthTo(c.getDepth()) * Math.cos(degree));

        int toX     = centerX  + (int)(depthTo(parentDepth)*Math.sin(degree));
        int toY     = centerY  + (int)(depthTo(parentDepth)*Math.cos(degree));

        DrawUI.drawLine(fromX, fromY, toX, toY, colour);

        // the dot of course
        DrawUI.drawCircle(fromX, fromY, vertexSize / 2, vertexSize / 2, c.getColour(), true);
        if(Clustering.GRAPHICS_IMPROVEMENTS){
            DrawUI.drawImage(vertexOverlay, vertexSize / 2+1, vertexSize / 2+1, fromX - vertexSize / 4, fromY + vertexSize / 4);
        }
        return degree;
    }

    private double drawLeaf(Cluster c, int parentDepth){
        // if it is a Leaf I just want the unit and draw it
        UnitRow unitRow = c.getUnits();
        double degree = drawUnit(unitRow.getUnit(0),c.getColour(), parentDepth);
        nthUnit++;
        return degree;
    }

    private double drawUnit(Unit u, Colour c,int parentDepth) {
        double degree = Math.PI*2*((double)nthUnit/numberOfUnits);

        int x           = width/2  + (int)(depthTo(0)*Math.sin(degree));
        int y           = height/2 + (int)(depthTo(0)*Math.cos(degree));
		
        int parentX     = width/2  + (int)(depthTo(parentDepth)*Math.sin(degree));
        int parentY     = height/2 + (int)(depthTo(parentDepth)*Math.cos(degree));




        if(Clustering.GRAPHICS_IMPROVEMENTS){
            // drawing the inherited line
            DrawUI.drawLine(x, y, parentX, parentY, ColourGradients.WHITE);

            vertexSize *=2;
            // drawing the vertex and setting the hot spot
            DrawUI.drawCircle(x, y, vertexSize, vertexSize, c, true);
            DrawUI.drawImage(vertexOverlay, vertexSize+1, vertexSize+1, x - vertexSize / 2, y + vertexSize / 2);
            DrawUI.setCircleHotspot(x, y, vertexSize, vertexSize, u.getLabel());
            vertexSize /=2;
        }
        else{
            // drawing the inherited line
            DrawUI.drawLine(x, y, parentX, parentY, ColourGradients.BLACK);

            // drawing the vertex
            DrawUI.drawCircle(x, y, vertexSize, vertexSize, c, true);
        }

        return degree;
    }

    void drawArc(int x,int y,int radius,double beginAngle,double endAngle,Colour c){
        if(beginAngle > endAngle){
            double temp = beginAngle;
            beginAngle = endAngle;
            endAngle = temp;
        }
        int fromX,fromY,toX,toY;
        double step = Math.PI/4;
        double d;
        for(d=beginAngle;d<endAngle-step;d+=step){
            fromX   = x + (int)(radius*Math.sin(d));
            fromY   = y + (int)(radius*Math.cos(d));
            toX     = x + (int)(radius*Math.sin(d+step));
            toY     = y + (int)(radius*Math.cos(d+step));
            DrawUI.drawLine(fromX, fromY, toX, toY, c);
        }


        // to get the last step accurately i will do it manually
        fromX   = x + (int)(radius*Math.sin(d));
        fromY   = y + (int)(radius*Math.cos(d));
        toX     = x + (int)(radius*Math.sin(endAngle));
        toY     = y + (int)(radius*Math.cos(endAngle));
        DrawUI.drawLine(fromX, fromY, toX, toY, c);
    }

    private int depthTo(int x) {
        return radius - depthSize * x;
    }
}
