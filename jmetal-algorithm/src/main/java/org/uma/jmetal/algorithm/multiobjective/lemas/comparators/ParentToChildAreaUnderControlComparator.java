package org.uma.jmetal.algorithm.multiobjective.lemas.comparators;/*
package org.uma.jmetal.algorithm.multiobjective.lemas.comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.BaseAgent;

import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants.FIRST_IS_BETTER;
import static org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants.NEITHER_IS_BETTER;
import static org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants.SECOND_IS_BETTER;

*/
/**
 * Parent to child area under control comparator.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * *//*

public class ParentToChildAreaUnderControlComparator<S extends BaseAgent<?>> extends EmasDominanceComparator<S> {

    */
/**
     * Compares two solutions.
     * @param agent1 Object representing the first <code>Solution</code>.
     * @param agent2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     * non-dominated, or solution1  is dominated by solution2, respectively.
     *//*

    @Override
    public int compare(BaseAgent agent1, BaseAgent agent2) {
        int compResult = super.compare(agent1,agent2);

        if (compResult != NEITHER_IS_BETTER)
            return compResult;

        if(isPartnerInsideDominationArea(agent1,agent2)==1){
            if(agent1.updateList==1) {
//                agent1.updateKnownNonDominatedObjectives(agent2);
//                agent1.updateListOfKnownNondominatedAgents(agent2);
            }
            return FIRST_IS_BETTER;
        }else{
            return SECOND_IS_BETTER;
        }
    }

    */
/**
     * Compares two solutions.
     * @param agent1 Object representing the first <code>Solution</code>.
     * @param agent2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     * non-dominated, or solution1  is dominated by solution2, respectively.
     *//*

    private int isPartnerInsideDominationArea(BaseAgent agent1, BaseAgent agent2) {
        EmasDominanceComparator comparator = new EmasDominanceComparator();
        for (Integer i: (List<Integer>)agent1.getListOfKnownNonDominatedAgentsIndexes()) {
            BaseAgent a = (BaseAgent) agent1.getEMAS().agentsRecords.get(i);
            if (comparator.compare(a,agent2) == FIRST_IS_BETTER){
                return SECOND_IS_BETTER;
            }
        }
        return NEITHER_IS_BETTER;
    }





}*/
