package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import lombok.Getter;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5ParallelAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JMetal5ParallelEMAS2<S extends Solution<?>> extends JMetal5BaseEMAS<S> {

    private int meetingQueuesNumber = 8;
    private int matingQueuesNumber = 2;
    private final int maxEvaluations = Constants.MAX_EVALUATIONS;
    private final int statsUpdateStep = 100;
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
        JMetal5ParallelAgent.stopCondition = false;
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
        int lastEvaluationCount = 0;
        for (; getEvaluations() < maxEvaluations;) {
            setNeitherIsBetterMeetingTypeCounter(hypervisor.neitherIsBetterMeetingCounter.get());
            setImBetterMeetingTypeCounter(hypervisor.imBetterMeetingCounter.get());
            if(lastEvaluationCount==getEvaluations() && lastEvaluationCount!=0)
            {
                iterationUpdate(getEvaluations());
                JMetal5ParallelAgent.stopCondition = true;
                break;
            }
            else
            {
                lastEvaluationCount=getEvaluations();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        JMetal5ParallelAgent.stopCondition = true;
        long stop = System.nanoTime();
    }

    @Override
    public void updateEvaluationsCounter() {
         {
             if(getEvaluations() > maxEvaluations)
                 JMetal5ParallelAgent.stopCondition = true;
            int evals = getEvaluations();
             synchronized (this) {
                 this.setEvaluations(evals + 1);
                 if (evals % statsUpdateStep == 0) {
                     iterationUpdate(evals);
                 }
             }

        }
    }

    private void iterationUpdate(int evals) {
        synchronized (population) {
            System.out.println("[ Population size: " + population.size() + " || Evaluations: " + evals + " || Name: " + getName() + " ]");
            updateProgress();
            results.add(getNonDominatedSolutions(getPopulation()));
        }
    }

    @Override
    public List<S> getPopulation() {
        synchronized(population)
        {
            return population.stream().map(JMetal5Agent::getGenotype).collect(Collectors.toList());
        }
    }

    @Override
    public void resetState() {
        super.resetState();
        meetingQueues.clear();
        matingQueues.clear();
        results.clear();
    }
}