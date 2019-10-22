package org.uma.jmetal.algorithm.multiobjective.lemas.agents;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.uma.jmetal.algorithm.multiobjective.lemas.agents.utils.ReproCondition;
import org.uma.jmetal.algorithm.multiobjective.lemas.algorithms.BaseEMAS;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.Constants;
import org.uma.jmetal.algorithm.multiobjective.lemas.utils.DeepCopier;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base Agent implementation that is used by EMAS algorithm. {@link BaseEMAS}
 * Each agent is Serializable as noted by {@link Serializable} interface as well as each agent must extend {@link Solution<S>}.
 * Contains necessary functions for Agents to reproduce, mutate and
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 9/02/2018
 * @param <S> is {@link Solution} where <?> can be <Double>, <Integer>, etc.
 * */
@Getter
@Setter
@EqualsAndHashCode
public class BaseAgent<S extends Solution<?>> implements Serializable {

    /**
     * Default boolean check if agent can reproduce. Can be set to other expression in derived classes by using {@link BaseAgent#setReproCondition(ReproCondition)};
     * @see ReproCondition
     * */
    private ReproCondition reproCondition = () -> getResourceLevel() >= Constants.REPRODUCTION_LEVEL_VALUE;

    /**
     * Used to store random seed for each different iteration which is also used as key.
     * */
    public transient static Map<Integer, Random> randomSave = new HashMap<>();

    /**
     * Used to store random seed of JMetal random implementation for each different iteration which is also used as key.
     * */
    public transient static Map<Integer, PseudoRandomGenerator> randomJMetalSave = new HashMap<>();

    /**
     * Id of a current agent. Used mainly to distinguish Agents between them.
     * */
    public int id;

    /**
     * Id counter for whole class.
     * */
    public static int idCount = 0;

    /**
     * Random instance for mutations and reproductions.
     * */
    private static Random random = new Random();


    /* Variables */
    /**
     * Boolean check to see if agent has already reproduced in that specific iteration.
     * */
    public boolean hasAlreadyReproduced;

    /**
     * Boolean check to see if agent has already met another agent in that specific iteration.
     * */
    private boolean met;

    /**
     * Boolean check to see if Agents are allowed to exchange knowledge between them when they meet. Set by EMAS algorithm when creating new Agents from builder.
     * */
    private boolean allowKnowledgeExchange;


    /**
     * Resources of each agent. Determines if agent is dead or not, or if hes still able to reproduce based on constants.
     * @see Constants#DEATH_LEVEL_VALUE
     * @see Constants#REPRODUCTION_LEVEL_VALUE
     * @see Constants#PROGRESSIVE_REPRODUCTION_LEVEL_VALUE
     * */
    private double resourceLevel;

    /**
     * Represents genotype of each agent. You could say it represent what agent is made of.
     * Its what gets mutated when they reproduce, its what they use to solve presented problem.
     * */
    public S genotype;

    /* utils */
    /**
     * Reference for EMAS. Used to get main components of the algorithm such as operators and Constants.
     * */
    public transient BaseEMAS<S> EMAS;

    /* Operators */
    /**
     * Used to do crossover genotypes between 2 Agents when they reproduce. Usually set by EMAS algorithm.
     * */
    public transient CrossoverOperator<S> crossoverOperator;

    /**
     * Used to mutate agent.
     * */
    private MutationOperator<S> mutationOperator;

    public BaseAgent(){
        met = false;
        resourceLevel = 0;
        hasAlreadyReproduced = false;
        allowKnowledgeExchange = false;
        id = idCount++;
    }

    public BaseAgent(int id){
        met = false;
        resourceLevel = 0;
        hasAlreadyReproduced = false;
        allowKnowledgeExchange = false;
        this.id = id;
    }

    /**
     * Subtract value from {@link BaseAgent#resourceLevel}.
     * @param delta resources to subtract from {@link BaseAgent#resourceLevel}
     * */
    private void loseResources(double delta) {
        this.resourceLevel -= delta;
    }

    /**
     * Add value to {@link BaseAgent#resourceLevel}.
     * @param delta resources to add to {@link BaseAgent#resourceLevel}
     * */
    private void gainResources(double delta) {
        this.resourceLevel += delta;
    }

    /**
     * Mutate genotype of agent based on mutationOperator operator to execute genotype on.
     * */
    private void mutate() {
        mutationOperator.execute(genotype);
    }

    /**
     * Transfer of resources from 'this' agent to agent provided as argument.
     * @param agent Receiver of resources.
     * @param resourceDelta number of resources to send to.
     * */
    protected void transferResourcesTo(BaseAgent<S> agent, double resourceDelta) {
        agent.gainResources(resourceDelta);
        loseResources(resourceDelta);
    }

    /**
     * Transfer of resources from provided agent to 'this' agent.
     * @param agent Provider of resources.
     * @param resourceDelta number of resources to take.
     * */
    protected void transferResourcesFrom(BaseAgent<S> agent, double resourceDelta) {
        agent.loseResources(resourceDelta);
        gainResources(resourceDelta);
    }

    /**
     * Meets two agent from meetPopulation and transfers resources to whoever dominates. If neither agent dominate, resources don't get transferred.
     * @param transferResourceValue resource value to transfer to 'better' agent.
     * @param meetPopulation population from which meeting partner will be searched.
     * @see BaseAgent#findMeetingPartner(List)
     * */
    public int doMeeting(List<? extends BaseAgent<S>> meetPopulation,
                         double transferResourceValue) {
        BaseAgent<S> meetingPartner = findMeetingPartner(meetPopulation);

        int comparatorResult = EMAS.compareAgents(this, meetingPartner);

        if (comparatorResult == Constants.FIRST_IS_BETTER) {
            transferResourcesFrom(meetingPartner, transferResourceValue);
        } else if (comparatorResult == Constants.SECOND_IS_BETTER) {
            transferResourcesTo(meetingPartner, transferResourceValue);
        }

        if (comparatorResult != Constants.NEITHER_IS_BETTER) {
            this.setMet(true);
            meetingPartner.setMet(true);
        }
        return comparatorResult;
    }

    /**
     * Function to reproduce agents. It creates new agent with genotype that is based on crossover ({@link BaseAgent#crossoverOperator}) of its parents genotypes.
     * It creates either 1 or 2 new agents.
     * @param listOfAgentsWhoCanReproduce Is used as argument for findMatingPartner.
     * @return Returns list of new born agents.
     * @see BaseAgent#findMatingPartner(List)
     * @see BaseAgent#reproAct(int, List, List)
     * */
    public List<? extends BaseAgent<S>> doReproduce(List<? extends BaseAgent<S>> listOfAgentsWhoCanReproduce){

        AgentBuilder<S> builder = EMAS.getAgentBuilder();
        BaseAgent<S> matingPartner = findMatingPartner(listOfAgentsWhoCanReproduce);

        List<? extends BaseAgent<S>> parentList = Arrays.asList(this, matingPartner);

        List<S> argList = parentList.stream().map(a -> a.genotype).collect(Collectors.toList());

        List<S> off = crossoverOperator.execute(argList);

        List<? extends BaseAgent<S>> offspringList = off.stream().map(genotype ->
                builder.build(EMAS, genotype, getAgentType(), Constants.OFFSPRING_INITIAL_RESOURCE_VALUE))
                .collect(Collectors.toList());

        return (this.equals(matingPartner))
                ? reproAct(1, parentList, offspringList)
                : reproAct(2, parentList, offspringList);
    }

    /**
     * Checks to see if created are allowed to be in population.
     * @param numberOfChildren number of children to produce.
     * @param parentList list that contains parents.
     * @param offspringList previously created offspring's. They will be mutated and their problem evaluated.
     * @return list of offspring's that went through series of checks.
     * */
    public List<BaseAgent<S>> reproAct(int numberOfChildren,
                                       List<? extends BaseAgent<S>> parentList,
                                       List<? extends BaseAgent<S>> offspringList) {


        List<BaseAgent<S>> listOfOffspringToBeReturned = new ArrayList<>();

        for (int index = 0; index < numberOfChildren; index++) {
            BaseAgent<S> offSpring = offspringList.get(index);
            BaseAgent<S> parent = parentList.get(index);
            offSpring.mutate();
            offSpring.evaluate(EMAS.getProblem());

            int isOffspringBetter = 0;
            if (EMAS.getWhenAddOffspringToPopulation() != Constants.ALWAYS) {
                isOffspringBetter = EMAS.compareParentToOffspring(parent, offSpring);
            }


            boolean isOffspringGoodEnough = false;
            switch(EMAS.getWhenAddOffspringToPopulation())
            {

                case Constants.IF_NOT_WORSE:
                    if(isOffspringBetter == Constants.NEITHER_IS_BETTER || isOffspringBetter == Constants.IS_BETTER) {
                        isOffspringGoodEnough = true;
                    }
                    break;

                case Constants.IF_BETTER_AND_COULD_NOT_KNOW:
                case Constants.IF_BETTER:
                    if(isOffspringBetter == Constants.IS_BETTER)
                        isOffspringGoodEnough = true;
                    break;

                case Constants.ALWAYS:
                default:
                    isOffspringGoodEnough = true;
                    break;
            }

            if(isOffspringGoodEnough)
            {
                offSpring.transferResourcesFrom(parent, Constants.INITIAL_RESOURCE_VALUE);
                listOfOffspringToBeReturned.add(offSpring);
            }

            parent.hasAlreadyReproduced = true;
        }
        return listOfOffspringToBeReturned;
    }


    /**
     * Evaluates provided problem using {@link BaseAgent#genotype}. Afterwards updates {@link BaseEMAS#updateEvaluationsCounter()}.
     * @param problem problem to evaluate.
     * */
    public void evaluate(Problem<S> problem) {
        problem.evaluate(getGenotype());
        EMAS.updateEvaluationsCounter();
    }

    /**
     * Searched for mating partner from provided population. It checks which agent out of population are still alive and chooses one randomly.
     * @param matingPopulation from which to search mating partner.
     * @return returns found mating partner or 'this' if there isn't one.
     * */
    protected BaseAgent<S> findMatingPartner(List<? extends BaseAgent<S>> matingPopulation) {

        if (matingPopulation.size() == 1)
            return this;
        else {
            List<BaseAgent<S>> filtered = matingPopulation.stream()
                    .filter(a -> a.isAlive() && !a.equals(this))
                    .collect(Collectors.toList());

            if (filtered.size() == 0) return this;
            return filtered.get(random.nextInt(filtered.size()));
        }
    }

    /**
     * Filters provided population to check if the agent was already met or its not 'this'.
     * @param meetPopulation population from which to find meeting partner.
     * @return returns reference for meeting partner. If non are found, returns this.
     * */
    protected BaseAgent<S> findMeetingPartner(List<? extends BaseAgent<S>> meetPopulation) {
        List<BaseAgent<S>> meetingPartners = meetPopulation.stream()
                .filter(a -> !a.isMet() && !a.equals(this))
                .collect(Collectors.toList());

        return (meetingPartners.size() == 0) ?
                this : meetingPartners.get(random.nextInt(meetingPartners.size()));
    }

    /**
     * Checks to see if agent is alive.
     * @return {@link BaseAgent#resourceLevel} > {@link Constants#DEATH_LEVEL_VALUE} ({@value Constants#DEATH_LEVEL_VALUE}).
     * */
    public boolean isAlive() {
        return resourceLevel > Constants.DEATH_LEVEL_VALUE;
    }

    /**
     * Checks to see if agent can reproduce.
     * @return {@link BaseAgent#resourceLevel} >= {@link Constants#REPRODUCTION_LEVEL_VALUE} ({@value Constants#REPRODUCTION_LEVEL_VALUE}).
     * */
    public boolean canReproduce() {
        return reproCondition.canReproduce();
    }

    /**
     * Creates a deep copy of 'this' agent and returns it.
     * @see DeepCopier#copy(Object)
     * @return newly created and copied agent.
     * */
    @SuppressWarnings("unchecked")
    public BaseAgent<S> copy(){

        BaseAgent<S> c = getEMAS().getAgentBuilder().buildForCopy(this.getAgentType(), this.getId());

        c.resourceLevel = this.resourceLevel;
        c.genotype = (S) DeepCopier.copy(this.genotype);
        c.hasAlreadyReproduced = hasAlreadyReproduced;
        c.met = this.met;
        c.allowKnowledgeExchange = this.allowKnowledgeExchange;

        return c;
    }


    /**
     * Saves random seed with key provided as parameter that should correspond to appropriate iteration.
     * @param iteration key used to save iteration in {@link BaseAgent#randomSave} copy of {@link BaseAgent#random}.
     * */
    public static void saveRandom(int iteration){
        randomSave.put(iteration, (Random) DeepCopier.copy(random));
    }


    /**
     * Saves random seed of JMetalRandom with key provided as parameter that should correspond to appropriate iteration.
     * @param iteration key used to save iteration.
     * */
    public static void saveJMetalRandom(int iteration){
        randomJMetalSave.put(iteration, (PseudoRandomGenerator) DeepCopier.copy(JMetalRandom.getInstance().getRandomGenerator()));
    }

    private void meetingLog(String preamble, BaseAgent agent1, BaseAgent agent2, int dominance, int isBetter) {
        if (Constants.LOG_LEVEL == 2) {
            System.out.println(preamble);
            System.out.println(agent1.genotype.getObjective(0) + " " + agent1.genotype.getObjective(1) + " " + agent1.getResourceLevel());
            System.out.println(agent2.genotype.getObjective(0) + " " + agent2.genotype.getObjective(1) + " " + agent2.getResourceLevel());
            System.out.println("dominance result: " + dominance + ". Extended dominance result: " + isBetter);
        }
    }

    /**
     * Adds agents to {@link BaseEMAS#agentsRecords} if its not already there.
     * */
    public void addToAgentRecords()
    {
        if(!this.EMAS.agentsRecords.contains(this))
            this.EMAS.agentsRecords.add(this);
    }


    public String getAgentType() {
        return "BaseAgent";
    }

    public static void setRandom(Random rand) { random = rand; }

    @Override
    public String toString() {
        return  getAgentType() + "{" +
                "id=" + id +
                ", resourceLevel=" + resourceLevel +
                '}';
    }
}

