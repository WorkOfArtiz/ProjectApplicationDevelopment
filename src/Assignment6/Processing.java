package Assignment6;

import java.util.Scanner;

/**
 * Created by Arthur on 2-6-14.
 */

public class Processing {
    public static Dataset processFile() {
        FileInputGUI.askUserForInput();
        //UIAuxiliaryMethods.askUserForInput();
        Scanner s = new Scanner(System.in);

        /*
        Scanner s = null;
        try {
            s = new Scanner(new File("milk.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */

        // first lines are for number of numberOfClusters and dimensions
        int numberOfClusters = processIntLine(s.nextLine());
        int numberOfEntries = processIntLine(s.nextLine());
        int numberOfRows = processIntLine(s.nextLine());

        // create a dataset with said properties
        Dataset d = new Dataset(numberOfRows, numberOfEntries, numberOfClusters);

        // read in the headings and store them in headings
        processHeadings(d,s.nextLine(), numberOfRows);

        // loop through entry lines and store them.
        // I chose for a while to make the program able to work
        // without the entries variable to be correct
        while (s.hasNext()) {
            d.add(processDataLine(s.nextLine(), numberOfRows));
        }

        s.close();
        return d;
    }

    private static int processIntLine(String input) {
        // only used to getRow the int value on the first 3 lines
        Scanner scanner = new Scanner(input);
        int result = scanner.nextInt();
        scanner.close();
        return result;
    }

    private static void processHeadings(Dataset d,String input, int numberOfRows) {
        Scanner s = new Scanner(input);

        // it always starts with the type of index
        // for instance Mammal
        d.setNameIndex(s.next());

        // create the headings, for instance
        // headings = {"fat_content(%)","protein_content(%)"}
        String[] headings = new String[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            headings[i] = s.next();
        }

        // they will be stored in the dataSet that we're working with
        d.setHeadings(headings);
        s.close();
    }

    private static Unit processDataLine(String input, int numberOfRows) {
        Scanner s = new Scanner(input);
        String label = s.next();

        // create a new unit to store rows from one line in
        Unit u = new Unit(numberOfRows, label);

        // storing the rows in the scanner
        for (int i = 0; i < numberOfRows; i++) {
            u.addVar(s.nextDouble());
        }

        s.close();
        return u;
    }
}
