package org.uma.jmetal.algorithm.impl;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.JMetalLogger;

import java.util.List;

/**
 * This is abstract base class for EMAS algorithms. It contains necessary structure to uphold as well as all the crucial fields that combine into EMAS Algorithm.
 * Developed using JMetal 6.0. To see its full documentation head over to https://github.com/jMetal/jMetal.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * */
@SuppressWarnings("serial")
public abstract class AbstractEMASAlgorithm<Solution, Result> implements Algorithm<Result> {
    protected Problem<Solution> problem;
    private boolean isIterationPaused = false;
    private boolean isLoadedState = false;


    /**
     * Sets {@link AbstractEMASAlgorithm#isLoadedState} to true.
     * */
    public void setLoadedState(){ isLoadedState = true; }

    /**
     * Returns population of agents.
     * @return Population of agents.
     * */
    public abstract List<Solution> getPopulation();

    /**
     * Sets population of agents.
     * @param population Population to be set.
     * */
    public abstract void setPopulation(List<Solution> population);

    /**
     * Sets current problem to solve.
     * @param problem Sets problem to solve.
     * */
    public void setProblem(Problem<Solution> problem) {
        this.problem = problem;
    }

    /**
     * Returns problem currently set problem.
     * @return Currently set problem to be solved by algorithm.
     * */
    public Problem<Solution> getProblem() {
        return problem;
    }

    /**
     * To return current iteration.
     * @return Current iteration of algorithm.
     * */
    public abstract int getIteration();

    /**
     * Sets current iteration.
     * @param iteration iteration to be set.
     * */
    public abstract void setIteration(int iteration);

    /**
     * Checks if iterations of algorithm are paused.
     * */
    private boolean isIterationPaused(){ return isIterationPaused; }


    /**
     * Pauses iterations of the algorithm.
     * */
    public void pauseIteration() { isIterationPaused = !isIterationPaused; }

    /**
     * Checks if the algorithm state was loaded. (Population, iteration etc.)
     * */
    private boolean isLoadedState() { return isLoadedState; }

    /**
     * Method of initializing anything that needs it before running main loop of algorithm. Called exactly once.
     * */
    protected abstract void initProgress();

    /**
     * Method of updating progress. Is called after every iteration of the algorithm.
     * */
    protected abstract void updateProgress();

    /**
     * Method of updating progress. Is called after every iteration of the algorithm.
     * @param iteration to update on.
     * */
    protected abstract void updateProgress(int iteration);

    /**
     * Method of saving iteration. It is called as a first function inside the main loop because it is supposed to also save iteration that has been generated through {@link #createInitialPopulation()} method, unless its paused.
     * @param iteration chosen iteration to save.
     * */
    protected abstract void saveIteration(int iteration);

    /**
     * Boolean check of main loop of the algorithm.
     * */
    protected abstract boolean isStoppingConditionReached();

    /**
     * @return returns result of algorithm.
     * */
    @Override
    public abstract Result getResult();

    /**
     * Second function called in main loop right after {@link #saveIteration(int)}. Its called exactly once every loop iteration, unless its paused. Supposed to be used as a step that makes agents meet each other to reproduce.
     * */
    protected abstract void meetStep();

    /**
     * Third function called in main loop. Its called exactly once every loop iteration, unless its paused. Supposed to be used as a step in which agents reproduce and form new generation.
     * */
    protected abstract void reproStep();

    /**
     * Fourth function called in main loop. Its called exactly once very loop iteration, unless its paused. Supposed to be used as a step in which agents that die get cleaned up or method in which agents die.
     * */
    protected abstract void deadStep();

    /**
     * Creates initial {@link #getPopulation()} of agents.
     * */
    protected abstract void createInitialPopulation();

    /**
     * Loads initial population only when {@link #isLoadedState()} returns True.
     * */
    protected abstract void loadInitialPopulation();

    /**
     * Core of the algorithm. Is responsible for creation of population as well as main loop.
     * */
    @Override
    public void run() {

        if(isLoadedState())
            loadInitialPopulation();
        else {
            createInitialPopulation();
            initProgress();
        }


        while (!isStoppingConditionReached()) {
            if(!isIterationPaused()) {
                saveIteration(getIteration());
                meetStep();
                reproStep();
                deadStep();
                updateProgress();
                //System.out.println(" [ Population size: " + getPopulation().size() + " || Iteration: " + getIteration() + " ||  Name: " + getName() + " ]");
            }
//           try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) { }
        }
    }
}
