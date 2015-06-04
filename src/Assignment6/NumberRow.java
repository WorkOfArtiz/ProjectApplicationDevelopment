package Assignment6;


public class NumberRow {
    private final int DEFAULT_SIZE = 3000;
    private double[] rows;
    private int numberOfRows = 0;

    public NumberRow(int rows) {
        this.rows = new double[rows];
    }

    public NumberRow() {
        this.rows = new double[DEFAULT_SIZE];
    }

    public double getRow(int index) { // parameter i
        return rows[index];
    }

    public void setRow(int index, double d) { // parameter i
        rows[index] = d;
    }

    public void add(double d) {
        if (numberOfRows < 0 || numberOfRows >= rows.length) {
            System.out.println("ERROR: COULD NOT ADD DOUBLE TO NUMBER ROW, OVERFLOW");
            System.exit(-1);
        }

        rows[numberOfRows++] = d;
    }

    public int getSize() {
        return numberOfRows;
    }

    public void dump() {
        for (int i=0;i<numberOfRows;i++) {
            System.out.printf("%.6f\t", rows[i]);
        }
    }
}
