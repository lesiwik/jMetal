package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


/**
 * This is just a copy of {@link AreaUnderControlComparator}
 * The only difference is, each agent keeps list of copies of solutions instead of agents' references.
 * This allows to use/change the list, asynchronously to other agents.
 * */
@Setter
@Getter
public class ThreadSafeAreaUnderControlComparator<Agent extends JMetal5Agent<?>> extends EmasDominanceComparator<Agent> {



    protected List<Solution> listOfKnownNonDominatedAgents;
    private int agent1ToListComparisonResult;
    private int agent2ToListComparisonResult;

    public ThreadSafeAreaUnderControlComparator() { listOfKnownNonDominatedAgents = new CopyOnWriteArrayList<>(); }

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

    protected int isPartnerUnderControl(Agent agentToFetchList, Agent agentToCompareTo) {
        List<Solution> listOfKnownNonDominatedAgents = getListOfKnownNonDominatedAgents(agentToFetchList);
        for (Solution agent:  listOfKnownNonDominatedAgents) {
            int isPartnerUnderControl = super.compare(agent, agentToCompareTo.genotype);
            if (isPartnerUnderControl != Constants.NEITHER_IS_BETTER)
                return isPartnerUnderControl;
        }
        return Constants.NEITHER_IS_BETTER;
    }

    protected void updateListOfKnownNondominatedAgents(Agent thisAgent, Agent meetingPartner) {
        /* TODO: Tutaj trzeba by sie przejsc po liscie i sprawdzic czy ten dodawany nie dominuje ktoregos jesli dominuje to wywalic te dominowane,
        od razu sprawdzic czy nie jest przez ktoregos dominowany jesli jest to nie dodajemy */
        List<Solution> thisAgentList = getListOfKnownNonDominatedAgents(thisAgent);
        List<Solution> meetingPartnerList = getListOfKnownNonDominatedAgents(meetingPartner);

        if (!thisAgentList.contains(meetingPartner.genotype)) {
            if(checkIfAgentIsGoodEnough(thisAgentList, meetingPartner))
                thisAgentList.add(meetingPartner.genotype.copy());
        }

        if (!meetingPartnerList.contains(thisAgent.genotype)) {
            if(checkIfAgentIsGoodEnough(meetingPartnerList, thisAgent))
                meetingPartnerList.add(thisAgent.genotype.copy());
        }
    }

    private boolean checkIfAgentIsGoodEnough(List<Solution> listOfKnownNonDominatedAgents, Agent agentToAdd)
    {
        boolean isAgentDominated = listOfKnownNonDominatedAgents.stream().anyMatch(agent ->{
           int comparison_result =  super.compare(agent, agentToAdd.genotype);
           return comparison_result == Constants.FIRST_IS_BETTER;
        });

        if(isAgentDominated)
            return false;

        List<Solution> dominatedAgents = listOfKnownNonDominatedAgents.stream().filter(agent ->
        {
            int comparison_result = super.compare(agent, agentToAdd.genotype);
            return comparison_result == Constants.SECOND_IS_BETTER;
        }).collect(Collectors.toList());

        listOfKnownNonDominatedAgents.removeAll(dominatedAgents);

        return true;
    }

    @SuppressWarnings("unchecked")
    public List<Solution> getListOfKnownNonDominatedAgents(Agent agent)
    {
        ThreadSafeAreaUnderControlComparator<Agent> agentComparator = (ThreadSafeAreaUnderControlComparator) agent.getComparator();
        return agentComparator.getListOfKnownNonDominatedAgents();
    }

    public List<Solution> getListOfKnownNonDominatedAgents()
    {
        return listOfKnownNonDominatedAgents;
    }
}
