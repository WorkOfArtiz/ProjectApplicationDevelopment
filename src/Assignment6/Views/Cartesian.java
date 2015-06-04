package Assignment6.Views;

import Assignment6.ClusterObjects.Cluster;
import Assignment6.ClusterObjects.ClusterRow;
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

public class Cartesian implements View {
    // the drawing interface is stored in this var
    DrawUserInterface drawUI;

    // absolute values of the drawing interface
    private int width, height;

    // relative calculated values of the graph
    private int startX, startY,diffX,diffY, endX, endY, vertexSize;

    // encircling with rectangles or ovals
    private boolean rectangles;

    // vertexOverlay to overlay on the vertices
    BufferedImage vertexOverlay;
    BufferedImage background;



    public Cartesian(DrawUserInterface drawUI, boolean rectangles) {
        this.drawUI = drawUI;

        this.width  = Clustering.DRAWING_SIZE_X; // due to some error in drawUI.getWidth/Height
        this.height = Clustering.DRAWING_SIZE_Y; // Im forced to using a static in the clustering class

        // begin and the end, sort of sounding like the bible there
        this.startX = width / 10;
        this.startY = height / 10;

        this.endX = width - width/10;
        this.endY = height - height/10;

        this.diffX = Math.abs(startX - endX);
        this.diffY = Math.abs(startY - endY);

        this.rectangles = rectangles;
        if(Clustering.GRAPHICS_IMPROVEMENTS){
            try {
                vertexOverlay   = ImageIO.read(new File(Clustering.VERTEXOVERLAY_PATH));
                background      = ImageIO.read(new File(Clustering.BACKGROUND_PATH));
            }
            catch (IOException e) { e.printStackTrace();}
        }
    }

    @Override
    public void draw(ClusterRow CR) {
        drawUI.clear();

        // first fill the with my background, I didnt really like the white background anyway
        if(Clustering.GRAPHICS_IMPROVEMENTS){
            drawUI.drawImage(background, width, height, 0, height);
        }

        // then the axes
        drawAxes(CR.getXHeading(), CR.getYHeading());

        // vertexSize, well... its the size... of a vertex...
        this.vertexSize = (((width < height) ? width : height) / CR.getNumberOfUnits());

        // draw every cluster in there
        for (int i = 0; i < CR.getNumberOfClusters(); i++) {
            draw(CR.getCluster(i));
        }

        // show what has happened so far
        drawUI.showChanges();
    }

    public void draw(Cluster c) {
        Colour colour = c.getColour();
        // to avoid having through all vertices of a cluster in the function to surround the cluster with a rectangle or oval
        // Im basically storing the extreme values right away
        int minX = width, maxX = 0, minY = height, maxY = 0;

        // these will be the coordinates of the vertex
        int x, y;

        // these variables are to make notations shorter (and hopefully just a little bit faster)
        UnitRow unitsCluster = c.getUnits();
        Unit u;

        // loop through each entry in the units of a clusterRow, one entry corresponds to one vertex
        for (int entry = 0; entry < c.getWidth(); entry++) {
            u = unitsCluster.getUnit(entry);

            // due to the fact that this is a 2D representation of the data,
            // I will use the first 2 normalized values to make the listPlot
            x = startX  + (int) (u.getRow(0) * diffX);
            y = startY  + (int) (u.getRow(1) * diffY);

            // this is basically the message you'll see when you hover over a vertex
            String onHover = String.format("Point at %.3f, %.3f\n", u.getRow(0), u.getRow(1));

            // selecting min and max coordinates of a cluster (for encircling)
            if (x < minX) {minX = x;}
            if (y < minY) {minY = y;}
            if (x > maxX) {maxX = x;}
            if (y > maxY) {maxY = y;}


            // draw the vertex
            drawUI.drawCircle(x, y, vertexSize, vertexSize, colour, true);

            if(Clustering.GRAPHICS_IMPROVEMENTS){
                // draw the vertexOverlay (the picture) on top, the +1 for the size is to correct errors with the vertexSize/2 part
                drawUI.drawImage(vertexOverlay, vertexSize + 1, vertexSize + 1, x - vertexSize / 2, y + vertexSize / 2);
                // and set the hotspot for the right area
                drawUI.setCircleHotspot(x, y, vertexSize, vertexSize, onHover);
            }
        }

        if (c.getWidth() > 1 && rectangles) {
            // draw a rectangle around it
            surroundWithRect(minX, maxX, minY, maxY, colour);
        }
        else if (c.getWidth() > 1 && !rectangles) {
            // draw an oval around it
            surroundWithOval(minX, maxX, minY, maxY, colour);
        }
    }

    private void surroundWithRect(int minX, int maxX, int minY, int maxY, Colour c) {
        // dimensions of rectangle around the data points with padding
        int     x = minX - vertexSize,
                y = maxY + vertexSize,
                w = 2 * vertexSize + maxX - minX,
                h = 2 * vertexSize + maxY - minY;

        drawUI.drawSquare(x, y, w, h, c, false);
    }


    private void surroundWithOval(int minX, int maxX, int minY, int maxY, Colour c) {
        // starting point for the oval
        int averX = (minX + maxX) / 2;
        int averY = (minY + maxY) / 2;

        // x and y diameters, calculated to include points also if they're in the corners
        int diameterX = vertexSize + (int) (((double) maxX - minX) / Math.sin(Math.PI / 4)); // Basically I use Rx * sin(45 degree) = the difference in corner coordinates
        int diameterY = vertexSize + (int) (((double) maxY - minY) / Math.cos(Math.PI / 4)); // so Rx (and Ry)  = diff / sin(45 degree)

        drawUI.drawCircle(averX, averY, diameterX, diameterY, c, false);
    }

    private void drawAxes(String xAxes, String yAxes){
        Colour c;
        if(Clustering.GRAPHICS_IMPROVEMENTS){  c = ColourGradients.WHITE; }
        else{
            c = ColourGradients.BLACK;

            // lets draw some extra lines
            // y lines
            for(int i=startX;i<=endX;i+=diffX/10){
                drawUI.drawLine(i,startY,i,endY,ColourGradients.GRAYISH);
            }
            // x lines
            for(int i=startY;i<=endY;i+=diffY/10){
                drawUI.drawLine(startX,i,endX, i,ColourGradients.GRAYISH);
            }
        }

        // x axis
        drawUI.drawText((startX + endX) / 2, startY - 2 * drawUI.getTextHeight(xAxes), xAxes, c);
        drawUI.drawLine(startX, startY, endX, startY, c);

        // y axis
        drawUI.drawText(startX - drawUI.getTextWidth(yAxes) / 2, endY + drawUI.getTextWidth(yAxes), yAxes, c);
        drawUI.drawLine(startX, startY, startX, endY, c);


        // also if I print this, it will return 30 eitherway, you might want to look into that
        // System.out.printf("text width, nothing:%d, something huge:%d", drawUI.getTextWidth(""), drawUI.getTextWidth("asdfghjklsdfghjklsdfghjklxcvbnm,kfsvgdbkbfasjdhfjshadbfkjhsadbfjsadfkjasbdjfbsdjh"));
    }

    public void changeRect(){rectangles = !rectangles;}
}
