package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ParallelAgent;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import lombok.Getter;

public class JMetal5ParallelEMAS2<S extends Solution<?>> extends JMetal5BaseEMAS<S> {

    private int meetingQueuesNumber = 8;
    private int matingQueuesNumber = 2;
    private List<List<JMetal5ParallelAgent<S>>> matingQueues = new ArrayList<>();
    private List<List<JMetal5ParallelAgent<S>>> meetingQueues = new ArrayList<>();
    private ParallelEMASHypervisor hypervisor = new ParallelEMASHypervisor();

    protected @Getter String agentType = "JMetal5ParallelAgent";

    @Override
    public void run() {
        long start = System.nanoTime();
        initProgress();
        for(int i=0;i<meetingQueuesNumber;i++)
            meetingQueues.add(new ArrayList<>());
        for(int i=0;i<matingQueuesNumber;i++)
            matingQueues.add(new ArrayList<>());
        createInitialPopulation();
        synchronized(population)
        {
            population.forEach(item -> {
                JMetal5ParallelAgent<S> agent = (JMetal5ParallelAgent<S>) item;
                agent.setPopulation(population);
                agent.setMatingQueues(matingQueues);
                agent.setMatingQueuesNumber(matingQueuesNumber);
                agent.setMeetingQueuesNumber(meetingQueuesNumber);
                agent.setMeetingQueues(meetingQueues);
                agent.setHypervisor(hypervisor);
                new Thread(agent).start();
            });
        }
        for (; getEvaluations() < 500000;) {
            setNeitherIsBetterMeetingTypeCounter(hypervisor.neitherIsBetterMeetingCounter.get());
            setImBetterMeetingTypeCounter(hypervisor.imBetterMeetingCounter.get());
            synchronized (population) {
                System.out.println(" [ Population size: " + getPopulation().size() + " || Iteration: " + getIteration() + " ||  Name: " + getName() + " ]");
                Double sum = population.stream().map(x->x.getResourceLevel()).reduce(Double::sum).get();
                System.out.println("energy sum: "+sum.toString());
                StringBuilder ids = new StringBuilder();
                population.forEach(x -> ids.append(x.getId()).append(" "));
                System.out.println(ids);
                System.out.println(getEvaluations());
                updateProgress();
            }
            try {
                Thread.sleep(100);
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

    @Override
    public void updateEvaluationsCounter() {
        synchronized (this) {
            this.setEvaluations(getEvaluations() + 1);
        }
    }
    @Override
    public List<S> getPopulation() {
    {
        synchronized(population)
        {
            return population.stream().map(JMetal5Agent::getGenotype).collect(Collectors.toList());
        }
    }
    }
}