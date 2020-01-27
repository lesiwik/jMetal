package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;


import lombok.*;
import org.uma.jmetal.algorithm.impl.AbstractEMASAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5AgentBuilder;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.Utils.ReproCondition;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.measure.Measurable;
import org.uma.jmetal.util.measure.MeasureManager;
import org.uma.jmetal.util.measure.impl.BasicMeasure;
import org.uma.jmetal.util.measure.impl.SimpleMeasureManager;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Base EMAS implementation derived from {@link AbstractEMASAlgorithm}.
 * Consists of all necessary components to construct complete algorithm and use it to produce result.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * @param <S> is {@link Solution<S>} where <S> can be <Double>, <Integer>, etc.
 * */
@Getter
@Setter
@EqualsAndHashCode(callSuper=false) /* Used due to lombok warning. */
public class JMetal5BaseEMAS<S extends Solution<?>> extends AbstractEMASAlgorithm<S, List<S>> implements Measurable{


    /**
     * Agent type used by builder to determine what kind of agent to build.
     * By default it is set to "JMetal5Base" but can be exchanged for something else. Be wary of it as it may break builder or EMAS should agent types be incompatible with each other.
     * @see JMetal5AgentBuilder
     * */
    protected String agentType = Constants.BASE_AGENT;

    /**
     * Max number of iterations.
     * Set to {@link Constants#MAX_ITERATIONS} = {@value Constants#MAX_ITERATIONS}
     * */
    private int maxNumberOfIterations;

    /**
     * Used to determine initial amount of agent where numberOfAgents = ( envEnergy / {@link JMetal5BaseEMAS#initialAgentResourceLevel } )
     * Set to {@link Constants#ENV_ENERGY} = {@value Constants#ENV_ENERGY}
     * */
    private int envEnergy;

    /**
     * Initial resources assigned to each agent upon his creation. See {@link JMetal5BaseEMAS#envEnergy} for more details.
     * Set to {@link Constants#INITIAL_RESOURCE_VALUE} = {@value Constants#INITIAL_RESOURCE_VALUE}
     * */
    private double initialAgentResourceLevel;

    /**
     * Initial resources assigned to each agent upon his conception by another agent.
     * Set to {@link Constants#OFFSPRING_INITIAL_RESOURCE_VALUE} = {@value Constants#OFFSPRING_INITIAL_RESOURCE_VALUE}
     * */
    private double initialOffspringResourceLevel;

    /**
     * Sets value of resource that is transferred to agent that "won" comparison showdown. Used in {@link JMetal5Agent#doMeeting(List, double)}.
     * Set to {@link Constants#TRANSFER_RESOURCE_VALUE} = {@value Constants#TRANSFER_RESOURCE_VALUE}
     * */
    private double transferAgentResourceLevel;

    /* Operators */
    /**
     * Reproduction condition set to all agents (initial ones and newly created). By default set to {@link Constants#BASIC_REPRODUCTION_LEVEL}.
     * */
    private ReproCondition reproCondition;

    /**
     * CrossoverOperators are used to combine the genetic information ({@link S}) of two parents ({@link JMetal5Agent<S>}) to generate new offspring.
     * Used in {@link JMetal5Agent#doReproduce(List)}
     * Set to {@link Constants#XOP} = {@link org.uma.jmetal.operator.crossover.impl.SBXCrossover} in constructor.
     * @see org.uma.jmetal.operator.crossover.impl.SBXCrossover
     * @see CrossoverOperator
     * */
    private CrossoverOperator<S> crossoverOperator;

    /**
     * MutationOperators are used to maintain genetic diversity from one generation of population to the next. It is analogous to biological mutation.
     * Used during reproduction of Agents.
     * Set to {@link Constants#MOP} = {@link org.uma.jmetal.operator.mutation.impl.PolynomialMutation}. With distribution index = 10.
     * @see MutationOperator<S>
     * @see org.uma.jmetal.operator.mutation.impl.PolynomialMutation
     * @see JMetal5Agent#reproAct(int, List, List)
     * */
    private MutationOperator<S> mutationOperator;

    /**
     * Strong MutationOperator. Analogous to mutationOperator, but with higher distribution index = 20.
     * Set to {@link Constants#STRONG_MOP} = {@link org.uma.jmetal.operator.mutation.impl.PolynomialMutation}
     * @deprecated Currently not in use.
     * @see JMetal5BaseEMAS#mutationOperator
     * */
    private MutationOperator<S> strongMutationOperator;

    /* Comparators */
    /**
     * Set as type of dominance comparator for each created agent. Every agent will have its own instance of said comparator created based on this string in {@link JMetal5AgentBuilder}.
     * Agents then use it to compare themselves to other Agents and determine stronger genes. By default set to {@link EmasDominanceComparator}
     * @see EmasDominanceComparator
     * @see JMetal5AgentBuilder
     * */
    private String baseComparatorType;

    /**
     * Set as type of dominance comparator for each created agent. Every agent will have its own Agents then use it to compare themselves to their parent Agents and determine stronger genes. See {@link JMetal5BaseEMAS#baseComparatorType} for more info.
     * @see EmasDominanceComparator
     * @see #baseComparatorType
     * */
    //TODO: Resolve issue of doubling list and its access in 'smarter' comparators.
    private String parentToChildComparatorType;

    /* Variables */
    private String algorithmName = "";

    /**
     * Number of iterations algorithm has already calculated.
     * */
    private int iterations;

    /**
     * Number of times Problem has been evaluated. Typically each agent evaluates once or twice per iteration.
     * */
    private int evaluations;

    /**
     * Meeting counter. Is a number of how many times meeting happened AND one of the Agents was better than the other.
     * Plotted on {@link org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.MeetingsChart}.
     * @see org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.MeetingsChart
     * */
    private int imBetterMeetingTypeCounter;

    /**
     * Meeting counter. Is a number of how many times meeting happened AND neither agent was better.
     * Plotted on {@link org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.MeetingsChart}.
     * @see org.uma.jmetal.algorithm.multiobjective.lemas.Visualization.MeetingsChart
     * */
    private int neitherIsBetterMeetingTypeCounter;

    /**
     * Hokus pokus.
     * @see Constants#ALWAYS
     * @see Constants#IF_BETTER
     * @see Constants#IF_BETTER_AND_COULD_NOT_KNOW
     * @see Constants#IF_NOT_WORSE
     * */
    private int whenAddOffspringToPopulation;

    /**
     * Boolean check to allow knowledge exchange between Agents.
     * */
    private boolean allowKnowledgeExchange;

    /* Utils */
    /**
     * Agent builder.
     * */
    private final JMetal5AgentBuilder<S> agentBuilder = new JMetal5AgentBuilder<>();

    /**
     * Used to save and load EMAS iterations. Stores as value FULL population of Agents as they were at the start of each iteration.
     * Key is a number of iteration to access. Saved ONLY when {@link JMetal5BaseEMAS#isDebugMode} is set to true.
     * */
    private TreeMap<Integer, ArrayList<JMetal5Agent<S>>> populationSavers;

    /**
     * Used to save all necessary variables from EMAS class for each iteration.
     * Saved ONLY when {@link JMetal5BaseEMAS#isDebugMode} is set to true.
     * */
    private List<JMetal5BaseEMASSaver> emasSavers;

    /**
     * Used to save seed of randomGenerator to fix randomness of EMAS algorithm during loading/saving chosen EMAS runs.
     * Saved ONLY when {@link JMetal5BaseEMAS#isDebugMode} is set to true. Stored in {@link JMetal5BaseEMAS#emasSavers}
     * */
    private PseudoRandomGenerator randomGeneratorSaver;

    /**
     * Boolean check of debugMode. Enable it to start saving chosen iterations.
     * */
    private boolean isDebugMode = false;

    /* Problem definition */
    /**
     * Problem for Agents to find solution to.
     * Example bench marks to see effectiveness of algorithm: {@link org.uma.jmetal.problem.multiobjective.zdt.ZDT1}, {@link org.uma.jmetal.problem.multiobjective.zdt.ZDT2}..
     * */
    private Problem<S> problem;

    /**
     * Used to manage collection, in EMAS case its the population.
     * @see SimpleMeasureManager
     * */
    private SimpleMeasureManager measureManager = new SimpleMeasureManager();

    /**
     * Used to represent measure that is pushed and stored in measureManager. Stores population.
     * @see BasicMeasure
     * */
    private BasicMeasure<Object> solutionListMeasure;

    /* Agent lists */
    /**
     * List of currently ALIVE Agents defined as Population. Set to NONE due to issues with lombok and hiding {@link AbstractEMASAlgorithm#setPopulation(List)}.
     * */
    protected List<JMetal5Agent<S>> population;

    /**
     * List of every agent that has ever lived (including {@link JMetal5BaseEMAS#population}).
     * TODO: Uzywac tego dalej czy nie? Moze wywalic
     * */
    public List<JMetal5Agent<S>> agentsRecords;

    /**
     * Builder constructor.
     * */
    protected JMetal5BaseEMAS()
    {
        /* Initialization */
        this.populationSavers = new TreeMap<>();
        this.emasSavers = new ArrayList<>();
        this.agentsRecords = new ArrayList<>();
        initMeasures();
    }


    @SuppressWarnings("unchecked")
    public JMetal5BaseEMAS(Problem problem, String algorithmName, int whenAddOffspringToPopulation,
                           boolean allowKnowledgeExchange,
                           EmasDominanceComparator meetingComparator,
                           EmasDominanceComparator parentChildComparator) {

        /* Operators */
        this.crossoverOperator = (CrossoverOperator<S>) Constants.XOP;
        this.mutationOperator = (MutationOperator<S>) Constants.MOP;
        this.strongMutationOperator = (MutationOperator<S>) Constants.STRONG_MOP;


        /* Constants */
        this.envEnergy = Constants.ENV_ENERGY;
        this.initialAgentResourceLevel = Constants.INITIAL_RESOURCE_VALUE;
        this.transferAgentResourceLevel = Constants.TRANSFER_RESOURCE_VALUE;
        this.maxNumberOfIterations = Constants.MAX_ITERATIONS;

        /* Constructor parameters */
        this.whenAddOffspringToPopulation = whenAddOffspringToPopulation;
        this.allowKnowledgeExchange = allowKnowledgeExchange;
        this.baseComparatorType = Constants.EMAS_DOMINANCE_COMPARATOR;
        this.parentToChildComparatorType = Constants.EMAS_DOMINANCE_COMPARATOR;
        this.problem = problem;
        this.setName(algorithmName);

        /* Initialization */
        this.populationSavers = new TreeMap<>();
        this.emasSavers = new ArrayList<>();
        this.agentsRecords = new ArrayList<>();
        initMeasures();
    }


    @SuppressWarnings("unchecked")
    public JMetal5BaseEMAS(String algorithmName, int whenAddOffspringToPopulation,
                           boolean allowKnowledgeExchange,
                           String meetingComparatorType,
                           String parentChildComparatorType) {

        /* Operators */
        this.problem = (Problem<S>) Constants.PROBLEM;
        this.crossoverOperator = (CrossoverOperator<S>) Constants.XOP;
        this.mutationOperator = (MutationOperator<S>) Constants.MOP;
        this.strongMutationOperator = (MutationOperator<S>) Constants.STRONG_MOP;

        /* Constants */
        this.envEnergy = Constants.ENV_ENERGY;
        this.initialAgentResourceLevel = Constants.INITIAL_RESOURCE_VALUE;
        this.transferAgentResourceLevel = Constants.TRANSFER_RESOURCE_VALUE;
        this.maxNumberOfIterations = Constants.MAX_ITERATIONS;

        /* Constructor Parameters */
        this.whenAddOffspringToPopulation = whenAddOffspringToPopulation;
        this.allowKnowledgeExchange = allowKnowledgeExchange;
        this.baseComparatorType = meetingComparatorType;
        this.parentToChildComparatorType = parentChildComparatorType;
        this.setName(algorithmName);

        /* Initialization */
        this.populationSavers = new TreeMap<>();
        this.emasSavers = new ArrayList<>();
        this.agentsRecords = new ArrayList<>();
        initMeasures();
    }

    /* Measures code */
    /**
     * Initializes measures.
     * @see JMetal5BaseEMAS#measureManager
     * @see JMetal5BaseEMAS#solutionListMeasure
     * */
    private void initMeasures() {
        solutionListMeasure = new BasicMeasure<>();
        measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
    }


    /**
     * Initializes all progress. Sets {@link JMetal5BaseEMAS#iterations} and {@link JMetal5BaseEMAS#evaluations} to 0.
     * */
    @Override
    protected void initProgress() {
        iterations = 0;
        evaluations = 0;
    }

    /**
     * Loads initial population from {@link JMetal5BaseEMAS#populationSavers} and sets JMetalRandom instance to {@link JMetal5BaseEMAS#randomGeneratorSaver}
     * @see JMetalRandom
     * */
    @Override
    protected void loadInitialPopulation() {
        int iteration = populationSavers.keySet().stream().min(Integer::compare).orElse(0);
        population = populationSavers.get(iteration);
        JMetalRandom.getInstance().setRandomGenerator(randomGeneratorSaver);
    }

    /**
     * Creates initial population of Agents (size is {@link JMetal5BaseEMAS#envEnergy}/{@link JMetal5BaseEMAS#initialAgentResourceLevel}).
     * That means it creates and initializes every agent as well as initializes their genotypes. After doing all that, it runs evaluate method of each created agent.
     * @see JMetal5Agent#evaluate(Problem)
     * */
    @Override
    protected void createInitialPopulation() {
        int numberOfAgents = (int) (envEnergy / initialAgentResourceLevel);
        population = Stream.generate(problem::createSolution)
                .limit(numberOfAgents)
                .map(genotype -> agentBuilder
                        .build(this, genotype, getAgentType(), initialAgentResourceLevel, reproCondition))
                .collect(Collectors.toList());
        population.forEach(agent -> agent.evaluate(problem));
    }

    /**
     * In this function there are two main steps done. Its purpose is to simulate Agents meeting.
     * 1. It resets all meeting statistics as well as sets {@link JMetal5Agent#isMet()} of all Agents to False.
     * 2. Then it runs through all of the Agents and makes them meet each other.
     * @see JMetal5Agent#doMeeting(List, double)
     * */
    @Override
    protected void meetStep(){
        populationLOG("----meetStep----");

        //resetMeetingStatistics();
        List<JMetal5Agent<S>> meetingAgents = population;
        meetingAgents.forEach(a -> a.setMet(false));

        for (JMetal5Agent<S> agent : meetingAgents) {
            if (!agent.isMet()) {
                int meetingResult = agent.doMeeting(meetingAgents,
                        transferAgentResourceLevel);
                updateMeetingStatistics(meetingResult);
            }
        }
    }

    /**
     * Step in algorithm that's responsible for Agents reproduction.
     * Firstly, it sets every Agents {@link JMetal5Agent#hasAlreadyReproduced} to false.
     * Then it runs over a list of Agents and makes 2 of them reproduce, making 1 or 2 additional Agents.
     * It checks which Agents can reproduce from {@link JMetal5BaseEMAS#getListOfAgentsWhoCanReproduce()}.
     * Newly created Agents ('offspring') are then added to population.
     * @see JMetal5Agent#doReproduce(List)
     * */
    @Override
    protected void reproStep() {
        populationLOG("----reproStep----");

        List<JMetal5Agent<S>> listOfAgentsWhoCanReproduce;
        List<JMetal5Agent<S>> offSpringList = new ArrayList<>();

        population.forEach(a -> a.hasAlreadyReproduced = false);

        while ((listOfAgentsWhoCanReproduce = getListOfAgentsWhoCanReproduce()).size() > 0) {
            offSpringList.addAll(listOfAgentsWhoCanReproduce.get(0)
                    .doReproduce(listOfAgentsWhoCanReproduce));
        }
        population.addAll(offSpringList);
    }


    /**
     * Step in algorithm which Agents that died (their resources reached 0 or below) are filtered out of population based on {@link JMetal5Agent#isAlive()}.
     * */
    @Override
    protected void deadStep() {
        population = population.stream().filter(JMetal5Agent::isAlive).collect(Collectors.toList());
    }

    /**
     * Checks if algorithm reached stopping condition.
     * @return {@link JMetal5BaseEMAS#iterations} >= {@link JMetal5BaseEMAS#maxNumberOfIterations}
     * */
    @Override
    protected boolean isStoppingConditionReached() {
        return evaluations >= 16000;//maxNumberOfIterations;
    }

    /**
     * Resets {@link JMetal5BaseEMAS#imBetterMeetingTypeCounter} and {@link JMetal5BaseEMAS#neitherIsBetterMeetingTypeCounter} to 0.
     * */
    private void resetMeetingStatistics() {
        this.imBetterMeetingTypeCounter = 0;
        this.neitherIsBetterMeetingTypeCounter = 0;
    }

    /**
     * Increments {@link JMetal5BaseEMAS#neitherIsBetterMeetingTypeCounter} or {@link JMetal5BaseEMAS#imBetterMeetingTypeCounter} depending on meeting result.
     * @param meetingResult meeting result of two Agents.
     * */
    private void updateMeetingStatistics(int meetingResult) {
        if (meetingResult == 0)
            neitherIsBetterMeetingTypeCounter++;
        else
            imBetterMeetingTypeCounter++;
    }

    /**
     * Counter for {@link JMetal5BaseEMAS#evaluations}.
     * */
    public void updateEvaluationsCounter() {
        this.evaluations++;
    }

    @Override
    protected void updateProgress(int iterations) {
        System.out.println( evaluations );
        this.iterations++;
        solutionListMeasure.push(getPopulation());
    }

    /**
     * Increments {@link JMetal5BaseEMAS#iterations} and pushes {@link JMetal5BaseEMAS#population} to {@link JMetal5BaseEMAS#solutionListMeasure}.
     * */
    @Override
    protected void updateProgress() {
        System.out.println( evaluations );
        iterations++;
        solutionListMeasure.push(getPopulation());
    }

    /**
     * Helper function for logging. Prints objective[0], objective[1] and resourceLevel.
     * @param preamble printed out before logs of Agents.
     * */
    private void populationLOG(String preamble) {
        if (Constants.LOG_LEVEL == 2) {
            System.out.println(preamble);
            for (JMetal5Agent a : population) {
                System.out.println(a.toString() + " " + a.genotype.getObjective(0) + " " + a.genotype.getObjective(1) + " " + a.getResourceLevel());
            }
        }
    }

    /**
     * Function to serialize and save {@link JMetal5BaseEMAS#populationSavers}. It creates folder in projects folder called "generated_genotype" and saves all chosen iterations there.
     * Important is that all iterations between startingIteration and prevIterationOffset need to be present in {@link JMetal5BaseEMAS#populationSavers}.
     * It ALWAYS saves backwards meaning startingIteration >= prevIterationOffset > 0.
     * @param startingIteration Iteration from which to start saving.
     * @param prevIterationOffset Iterations to end on.
     * @throws IOException When it cannot create '.ser' file of population or emasSaver it will throw.
     * */
    public void serializeGenotype(int startingIteration, int prevIterationOffset) throws IOException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("(dd-MM-yy_HH;mm)");
        String filePath = System.getProperty("user.dir") + "\\generated_genotypes\\" + formatter.format(new Date()) + "\\";

        File folders = new File(filePath);
        folders.mkdirs();

        String fileName = filePath + getName();

        for(int i = startingIteration; i != startingIteration - prevIterationOffset; i--) {

            try(FileOutputStream fileOutputStream = new FileOutputStream(fileName + "_iteration_" + i + ".ser");
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
                outputStream.writeObject(populationSavers.get(i));
            }
            try(FileOutputStream fileOutputStream = new FileOutputStream(fileName + "_EMAS" + "_iteration_" + i + ".ser");
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
                outputStream.writeObject(emasSavers.get(i));
            }
        }
        serializeRandomSeed(filePath);
    }

    /**
     * Saves 2 different random seeds (because they are both independently used) that are statically used in {@link JMetal5Agent}.
     * @see JMetal5Agent#randomSave
     * @see JMetal5Agent#randomJMetalSave
     * @param filePath path in which seeds will be stored.
     * @throws IOException If saving to '.ser' file is not successful.
     * */
    private void serializeRandomSeed(String filePath) throws IOException
    {
        String agentSeedFileName = filePath + "randomAgentSeed_" + getName();
        try(FileOutputStream fileOutputStream = new FileOutputStream(agentSeedFileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream))
        {
            outputStream.writeObject(JMetal5Agent.randomSave);
        }

        String jMetalSeedFileName = filePath + "randomJMetalSeed_" + getName();
        try(FileOutputStream fileOutputStream = new FileOutputStream(jMetalSeedFileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream))
        {
            outputStream.writeObject(JMetal5Agent.randomJMetalSave);
        }

    }

    /**
     * Saves chosen iteration. It creates deep copies of objects that are required to have exactly the same EMAS iteration.
     * That includes {@link JMetal5Agent#randomJMetalSave}, {@link JMetal5Agent#randomSave}, {@link JMetal5BaseEMASSaver} instance of copied variables from 'this' reference,
     * @param iteration chosen iteration to save.
     * */
    @Override
    protected void saveIteration(int iteration) {
        if(isDebugMode) {
            JMetal5Agent.saveRandom(iterations);
            JMetal5Agent.saveJMetalRandom(iterations);
            JMetal5BaseEMASSaver saver = new JMetal5BaseEMASSaver();
            saver.save(this);
            emasSavers.add(saver);

            ArrayList<JMetal5Agent<S>> currentIteration = new ArrayList<>();
            Iterator<JMetal5Agent<S>> iterator = population.iterator();

            while(iterator.hasNext()) {
                JMetal5Agent<S> a = iterator.next();
                JMetal5Agent<S> c = a.copy();
                currentIteration.add(c);
            }

            populationSavers.put(iterations, currentIteration);
        }
    }

    /**
     * Loads serialized RANDOM seed from a file. The file needs to have specific name convention (preferable same as from {@link JMetal5BaseEMAS#serializeRandomSeed(String)}.
     * @param iterationToSet iteration to which set to specific seed.
     * @param file file from which to load seed.
     * */
    @SuppressWarnings("unchecked")
    public void loadSerializedRandomSeed(File file, int iterationToSet)
    {
        try(FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            if(file.getName().contains("Agent"))
            {
                Map<Integer, Random> randomSave = (HashMap<Integer, Random>) objectInputStream.readObject();
                JMetal5Agent.setRandom(randomSave.get(iterationToSet));
            }
            else if(file.getName().contains("JMetal"))
            {
                Map<Integer, PseudoRandomGenerator> randomJMetalSave = (HashMap<Integer, PseudoRandomGenerator>) objectInputStream.readObject();
                randomGeneratorSaver = randomJMetalSave.get(iterationToSet);
            }

        }
        catch(Exception e)
        {
            System.err.println("Error loading serialized randomSeed to BaseEMAS: " + e);
            System.err.println("Random seed left as default");
        }
    }

    /**
     * Loads serialized EMAS (sets all variables to what they were).
     * @param file chosen serialized file with EMAS in it.
     * */
    public void loadSerializedEMAS(File file){
        try(FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            JMetal5BaseEMASSaver saver = (JMetal5BaseEMASSaver) objectInputStream.readObject();
            saver.load(this);
        }catch(Exception e)
        {
            System.err.println("Error loading serialized genotype to BaseEMAS " + e);
        }
    }

    /**
     * Loads serialized POPULATION. It then sets all the necessary fields of every loaded agent.
     * @param file chosen serialized file with agent population in it. It has to have iteration NUMBER in its name.
     * */
    @SuppressWarnings("unchecked")
    public void loadSerializedGenotype(File file)
    {
        int iteration = Integer.valueOf(file.getName().replaceAll("[A-Za-z_.]+", ""));
        try(FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){

            ArrayList<JMetal5Agent<S>> loadedGenotype = (ArrayList<JMetal5Agent<S>>) objectInputStream.readObject();
            if(loadedGenotype == null)
                throw new JMetalException("Loaded genotype was null!");

            for(JMetal5Agent<S> agent: loadedGenotype){
                agent.setEMAS(this);
                agent.addToAgentRecords();
                agent.setCrossoverOperator(this.getCrossoverOperator());
            }
            populationSavers.put(iteration, loadedGenotype);
        }catch(Exception e)
        {
            System.err.println("Error loading serialized genotype to BaseEMAS " + e);
        }
    }

    /**
     * Helper function to filter Agents that can reproduce out of population.
     * @return lists of Agents who can reproduce.
     * */
    private List<JMetal5Agent<S>> getListOfAgentsWhoCanReproduce() {
        return population.stream().filter(a -> a.canReproduce()
                && !a.hasAlreadyReproduced).collect(Collectors.toList());
    }

    /**
     * Debug functions.
     * @return returns sum of resources from population.
     * */
    public double getSumOfResources()
    {
        return population.stream().mapToDouble(JMetal5Agent::getResourceLevel).sum();
    }

    @Override
    public Problem<S> getProblem() {
        return problem;
    }

    @Override
    public List<S> getPopulation() {
        return population.stream().map(JMetal5Agent::getGenotype).collect(Collectors.toList());
    }

    /**
     * Setter to set each Agents GENOTYPE to the one provided in argument.
     * @param genotype list of genotypes to set to each agent.
     * */
    @Override
    public void setPopulation(List<S> genotype){
        if(this.population == null)
            createInitialPopulation();
        if(this.population.size() != genotype.size())
            throw new JMetalException("Genotype size doesn't match population size.\n" +
                    "Genotype size:" + genotype.size() +
                    "Population size:" + population.size());
        int genotypeIndex = 0;
        for(JMetal5Agent<S> agent: population)
        {
            agent.setGenotype(genotype.get(genotypeIndex++));
        }
    }

    @Override
    public int getIteration() {
        return iterations;
    }

    @Override
    public void setIteration(int iterations) { this.iterations = iterations; }

    @Override
    public List<S> getResult() {
        return getNonDominatedSolutions(getPopulation());
    }

    private List<S> getNonDominatedSolutions(List<S> solutionList) {
        return SolutionListUtils.getNonDominatedSolutions(solutionList);
    }

    @Override
    public String getName() {
        return this.algorithmName;
    }

    public void setName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    @Override
    public String getDescription() {
        return Constants.BASE_EMAS;
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measureManager;
    }

    public void setDebugMode(){ this.isDebugMode = true; }

    public boolean isKnowledgeExchangeAllowed(){ return allowKnowledgeExchange; }

    public void setAgentType(String agentType)
    {
        this.agentType = Optional.ofNullable(agentType).orElse(Constants.BASE_AGENT);
    }

    @Override
    public void run() {


            createInitialPopulation();
            initProgress();


        int lastTimeChecked = 0;
        while (!isStoppingConditionReached()) {
            saveIteration(getIteration());
            meetStep();
            reproStep();
            deadStep();
            if (evaluations > lastTimeChecked+100) {
                updateProgress();
                lastTimeChecked+=100;

                System.out.println(" [ Population size: " + getPopulation().size() + " || Iteration: " + getIteration() + " ||  Name: " + getName() + " ]");
            }
        }
/*           try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }*/

    }

}
