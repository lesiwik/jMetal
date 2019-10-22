package org.uma.jmetal.algorithm.multiobjective.lemas.agents;

import org.uma.jmetal.algorithm.multiobjective.lemas.agents.utils.ReproCondition;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.EmasDominanceComparator;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Classic implementation of builder for {@link BaseAgent}, {@link GlobalRankAgent} and {@link ProgressiveAgent}.
 * Provides 3 different builds:
 * 1. Classic building method that chains functions.
 * 2. Spin on building method that provides parameters in build function.
 * 3. Build specifically made for copy of an Agents. Supposed to be used without any chaining functions and with combination of {@link org.uma.jmetal.algorithm.multiobjective.lemas.Utils.DeepCopier}.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public final class AgentBuilder<S extends Solution<?>> {

    /* utils */
    private BaseEMAS EMAS;

    /* Problem */
    private S genotype;

    /* Variables */
    private boolean allowKnowledgeExchange = false;
    private int currentIsland = 0;
    private double initialResourcesValue;
    private String type;

    /* Operators */
    private CrossoverOperator crossoverOperator;
    private MutationOperator mutationOperator;
    private EmasDominanceComparator comparator;
    private EmasDominanceComparator parentToChildComparator;
    private ReproCondition reproCondition;

    public AgentBuilder<S> withReproCondition(ReproCondition reproCondition)
    {
        this.reproCondition = reproCondition;
        return this;
    }

    public AgentBuilder<S> withGenotype(S genotype) {
        this.genotype = genotype;
        return this;
    }

    public AgentBuilder<S> withEMAS(BaseEMAS EMAS) {
        this.EMAS = EMAS;
        return this;
    }


    public AgentBuilder<S> withCrossover(CrossoverOperator crossover) {
        this.crossoverOperator = crossover;
        return this;
    }

    public AgentBuilder<S> withMutation(MutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
        return this;
    }


    public AgentBuilder<S> withAgentType(String type) {
        this.type = type;
        return this;
    }

    public AgentBuilder<S> withCurrentIsland(int currentIsland) {
        this.currentIsland = currentIsland;
        return this;
    }

    public AgentBuilder<S> withDominanceComparator(EmasDominanceComparator comparator) {
        this.comparator = comparator;
        return this;
    }

    public AgentBuilder<S> withParentToChildComparator(EmasDominanceComparator parentToChildComparator) {
        this.parentToChildComparator = parentToChildComparator;
        return this;
    }


    public AgentBuilder<S> withInitialResourcesValue(double initialResourcesValue) {
        this.initialResourcesValue = initialResourcesValue;
        return this;
    }

    public AgentBuilder<S> withKnowledgeExchange(boolean allow) {
        this.allowKnowledgeExchange = allow;
        return this;
    }

    @SuppressWarnings("unchecked")
    public BaseAgent<S> build() {
        BaseAgent<S> agent;
        switch (Optional.ofNullable(type).orElse("")) {
            default:
            case "BaseAgent":
                agent = new BaseAgent<>();
                break;
            case "GlobalRankAgent":
                agent = new GlobalRankAgent();
                break;
            case "AreaControlAgent":
                agent = new AreaControlAgent();
                break;
            case "ProgressiveAgent":
                agent = new ProgressiveAgent();
                break;
        }
        agent.setGenotype(genotype);
        agent.setResourceLevel(initialResourcesValue);
        agent.setCrossoverOperator(crossoverOperator);
        agent.setMutationOperator(mutationOperator);
        agent.setEMAS(EMAS);
        agent.addToAgentRecords();
        agent.setAllowKnowledgeExchange(allowKnowledgeExchange);
        return agent;
    }

    @SuppressWarnings("unchecked")
    public BaseAgent<S> build(BaseEMAS EMAS, S genotype, String agentClassName, double initialResourceLevel) {
        BaseAgent<S> agent;
        switch (agentClassName) {
            default:
            case "BaseAgent":
                agent = new BaseAgent<>();
                break;
            case "GlobalRankAgent":
                agent = new GlobalRankAgent<>();
                break;
            case "AreaControlAgent":
                agent = new AreaControlAgent<>();
                break;
            case "ProgressiveAgent":
                agent = new ProgressiveAgent<>();
                break;
        }
        agent.setReproCondition(Optional.ofNullable(reproCondition).orElse(agent.getReproCondition()));
        agent.setGenotype(genotype);
        agent.setResourceLevel(initialResourceLevel);
        agent.setCrossoverOperator(EMAS.getCrossoverOperator());
        agent.setEMAS(EMAS);
        agent.addToAgentRecords();
        agent.setMutationOperator(EMAS.getMutationOperator());
        agent.setAllowKnowledgeExchange(EMAS.isKnowledgeExchangeAllowed());
        return agent;
    }

    @SuppressWarnings("unchecked")
    public BaseAgent<S> buildForCopy(String agentClassName, int id) {
        BaseAgent<S> agent;
        switch (agentClassName) {
            default:
            case "BaseAgent":
                agent = new BaseAgent<>(id);
                break;
            case "GlobalRankAgent":
                agent = new GlobalRankAgent(id);
                break;
            case "AreaControlAgent":
                agent = new AreaControlAgent(id);
                break;
            case "ProgressiveAgent":
                agent = new ProgressiveAgent(id);
                break;
        }
        return agent;
    }

}