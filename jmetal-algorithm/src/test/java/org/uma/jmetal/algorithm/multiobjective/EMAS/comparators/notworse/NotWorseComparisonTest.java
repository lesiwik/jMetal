package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.notworse;

import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.ComparatorUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.NotWorseComparator;

import static org.junit.Assert.assertEquals;

public class NotWorseComparisonTest {

    public static void compareAgentWithResult(JMetal5Agent<?> parent, JMetal5Agent<?> offspring, final int EXPECTED_RESULT)
    {
        final NotWorseComparator<JMetal5Agent<?>> COMPARATOR = new NotWorseComparator<>();
        int comparatorResult = COMPARATOR.compare(parent, offspring);
        System.out.println(ComparatorUtils.constructComparisonResultString(COMPARATOR, parent, offspring, comparatorResult, EXPECTED_RESULT, ""));
        assertEquals(EXPECTED_RESULT, comparatorResult);
    }
}
