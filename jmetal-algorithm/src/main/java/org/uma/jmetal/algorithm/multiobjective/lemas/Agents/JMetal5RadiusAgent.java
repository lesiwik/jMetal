package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.List;


/**
 * Specialized Agent class that calculates met agents within specified radius. It then uses said ratio (metInRadius/met) to determine which agent to promote.
 * @author M. Kasprzyk <michal0kasprzyk@gmail.com>
 * @since 17/02/2020
 * @param <S> is {@link Solution} where <?> can be <Double>, <Integer>, etc.
 * */
@Setter
@Getter
public class JMetal5RadiusAgent<S extends Solution<?>> extends JMetal5Agent<S>{

    private double radius;

    private int metAgents = 0;
    private int metAgentsInRadius = 0;

    public JMetal5RadiusAgent(final double radius)
    {
        this.radius = radius;
    }

    /**
     * In event that agent comparison comes out as NEITHER_IS_BETTER then the ratio of met agents will be used to determine which agent to promote.
     * @param transferResourceValue resource value to transfer to 'better' agent.
     * @param meetPopulation population from which meeting partner will be searched.
     * @see JMetal5RadiusAgent#updateMetAgents(JMetal5RadiusAgent)
     * @see JMetal5RadiusAgent#getMeetingRatio()
     * */
    @Override
    public int doMeeting(List<? extends JMetal5Agent<S>> meetPopulation,
                         double transferResourceValue) {
        JMetal5RadiusAgent<S> meetingPartner = (JMetal5RadiusAgent<S>) findMeetingPartner(meetPopulation);
        updateMetAgents(meetingPartner);

        int comparatorResult = compareAgents(this, meetingPartner);
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


    public double getMeetingRatio() { return (double) metAgentsInRadius/metAgents; }

    private void updateMetAgents(JMetal5RadiusAgent<S> metAgent)
    {
        metAgents++;
        metAgent.metAgents++;

        if(isAgentInRadius(metAgent))
            metAgentsInRadius++;

        if(metAgent.isAgentInRadius(this))
            metAgent.metAgentsInRadius++;
    }

    public boolean isAgentInRadius(JMetal5RadiusAgent<S> agent)
    {
        double centerX = getGenotype().getObjective(0);
        double centerY = getGenotype().getObjective(1);

        double x = agent.getGenotype().getObjective(0);
        double y = agent.getGenotype().getObjective(1);

        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) < Math.pow(radius, 2);
    }

    @Override
    public String getAgentType() {
        return Constants.RADIUS_AGENT;
    }
}
