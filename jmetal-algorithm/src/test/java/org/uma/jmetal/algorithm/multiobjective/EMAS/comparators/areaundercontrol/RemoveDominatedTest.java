package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.areaundercontrol;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.emasdominance.EmasDominanceComparisonTest;
import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.AgentUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.PointSolution;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RemoveDominatedTest {


    private JMetal5Agent<PointSolution> agent1;
    private JMetal5Agent<PointSolution> agent2;
    private ArrayList<JMetal5Agent<PointSolution>> agent1List;
    private ArrayList<JMetal5Agent<PointSolution>> expectedListAfterRemoval;
    private final String COMPARATOR_TYPE = Constants.AREA_UNDER_CONTROL_COMPARATOR;

    @Before
    public void setup()
    {
        // A1 = [0.5, 0.5] with list = [0.54, 0.45] + [0.64, 0.40]
        // A2 = [0.52, 0.47]
        agent1List = new ArrayList<>();
        agent1 = AgentUtils.createPointSolutionAgent(new double[]{0.5, 0.5}, COMPARATOR_TYPE);
        AreaUnderControlComparator<JMetal5Agent<PointSolution>> a1Comparator = (AreaUnderControlComparator) agent1.getComparator();
        JMetal5Agent<PointSolution> a1_1 = AgentUtils.createPointSolutionAgent(new double[]{0.54, 0.45}, COMPARATOR_TYPE);
        JMetal5Agent<PointSolution> a1_2 = AgentUtils.createPointSolutionAgent(new double[]{0.64, 0.40}, COMPARATOR_TYPE);
        agent1List.add(a1_1);
        agent1List.add(a1_2);
        a1Comparator.setListOfKnownNonDominatedAgents(agent1List);

        agent2 = AgentUtils.createPointSolutionAgent(new double[]{0.52, 0.47}, COMPARATOR_TYPE);

        expectedListAfterRemoval = new ArrayList<>();
        expectedListAfterRemoval.add(agent2);

    }


    @Test
    public void agentsAreCorrectlyRemovedFromList()
    {
        neitherAgentIsBetterEmasDominance();
        nonDominatedListIsNonDominated();
        System.out.println("Checking if lists are equal. \nExpected list1: " + expectedListAfterRemoval
                + "\nActual list2:" + agent1List);

        agent1.getComparator().compare(agent1, agent2);
        assertEquals(expectedListAfterRemoval, agent1List);
    }

    public void neitherAgentIsBetterEmasDominance()
    {
        EmasDominanceComparisonTest.compareAgentWithResult(agent1, agent2, Constants.NEITHER_IS_BETTER);
    }

    public void nonDominatedListIsNonDominated()
    {
        EmasDominanceComparisonTest.listIsNonDominated(agent1List);
    }


}
