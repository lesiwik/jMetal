package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Area under control comparator for agents in EMAS algorithms.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
@Setter
@Getter
public class AreaUnderControlComparator<Agent extends JMetal5Agent<?>> extends EmasDominanceComparator<Agent> {



    protected List<Agent> listOfKnownNonDominatedAgents;
    private int agent1ToListComparisonResult;
    private int agent2ToListComparisonResult;

    public AreaUnderControlComparator() { listOfKnownNonDominatedAgents = new ArrayList<>(); }


    /**
     * Compares to given agent based. First it makes call to {@link EmasDominanceComparator#compare(JMetal5Agent, JMetal5Agent)}.
     * If result is 0 (both are equal) then it calls {@link AreaUnderControlComparator#isPartnerUnderControl(Agent, Agent)}.
     * If again both are equal, then it updates Non Dominated lists {@link #updateListOfKnownNondominatedAgents(Agent, Agent)}.
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

            agent2ToListComparisonResult = firstComparison;
            agent1ToListComparisonResult = secondComparison;
            if(firstComparison == secondComparison)
            {
                switch(firstComparison)
                {
                    case Constants.SECOND_IS_BETTER: //2-2
                    case Constants.NEITHER_IS_BETTER: //3-3
                        updateListOfKnownNondominatedAgents(agent1, agent2);
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
     * Fetches {@link #listOfKnownNonDominatedAgents} from agentToFetchList and
     * then for every agent in that list, it compares them to agentToCompareTo using {@link EmasDominanceComparator}.
     * @param agentToFetchList agent to fetch list from.
     * @param agentToCompareTo agent to compare agents from list to.
     * @return result of comparison.
     * */
    protected int isPartnerUnderControl(Agent agentToFetchList, Agent agentToCompareTo) {

        //TODO: A co w przypadku w którym w liscie jest [ niedominowany, dominowany, dominujacy] ?
        //TODO II: Wywalac przy okazji te dominowane raczej nie?
        //TODO III: Co zrobic z dominujacymi?

        List<Agent> listOfKnownNonDominatedAgents = getListOfKnownNonDominatedAgents(agentToFetchList);
        for (Agent agent:  listOfKnownNonDominatedAgents) {
            int isPartnerUnderControl = super.compare(agent, agentToCompareTo);
            if (isPartnerUnderControl != Constants.NEITHER_IS_BETTER)
                return isPartnerUnderControl;
        }
        return Constants.NEITHER_IS_BETTER;
    }



    /**
     * Updates {@link #listOfKnownNonDominatedAgents} by adding agent in parameter if its not present already.
     * @param meetingPartner agent to check.
     * */
    protected void updateListOfKnownNondominatedAgents(Agent thisAgent, Agent meetingPartner) {
        /* TODO: Tutaj trzeba by sie przejsc po liscie i sprawdzic czy ten dodawany nie dominuje ktoregos jesli dominuje to wywalic te dominowane,
        od razu sprawdzic czy nie jest przez ktoregos dominowany jesli jest to nie dodajemy */
        List<Agent> thisAgentList = getListOfKnownNonDominatedAgents(thisAgent);
        List<Agent> meetingPartnerList = getListOfKnownNonDominatedAgents(meetingPartner);

        if (!thisAgentList.contains(meetingPartner)) {
            thisAgentList.add(meetingPartner);
        }

        if (!meetingPartnerList.contains(thisAgent)) {
            meetingPartnerList.add(thisAgent);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Agent> getListOfKnownNonDominatedAgents(Agent agent)
    {
        AreaUnderControlComparator<Agent> agentComparator = (AreaUnderControlComparator) agent.getComparator();
        return agentComparator.getListOfKnownNonDominatedAgents();
    }

    public List<Agent> getListOfKnownNonDominatedAgents()
    {
        return listOfKnownNonDominatedAgents;
    }
}
