package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import java.util.concurrent.atomic.AtomicInteger;

public class ParallelEMASHypervisor {

    public AtomicInteger neitherIsBetterMeetingCounter;
    public AtomicInteger imBetterMeetingCounter;

    public ParallelEMASHypervisor() {
        neitherIsBetterMeetingCounter = new AtomicInteger(0);
        imBetterMeetingCounter = new AtomicInteger(0);
    }
}