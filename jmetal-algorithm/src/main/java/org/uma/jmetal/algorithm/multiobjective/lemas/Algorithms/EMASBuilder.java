package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.Utils.ReproCondition;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;


/**
 * Base EMAS Builder.
 * @author M. Kasprzyk michal0kasprzyk@gmail.com
 * @since 10/09/2019
 * @see JMetal5BaseEMAS
 * @see JMetal5GlobalRankEMAS
 * @see JMetal5ProgressiveEMAS
 * */
public class EMASBuilder<S extends Solution<?>> {

    /* Operators */
    private CrossoverOperator<S> crossoverOperator;
    private MutationOperator<S> mutationOperator;
    private MutationOperator<S> strongMutationOperator;

    /* Utils */
    private String agentType;
    private String algorithmName;
    private String emasType;
    private String comparatorType;
    private String parentToChildComparatorType;
    private boolean allowKnowledgeExchange;

    /* Constants */
    private Problem<S> problem;
    private int whenAddOffspringToPopulation;
    private int envEnergy;
    private int maxNumberOfIterations;
    private double initialAgentResourceLevel;
    private double transferAgentResourceLevel;
    private ReproCondition reproCondition;
    private double radiusToCheckMetAgentsIn;
    private Constants.QualityTypes currentQualityType;
    private Constants.StoppingConditions stoppingConditions;
    private double differenceConstant;
    private double qualityThreshold;
    private int matingDifferenceThreshold;


    @SuppressWarnings("unchecked")
    public EMASBuilder()
    {
        /* Operators */
        this.crossoverOperator = (CrossoverOperator<S>) Constants.XOP;
        this.mutationOperator = (MutationOperator<S>) Constants.MOP;
        this.strongMutationOperator = (MutationOperator<S>) Constants.STRONG_MOP;

        /* Constants */
        this.envEnergy = Constants.ENV_ENERGY;
        this.initialAgentResourceLevel = Constants.INITIAL_RESOURCE_VALUE;
        this.transferAgentResourceLevel = Constants.TRANSFER_RESOURCE_VALUE;
        this.maxNumberOfIterations = Constants.MAX_ITERATIONS;
        this.problem = (Problem<S>) Constants.PROBLEM;
        this.reproCondition = Constants.BASIC_REPRODUCTION_LEVEL;
    }

    @SuppressWarnings("unchecked")
    public JMetal5BaseEMAS<S> build()
    {
        JMetal5BaseEMAS<S> emas;
        switch (Optional.ofNullable(emasType).orElse("")) {
            default:
            case Constants.BASE_EMAS:
                emas = new JMetal5BaseEMAS<>();
                break;
            case Constants.GLOBAL_RANK_EMAS:
                emas = new JMetal5GlobalRankEMAS<>();
                break;
            case Constants.PROGRESSIVE_EMAS:
                emas = new JMetal5ProgressiveEMAS<>();
                break;
            case Constants.PARALLEL_EMAS:
                emas = new JMetal5ParallelEMAS2<>();
                break;
            case Constants.MEETING_EMAS:
                emas = new MeetingRoomEMAS<>(matingDifferenceThreshold);
                break;
        }
        setBaseFields(emas);
        return emas;
    }

    private void setBaseFields(JMetal5BaseEMAS<S> emas)
    {
        emas.setName(algorithmName);
        emas.setAgentType(agentType);
        emas.setEnvEnergy(envEnergy);
        emas.setInitialAgentResourceLevel(initialAgentResourceLevel);
        emas.setMaxNumberOfIterations(maxNumberOfIterations);
        emas.setWhenAddOffspringToPopulation(whenAddOffspringToPopulation);
        emas.setCrossoverOperator(crossoverOperator);
        emas.setMutationOperator(mutationOperator);
        emas.setStrongMutationOperator(strongMutationOperator);
        emas.setBaseComparatorType(comparatorType);
        emas.setParentToChildComparatorType(parentToChildComparatorType);
        emas.setProblem(problem);
        emas.setAllowKnowledgeExchange(allowKnowledgeExchange);
        emas.setTransferAgentResourceLevel(transferAgentResourceLevel);
        emas.setReproCondition(reproCondition);
        emas.setRadiusToCheckMetAgentsIn(radiusToCheckMetAgentsIn);
        emas.setQualityThreshold(qualityThreshold);
        emas.setDifferenceConstant(differenceConstant);
        emas.setCurrentQualityType(currentQualityType);
        emas.setStoppingConditions(Optional.ofNullable(stoppingConditions).orElse(Constants.StoppingConditions.ITERATIONS));
    }

    public EMASBuilder<S> matingDifferenceThreshold(int matingDifferenceThreshold)
    {
        this.matingDifferenceThreshold = matingDifferenceThreshold;
        return this;
    }

    public EMASBuilder<S> qualityType(Constants.QualityTypes qualityTypes)
    {
        this.currentQualityType = qualityTypes;
        return this;
    }

    public EMASBuilder<S> differenceConstant(double differenceConstant)
    {
        this.differenceConstant = differenceConstant;
        return this;
    }

    public EMASBuilder<S> qualityThreshold(double qualityThreshold)
    {
        this.qualityThreshold = qualityThreshold;
        return this;
    }

    public EMASBuilder<S> agentType(String agentType)
    {
        this.agentType = agentType;
        return this;
    }


    public EMASBuilder<S> emasType(String emasType)
    {
        this.emasType = emasType;
        return this;
    }

    public EMASBuilder<S> reproCondition(ReproCondition reproCondition)
    {
        this.reproCondition = reproCondition;
        return this;
    }

    public EMASBuilder<S> trasnferAgentResourceLevel(double transferAgentResourceLevel)
    {
        this.transferAgentResourceLevel = transferAgentResourceLevel;
        return this;
    }

    public EMASBuilder<S> strongMutationOperator(MutationOperator<S> strongMutationOperator)
    {
        this.strongMutationOperator = strongMutationOperator;
        return this;
    }

    public EMASBuilder<S> radiusToCheckMetAgentsIn(double radiusToCheckMetAgentsIn)
    {
        this.radiusToCheckMetAgentsIn = radiusToCheckMetAgentsIn;
        return this;
    }

    public EMASBuilder<S> mutationOperator(MutationOperator<S> mutationOperator)
    {
        this.mutationOperator = mutationOperator;
        return this;
    }

    public EMASBuilder<S> crossoverOperator(CrossoverOperator<S> crossoverOperator)
    {
        this.crossoverOperator = crossoverOperator;
        return this;
    }

    public EMASBuilder<S> parentToChildComparator(String parentToChildComparatorType)
    {
        this.parentToChildComparatorType = parentToChildComparatorType;
        return this;
    }

    public EMASBuilder<S> comparator(String comparatorType)
    {
        this.comparatorType = comparatorType;
        return this;
    }

    public EMASBuilder<S> problem(Problem<S> problem)
    {
        this.problem = problem;
        return this;
    }

    public EMASBuilder<S> envEnergy(int envEnergy)
    {
        this.envEnergy= envEnergy;
        return this;
    }

    public EMASBuilder<S> whenAddOffspringToPopulation(int whenAddOffspringToPopulation)
    {
        this.whenAddOffspringToPopulation = whenAddOffspringToPopulation;
        return this;
    }

    public EMASBuilder<S> initialAgentResourceLevel(double initialAgentResourceLevel)
    {
        this.initialAgentResourceLevel = initialAgentResourceLevel;
        return this;
    }

    public EMASBuilder<S> maxNumberOfIterations(int maxNumberOfIterations)
    {
        this.maxNumberOfIterations = maxNumberOfIterations;
        return this;
    }

    public EMASBuilder<S> algorithmName(String algorithmName)
    {
        this.algorithmName = algorithmName;
        return this;
    }

    public EMASBuilder<S> allowKnowledgeExchange(boolean allowKnowledgeExchange)
    {
        this.allowKnowledgeExchange = allowKnowledgeExchange;
        return this;
    }

    public EMASBuilder<S> stoppingCondition(Constants.StoppingConditions stoppingConditions)
    {
        this.stoppingConditions = stoppingConditions;
        return this;
    }

}
