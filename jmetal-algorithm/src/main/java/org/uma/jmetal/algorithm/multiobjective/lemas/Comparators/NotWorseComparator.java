package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;

/**
 * Not worse comparator for EMAS. Used ONLY to compare parents to its offspring.
 * @author M. Kasprzyk <michal0kasprzyk@gmail.com>
 * @since 2/8/2020
 * */
public class NotWorseComparator<Agent extends JMetal5Agent<?>> extends EmasDominanceComparator<Agent> {

    private double b;

    @Override
    public int compare(Agent parent, Agent offspring) {
        int result = super.compare(parent, offspring);
        if (result == Constants.NEITHER_IS_BETTER)
        {
            result = notWorseTest(parent, offspring);
        }

        return result;
    }

    /**
     * Creates slope from parents genotype then checks if offspring genotype (x0, y0) are below said slope.
     * If they are, it means offspring is NOT_WORSE at very least but can also be BETTER.
     * Slope:
     *      a = -1.0
     *      b = sum(parent.getObjectives())
     *      y = ax + b
     * Comparison: y0 > a * x0 + b
     * @param parent its objectives are used to create slope. (x1, y1)
     * @param offspring its objectives are used to compare to slope. (x0, y0).
     * */
    private int notWorseTest(Agent parent, Agent offspring)
    {
        int result;
        for(int i = 0 ; i < parent.getGenotype().getNumberOfObjectives() ; i++)
        {
            b += parent.getGenotype().getObjective(i);
        }

        double x0 = offspring.getGenotype().getObjective(0);
        double y0 = offspring.getGenotype().getObjective(1);
        double functionValue = valueAt(x0);

        result = Double.compare(functionValue, y0);

        return result;
    }

    private double valueAt(double x){ return -x + b; }

}
