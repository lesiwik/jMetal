package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JMetal5RadiusParallelAgent<S extends Solution<?>> extends JMetal5ParallelAgent<S>{


    private final double radius;

    private AtomicInteger metAgents = new AtomicInteger(0);
    private AtomicInteger metAgentsInRadius = new AtomicInteger(0);



    public JMetal5RadiusParallelAgent(final double radius){
        this.radius = radius;
    }

    @Override
    public int doMeeting(List<? extends JMetal5Agent<S>> meetPopulation,
                         double transferResourceValue) {
        JMetal5RadiusParallelAgent<S> meetingPartner = (JMetal5RadiusParallelAgent<S>) findMeetingPartner(meetPopulation);
        updateMetAgents(meetingPartner);

        int comparatorResult = compareAgents(this, meetingPartner);

        if((this.isAgentInRadius(meetingPartner) && meetingPartner.isAgentInRadius(this))
                && comparatorResult == Constants.NEITHER_IS_BETTER)
        {
            return Constants.NEITHER_IS_BETTER;
        }

        if (comparatorResult == Constants.NEITHER_IS_BETTER)
        {
            /* Promoting agent which has lower meetingRatio (and therefore is less crowded around him). */
            comparatorResult = Double.compare(getMeetingRatio(), meetingPartner.getMeetingRatio());
        }
        if (comparatorResult == Constants.FIRST_IS_BETTER) {
            transferResourcesFrom(meetingPartner, transferResourceValue);
        } else if (comparatorResult == Constants.SECOND_IS_BETTER) {
            transferResourcesTo(meetingPartner, transferResourceValue);
        }

        if (comparatorResult != Constants.NEITHER_IS_BETTER) {
            this.setMet(true);
            meetingPartner.setMet(true);
        }
        return comparatorResult;
    }


    public double getMeetingRatio() { return (double) metAgentsInRadius.get()/metAgents.get(); }

    private void updateMetAgents(JMetal5RadiusParallelAgent<S> metAgent)
    {
        metAgents.getAndIncrement();
        metAgent.metAgents.getAndIncrement();

        if(isAgentInRadius(metAgent))
            metAgentsInRadius.getAndIncrement();

        if(metAgent.isAgentInRadius(this))
            metAgent.metAgentsInRadius.getAndIncrement();
    }

    public boolean isAgentInRadius(JMetal5RadiusParallelAgent<S> agent)
    {
        double centerX = getGenotype().getObjective(0);
        double centerY = getGenotype().getObjective(1);

        double x = agent.getGenotype().getObjective(0);
        double y = agent.getGenotype().getObjective(1);

        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) < Math.pow(radius, 2);
    }
}
