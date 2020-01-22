package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ParallelAgent;
import org.uma.jmetal.solution.Solution;

import lombok.Getter;

public class JMetal5ParallelEMAS2<S extends Solution<?>> extends JMetal5BaseEMAS<S> {

    private Object matingLock = new Object();
    private Object meetingLock = new Object();
    private List<JMetal5ParallelAgent<S>> matingQueue = new ArrayList<>();
    private List<JMetal5ParallelAgent<S>> meetingQueue = new ArrayList<>();
    private ParallelEMASHypervisor hypervisor = new ParallelEMASHypervisor();

    protected @Getter String agentType = "JMetal5ParallelAgent";

    @Override
    public void run() {
        long start = System.nanoTime();
        initProgress();
        createInitialPopulation();
        synchronized(population)
        {
            population.forEach(item -> {
                JMetal5ParallelAgent<S> agent = (JMetal5ParallelAgent<S>) item;
                agent.setPopulation(population);
                agent.setMatingLock(matingLock);
                agent.setMatingQueue(matingQueue);
                agent.setMeetingLock(meetingLock);
                agent.setMeetingQueue(meetingQueue);
                agent.setHypervisor(hypervisor);
                new Thread(agent).start();
            });
        }
        for (int iteration = 0; iteration < 10000000; iteration++) {
            setNeitherIsBetterMeetingTypeCounter(hypervisor.neitherIsBetterMeetingCounter.get());
            setImBetterMeetingTypeCounter(hypervisor.imBetterMeetingCounter.get());
            synchronized (population) {
                System.out.println(" [ Population size: " + getPopulation().size() + " || Iteration: " + getIteration() + " ||  Name: " + getName() + " ]");
                StringBuilder ids = new StringBuilder();
                population.forEach(x -> ids.append(x.getId()).append(" "));
                System.out.println(ids);
                System.out.println(getEvaluations());
                updateProgress();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        synchronized(population)
        {
            population.forEach(i->((JMetal5ParallelAgent<S>)i).setStopCondition(true));
        }
        long stop = System.nanoTime();
        System.out.println(stop-start);
    }
}