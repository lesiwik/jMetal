package org.uma.jmetal.algorithm.multiobjective.EMAS.utils;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5AgentBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;

public class AgentUtils {

    private static final JMetal5AgentBuilder<PointSolution> agentPointSolBuilder = new JMetal5AgentBuilder<>();

    public static JMetal5Agent<PointSolution> createPointSolutionAgent(double[] objectivesValues, String comparatorType, String agentType){
        PointSolution solution = new PointSolution(objectivesValues.length);
        for(int i = 0 ; i < objectivesValues.length; i++)
        {
            solution.setObjective(i, objectivesValues[i]);
        }
        return agentPointSolBuilder.withGenotype(solution)
                .withComparator(comparatorType)
                .withMutation(Constants.NO_MUTATION_MOP)
                .withAgentType(agentType)
                .build();
    }


    public static JMetal5Agent<PointSolution> createPointSolutionAgent(double[] objectivesValues, String comparatorType, String agentType, String parentToChildComparator){
        PointSolution solution = new PointSolution(objectivesValues.length);
        for(int i = 0 ; i < objectivesValues.length; i++)
        {
            solution.setObjective(i, objectivesValues[i]);
        }
        return agentPointSolBuilder.withGenotype(solution)
                .withComparator(comparatorType)
                .withMutation(Constants.NO_MUTATION_MOP)
                .withParentToChildComparator(parentToChildComparator)
                .withAgentType(agentType)
                .build();
    }
}
