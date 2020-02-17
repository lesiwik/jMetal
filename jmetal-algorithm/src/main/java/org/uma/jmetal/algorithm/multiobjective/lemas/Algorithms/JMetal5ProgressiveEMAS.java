package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Progressive EMAS Algorithm. Main difference is that {@link JMetal5ProgressiveEMAS#getAgentType()}
 * returns different string thus the algorithm uses different type of Agents: {@link org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ProgressiveAgent}.
 * @see org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ProgressiveAgent
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * TODO: Usunac ta klase.
 * */
public class JMetal5ProgressiveEMAS<S extends Solution<?>> extends JMetal5BaseEMAS<S> {


    public JMetal5ProgressiveEMAS(String algorithmName,
                                  int whenAddOffspringToPopulation,
                                  boolean allowKnowledgeExchange,
                                  String comparatorType,
                                  String parentToChildComparatorType) {
        super(algorithmName,whenAddOffspringToPopulation, allowKnowledgeExchange,
                comparatorType, parentToChildComparatorType);
        agentType = Constants.PROGRESSIVE_AGENT;
    }

    public JMetal5ProgressiveEMAS(Problem<S> problem, String algorithmName,
                                  int whenAddOffspringToPopulation,
                                  boolean allowKnowledgeExchange,
                                  EmasDominanceComparator comparator,
                                  EmasDominanceComparator parentToChildComparator) {
        super(problem, algorithmName,whenAddOffspringToPopulation, allowKnowledgeExchange,
                comparator, parentToChildComparator);
        agentType = Constants.PROGRESSIVE_AGENT;
    }

    JMetal5ProgressiveEMAS()
    {
        super();
        agentType = Constants.PROGRESSIVE_AGENT;
    }

    @Override
    public String getDescription() {
        return Constants.PROGRESSIVE_EMAS;
    }

    @Override
    public void setAgentType(String agentType) {
        this.agentType = Optional.ofNullable(agentType).orElse(Constants.PROGRESSIVE_AGENT);
    }

}
