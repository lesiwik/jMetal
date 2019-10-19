package org.uma.jmetal.algorithm.multiobjective.lemas.algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.ProgressiveAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.EmasDominanceComparator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Progressive EMAS Algorithm. Main difference is that {@link ProgressiveEMAS#getAgentType()}
 * returns different string thus the algorithm uses different type of agents: {@link ProgressiveAgent}.
 * @see ProgressiveAgent
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public class ProgressiveEMAS<S extends Solution<?>> extends BaseEMAS {


    public ProgressiveEMAS(String algorithmName,
                           int whenAddOffspringToPopulation,
                           boolean allowKnowledgeExchange,
                           EmasDominanceComparator comparator,
                           EmasDominanceComparator parentToChildComparator) {
        super(algorithmName,whenAddOffspringToPopulation, allowKnowledgeExchange,
                comparator, parentToChildComparator);
        agentType = "ProgressiveAgent";
    }

    public ProgressiveEMAS(Problem problem, String algorithmName,
                           int whenAddOffspringToPopulation,
                           boolean allowKnowledgeExchange,
                           EmasDominanceComparator comparator,
                           EmasDominanceComparator parentToChildComparator) {
        super(problem, algorithmName,whenAddOffspringToPopulation, allowKnowledgeExchange,
                comparator, parentToChildComparator);
        agentType = "ProgressiveAgent";
    }

    public ProgressiveEMAS()
    {
        super();
        agentType = "ProgressiveAgent";
    }

    @Override
    public String getDescription() {
        return "ProgressiveEMAS";
    }

    @Override
    public void setAgentType(String agentType) {
        this.agentType = Optional.ofNullable(agentType).orElse("ProgressiveAgent");
    }

}
