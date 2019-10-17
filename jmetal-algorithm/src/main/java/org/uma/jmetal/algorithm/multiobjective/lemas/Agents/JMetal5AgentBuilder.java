package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.Utils.ReproCondition;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Classic implementation of builder for {@link JMetal5Agent}, {@link JMetal5GlobalRankAgent} and {@link JMetal5ProgressiveAgent}.
 * Provides 3 different builds:
 * 1. Classic building method that chains functions.
 * 2. Spin on building method that provides parameters in build function.
 * 3. Build specifically made for copy of an agents. Supposed to be used without any chaining functions and with combination of {@link org.uma.jmetal.algorithm.multiobjective.lemas.Utils.DeepCopier}.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public final class JMetal5AgentBuilder<S extends Solution<?>> {

    /* Utils */
    private JMetal5BaseEMAS EMAS;

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

    public JMetal5AgentBuilder<S> withReproCondition(ReproCondition reproCondition)
    {
        this.reproCondition = reproCondition;
        return this;
    }

    public JMetal5AgentBuilder<S> withGenotype(S genotype) {
        this.genotype = genotype;
        return this;
    }

    public JMetal5AgentBuilder<S> withEMAS(JMetal5BaseEMAS EMAS) {
        this.EMAS = EMAS;
        return this;
    }


    public JMetal5AgentBuilder<S> withCrossover(CrossoverOperator crossover) {
        this.crossoverOperator = crossover;
        return this;
    }

    public JMetal5AgentBuilder<S> withMutation(MutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
        return this;
    }


    public JMetal5AgentBuilder<S> withAgentType(String type) {
        this.type = type;
        return this;
    }

    public JMetal5AgentBuilder<S> withCurrentIsland(int currentIsland) {
        this.currentIsland = currentIsland;
        return this;
    }

    public JMetal5AgentBuilder<S> withDominanceComparator(EmasDominanceComparator comparator) {
        this.comparator = comparator;
        return this;
    }

    public JMetal5AgentBuilder<S> withParentToChildComparator(EmasDominanceComparator parentToChildComparator) {
        this.parentToChildComparator = parentToChildComparator;
        return this;
    }


    public JMetal5AgentBuilder<S> withInitialResourcesValue(double initialResourcesValue) {
        this.initialResourcesValue = initialResourcesValue;
        return this;
    }

    public JMetal5AgentBuilder<S> withKnowledgeExchange(boolean allow) {
        this.allowKnowledgeExchange = allow;
        return this;
    }

    @SuppressWarnings("unchecked")
    public JMetal5Agent<S> build() {
        JMetal5Agent<S> agent;
        switch (Optional.ofNullable(type).orElse("")) {
            default:
            case "JMetal5BaseAgent":
                agent = new JMetal5Agent<>();
                break;
            case "JMetal5GlobalRankAgent":
                agent = new JMetal5GlobalRankAgent();
                break;
            case "JMetal5AreaControlAgent":
                agent = new JMetal5AreaControlAgent();
                break;
            case "JMetal5ProgressiveAgent":
                agent = new JMetal5ProgressiveAgent();
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
    public JMetal5Agent<S> build(JMetal5BaseEMAS EMAS, S genotype, String agentClassName, double initialResourceLevel) {
        JMetal5Agent<S> agent;
        switch (agentClassName) {
            default:
            case "JMetal5BaseAgent":
                agent = new JMetal5Agent<>();
                break;
            case "JMetal5GlobalRankAgent":
                agent = new JMetal5GlobalRankAgent<>();
                break;
            case "JMetal5AreaControlAgent":
                agent = new JMetal5AreaControlAgent<>();
                break;
            case "JMetal5ProgressiveAgent":
                agent = new JMetal5ProgressiveAgent<>();
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
    public JMetal5Agent<S> buildForCopy(String agentClassName, int id) {
        JMetal5Agent<S> agent;
        switch (agentClassName) {
            default:
            case "JMetal5BaseAgent":
                agent = new JMetal5Agent<>(id);
                break;
            case "JMetal5GlobalRankAgent":
                agent = new JMetal5GlobalRankAgent(id);
                break;
            case "JMetal5AreaControlAgent":
                agent = new JMetal5AreaControlAgent(id);
                break;
            case "JMetal5ProgressiveAgent":
                agent = new JMetal5ProgressiveAgent(id);
                break;
        }
        return agent;
    }

}