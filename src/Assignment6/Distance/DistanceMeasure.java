package Assignment6.Distance;

import Assignment6.Unit;

public interface DistanceMeasure {

    /**
     * Calculates the Distance between two units.
     *
     * @param unit1 The first unit from which the Distance is calculated.
     * @param unit2 The second unit to which the Distance is calculated.
     * @return The Distance between unit1 and unit2.
     */
    double calculateDistance(Unit unit1, Unit unit2);

}
