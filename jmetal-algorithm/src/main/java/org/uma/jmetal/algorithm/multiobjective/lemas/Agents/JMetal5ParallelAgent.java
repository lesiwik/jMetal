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
     * Increments {@link JMetal5BaseEMAS#neitherIsBetterMeetingTypeCounter} or
     * {@link JMetal5BaseEMAS#imBetterMeetingTypeCounter} depending on meeting
     * result.
     *
     * @param meetingResult meeting result of two agents.
     */

    private void register() {
        synchronized (population) {
            id = staticId.incrementAndGet();
//            print("register");
            if(!population.contains(this))
                population.add(this);
        }
    }

    private void deregister() {
        synchronized (population) {
//            print("deregister");
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
        //print("hello from agent");
        while (isAlive() && !stopCondition) {
            meet();
            if (canReproduce()) {
                reproduce();
            }

            setMet(false);
            setHasAlreadyReproduced(false);
//            try {
//                //Thread.sleep(100);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
//        print("bye from agent " + new Integer(imBetterMeetingCounter).toString() + " " + new Integer(neitherIsBetterMeetingCounter).toString() + " " + timesMated.toString());

        hypervisor.imBetterMeetingCounter.addAndGet(imBetterMeetingCounter);
        hypervisor.neitherIsBetterMeetingCounter.addAndGet(neitherIsBetterMeetingCounter);

        deregister();
    }

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
                    //              print("MATING: stoję w kolejce");
                    while (!picked)
                        matingQueue.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                //         print("MATING: ciągnę z kolejki");
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
                    parallelAgent.setMeetingQueues(meetingQueues);
                    parallelAgent.setMatingQueues(matingQueues);
                    parallelAgent.setPopulation(population);
                    parallelAgent.setMeetingQueuesNumber(meetingQueuesNumber);
                    parallelAgent.setMatingQueuesNumber(matingQueuesNumber);
                    parallelAgent.setHypervisor(hypervisor);
//                            parallelAgent.setStopCondition(stopCondition);
                    new Thread(parallelAgent).start();
                }
            }
        }
    }

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
//                     print("MEETING: stoję w kolejce");
                    while (!picked)
                        meetingQueue.wait();
                } catch (InterruptedException e) {
                }
            } else {
                //    print("MEETING: ciągnę z kolejki");
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
