package Assignment6.Distance;

import Assignment6.Unit;

/**
 * Created by Arthur on 3-6-14.
 */
public class Pearson implements DistanceMeasure {
    @Override
    public double calculateDistance(Unit unit1, Unit unit2) {
        double averageUnit1 = getAverageOfRows(unit1);
        double averageUnit2 = getAverageOfRows(unit2);

        double standardDeviationUnit1 = getStandardDeviation(unit1, averageUnit1);
        double standardDeviationUnit2 = getStandardDeviation(unit2, averageUnit2);

        double sumDiff = 0;

        for (int i = 0; i < unit1.getSize(); i++) {
            sumDiff +=
                    ((unit1.getRow(i) - averageUnit1) / standardDeviationUnit1) *
                    ((unit2.getRow(i) - averageUnit2) / standardDeviationUnit2);
        }

        double pearsonCorrelation = sumDiff / (unit1.getSize() - 1);

        return Math.abs(1 - pearsonCorrelation);
    }

    private double getStandardDeviation(Unit u, double averageX) {
        double sumDifference = 0;
        double standardDev;

        for (int row = 0; row < u.getSize(); row++) {
            // sumDifference += (Xi - Xaverage)^2;
            sumDifference += Math.pow(u.getRow(row) - averageX, 2);
        }

        standardDev = Math.sqrt(sumDifference / (u.getSize() - 1));

        return standardDev;
    }

    private double getAverageOfRows(Unit u) {
        double sum = 0;

        for (int row = 0; row < u.getSize(); row++) {
            sum += u.getRow(row);
        }

        return sum / u.getSize();
    }
}
