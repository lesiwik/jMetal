package org.uma.jmetal.algorithm.multiobjective.lemas.algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.GlobalRankAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.EmasDominanceComparator;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Global Rank EMAS Algorithm. Main difference is that {@link GlobalRankEMAS#getAgentType()}
 * returns different string thus the algorithm uses different type of agents: {@link GlobalRankAgent}.
 * @see GlobalRankAgent
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public class GlobalRankEMAS<S extends Solution<?>> extends BaseEMAS {



    public GlobalRankEMAS(String algorithmName, int replaceOnlyIfBetter,
                          boolean allowKnowledgeExchange,
                          EmasDominanceComparator comparator,
                          EmasDominanceComparator parentToChildComparator) {
        super(algorithmName,replaceOnlyIfBetter,
                allowKnowledgeExchange,comparator, parentToChildComparator);
        agentType = "GlobalRankAgent";
    }


    public GlobalRankEMAS(){
        super();
        agentType = "GlobalRankAgent";
    }

    @Override
    public String getDescription() {
        return "GlobalRankEMAS";
    }

    @Override
    public void setAgentType(String agentType) {
        this.agentType = Optional.ofNullable(agentType).orElse("GlobalRankAgent");
    }
}
