package Assignment6;

/**
 * Created by Arthur on 2-6-14.
 */

public class Unit {
    private String label;
    private NumberRow data;

    public Unit(int rows, String label) {
        data = new NumberRow(rows);
        this.label = label;
    }

    public void dump() {
        System.out.print(label + "\t");
        data.dump();
    }

    public void addVar(double d) {
        data.add(d);
    }

    public String getLabel() {
        return label;
    }

    public double getRow(int row) {
        if (row < 0 || row >= data.getSize()) {
            System.out.println("ERROR: COULD NOT GET ROW, INDEX WAS OUT OF BOUNDS");
            System.out.println("ROW:" + row + " - array length:" + data.getSize());
            throw new ArrayIndexOutOfBoundsException();
        }

        return data.getRow(row);
    }

    public void setRow(int row, double d) {
        if (row < 0 || row >= data.getSize()) {
            System.out.println("ERROR: COULD NOT SET ROW, INDEX WAS OUT OF BOUNDS");
            System.exit(-1);
        }

        data.setRow(row, d);
    }

    public int getSize() {
        return data.getSize();
    }

    public void setData(NumberRow replacement) {
        data = replacement;
    }
}
