package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5AreaControlAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
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
            int firstComparison = isPartnerUnderControl(agent1, agent2);
            int secondComparison = isPartnerUnderControl(agent2, agent1);

            if(firstComparison == secondComparison)
            {
                switch(firstComparison)
                {
                    case Constants.SECOND_IS_BETTER: //2-2
                    case Constants.NEITHER_IS_BETTER: //3-3
                        agent1.updateListOfKnownNondominatedAgents((JMetal5AreaControlAgent) agent2);
                        agent2.updateListOfKnownNondominatedAgents((JMetal5AreaControlAgent) agent1);
                    case Constants.FIRST_IS_BETTER: //1-1
                        return Constants.NEITHER_IS_BETTER;
                }
            }
            //1-2  + 1-3
            else if(firstComparison == Constants.FIRST_IS_BETTER &&
                    (secondComparison == Constants.SECOND_IS_BETTER || secondComparison == Constants.NEITHER_IS_BETTER))
            {
                return Constants.FIRST_IS_BETTER;
            }
            //2-1 + 2-3
            else if(firstComparison == Constants.SECOND_IS_BETTER &&
                    (secondComparison == Constants.FIRST_IS_BETTER || secondComparison == Constants.NEITHER_IS_BETTER))
            {
                return Constants.SECOND_IS_BETTER;
            }
            //3-1
            else if(firstComparison == Constants.NEITHER_IS_BETTER &&
                    secondComparison == Constants.FIRST_IS_BETTER) {
                return Constants.SECOND_IS_BETTER;
            }
            //3-2
            else if(firstComparison == Constants.NEITHER_IS_BETTER &&
                    secondComparison == Constants.SECOND_IS_BETTER)
            {
                return Constants.FIRST_IS_BETTER;
            }

        }
        return isBetter;
    }

    /**
     * Fetches {@link JMetal5AreaControlAgent#getListOfKnownNonDominatedAgents()} from agent1 and
     * then for every agent in that list, it compares them to agent2 using {@link EmasDominanceComparator}.
     * @param agentToFetchList agent to fetch list from.
     * @param agentToCompareToList agent to compare agents from list to.
     * @return result of comparison.
     * */
    private int isPartnerUnderControl(Agent agentToFetchList, Agent agentToCompareToList) {

        //TODO: A co w przypadku w którym w liscie jest [ niedominowany, dominowany, dominujacy] ?
        //TODO II: Wywalac przy okazji te dominowane raczej nie?
        //TODO III: Co zrobic z dominujacymi?

        for (JMetal5AreaControlAgent agent:  agentToFetchList.getListOfKnownNonDominatedAgents()) {
            int isPartnerUnderControl = super.compare((Agent) agent, agentToCompareToList);
            if (isPartnerUnderControl != Constants.NEITHER_IS_BETTER)
                return isPartnerUnderControl;
        }
        return Constants.NEITHER_IS_BETTER;
    }
}
