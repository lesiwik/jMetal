package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Random;

/**
 * Extension class from {@link DominanceComparator} to allow two {@link JMetal5Agent} as parameters.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public class NoisyEmasDominanceComparator<Agent extends JMetal5Agent<?>> extends EmasDominanceComparator<Agent>{

    /**
     * Private instance of dominance comparator. There is no need of creating more than one instance of it already.
     * */
    private static final DominanceComparator DOMINANCE_COMPARATOR = new DominanceComparator();

    /**
     * Compares two Agents genotypes using {@link DominanceComparator#compare(Solution, Solution)}.
     * @param a1 agent to compare.
     * @param a2 agent to compare.
     * @return result of comparison.
     * */
    @Override
    public int compare(Agent a1, Agent a2) {
        return this.compare(a1.genotype, a2.genotype);
    }

    @Override
    public int compare(Solution a1, Solution a2) {

        double rand = Math.random();
        int result = DOMINANCE_COMPARATOR.compare(a1, a2);
        if (rand < Constants.NOISE_LEVEL)
            return -result;
        return result;

    }
}
