package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

/**
 * EMAS Single Objective comparator using {@link ObjectiveComparator}.
 * @author dr inż. Siwik Leszek siwik@agh.edu.pl
 * @since 8/27/2018
 * */
public class EmasSingleObjectiveComparator<S extends JMetal5Agent<?>> extends EmasDominanceComparator<S> {

    /**
     * Compares two genotypes of agents using instance of newly {@link ObjectiveComparator} with ObjectiveID = 0.
     * @param a1 agent to compare genotype from.
     * @param a2 agent to compare genotype from.
     * @return result of comparison from {@link ObjectiveComparator#compare(Solution, Solution)}.
     * */
    @Override
    public int compare(JMetal5Agent a1, JMetal5Agent a2) {
        return new ObjectiveComparator(0).compare(a1.genotype, a2.genotype);
    }
}
