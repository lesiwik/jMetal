package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;

import java.util.Random;

/**
 * @deprecated Currently not in use and lacks implementation.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public class CloseToCornerComparator<S extends Solution<?>> extends DominanceComparator<S> {

    private Random random;
    private double falseNegativeRate;
    private DominanceComparator<S> dominanceOperator;

    public CloseToCornerComparator() {
        dominanceOperator = null;
        falseNegativeRate = 0;
        random = null;
    }

    public CloseToCornerComparator(DominanceComparator<S> dominanceComparator, Random random, double falseNegativeRate) {
        this.dominanceOperator = dominanceComparator;
        this.random = random;
        this.falseNegativeRate = falseNegativeRate;
    }

    /**
     * Compares normalization of genotypes first objective of each agent.
     * @param a1 agent to compare to.
     * @param a2 agent to compare to.
     * @return comparison result.
     * */
    public int compare(JMetal5Agent a1, JMetal5Agent a2) {
        //TODO: to be redefined, for very preliminary verification only
        if (1 - a1.genotype.getObjective(0) <= 1 - a2.genotype.getObjective(0)) {
            return -1;
        } else {
            return 1;
        }
    }


    @Override
    public int compare(S solution1, S solution2) {
        return 0;
    }
}