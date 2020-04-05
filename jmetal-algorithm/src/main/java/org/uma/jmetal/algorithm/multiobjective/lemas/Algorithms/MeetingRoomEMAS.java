package org.uma.jmetal.algorithm.multiobjective.lemas.Algorithms;

import lombok.Getter;
import lombok.Setter;
import org.uma.jmetal.solution.Solution;


public class MeetingRoomEMAS<S extends Solution<?>> extends JMetal5BaseEMAS<S> {


    @Getter
    @Setter
    private int matingDifferenceThreshold;

    public MeetingRoomEMAS(int matingDifferenceThreshold) {
        super();
        this.matingDifferenceThreshold = matingDifferenceThreshold;
    }

}
