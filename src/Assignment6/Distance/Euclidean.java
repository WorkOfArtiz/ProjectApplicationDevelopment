package Assignment6.Distance;

import Assignment6.Unit;

/**
 * Created by Arthur on 3-6-14.
 */
public class Euclidean implements DistanceMeasure {

    @Override
    public double calculateDistance(Unit unit1, Unit unit2) {
        double distance = 0;
        double sum = 0;

        for (int i = 0; i < unit1.getSize(); i++) {
            sum += Math.pow(unit1.getRow(i) - unit2.getRow(i), 2);
        }
        distance = Math.sqrt(sum);

        return distance;
    }
}
