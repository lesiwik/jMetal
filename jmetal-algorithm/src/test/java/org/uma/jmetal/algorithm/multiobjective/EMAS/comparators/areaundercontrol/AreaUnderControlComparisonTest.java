package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.areaundercontrol;

import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.ComparatorUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Class to compare agents to each other using AreaUnderControlComparator.
 * Potential dangers:
 * 1. {@link AreaUnderControlComparisonTest#compareAgentsWithResult(JMetal5Agent, JMetal5Agent, int)} creates clean {@link AreaUnderControlComparator} to ensure it uses it to compare.
 * 2. For reason above, {@link #compareListsToAgentsWithResult(ArrayList, ArrayList, JMetal5Agent, JMetal5Agent, int, int)} creates new clean {@link AreaUnderControlComparator} for each comparison.
 * By design its implementation holds necessary information to compare BUT the comparison itself does not make call to its internals through instance that is comparing.
 * It always makes calls to get comparator of actual agent and then compares.
 * */
public class AreaUnderControlComparisonTest {



    public static void compareAgentsWithResult(JMetal5Agent<?> agent1, JMetal5Agent<?> agent2, final int EXPECTED_RESULT)
    {
        final AreaUnderControlComparator COMPARATOR = new AreaUnderControlComparator<>();
        int comparatorResult = COMPARATOR.compare(agent1, agent2);
        System.out.println(
                ComparatorUtils.constructComparisonResultString(COMPARATOR, agent1, agent2, comparatorResult, Constants.NEITHER_IS_BETTER,
                ComparatorUtils.constructAreaUnderComparisonResultString(
                        COMPARATOR.getAgent2ToListComparisonResult(),
                        COMPARATOR.getAgent1ToListComparisonResult())));

        assertEquals(EXPECTED_RESULT, comparatorResult);
    }

    public static void compareListsToAgentsWithResult(ArrayList<JMetal5Agent<PointSolution>> agent1ListOfKnownNonDominated,
                                                      ArrayList<JMetal5Agent<PointSolution>> agent2ListOfKnownNonDominated,
                                                      JMetal5Agent<PointSolution> agent1,
                                                      JMetal5Agent<PointSolution> agent2,
                                                      final int EXPECTED_RESULT_LIST1,
                                                      final int EXPECTED_RESULT_LIST2)
    {
        agent1ListOfKnownNonDominated.forEach(agent -> {
            AreaUnderControlComparator comparator = new AreaUnderControlComparator();
            int comparatorResult = comparator.compare(agent1, agent);
            System.out.println(ComparatorUtils.constructComparisonResultString(comparator, agent, agent1, comparatorResult, EXPECTED_RESULT_LIST1, ""));
            assertEquals(EXPECTED_RESULT_LIST1, comparatorResult);
        });

        agent2ListOfKnownNonDominated.forEach(agent -> {
            AreaUnderControlComparator comparator = new AreaUnderControlComparator();
            int comparatorResult = comparator.compare(agent2, agent);
            System.out.println(ComparatorUtils.constructComparisonResultString(comparator, agent, agent2, comparatorResult, EXPECTED_RESULT_LIST2, ""));
            assertEquals(EXPECTED_RESULT_LIST2, comparatorResult);
        });
    }
}
