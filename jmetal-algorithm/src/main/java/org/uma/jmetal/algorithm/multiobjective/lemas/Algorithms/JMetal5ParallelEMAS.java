package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.queue.SynchronizedQueue;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.algorithm.impl.AbstractEMASAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionUtils;

public class JMetal5ParallelEMAS<S extends Solution<?>> extends JMetal5BaseEMAS<S> {
    private int meetStepsLeft = (int) (Constants.MAX_ITERATIONS * Constants.ENV_ENERGY
            / Constants.INITIAL_RESOURCE_VALUE);
    private final Lock meetableLock = new ReentrantLock();
    private final Lock mateableLock = new ReentrantLock();
    private final Lock stepsLock = new ReentrantLock();
    private final Lock populationLock = new ReentrantLock();
    private final List<JMetal5Agent<S>> meetable = new LinkedList<JMetal5Agent<S>>();
    private final List<JMetal5Agent<S>> mateable = new LinkedList<JMetal5Agent<S>>();
    public int threadsCount = 1;

    private final Random random = new Random();

    private static <T> Pair<T, T> getPairOfElemets(List<T> list, Lock listLock, Random random) {
        listLock.lock();
        int meetableCount = list.size();
        if (meetableCount < 2) {
            listLock.unlock();
            return null;
        }
        int randomElementIndex = random.nextInt(meetableCount);
        T first = list.get(randomElementIndex);
        list.remove(randomElementIndex);
        randomElementIndex = random.nextInt(meetableCount - 1);
        T second = list.get(randomElementIndex);
        list.remove(randomElementIndex);
        listLock.unlock();
        return new ImmutablePair<T, T>(first, second);
    }

    @Override
    public void reproStep() {
        Pair<JMetal5Agent<S>, JMetal5Agent<S>> agents = getPairOfElemets(mateable, mateableLock, random);
        if (agents == null)
            return;
        System.out.println("agents mateing "+ mateable.size());
        List<? extends JMetal5Agent<S>> newBorn = agents.getLeft().doReproduce(Arrays.asList(agents.getLeft(), agents.getRight()));
        populationLock.lock();
        population.addAll(newBorn);
        populationLock.unlock();
        for (JMetal5Agent<S> agent : newBorn) {
            AddMeetingAgent(agent);
        }
        AddMeetingAgent(agents.getLeft());
        AddMeetingAgent(agents.getRight());
    }

    @Override
    public void meetStep() {
        Pair<JMetal5Agent<S>, JMetal5Agent<S>> agents = getPairOfElemets(meetable, meetableLock, random);
        if (agents == null)
            return;
        int meetingResult;
        meetingResult = agents.getLeft().doMeeting(Arrays.asList(agents.getRight()), Constants.TRANSFER_RESOURCE_VALUE);
        updateMeetingStatistics(meetingResult);
        agents.getLeft().setMet(false);
        agents.getRight().setMet(false);
        if(meetingResult!=0)
            System.out.println("agents meeting " + meetable.size());
        if (agents.getLeft().canReproduce())
            AddMateingAgent(agents.getLeft());
        else if (agents.getLeft().isAlive())
            AddMeetingAgent(agents.getLeft());
        if (agents.getRight().canReproduce())
            AddMateingAgent(agents.getRight());
        else if (agents.getRight().isAlive())
            AddMeetingAgent(agents.getRight());
    }

    private void AddMeetingAgent(JMetal5Agent<S> agent) {
        meetableLock.lock();
        meetable.add(agent);
        meetableLock.unlock();
    }

    private void AddMateingAgent(JMetal5Agent<S> agent) {
        mateableLock.lock();
        mateable.add(agent);
        mateableLock.unlock();
    }

    class AgentManager implements Runnable {

        @Override
        public void run() {
            while (true) {
                stepsLock.lock();
                if (meetStepsLeft > 0) {
                    meetStepsLeft -= 150;
                    stepsLock.unlock();
                } else {
                    stepsLock.unlock();
                    return;
                }
                for (int i = 0; i < 150; i++) {
                    for(int j=0;j<5;j++)
                        meetStep();
                    reproStep();
                    deadStep();
                }
                int isBetter = 0;
                // for(int i = 0; i<meetable.size();i++)
                // {
                //     for(int j = i+1; j<meetable.size();j++)
                //     {
                //         int agentComparison = compareAgents(meetable.get(i), meetable.get(j));
                //         isBetter += agentComparison==0?0:1;
                //     }
                // }
                // System.out.println("isBetter: " + isBetter);
                //System.out.println("ONE ITERATION");
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                populationLock.lock();
                updateProgress();
                populationLock.unlock();
            }
        }

    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        createInitialPopulation();
        for (JMetal5Agent<S> jMetal5Agent : population) {
            AddMeetingAgent(jMetal5Agent);
        }
        initProgress();
        for (int i = 0; i < threadsCount; i++)
            executor.submit(new AgentManager());
        while (!isStoppingConditionReached()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            populationLock.lock();
            updateProgress();
            populationLock.unlock();
            System.out.println(" [ Population size: " + getPopulation().size() + " || Iteration: " + getIteration() + " ||  Name: " + getName() + " ]");
        }
    }
}