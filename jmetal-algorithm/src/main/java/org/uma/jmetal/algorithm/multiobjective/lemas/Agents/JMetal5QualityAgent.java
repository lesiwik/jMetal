package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;
import java.util.ArrayList;
import java.util.List;

import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.NEITHER_IS_BETTER;
import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.FIRST_IS_BETTER;
import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.SECOND_IS_BETTER;
import static org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants.QualityTypes;

@Getter
@Setter
public class JMetal5QualityAgent<S extends Solution<?>> extends JMetal5Agent<S> {

    private int totalMeetings = 0;
    private int dominatedMeetings = 0;
    private QualityTypes currentQualityType;

    private int differenceConstant;
    private double qualityThreshold;
    private double averageQuality;
    private double qualitySum;


    public JMetal5QualityAgent(QualityTypes selectedQualityType)
    {
        this.currentQualityType = selectedQualityType;
    }

    @Override
    public int doMeeting(List<? extends JMetal5Agent<S>> meetPopulation,
                         double transferResourceValue) {
        JMetal5QualityAgent<S> meetingPartner = (JMetal5QualityAgent<S>) findMeetingPartner(meetPopulation);
        meetingPartner.totalMeetings++;
        this.totalMeetings++;

        int comparatorResult = compareAgents(this, meetingPartner);
        if(comparatorResult != NEITHER_IS_BETTER) {
            comparatorResult = qualityCheck(this, meetingPartner, comparatorResult);
        }
        if (comparatorResult == FIRST_IS_BETTER) {
            transferResourcesFrom(meetingPartner, transferResourceValue);
            this.dominatedMeetings++;
        } else if (comparatorResult == SECOND_IS_BETTER) {
            transferResourcesTo(meetingPartner, transferResourceValue);
            meetingPartner.dominatedMeetings++;
        }

        if(this.currentQualityType == QualityTypes.AVERAGE)
            updateAverages(this, meetingPartner);

        if (comparatorResult != NEITHER_IS_BETTER) {
            this.setMet(true);
            meetingPartner.setMet(true);
        }
        return comparatorResult;
    }


    @Override
    public List<JMetal5Agent<S>> reproAct(int numberOfChildren,
                                          List<? extends JMetal5Agent<S>> parentList,
                                          List<? extends JMetal5Agent<S>> offspringList) {
        List<JMetal5Agent<S>> listOfOffspringToBeReturned = new ArrayList<>();

        for (int index = 0; index < numberOfChildren; index++) {
            JMetal5QualityAgent<S> offSpring = (JMetal5QualityAgent<S>) offspringList.get(index);
            JMetal5QualityAgent<S> parent    = (JMetal5QualityAgent<S>) parentList.get(index);
            offSpring.mutate();
            offSpring.evaluate(EMAS.getProblem());

            offSpring.transferResourcesFrom(parent, EMAS.getInitialAgentResourceLevel());
            offSpring.transferKnowledgeFrom(parent);
            listOfOffspringToBeReturned.add(offSpring);

            parent.hasAlreadyReproduced = true;
        }
        return listOfOffspringToBeReturned;
    }

    private void updateAverages(JMetal5QualityAgent<S> agent1, JMetal5QualityAgent<S> agent2)
    {
        agent1.qualitySum += agent2.getQualityRatio();
        agent1.averageQuality = agent1.qualitySum / agent1.totalMeetings;

        agent2.qualitySum += agent1.getQualityRatio();
        agent2.averageQuality = agent2.qualitySum / agent2.totalMeetings;
    }

    private void transferKnowledgeFrom(JMetal5QualityAgent<S> parent)
    {
        this.averageQuality = parent.getAverageQuality();
    }


    private int qualityCheck(JMetal5QualityAgent<S> agent1, JMetal5QualityAgent<S> agent2, int comparisonResult)
    {
        switch(currentQualityType)
        {
            case AVERAGE:
                comparisonResult = averageQuality(agent1, agent2, comparisonResult);
                break;
            case DIFFERENCE:
                comparisonResult = differenceQuality(agent1, agent2, comparisonResult);
                break;
            case ABOVE_CONSTANT:
                comparisonResult = constantQuality(agent1, agent2, comparisonResult);
                break;
            default:
                throw new IllegalArgumentException("Unsupported quality type");
        }
        return comparisonResult;
    }

    private int averageQuality(JMetal5QualityAgent<S> agent1, JMetal5QualityAgent<S> agent2, int comparisonResult)
    {
        if (comparisonResult == FIRST_IS_BETTER)
            return agent2.getQualityRatio() > agent2.averageQuality ? NEITHER_IS_BETTER : comparisonResult;
        else if (comparisonResult == SECOND_IS_BETTER)
            return agent1.getQualityRatio() > agent1.averageQuality ? NEITHER_IS_BETTER : comparisonResult;
        return comparisonResult;
    }

    private int differenceQuality(JMetal5QualityAgent<S> agent1, JMetal5QualityAgent<S> agent2, int comparisonResult)
    {
        double diff = Math.abs(agent1.getQualityRatio() - agent2.getQualityRatio());
        return diff < differenceConstant ? NEITHER_IS_BETTER : comparisonResult;
    }

    private int constantQuality(JMetal5QualityAgent<S> agent1, JMetal5QualityAgent<S> agent2, int comparisonResult)
    {
        if (comparisonResult == FIRST_IS_BETTER)
            return agent2.getQualityRatio() > qualityThreshold ? NEITHER_IS_BETTER : comparisonResult;
        else if (comparisonResult == SECOND_IS_BETTER)
            return agent1.getQualityRatio() > qualityThreshold ? NEITHER_IS_BETTER : comparisonResult;
        return comparisonResult;
    }

    @Override
    public String getAgentType() {
        return Constants.QUALITY_AGENT;
    }

    public double getQualityRatio(){ return (double) dominatedMeetings / totalMeetings;}
}
