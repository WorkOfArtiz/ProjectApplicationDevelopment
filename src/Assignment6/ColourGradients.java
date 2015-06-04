package Assignment6;

import ui.Colour;


/**
 * [Optional Class]
 * Created by Arthur on 6-6-14.
 */
public class ColourGradients {
    // change these colours if you want a different gradient
    public static String colour1 = "FFFF00";
    public static String colour2 = "FF580A";
    public static String colour3 = "8509AB";

    // a static colour white
    public static final Colour WHITE = new Colour(240,240,240);
    public static final Colour BLACK = new Colour(0,0,0);
    public static final Colour GRAYISH = new Colour(200,200,200);

    public static Colour genColour(){
        int R = (int)(Math.random()*255),
            G = (int)(Math.random()*255),
            B = (int)(Math.random()*255);

        return new Colour(R,G,B);
    }


    public static Colour genColour(int place, int total) {
        int R,G,B; // final values;

        // I want a gradient from deep purple to orange to yellow
        int[] color1 = extractColour(ColourGradients.colour1);      // yellow
        int[] color2 = extractColour(ColourGradients.colour2);      // orange
        int[] color3 = extractColour(ColourGradients.colour3);      // purple

        // differences between color1 vs color2 and color2 vs color3
        int[] diff1 = new int[3];
        int[] diff2 = new int[3];

        for (int i = 0; i < diff1.length; i++) {
            diff1[i] = color2[i] - color1[i];
            diff2[i] = color3[i] - color2[i];
        }

        // where on the spectrum is this dot
        double spot = (double) place / (total - 1);

        if(spot < 0.5){
            spot *= 2;

            R = color1[0] + (int) (spot * diff1[0]);
            G = color1[1] + (int) (spot * diff1[1]);
            B = color1[2] + (int) (spot * diff1[2]);
        }
        else{
            spot -= 0.5;
            spot *= 2;

            R = color2[0] + (int) (spot * diff2[0]);
            G = color2[1] + (int) (spot * diff2[1]);
            B = color2[2] + (int) (spot * diff2[2]);
        }
        return new Colour(R, G, B);
    }

    public static Colour duplicateColor(Colour c){
        int R = c.red, G = c.green, B = c.blue;

        return new Colour(R,G,B);
    }

    public static Colour average(Colour c1,int weightC1,Colour c2, int weightC2){
        int R1 = c1.red*weightC1, G1 = c1.green*weightC1, B1 = c1.blue*weightC1;
        int R2 = c2.red*weightC1, G2 = c2.green*weightC2, B2 = c2.blue*weightC2;
        int R = (R1+R2)/(weightC1+weightC2),G = (G1+G2)/(weightC1+weightC2),B = (B1+B2)/(weightC1+weightC2);

        return new Colour(R,G,B);
    }

    public static Colour average(Colour c1,Colour c2){
        int R1 = c1.red, G1 = c1.green, B1 = c1.blue;
        int R2 = c2.red, G2 = c2.green, B2 = c2.blue;
        int R = (R1+R2)/2,G = (G1+G2)/2,B = (B1+B2)/2;

        return new Colour(R,G,B);
    }

    private static int[] extractColour(String hex){
        if(hex.length() != 6){
            System.out.println("HEX VALUE COULD NOT BE TRANSLATED");
            System.exit(-1);
        }

        int[] RGB = new int[3];

        RGB[0] = Integer.parseInt(hex.substring(0,2), 16);
        RGB[1] = Integer.parseInt(hex.substring(2,4), 16);
        RGB[2] = Integer.parseInt(hex.substring(4,6), 16);

        return RGB;
    }

}
