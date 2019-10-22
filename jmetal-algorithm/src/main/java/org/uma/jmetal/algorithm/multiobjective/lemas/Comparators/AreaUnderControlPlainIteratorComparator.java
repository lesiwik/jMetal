package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;/*
package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;

import java.util.List;

*/
/**
 * Area under control comparator. Instead of using Agents, it uses their objective values.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * *//*

public class AreaUnderControlPlainIteratorComparator<S extends JMetal5Agent<?>> extends EmasDominanceComparator<S> {




    */
/**
     * Compares two agent to each other. First using super call which calls {@link EmasDominanceComparator#compare(JMetal5Agent, JMetal5Agent)},
     * then if neither agent is better it calls {@link AreaUnderControlPlainIteratorComparator#isPartnerUnderControl(JMetal5Agent, JMetal5Agent)}.
     * If again neither is better it updates lists of known non dominated objects and Agents in agent1 only.
     * @param agent1 agent to compare to.
     * @param agent2 agent to compare to.
     * @return result of comparison.
     * *//*

    @Override
    public int compare(JMetal5Agent agent1, JMetal5Agent agent2) {

        int isBetter = super.compare(agent1, agent2);
        if (isBetter == Constants.NEITHER_IS_BETTER) {
            isBetter = -isPartnerUnderControl(agent1, agent2); //TODO: Czemu negacja i dlaczego kolejnosc taka? (9)
            if (isBetter == Constants.NEITHER_IS_BETTER) {
                isBetter = isPartnerUnderControl(agent2, agent1);
            }
        }
        if(isBetter == Constants.NEITHER_IS_BETTER){
            agent1.updateKnownNonDominatedObjectives(agent2);
            agent1.updateListOfKnownNondominatedAgents(agent2);
        }
        return isBetter;
    }


    */
/**
     * Compares genotype objectives of agent2 to known non dominated objective values from agent1.
     * @param agent1 agent from which to pull known non dominated values.
     * @param agent2 agent to which compare known non dominated values.
     * @return result of comparison.
     * *//*

    private int isPartnerUnderControl(JMetal5Agent agent1, JMetal5Agent agent2) {
        for (List<Double> l : (List<List<Double>>) agent1.getKnownNonDominatedObjectiveValues()) {
            int isPartnerUnderControl = 1;
            for (int i = 0; i < Constants.PROBLEM.getProblem().getNumberOfObjectives(); i++) {
                if (agent2.genotype.getObjective(i) <= l.get(i)) {   //TODO: "FIX ME:tu jest strzal do poprawy" -> O co chodzi? (8)
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
