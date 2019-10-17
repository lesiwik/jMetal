package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Progressive EMAS Algorithm. Main difference is that {@link JMetal5ProgressiveEMAS#getAgentType()}
 * returns different string thus the algorithm uses different type of agents: {@link org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ProgressiveAgent}.
 * @see org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ProgressiveAgent
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public class JMetal5ProgressiveEMAS<S extends Solution<?>> extends JMetal5BaseEMAS {


    public JMetal5ProgressiveEMAS(String algorithmName,
                                  int whenAddOffspringToPopulation,
                                  boolean allowKnowledgeExchange,
                                  EmasDominanceComparator comparator,
                                  EmasDominanceComparator parentToChildComparator) {
        super(algorithmName,whenAddOffspringToPopulation, allowKnowledgeExchange,
                comparator, parentToChildComparator);
        agentType = "JMetal5ProgressiveAgent";
    }

    public JMetal5ProgressiveEMAS(Problem problem, String algorithmName,
                                  int whenAddOffspringToPopulation,
                                  boolean allowKnowledgeExchange,
                                  EmasDominanceComparator comparator,
                                  EmasDominanceComparator parentToChildComparator) {
        super(problem, algorithmName,whenAddOffspringToPopulation, allowKnowledgeExchange,
                comparator, parentToChildComparator);
        agentType = "JMetal5ProgressiveAgent";
    }

    JMetal5ProgressiveEMAS()
    {
        super();
        agentType = "JMetal5ProgressiveAgent";
    }

    @Override
    public String getDescription() {
        return "JMetal5ProgressiveEMAS";
    }

    @Override
    public void setAgentType(String agentType) {
        this.agentType = Optional.ofNullable(agentType).orElse("JMetal5ProgressiveAgent");
    }

}
