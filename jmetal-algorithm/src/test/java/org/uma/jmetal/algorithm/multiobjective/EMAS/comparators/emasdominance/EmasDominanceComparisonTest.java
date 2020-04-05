package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.emasdominance;

import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.ComparatorUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class EmasDominanceComparisonTest {

    private static final EmasDominanceComparator<JMetal5Agent<?>> COMPARATOR = new EmasDominanceComparator<>();

    public static void compareAgentWithResult(JMetal5Agent<?> agent1, JMetal5Agent<?> agent2, final int EXPECTED_RESULT)
    {
        int comparatorResult = COMPARATOR.compare(agent1, agent2);
        System.out.println(ComparatorUtils.constructComparisonResultString(COMPARATOR, agent1, agent2, comparatorResult, EXPECTED_RESULT, ""));
        assertEquals(EXPECTED_RESULT, comparatorResult);
    }

    public static void listIsNonDominated(ArrayList<JMetal5Agent<PointSolution>> nonDominatedList)
    {
        for(int i = 0; i < nonDominatedList.size(); i++)
        {
            JMetal5Agent<PointSolution> a1 = nonDominatedList.get(i);
            for (int j = 0; j < nonDominatedList.size(); j++) {
                if (j == i)
                    continue;
                JMetal5Agent<PointSolution> a2 = nonDominatedList.get(j);
                compareAgentWithResult(a1, a2 , Constants.NEITHER_IS_BETTER);
            }
        }
    }

}
