package Assignment6;

/**
 * Created by Arthur on 2-6-14.
 */

public class Dataset {
    // dimensions
    private int numberOfRows;            // number of numberOfRows
    private int numberOfEntries;

    // headings
    // as can be seen, I seperate name of the index from other headings
    // This is because the index is not seen as a column in the format
    private String[] headings;   // for instance properties of flowers
    private String nameIndex;    // for instance name of the flower.

    // clusterData
    private int numberOfClusters;

    // rows
    private UnitRow entries;          // this contains specific entries


    public Dataset(int numberOfRows, int numberOfEntries, int numberOfClusters) {
        // 3 variables that have to be known
        this.numberOfRows = numberOfRows;
        this.numberOfEntries = numberOfEntries;
        this.numberOfClusters = numberOfClusters;

        // first create unit column with the number of entries
        entries = new UnitRow(numberOfEntries);
    }

    public void add(Unit u) {
        entries.addUnit(u);
    }

    public Unit getEntry(int entry) {
        return entries.getUnit(entry);
    }

    public void setEntry(int entry, Unit u) {
        entries.setUnit(entry, u);
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    public int getNumberOfEntries() {
        return entries.getSize();
    }

    public void setHeadings(String[] headings) {
        this.headings = headings;
    }

    public String[] getHeadings() {
        return headings;
    }

    public String getHeading(int index) {
        return headings[index];
    }

    public void setNameIndex(String nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void normalizeDataset() {
        // applying (V-Vmin)/(Vmax-Vmin)
        double minimumRowValue;
        double maximumRowValue;
        double diffMinMax;      // (Vmax-Vmin)
        double normalisedValue; // temporary normalised value
        Unit modified;          // temporary Unit

        for (int row = 0; row < numberOfRows; row++) {
            minimumRowValue = getMinOfRow(row);
            maximumRowValue = getMaxOfRow(row);

            diffMinMax = maximumRowValue - minimumRowValue;

            for (int entry = 0; entry < numberOfEntries; entry++) {
                modified = getEntry(entry);
                normalisedValue = (modified.getRow(row) - minimumRowValue) / diffMinMax;
                modified.setRow(row, normalisedValue);
                setEntry(entry, modified);
            }
        }
    }

    public void preselection() {
        // ONLY CHANGE IF MORE THEN 50 VARS
        if (headings.length > 50) {
            double[] standardDeviations = getStandardDeviation();

            Sorting.extractTop50Rows(this, standardDeviations);
        }
    }

    private double[] getStandardDeviation() {
        double[] standardDeviations = new double[numberOfRows];

        for (int row = 0; row < numberOfRows; row++) {
            standardDeviations[row] = getStandardDeviation(row);
         }
        System.out.println();
        return standardDeviations;
    }

    private double getStandardDeviation(int row) {
        double averageX = getAverageOfRow(row);
        

        double sumdiff = 0;
        double standardDev;

        for (int entry = 0; entry < numberOfEntries; entry++) {
            // sumdiff += (Xi - Xaverage)^2;
            sumdiff += Math.pow(getEntry(entry).getRow(row) - averageX, 2);
        }

        standardDev = Math.sqrt(sumdiff / (numberOfEntries - 1));

        return standardDev;
    }

    private double getAverageOfRow(int row) {
        double sum = 0;

        for (int entry = 0; entry < numberOfEntries; entry++) {
            sum += getEntry(entry).getRow(row);
        }
        return sum / numberOfEntries;
    }

    private double getMinOfRow(int row) {
        double minValue = Double.MAX_VALUE;

        for (int entry = 0; entry < entries.getSize(); entry++) {
            if (entries.getUnit(entry).getRow(row) < minValue) {
                minValue = entries.getUnit(entry).getRow(row);
            }
        }

        return minValue;
    }

    public double getMaxOfRow(int row) {
        double maxValue = -Double.MAX_VALUE;

        for (int entry = 0; entry < entries.getSize(); entry++) {
            if (entries.getUnit(entry).getRow(row) > maxValue) {
                maxValue = entries.getUnit(entry).getRow(row);
            }
        }

        return maxValue;
    }

    public void dump() {
        System.out.print(nameIndex + "\t");
        for (String h : headings) {
            System.out.print(h + "\t");
        }
        System.out.println();
        entries.dump();
    }
}
