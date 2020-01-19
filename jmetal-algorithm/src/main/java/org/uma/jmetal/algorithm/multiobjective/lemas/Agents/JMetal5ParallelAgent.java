package org.uma.jmetal.algorithm.multiobjective.lemas.Agents;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms.ParallelEMASHypervisor;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;

import lombok.Getter;
import lombok.Setter;

public class JMetal5ParallelAgent<S extends Solution<?>> extends JMetal5AreaControlAgent<S> implements Runnable {

    private @Setter List<JMetal5Agent<S>> population;
    private @Setter Object meetingLock;
    private @Setter Object matingLock;
    private @Setter List<JMetal5ParallelAgent<S>> meetingQueue;
    private @Setter List<JMetal5ParallelAgent<S>> matingQueue;
    private @Setter ParallelEMASHypervisor hypervisor;
    private @Getter int neitherIsBetterMeetingCounter = 0;
    private @Getter int imBetterMeetingCounter = 0;
    private @Setter boolean stopCondition = false;
    private boolean picked;
    private boolean picked2;
    private Integer id;

    static AtomicInteger staticId = new AtomicInteger(0);

    protected @Getter String agentType = "JMetal5ParallelAgent";

    /**
     * Increments {@link JMetal5BaseEMAS#neitherIsBetterMeetingTypeCounter} or
     * {@link JMetal5BaseEMAS#imBetterMeetingTypeCounter} depending on meeting
     * result.
     * 
     * @param meetingResult meeting result of two agents.
     */

    private void register() {
        synchronized (population) {
            id = staticId.incrementAndGet();
            population.add(this);
        }
    }

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

    private void print(String text) {
        System.out.println(id.toString() + ": " + text);
    };

    private void echo() {
        System.out.println(id.toString() + ": ECHO");
    };

    public void run() {
        register();
        Optional<JMetal5ParallelAgent<S>> secondAgent = Optional.empty();
        JMetal5ParallelAgent<S> objectTosynchronise = null;
        // print("hello from agent");
        while (isAlive() && !stopCondition) {
            secondAgent = Optional.empty();
            picked = false;
            picked2 = false;
            synchronized (meetingLock) {
                if (meetingQueue.size() == 0) {
                    meetingQueue.add(this);
                    try {
                        // print("MEETING: stoję w kolejce");
                        while (!picked)
                            meetingLock.wait();
                    } catch (InterruptedException e) {
                    }
                } else {
                    // print("MEETING: ciągnę z kolejki");
                    secondAgent = Optional.of(meetingQueue.get(0));
                    meetingQueue.clear();
                    secondAgent.get().picked = true;
                    meetingLock.notify();
                }
            }
            if (secondAgent.isPresent())
                objectTosynchronise = secondAgent.get();
            else
                objectTosynchronise = this;
            synchronized (objectTosynchronise) {
                if (objectTosynchronise == this) {
                    while (!picked2)
                        try {
                            // print("MEETING: czekam na doMeeting");
                            objectTosynchronise.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                } else {
                    // print("MEETING: robie domeeting");
                    int meetingResult = this.doMeeting(Arrays.asList(secondAgent.get()),
                            Constants.TRANSFER_RESOURCE_VALUE);
                    // print(new Integer(meetingResult).toString());
                    secondAgent.get().picked2 = true;
                    objectTosynchronise.notify();
                    updateMeetingStatistics(meetingResult);
                }
                // print("MEETING: koniec meetingu");
            }
            if (canReproduce()) {
                picked = false;
                secondAgent = Optional.empty();
                synchronized (matingLock) {
                    if (matingQueue.size() == 0) {
                        matingQueue.add(this);
                        try {
                            // print("MATING: stoję w kolejce");
                            while (!picked)
                                matingLock.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        // print("MATING: ciągnę z kolejki");
                        secondAgent = Optional.of(matingQueue.get(0));
                        matingQueue.clear();
                        secondAgent.get().picked = true;
                        matingLock.notify();
                    }
                }
                if (secondAgent.isPresent()) {
                    objectTosynchronise = secondAgent.get();
                } else
                    objectTosynchronise = this;
                synchronized (objectTosynchronise) {
                    if (objectTosynchronise == this)
                        while (!picked2)
                            try {
                                // print("MATING: czekam na doReproduce");
                                objectTosynchronise.wait();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                    else {
                        List<? extends JMetal5Agent<S>> newBorn = this
                                .doReproduce(Arrays.asList(secondAgent.get(), this));
                        // print("MATING: robię doReproduce");
                        objectTosynchronise.picked2 = true;
                        objectTosynchronise.notify();
                        for (JMetal5Agent<S> jMetal5Agent : newBorn) {
                            JMetal5ParallelAgent<S> parallelAgent = (JMetal5ParallelAgent<S>) jMetal5Agent;
                            parallelAgent.setMeetingLock(meetingLock);
                            parallelAgent.setMatingLock(matingLock);
                            parallelAgent.setMeetingQueue(meetingQueue);
                            parallelAgent.setMatingQueue(matingQueue);
                            parallelAgent.setPopulation(population);
                            parallelAgent.setHypervisor(hypervisor);
                            new Thread(parallelAgent).start();
                        }
                    }
                }
            }

            setMet(false);
            setHasAlreadyReproduced(false);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        hypervisor.imBetterMeetingCounter.addAndGet(imBetterMeetingCounter);
        hypervisor.neitherIsBetterMeetingCounter.addAndGet(neitherIsBetterMeetingCounter);
        print("bye from agent");
        deregister();
    }

}