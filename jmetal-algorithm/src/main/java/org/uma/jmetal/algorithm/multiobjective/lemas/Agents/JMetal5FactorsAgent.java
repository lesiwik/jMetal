package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.List;

@Getter
@Setter
public class JMetal5FactorsAgent<S extends Solution<?>> extends JMetal5RadiusAgent<S>{

    private int dominations = 0; // how many times agent was dominated

    JMetal5FactorsAgent(final double radius) {
        super(radius);
    }

    @Override
    public int doMeeting(List<? extends JMetal5Agent<S>> meetPopulation,
                         double transferResourceValue) {
        JMetal5FactorsAgent<S> meetingPartner = (JMetal5FactorsAgent<S>) findMeetingPartner(meetPopulation);

        int comparatorResult = compareAgents(this, meetingPartner);

        if (comparatorResult == Constants.FIRST_IS_BETTER) {
            meetingPartner.dominations++;
            transferResourcesFrom(meetingPartner, transferResourceValue);
        } else if (comparatorResult == Constants.SECOND_IS_BETTER) {
            this.dominations++;
            transferResourcesTo(meetingPartner, transferResourceValue);
        }

        if (comparatorResult == Constants.NEITHER_IS_BETTER)
        {
            comparatorResult = secondaryCompare(meetingPartner);
        }

        updateMetAgents(meetingPartner);

        if (comparatorResult != Constants.NEITHER_IS_BETTER) {
            this.setMet(true);
            meetingPartner.setMet(true);
        }
        return comparatorResult;
    }

    @Override
    protected int secondaryCompare(JMetal5RadiusAgent<S> meetingPartner) {
        JMetal5FactorsAgent partner = (JMetal5FactorsAgent) meetingPartner;

        if (this.getFactor() < partner.getFactor()) {
            return Constants.FIRST_IS_BETTER;
        }
        if (this.getFactor() > partner.getFactor()) {
            return Constants.SECOND_IS_BETTER;
        }

        return super.secondaryCompare(meetingPartner);
    }

    public double getFactor() {
        if ( metAgents == 0 ) {
            return 1;
        }
        return dominations / (double) metAgents;
    }

    @Override
    public String getAgentType() {
        return Constants.FACTORS_AGENT;
    }

}
