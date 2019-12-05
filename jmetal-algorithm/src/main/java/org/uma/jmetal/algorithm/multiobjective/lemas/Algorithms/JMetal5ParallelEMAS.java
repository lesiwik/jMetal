package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

public class JMetal5ParallelEMAS<S extends Solution<?>> extends JMetal5BaseEMAS<S> {
    private int meetStepsLeft = (int) (Constants.MAX_ITERATIONS * Constants.ENV_ENERGY
            / Constants.INITIAL_RESOURCE_VALUE);
    private final List<Lock> meetableLocks = new CopyOnWriteArrayList<>();
    private final List<Lock> mateableLocks = new CopyOnWriteArrayList<>();
    private final Lock stepsLock = new ReentrantLock();
    private final Lock populationLock = new ReentrantLock();
    private final List<List<JMetal5Agent<S>>> meetables = new ArrayList<>();
    private final List<List<JMetal5Agent<S>>> mateables = new ArrayList<>();
    public int threadsCount = 8;

    private final Random random = new Random();

    private <T> Pair<T, T> getPairOfElemets(List<List<T>> lists, List<Lock> listLocks) {
        int listNumber = random.nextInt(threadsCount);
        listLocks.get(listNumber).lock();
        int meetableCount = lists.get(listNumber).size();
        if (meetableCount < 2) {
            listLocks.get(listNumber).unlock();
            return null;
        }
        int randomElementIndex = random.nextInt(meetableCount);
        T first = lists.get(listNumber).get(randomElementIndex);
        lists.get(listNumber).remove(randomElementIndex);
        randomElementIndex = random.nextInt(meetableCount - 1);
        T second = lists.get(listNumber).get(randomElementIndex);
        lists.get(listNumber).remove(randomElementIndex);
        listLocks.get(listNumber).unlock();
        return new ImmutablePair<T, T>(first, second);
    }

    @Override
    public void reproStep() {
        Pair<JMetal5Agent<S>, JMetal5Agent<S>> agents = getPairOfElemets(mateables, mateableLocks);
        if (agents == null)
            return;
        // System.out.println("agents mateing "+ mateable.size());
        List<? extends JMetal5Agent<S>> newBorn = agents.getLeft()
                .doReproduce(Arrays.asList(agents.getLeft(), agents.getRight()));
        populationLock.lock();
        population.addAll(newBorn);
        populationLock.unlock();
        System.out.println("matin");
        for (JMetal5Agent<S> agent : newBorn) {
            AddMeetingAgent(agent);
        }
        AddMeetingAgent(agents.getLeft());
        AddMeetingAgent(agents.getRight());
    }

    @Override
    public void meetStep() {
        Pair<JMetal5Agent<S>, JMetal5Agent<S>> agents = getPairOfElemets(meetables, meetableLocks);
        if (agents == null)
            return;
        int meetingResult;
        meetingResult = agents.getLeft().doMeeting(Arrays.asList(agents.getRight()), Constants.TRANSFER_RESOURCE_VALUE);
        updateMeetingStatistics(meetingResult);
        agents.getLeft().setMet(false);
        agents.getRight().setMet(false);
        if(meetingResult!=0)
             System.out.println("agents meeting from");
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
        int listNumber = random.nextInt(threadsCount);
        meetableLocks.get(listNumber).lock();
        meetables.get(listNumber).add(agent);
        meetableLocks.get(listNumber).unlock();
    }

    private void AddMateingAgent(JMetal5Agent<S> agent) {
        int listNumber = random.nextInt(threadsCount);
        mateableLocks.get(listNumber).lock();
        mateables.get(listNumber).add(agent);
        mateableLocks.get(listNumber).unlock();
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
                    for (int j = 0; j < 5; j++)
                        meetStep();
                    reproStep();
                }
                
                updateProgress();
                
            }
        }
    }

    @Override
    public List<S> getPopulation() {
        populationLock.lock();
        List<S> result = population.stream().map(JMetal5Agent::getGenotype).collect(Collectors.toList());
        populationLock.unlock();
        return result;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        createInitialPopulation();
        for (int i = 0; i < threadsCount; i++)
            {
            mateableLocks.add(new ReentrantLock());
            meetableLocks.add(new ReentrantLock());
            meetables.add(new ArrayList<>());
            mateables.add(new ArrayList<>());
            }
        for (JMetal5Agent<S> jMetal5Agent : population) {
            AddMeetingAgent(jMetal5Agent);
        }
        initProgress();
        for (int i = 0; i < threadsCount; i++)
            executor.submit(new AgentManager());
        int seconds=0;
        while (!isStoppingConditionReached()) {

            populationLock.lock();
            deadStep();
            System.out.println(seconds+" "+getIteration());
            //System.out.println(" [ Population size: " + getPopulation().size() + " || Iteration: " + getIteration() + " ||  Name: " + getName() + " ]");
            populationLock.unlock();
            try {
                Thread.sleep(1000);
                seconds++;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            populationLock.lock();
            updateProgress();
            System.out.println(" [ Population size: " + getPopulation().size() + " || Iteration: " + getIteration() + " ||  Name: " + getName() + " ]");
            populationLock.unlock();
        }
    }
}