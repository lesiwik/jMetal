package org.uma.jmetal.algorithm.multiobjective.lemas.comparators;/*
package org.uma.jmetal.algorithm.multiobjective.lemas.comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.BaseAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;


*/
/**
 * Comparator to compare area under control as well as distance to closest neighbour. Extends {@link AreaUnderControlComparator}.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * *//*

public class AreaUnderControlDistanceToClosesNeighbourComparator<S extends BaseAgent<?>> extends AreaUnderControlComparator<S> {


    */
/**
     * Compares two agents. Firstly it calls to super {@link AreaUnderControlComparator#compare(JMetal5Agent, JMetal5Agent)}.
     * Then if result is 0 (neither is better), it will compare their distances {@link JMetal5Agent#getDistanceToClosestNeighbour()}.
     * @param agent1 agent1 to compare to. Keep in mind this makes a difference in {@link AreaUnderControlComparator}.
     * @param agent2 agent2 to compare to.
     * @return result of comparison. If distances are equal returns 0, otherwise if agent1 distance is lower returns 1, if not -1.
     * *//*

    @Override
    public int compare(BaseAgent agent1, BaseAgent agent2) {
        int isBetter = super.compare(agent1,agent2);
        if (isBetter == Constants.NEITHER_IS_BETTER) {
            if (agent1.getDistanceToClosestNeighbour()==agent2.getDistanceToClosestNeighbour())
                return Constants.NEITHER_IS_BETTER;
            else
                return (agent1.getDistanceToClosestNeighbour() < agent2.getDistanceToClosestNeighbour()) ?  Constants.SECOND_IS_BETTER : Constants.FIRST_IS_BETTER;
        }

        return isBetter;
    }

}*/
