package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import lombok.Getter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.MeetingRoomEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.List;
import java.util.stream.Collectors;

public class JMetal5MeetingAgent<S extends Solution<?>> extends JMetal5Agent<S>{

    @Getter
    protected int dominationLevel;

    public JMetal5MeetingAgent() {
        this.dominationLevel = 0;
    }

    @Override
    public int doMeeting(List<? extends JMetal5Agent<S>> meetPopulation, double transferResourceValue) {
        JMetal5MeetingAgent<S> meetingPartner = (JMetal5MeetingAgent<S>) findMeetingPartner(meetPopulation);

        int comparatorResult = compareAgents(this, meetingPartner);

        if (comparatorResult == Constants.FIRST_IS_BETTER) {
            this.dominationLevel++;
            transferResourcesFrom(meetingPartner, transferResourceValue);
        } else if (comparatorResult == Constants.SECOND_IS_BETTER) {
            transferResourcesTo(meetingPartner, transferResourceValue);
            meetingPartner.dominationLevel++;
        }

        if (comparatorResult != Constants.NEITHER_IS_BETTER) {
            this.setMet(true);
            meetingPartner.setMet(true);
        }
        return comparatorResult;
    }

    @Override
    protected JMetal5Agent<S> findMeetingPartner(List<? extends JMetal5Agent<S>> meetPopulation) {

        int searchedDominationLevel = 0;
        while(searchedDominationLevel <= dominationLevel)
        {
            List<JMetal5MeetingAgent<S>> potentialMeetingAgents = getAgentsWithDominationLevel((List<? extends JMetal5MeetingAgent<S>>) meetPopulation, searchedDominationLevel);
            if(potentialMeetingAgents.size() > 0)
                return potentialMeetingAgents.get(random.nextInt(potentialMeetingAgents.size()));
            searchedDominationLevel++;
        }
        return this;
    }

    @Override
    protected JMetal5Agent<S> findMatingPartner(List<? extends JMetal5Agent<S>> matingPopulation) {
        MeetingRoomEMAS<S> meetingEMAS = (MeetingRoomEMAS<S>) getEMAS();

        int searchedDominationLevel = meetingEMAS.getMatingDifferenceThreshold() + dominationLevel;
        while(searchedDominationLevel >= dominationLevel)
        {
            List<JMetal5MeetingAgent<S>> potentialMeetingAgents = getAgentsWithDominationLevel((List<? extends JMetal5MeetingAgent<S>>) matingPopulation, searchedDominationLevel);
            if(potentialMeetingAgents.size() > 0)
                return potentialMeetingAgents.get(random.nextInt(potentialMeetingAgents.size()));
            searchedDominationLevel--;
        }
        return this;
    }

    private List<JMetal5MeetingAgent<S>> getAgentsWithDominationLevel(List<? extends JMetal5MeetingAgent<S>> agents, int searchedDominationLevel)
    {
        return agents.stream()
                .filter(a -> !a.isMet() && !a.equals(this))
                .filter(a -> a.getDominationLevel() == searchedDominationLevel)
                .collect(Collectors.toList());
    }

    @Override
    public String getAgentType() {
        return Constants.MEETING_AGENT;
    }
}
