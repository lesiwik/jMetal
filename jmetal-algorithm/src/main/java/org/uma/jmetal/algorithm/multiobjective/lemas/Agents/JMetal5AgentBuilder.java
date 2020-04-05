package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.Utils.ReproCondition;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.*;
import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.JMetal5BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.ThreadSafeAreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;

/**
 * Classic implementation of builder for {@link JMetal5Agent}, {@link JMetal5GlobalRankAgent} and {@link JMetal5ProgressiveAgent}.
 * Provides 3 different builds:
 * 1. Classic building method that chains functions.
 * 2. Spin on building method that provides parameters in build function.
 * 3. Build specifically made for copy of an Agents. Supposed to be used without any chaining functions and with combination of {@link org.uma.jmetal.algorithm.multiobjective.lemas.Utils.DeepCopier}.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public final class JMetal5AgentBuilder<S extends Solution<?>> {

    /* Utils */
    private JMetal5BaseEMAS<S> EMAS;

    /* Problem */
    private S genotype;

    /* Variables */
    private boolean allowKnowledgeExchange = false;
    private int currentIsland = 0;
    private double initialResourcesValue;
    private String type;
    private String comparatorType;
    private String parentToChildComparator;

    /* Operators */
    private CrossoverOperator<S> crossoverOperator;
    private MutationOperator<S> mutationOperator;
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

    public JMetal5AgentBuilder<S> withEMAS(JMetal5BaseEMAS<S> EMAS) {
        this.EMAS = EMAS;
        return this;
    }


    public JMetal5AgentBuilder<S> withCrossover(CrossoverOperator<S> crossover) {
        this.crossoverOperator = crossover;
        return this;
    }

    public JMetal5AgentBuilder<S> withMutation(MutationOperator<S> mutationOperator) {
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

    public JMetal5AgentBuilder<S> withInitialResourcesValue(double initialResourcesValue) {
        this.initialResourcesValue = initialResourcesValue;
        return this;
    }

    public JMetal5AgentBuilder<S> withKnowledgeExchange(boolean allow) {
        this.allowKnowledgeExchange = allow;
        return this;
    }

    public JMetal5AgentBuilder<S> withComparator(String comparatorType)
    {
        this.comparatorType = comparatorType;
        return this;
    }

    public JMetal5AgentBuilder<S> withParentToChildComparator(String comparatorType)
    {
        this.parentToChildComparator = comparatorType;
        return this;
    }

    public JMetal5Agent<S> build() {
        JMetal5Agent<S> agent;
        switch (Optional.ofNullable(type).orElse("")) {
            default:
            case Constants.BASE_AGENT:
                agent = new JMetal5Agent<>();
                break;
            case Constants.GLOBAL_RANK_AGENT:
                agent = new JMetal5GlobalRankAgent<>();
                break;
            case Constants.MEETING_AGENT:
                agent = new JMetal5MeetingAgent<>();
                break;
            case Constants.QUALITY_AGENT:
                agent = new JMetal5QualityAgent<>(EMAS.getCurrentQualityType());
                break;
            case Constants.PROGRESSIVE_AGENT:
                agent = new JMetal5ProgressiveAgent<>();
                break;
            case Constants.RADIUS_AGENT:
                agent = new JMetal5RadiusAgent<>(0);
                break;
        }
        agent.setGenotype(genotype);
        agent.setResourceLevel(initialResourcesValue);
        agent.setCrossoverOperator(crossoverOperator);
        agent.setMutationOperator(mutationOperator);
        agent.setEMAS(EMAS);
        //agent.addToAgentRecords();
        agent.setComparator(buildComparator(comparatorType));
        agent.setParentToChildComparator(buildComparator(parentToChildComparator));
        agent.setAllowKnowledgeExchange(allowKnowledgeExchange);
        return agent;
    }

    public JMetal5Agent<S> build(JMetal5BaseEMAS<S> EMAS,
                                 S genotype,
                                 String agentClassName,
                                 double initialResourceLevel,
                                 ReproCondition reproCondition) {
        JMetal5Agent<S> agent;
        switch (agentClassName) {
            default:
            case Constants.BASE_AGENT:
                agent = new JMetal5Agent<>();
                break;
            case Constants.GLOBAL_RANK_AGENT:
                agent = new JMetal5GlobalRankAgent<>();
                break;
            case Constants.PROGRESSIVE_AGENT:
                agent = new JMetal5ProgressiveAgent<>();
                break;
            case Constants.PARALLEL_AGENT:
                agent = new JMetal5ParallelAgent<>();
            case Constants.MEETING_AGENT:
                agent = new JMetal5MeetingAgent<>();
                break;
            case Constants.QUALITY_AGENT:
                agent = new JMetal5QualityAgent<>(EMAS.getCurrentQualityType());
                break;
            case Constants.RADIUS_AGENT:
                agent = new JMetal5RadiusAgent<>(EMAS.getRadiusToCheckMetAgentsIn());
                break;
        }
        agent.setReproCondition(reproCondition);
        agent.setGenotype(genotype);
        agent.setResourceLevel(initialResourceLevel);
        agent.setCrossoverOperator(EMAS.getCrossoverOperator());
        agent.setEMAS(EMAS);
        //agent.addToAgentRecords();
        agent.setComparator(buildComparator(EMAS.getBaseComparatorType()));
        agent.setParentToChildComparator(buildComparator(EMAS.getParentToChildComparatorType()));
        agent.setMutationOperator(EMAS.getMutationOperator());
        agent.setAllowKnowledgeExchange(EMAS.isKnowledgeExchangeAllowed());
        return agent;
    }

    public JMetal5Agent<S> buildForCopy(String agentClassName, int id) {
        JMetal5Agent<S> agent;
        switch (agentClassName) {
            default:
            case Constants.BASE_AGENT:
                agent = new JMetal5Agent<>(id);
                break;
            case Constants.GLOBAL_RANK_AGENT:
                agent = new JMetal5GlobalRankAgent<>(id);
                break;
            case Constants.PROGRESSIVE_AGENT:
                agent = new JMetal5ProgressiveAgent<>(id);
                break;
        }
        return agent;
    }



    private EmasDominanceComparator<JMetal5Agent<?>> buildComparator(String comparatorType)
    {
        EmasDominanceComparator<JMetal5Agent<?>> comparator;
        switch(Optional.ofNullable(comparatorType).orElse(""))
        {
            case Constants.AREA_UNDER_CONTROL_COUNTER_COMPARATOR:
                comparator = new AreaUnderControlCounterComparator<>();
                break;
            case Constants.NOT_WORSE_COMPARATOR:
                comparator = new NotWorseComparator<>();
                break;
            case Constants.AREA_UNDER_CONTROL_COMPARATOR:
                comparator = new AreaUnderControlComparator<>();
                break;
            case Constants.THREAD_SAFE_AREA_UNDER_CONTROL_COMPARATOR:
                comparator = new ThreadSafeAreaUnderControlComparator<>();
                break;
            default:
            case Constants.EMAS_DOMINANCE_COMPARATOR:
                comparator = new EmasDominanceComparator<>();
                break;
        }

        return comparator;
    }

}