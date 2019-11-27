package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;

public class AreaUnderControlExtendedComparator<Agent extends JMetal5Agent<?>> extends AreaUnderControlComparator<Agent> {

    @Override
    public int compare(Agent a1, Agent a2)
    {
        int areaControlComparisonResult = super.compare(a1, a2);
        //TODO: To be implemented
        return areaControlComparisonResult;
    }
}
