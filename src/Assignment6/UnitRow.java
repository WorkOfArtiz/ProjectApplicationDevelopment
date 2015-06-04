package Assignment6;

/**
 * Created by Arthur on 2-6-14.
 */

public class UnitRow {
    // BufferSize
    final static int DEFAULT_SIZE = 3000;

    // Unit array with all data in it
    private Unit[] units;
    // numberOfCurrentElements will be used to avoid overflows
    private int numberOfCurrentElements = 0;


    public UnitRow(int numberOfEntries) { // work as an array
        units = new Unit[numberOfEntries];
    }

    public UnitRow() { // work as an arraylist
        units = new Unit[DEFAULT_SIZE];
    }

    public void addUnit(Unit u) {
        if (numberOfCurrentElements >= units.length) {
            System.out.println("ERROR: TOO MANY UNITS LIMIT REACHED");
            System.exit(-1);
        }

        units[numberOfCurrentElements++] = u;
    }

    public Unit getUnit(int entry) {
        if (entry < 0 || entry >= units.length) {
            System.out.println("ERROR: COULD NOT GET ENTRY, INDEX WAS OUT OF BOUNDS");
            System.exit(-1);
        }

        return units[entry];
    }

    public void setUnit(int entry, Unit u) {
        if (entry < 0 || entry >= units.length) {
            System.out.println("ERROR: COULD NOT SET ENTRY, INDEX WAS OUT OF BOUNDS");
            System.exit(-1);
        }

        units[entry] = u;
    }

    public int getSize() {
        return numberOfCurrentElements;
    }

    public void dump() {
        for (int i = 0; i < numberOfCurrentElements; i++) {
            units[i].dump();
            System.out.println();
        }
    }


}
