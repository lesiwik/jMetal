package org.uma.jmetal.algorithm.multiobjective.lemas.comparators;/*
package org.uma.jmetal.algorithm.multiobjective.lemas.comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.BaseAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;

import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants.*;

*/
/**
 * Parent to child area under control comparator with plain iterator.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * *//*

public class ParentToChildAreaUnderControlPlainIteratorComparator<S extends BaseAgent<?>> extends EmasDominanceComparator<S> {

    */
/**
     * Compares two solutions.
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     * non-dominated, or solution1  is dominated by solution2, respectively.
     *//*

    @Override
    public int compare(BaseAgent agent1, BaseAgent agent2) {
        int compResult = super.compare(agent1,agent2);

        if (compResult != NEITHER_IS_BETTER)
            return compResult;

        if(isPartnerUnderControl(agent1,agent2)==0){
            if(agent1.updateList==1) {
                agent1.updateKnownNonDominatedObjectives(agent2);
                agent1.updateListOfKnownNondominatedAgents(agent2);
            }
            return SECOND_IS_BETTER;
        }else{
            return FIRST_IS_BETTER;
        }
    }


    */
/**
     * Compares two solutions.
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     * non-dominated, or solution1  is dominated by solution2, respectively.
     *//*

    private int isPartnerUnderControl(BaseAgent agent1, BaseAgent agent2) {
        for (List<Double> l : (List<List<Double>>)agent1.getKnownNonDominatedObjectiveValues()) {
            int isPartnerUnderControl = 1;
            for (int i = 0; i < Constants.PROBLEM.getProblem().getNumberOfObjectives(); i++) {
                if (agent2.genotype.getObjective(i) <= l.get(i)) {   //FIXME: tu jest strzal do poprawy
                    isPartnerUnderControl = 0;
                }
            }
            if (isPartnerUnderControl == 1) {
                return 1;
            }
        }
        return 0;
    }

}*/
