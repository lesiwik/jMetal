package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.ParallelEMASHypervisor;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import lombok.Getter;
import lombok.Setter;

public class JMetal5ParallelAgent<S extends Solution<?>> extends JMetal5Agent<S> implements Runnable {

    private @Setter List<JMetal5Agent<S>> population;
    private @Setter List<List<JMetal5ParallelAgent<S>>> meetingQueues;
    private @Setter List<List<JMetal5ParallelAgent<S>>> matingQueues;
    private @Setter ParallelEMASHypervisor hypervisor;
    private Random random = new Random();
    private @Setter int meetingQueuesNumber;
    private @Setter int matingQueuesNumber;
    private @Getter int neitherIsBetterMeetingCounter = 0;
    private @Getter int imBetterMeetingCounter = 0;
    private Integer timesMated = 0;
    public static boolean stopCondition = false;
    private boolean picked;
    private boolean picked2;
    private Integer id;
    static AtomicInteger staticId = new AtomicInteger(0);

    protected @Getter String agentType = "JMetal5ParallelAgent";
    /**
     * Registers itself in the population injected via {@link JMetal5ParallelAgent#setPopulation}
     *
     */
    private void register() {
        synchronized (population) {
            id = staticId.incrementAndGet();
            if(!population.contains(this))
                population.add(this);
        }
    }

    /**
     * Deregisters itself in the population injected via {@link JMetal5ParallelAgent#setPopulation}
     *
     */
    private void deregister() {
        synchronized (population) {
            population.remove(this);
        }
    }

    protected void updateMeetingStatistics(int meetingResult) {
        if (meetingResult == 0)
            neitherIsBetterMeetingCounter++;
        else
            imBetterMeetingCounter++;
    }

    @Override
    public String getAgentType() {
        return "JMetal5ParallelAgent";
    }

    /**
     * Registers itself, runs in loop until is dead or algorithm has been stopped.
     * Then passes its stats to hypervisor and deregisters itself
     */
    public void run() {
        register();
        while (isAlive() && !stopCondition) {
            meet();
            if (canReproduce()) {
                reproduce();
            }
            setMet(false);
            setHasAlreadyReproduced(false);
        }
        hypervisor.imBetterMeetingCounter.addAndGet(imBetterMeetingCounter);
        hypervisor.neitherIsBetterMeetingCounter.addAndGet(neitherIsBetterMeetingCounter);
        deregister();
    }

    /**
     * This method finds and reproduces with another agent.
     * If someone is waiting in mating queue, agent picks him up and they reproduce.
     * Otherwise agent adds himself to queue and waits for another agent
     *
     * During reproduction agents are synchronised on agent who previously waited in queue.
     * The same agent waits again, while second one performs reproduction action
     */
    private void reproduce() {
        Optional<JMetal5ParallelAgent<S>> secondAgent;
        JMetal5ParallelAgent<S> objectTosynchronise;
        List<JMetal5ParallelAgent<S>> matingQueue =  matingQueues.get(random.nextInt(matingQueuesNumber));
        timesMated++;
        picked = false;
        secondAgent = Optional.empty();
        synchronized (matingQueue) {
            if (matingQueue.size() == 0) {
                matingQueue.add(this);
                try {
                    while (!picked && !stopCondition)
                        matingQueue.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                secondAgent = Optional.of(matingQueue.get(0));
                matingQueue.clear();
                secondAgent.get().picked = true;
                matingQueue.notify();
            }
        }
        if (secondAgent.isPresent()) {
            objectTosynchronise = secondAgent.get();
        } else
            objectTosynchronise = this;
        synchronized (objectTosynchronise) {
            if (objectTosynchronise == this)
                while (!picked2 && !stopCondition)
                    try {
                        objectTosynchronise.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            else {
                List<? extends JMetal5Agent<S>> newBorn = this
                        .doReproduce(Arrays.asList(secondAgent.get(), this));
                objectTosynchronise.picked2 = true;
                objectTosynchronise.notify();
                for (JMetal5Agent<S> jMetal5Agent : newBorn) {
                    JMetal5ParallelAgent<S> parallelAgent = (JMetal5ParallelAgent<S>) jMetal5Agent;
                    parallelAgent.setMeetingQueues(meetingQueues);
                    parallelAgent.setMatingQueues(matingQueues);
                    parallelAgent.setPopulation(population);
                    parallelAgent.setMeetingQueuesNumber(meetingQueuesNumber);
                    parallelAgent.setMatingQueuesNumber(matingQueuesNumber);
                    parallelAgent.setHypervisor(hypervisor);
                    new Thread(parallelAgent).start();
                }
            }
        }
    }

    /**
     * Finds and meets another agent,
     *
     * Synchronisation mechanisms are similar to these in reproduce()
     */
    private void meet() {
        Optional<JMetal5ParallelAgent<S>> secondAgent;
        JMetal5ParallelAgent<S> objectTosynchronise;
        List<JMetal5ParallelAgent<S>> meetingQueue =  meetingQueues.get(random.nextInt(meetingQueuesNumber));
        secondAgent = Optional.empty();
        picked = false;
        picked2 = false;
        synchronized (meetingQueue) {
            if (meetingQueue.size() == 0) {
                meetingQueue.add(this);
                try {
                    while (!picked && !stopCondition)
                        meetingQueue.wait(1000);
                } catch (InterruptedException e) {
                }
            } else {
                secondAgent = Optional.of(meetingQueue.get(0));
                meetingQueue.clear();
                secondAgent.get().picked = true;
                meetingQueue.notify();
            }
        }
        if (secondAgent.isPresent())
            objectTosynchronise = secondAgent.get();
        else
            objectTosynchronise = this;
        synchronized (objectTosynchronise) {
            if (objectTosynchronise == this) {
                while (!picked2 && !stopCondition)
                    try {
                        objectTosynchronise.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            } else {
                int meetingResult = this.doMeeting(Arrays.asList(secondAgent.get()),
                        Constants.TRANSFER_RESOURCE_VALUE);
                secondAgent.get().picked2 = true;
                objectTosynchronise.notify();
                updateMeetingStatistics(meetingResult);
            }
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(o==null)return false;
        if(getClass()!=o.getClass()) return false;
        JMetal5ParallelAgent<S> a = (JMetal5ParallelAgent<S>)o;
        return id == a.id;
    }
}
