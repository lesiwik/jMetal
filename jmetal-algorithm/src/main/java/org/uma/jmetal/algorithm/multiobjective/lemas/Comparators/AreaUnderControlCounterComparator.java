package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;

import java.util.*;


/**
 * Area under control comparator with counting agents that got got dominated for agents in EMAS algorithms.
 * @author M. Kasprzyk <michal0kasprzyk@gmail.com>
 * @since 8/27/2018
 * */
@Setter
@Getter
public class AreaUnderControlCounterComparator<Agent extends JMetal5Agent<?>> extends EmasDominanceComparator<Agent> {



    protected List<Agent> listOfKnownNonDominatedAgents;
    private int agent1ToListComparisonResult;
    private int agent2ToListComparisonResult;

    private Map<Integer, List<Agent>> meetingResults;

    public AreaUnderControlCounterComparator() {
        listOfKnownNonDominatedAgents = new ArrayList<>();
        meetingResults = initializeMeetingMap();
    }


    /**
     * Compares to given agent based. First it makes call to {@link EmasDominanceComparator#compare(JMetal5Agent, JMetal5Agent)}.
     * If result is 0 (both are equal) then it calls {@link AreaUnderControlComparator#isPartnerUnderControl(Agent, Agent)}.
     * If again both are equal, then it updates Non Dominated lists {@link #updateListOfKnownNonDominatedAgents(Agent, Agent)}.
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
            Map<Integer, List<Agent>> agent1MeetingResults = getMeetingResults(agent1);
            Map<Integer, List<Agent>> agent2MeetingResults = getMeetingResults(agent2);

            /* Fetch only those who got dominated during meeting. */
            removeDominatedAgents(agent1, agent2MeetingResults.get(Constants.SECOND_IS_BETTER));
            removeDominatedAgents(agent2, agent1MeetingResults.get(Constants.SECOND_IS_BETTER));

            agent2ToListComparisonResult = firstComparison;
            agent1ToListComparisonResult = secondComparison;
            if(firstComparison == secondComparison)
            {
                switch(firstComparison)
                {
                    case Constants.SECOND_IS_BETTER: //2-2
                    case Constants.NEITHER_IS_BETTER: //3-3
                        updateListOfKnownNonDominatedAgents(agent1, agent2);
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

        /*
        * TODO: Co w przypadku w którym listy są równej wielkości?
        * TODO: Dopracować pomysł -> przypadki kiedy listy sa spotkan sa rowne, roznia sie ale nie sa niezerowe itd.
        * */

        List<Agent> listOfKnownNonDominatedAgents = getListOfKnownNonDominatedAgents(agentToFetchList);
        if( listOfKnownNonDominatedAgents.size() == 0 )
            return Constants.NEITHER_IS_BETTER;

        Map<Integer, List<Agent>> meetingMap = getMeetingResults(agentToCompareTo);
        meetingMap.values().forEach(List::clear);

        for (Agent agent:  listOfKnownNonDominatedAgents) {
            int isPartnerUnderControl = super.compare(agent, agentToCompareTo);
            meetingMap.get(isPartnerUnderControl).add(agent);
        }

        int currentMax = 0;
        int highestMeetingType = Constants.NEITHER_IS_BETTER;
        for (Map.Entry<Integer, List<Agent>> entry : meetingMap.entrySet())
        {
            if (entry.getValue().size() > currentMax) {
                currentMax = entry.getValue().size();
                highestMeetingType = entry.getKey();
            }
        }
        return highestMeetingType;
    }

    private Map<Integer, List<Agent>> initializeMeetingMap()
    {
        Map<Integer, List<Agent>> map = new HashMap<>();
        map.put(Constants.NEITHER_IS_BETTER, new ArrayList<>());
        map.put(Constants.FIRST_IS_BETTER, new ArrayList<>());
        map.put(Constants.SECOND_IS_BETTER, new ArrayList<>());
        return map;
    }


    private void removeDominatedAgents(Agent agent, List<Agent> dominatedAgents)
    {
        List<Agent> listOfKnownNonDominatedAgents = getListOfKnownNonDominatedAgents(agent);
        listOfKnownNonDominatedAgents.removeAll(dominatedAgents);
    }


    /**
     * Updates {@link #listOfKnownNonDominatedAgents} by adding agent in parameter if its not present already.
     * @param meetingPartner agent to check.
     * */
    protected void updateListOfKnownNonDominatedAgents(Agent thisAgent, Agent meetingPartner) {

        Set<Agent> union = new LinkedHashSet<>(getListOfKnownNonDominatedAgents(thisAgent));
        union.addAll(getListOfKnownNonDominatedAgents(meetingPartner));

        List<Agent> thisAgentList = new ArrayList<>(union);
        List<Agent> meetingPartnerList = new ArrayList<>(union);

        setListOfKnownNonDominatedAgents(meetingPartner, meetingPartnerList);
        setListOfKnownNonDominatedAgents(thisAgent, thisAgentList);

        if (!thisAgentList.contains(meetingPartner)) {
            if(checkIfAgentIsGoodEnough(thisAgentList, meetingPartner))
                thisAgentList.add(meetingPartner);
        }

        if (!meetingPartnerList.contains(thisAgent)) {
            if(checkIfAgentIsGoodEnough(meetingPartnerList, thisAgent))
                meetingPartnerList.add(thisAgent);
        }
    }


    /**
     * Checks if agent is non dominated by other agents in the list, so that he can  be added to listOfKnownNonDominatedAgents.
     * It also checks if agents in the list are not dominated by him, and if they are removes them.
     * @param agentToAdd agent to be compared to list.
     * @param listOfKnownNonDominatedAgents list to compare to.
     * */
    private boolean checkIfAgentIsGoodEnough(List<Agent> listOfKnownNonDominatedAgents, Agent agentToAdd)
    {
        return listOfKnownNonDominatedAgents.stream().anyMatch(agent ->{
            int comparison_result =  super.compare(agent, agentToAdd);
            return comparison_result != Constants.FIRST_IS_BETTER;
        });
    }


    public List<Agent> getListOfKnownNonDominatedAgents(Agent agent)
    {
        AreaUnderControlCounterComparator<Agent> agentComparator = (AreaUnderControlCounterComparator<Agent>) agent.getComparator();
        return agentComparator.getListOfKnownNonDominatedAgents();
    }


    public Map<Integer, List<Agent>> getMeetingResults(Agent agent)
    {
        AreaUnderControlCounterComparator<Agent> agentComparator = (AreaUnderControlCounterComparator<Agent>) agent.getComparator();
        return agentComparator.getMeetingResults();
    }

    public List<Agent> getListOfKnownNonDominatedAgents()
    {
        return listOfKnownNonDominatedAgents;
    }


    public void setListOfKnownNonDominatedAgents(Agent agent, List<Agent> nonDominatedList)
    {
        AreaUnderControlCounterComparator<Agent> agentComparator = (AreaUnderControlCounterComparator<Agent>) agent.getComparator();
        agentComparator.setListOfKnownNonDominatedAgents(nonDominatedList);
    }
}
