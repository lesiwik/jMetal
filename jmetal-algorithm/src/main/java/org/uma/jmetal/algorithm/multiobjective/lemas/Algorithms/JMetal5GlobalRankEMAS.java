package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Global Rank EMAS Algorithm. Main difference is that {@link JMetal5GlobalRankEMAS#getAgentType()}
 * returns different string thus the algorithm uses different type of Agents: {@link org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5GlobalRankAgent}.
 * @see org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5GlobalRankAgent
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public class JMetal5GlobalRankEMAS<S extends Solution<?>> extends JMetal5BaseEMAS {



    public JMetal5GlobalRankEMAS(String algorithmName, int replaceOnlyIfBetter,
                                 boolean allowKnowledgeExchange,
                                 EmasDominanceComparator comparator,
                                 EmasDominanceComparator parentToChildComparator) {
        super(algorithmName,replaceOnlyIfBetter,
                allowKnowledgeExchange,comparator, parentToChildComparator);
        agentType = "JMetal5GlobalRankAgent";
    }


    JMetal5GlobalRankEMAS(){
        super();
        agentType = "JMetal5GlobalRankAgent";
    }

    @Override
    public String getDescription() {
        return "JMetal5GlobalRankEMAS";
    }

    @Override
    public void setAgentType(String agentType) {
        this.agentType = Optional.ofNullable(agentType).orElse("JMetal5GlobalRankAgent");
    }
}
