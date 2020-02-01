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
        initProgress();
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
        for (; getEvaluations() < 16000;) {
            setNeitherIsBetterMeetingTypeCounter(hypervisor.neitherIsBetterMeetingCounter.get());
            setImBetterMeetingTypeCounter(hypervisor.imBetterMeetingCounter.get());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        JMetal5ParallelAgent.stopCondition = true;

        long stop = System.nanoTime();
        System.out.println(stop-start);
    }

    @Override
    public void updateEvaluationsCounter() {
         {
            int evals = getEvaluations();
             synchronized (this) {
                 this.setEvaluations(evals + 1);
                 if (evals % 100 == 0) {
                     synchronized (population) {
                         System.out.println(getEvaluations());
                         Double sum = population.stream().map(JMetal5Agent::getResourceLevel).reduce(Double::sum).get();
                         System.out.println("energy sum: " + sum.toString());
                         StringBuilder ids = new StringBuilder();
                         population.forEach(x -> ids.append(x.getId()).append(" "));
                         System.out.println(ids);
                         System.out.println("Evaluations: " + evals);
                         updateProgress();
                         results.add(getNonDominatedSolutions(getPopulation()));
                     }
                 }
             }

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