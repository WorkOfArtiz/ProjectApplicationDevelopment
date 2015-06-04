package Assignment6;

import java.util.Arrays;

public class Sorting {
    // used for extract top 50 rows is the amount used in this method... I have no clue what value this should have
    private static final int EXTRACTION_LIMIT = 50;

    // this class is specifically for sorting an array based on another array
    // of similar length.

    public static void extractTop50Rows(Dataset d, double[] criterium) {
        if (d.getEntry(0).getSize() < 50) {
            System.out.println("ERROR: CANT CUT OUT ROWS WHEN LOWER THAN MINIMUM OF 50 ROWS");
            System.exit(-1);
        }

        if (d.getEntry(0).getSize() != criterium.length) {
            System.out.println("ERROR: SORTING ARRAY IMPOSSIBLE SINCE THERE ISNT A GOOD UNIT-CRITERIUM MAPPING");
            System.exit(-1);
        }

        // current indexes are 0,1,..,n-1,n where n = length of the rows
        int[] indexes = new int[d.getEntry(0).getSize()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        // an array is created with the index mapping of the criteria
        indexes = mergeSort(indexes, criterium);

        // since this index is now in the right order, ill get the top 50 (position 0 until 49)
        indexes = cut(indexes, 0, EXTRACTION_LIMIT);

        // sort those in normal order (this was done in the example output for some reason)
        Arrays.sort(indexes);

        // then modify the dataset to only have the rows where the index is in the indexes array
        modifyRows(d, indexes);
    }

    private static void modifyRows(Dataset d, int[] indexes) {
        // modify the headings
        d.setHeadings(modifyRows(d.getHeadings(), indexes));

        Unit modifiedUnit;  // temporary Unit
        
        for (int entry = 0; entry < d.getNumberOfEntries(); entry++) {
            // copying current entry into modified Unit
            modifiedUnit = d.getEntry(entry);
            // modifying Rows into modifiedRows
            modifyRows(modifiedUnit, indexes);

            // setting modifiedUnit in Dataset
            d.setEntry(entry, modifiedUnit);
        }
    }

    private static String[] modifyRows(String[] oldRows, int[] indexes) {
        String[] newRows = new String[indexes.length];
        int counter = 0;
        for (int i : indexes) {
            newRows[counter++] = oldRows[i];
        }
        return newRows;
    }

    private static void modifyRows(Unit u, int[] indexes) {
        NumberRow replacement = new NumberRow(indexes.length);

        for (int i : indexes) {
            replacement.add(u.getRow(i));
        }

        u.setData(replacement);
    }

    private static int[] mergeSort(int[] indexes, double[] criterium) {
        if (indexes.length == 2) {
            if (criterium[indexes[0]] > criterium[indexes[1]]) {
                return indexes;
            } else {
                int temp = indexes[0];
                indexes[0] = indexes[1];
                indexes[1] = temp;
                return indexes;
            }
        }

        if (indexes.length > 2) {
            int[] indexes1 = cut(indexes, 0, indexes.length / 2);
            int[] indexes2 = cut(indexes, indexes.length / 2, indexes.length);

            indexes1 = mergeSort(indexes1, criterium);
            indexes2 = mergeSort(indexes2, criterium);

            int i1 = 0; // index of first indexes array
            int i2 = 0; // index of second indexes array

            int counter = 0;

            for (int i = 0; i < indexes.length; i++) {
                indexes[i] = 0;
            }

            while (i1 + i2 < indexes.length) {
                if (i1 == indexes1.length) {
                    indexes[counter++] = indexes2[i2++];
                    continue;
                }
                if (i2 == indexes2.length) {
                    indexes[counter++] = indexes1[i1++];
                    continue;
                }

                if (criterium[indexes1[i1]] > criterium[indexes2[i2]]) {
                    //System.out.println("SELECTS I1 with "+i1);
                    indexes[counter++] = indexes1[i1++];
                } else {
                    //System.out.println("SELECTS I2 with "+i2);
                    indexes[counter++] = indexes2[i2++];
                }
            }
        }

        return indexes;
    }

    private static int[] cut(int[] arr, int start, int end) {
        int[] small = new int[end - start];
        int counter = 0;

        for (int i = start; i < end; i++) {
            small[counter++] = arr[i];
        }

        return small;
    }
}
