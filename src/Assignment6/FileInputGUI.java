package Assignment6;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileInputGUI
{
    private FileInputGUI(){}

    public static void askUserForInput(){
        // create a frame to put the filechooser into
        JFrame explorer = new JFrame("");
        // set the exit method
        explorer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create the fileChooser and set the preferred size
        JFileChooser fc = new JFileChooser();
        fc.setPreferredSize(new Dimension(800,600));

        // this basically will return whether or not a file has been selected
        int returnVal = fc.showOpenDialog(explorer);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // this means a file was selected, now the file is extracted in file variable
            File file = fc.getSelectedFile();

            // now I create an inputstream to change the System.in to
            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // set it
            System.setIn(is);
        }
        else {
            System.out.println("Open command cancelled by user.\nTurning off now.");
            System.exit(0);
        }
    }
}
