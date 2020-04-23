package org.uma.jmetal.algorithm.multiobjective.lemas.Comparators;

import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.CrowdingDistanceComparator;
import org.uma.jmetal.util.comparator.RankingComparator;

import java.io.Serializable;
import java.util.Comparator;

public class NSGAIINoisyCrowdingAndDistanceComparator<S extends Solution<?>> implements Comparator<S>, Serializable {

    private final Comparator<S> rankComparator = new RankingComparator<S>();
    private final Comparator<S> crowdingDistanceComparator = new CrowdingDistanceComparator<S>() ;
    /**
     * Compares two solutions.
     *
     * @param solution1 Object representing the first solution
     * @param solution2 Object representing the second solution.
     * @return -1, or 0, or 1 if solution1 is less than, equal, or greater than solution2,
     * respectively.
     */
    @Override
    public int compare(S solution1, S solution2) {
        int result = rankComparator.compare(solution1, solution2) ;
        if (result == 0) {
            result = crowdingDistanceComparator.compare(solution1, solution2);
        }
        double rand = Math.random();
        if (rand < Constants.NOISE_LEVEL)
            return -result;
        return result;
    }
}
