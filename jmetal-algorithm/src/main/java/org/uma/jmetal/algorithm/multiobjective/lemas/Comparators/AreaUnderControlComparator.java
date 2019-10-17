package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5AreaControlAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;


/**
 * Area under control comparator for agents in EMAS algorithms.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */

public class AreaUnderControlComparator<Agent extends JMetal5AreaControlAgent<?>> extends EmasDominanceComparator<Agent> {


    /**
     * Compares to given agent based. First it makes call to {@link EmasDominanceComparator#compare(JMetal5Agent, JMetal5Agent)}.
     * If result is 0 (both are equal) then it calls {@link AreaUnderControlComparator#isPartnerUnderControl(Agent, Agent)}.
     * If again both are equal, then it updates Non Dominated lists {@link JMetal5AreaControlAgent#updateListOfKnownNondominatedAgents(JMetal5AreaControlAgent)}.
     * @param agent1 agent to compare.
     * @param agent2 agent to compare.
     * @return result of comparison.
     * */
    @Override
    public int compare(Agent agent1, Agent agent2) {

        int isBetter = super.compare(agent1, agent2);
        if (isBetter == Constants.NEITHER_IS_BETTER) {
            isBetter = isPartnerUnderControl(agent1, agent2);
        }
        if(isBetter == Constants.NEITHER_IS_BETTER){
            //agent1.updateKnownNonDominatedObjectives(agent2); //TODO: Why only agent1? (10)
            agent1.updateListOfKnownNondominatedAgents((JMetal5AreaControlAgent) agent2);
        }
        return isBetter;
    }


    /**
     * Fetches {@link JMetal5AreaControlAgent#getListOfKnownNonDominatedAgents()} from agent1 and
     * then for every agent in that list, it compares them to agent2 using {@link EmasDominanceComparator}.
     * @param agent1 agent to fetch list from.
     * @param agent2 agent to compare agents from list to.
     * @return result of comparison.
     * */
    private int isPartnerUnderControl(Agent agent1, Agent agent2) {
        for (JMetal5AreaControlAgent agent:  agent1.getListOfKnownNonDominatedAgents()) {
            int isPartnerUnderControl = super.compare((Agent) agent, agent2);
            if (isPartnerUnderControl != Constants.NEITHER_IS_BETTER)
                return isPartnerUnderControl;
        }
        return Constants.NEITHER_IS_BETTER;
    }
}
