package org.uma.jmetal.algorithm.multiobjective.lemas.algorithms;

import org.uma.jmetal.algorithm.multiobjective.lemas.comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import java.util.Optional;


/**
 * Base EMAS Builder.
 * @author M. Kasprzyk michal0kasprzyk@gmail.com
 * @since 10/09/2019
 * @see BaseEMAS
 * @see GlobalRankEMAS
 * @see ProgressiveEMAS
 * */
public class EMASBuilder<S extends Solution<?>> {

    /* Operators */
    private CrossoverOperator<S> crossoverOperator;
    private MutationOperator<S> mutationOperator;
    private MutationOperator<S> strongMutationOperator;

    /* utils */
    private String agentType;
    private String algorithmName;
    private String emasType;
    private EmasDominanceComparator comparator;
    private EmasDominanceComparator parentToChildComparator;
    private boolean allowKnowledgeExchange;

    /* Constants */
    private Problem<S> problem;
    private int whenAddOffspringToPopulation;
    private int envEnergy;
    private int maxNumberOfIterations;
    private double initialAgentResourceLevel;
    private double transferAgentResourceLevel;


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
        this.problem = (Problem<S>) Constants.PROBLEM.getProblem();
    }

    @SuppressWarnings("unchecked")
    public BaseEMAS<S> build()
    {
        BaseEMAS<S> emas;
        switch (Optional.ofNullable(emasType).orElse("")) {
            default:
            case "Base":
                emas = new BaseEMAS<>();
                break;
            case "GlobalRank":
                emas = new GlobalRankEMAS<>();
                break;
            case "Progressive":
                emas = new ProgressiveEMAS<>();
                break;
        }
        setBaseFields(emas);
        return emas;
    }

    private void setBaseFields(BaseEMAS<S> emas)
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
        emas.setComparator(comparator);
        emas.setParentToChildComparator(parentToChildComparator);
        emas.setProblem(problem);
        emas.setAllowKnowledgeExchange(allowKnowledgeExchange);
        emas.setTransferAgentResourceLevel(transferAgentResourceLevel);
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

    public EMASBuilder<S> parentToChildComparator(EmasDominanceComparator parentToChildComparator)
    {
        this.parentToChildComparator = parentToChildComparator;
        return this;
    }

    public EMASBuilder<S> comparator(EmasDominanceComparator comparator)
    {
        this.comparator = comparator;
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

}
