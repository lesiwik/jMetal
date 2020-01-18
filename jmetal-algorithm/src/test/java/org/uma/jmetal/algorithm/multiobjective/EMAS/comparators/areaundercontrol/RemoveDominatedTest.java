package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.areaundercontrol;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.emasdominance.EmasDominanceComparisonTest;
import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.AgentUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RemoveDominatedTest {


    private JMetal5Agent<PointSolution> agent1;
    private JMetal5Agent<PointSolution> agent2;
    private ArrayList<JMetal5Agent<PointSolution>> agent1List;
    private ArrayList<JMetal5Agent<PointSolution>> agent2List;
    private ArrayList<JMetal5Agent<PointSolution>> expectedListAfterRemoval;
    private final String COMPARATOR_TYPE = Constants.AREA_UNDER_CONTROL_COMPARATOR;
    private final String AGENT_TYPE = Constants.BASE_AGENT;

    @Before
    public void setup()
    {
        // https://gyazo.com/bb9947e147161d546b2215fb1afa4dad
        // A1 = [0.5, 0.5] with list = [0.54, 0.45] + [0.64, 0.40]
        // A2 = [0.52, 0.39] with list = [0.53, 0.55]
        agent1List = new ArrayList<>();
        agent1 = AgentUtils.createPointSolutionAgent(new double[]{0.5, 0.5}, COMPARATOR_TYPE, AGENT_TYPE);
        AreaUnderControlComparator<JMetal5Agent<PointSolution>> a1Comparator = (AreaUnderControlComparator) agent1.getComparator();
        JMetal5Agent<PointSolution> a1_1 = AgentUtils.createPointSolutionAgent(new double[]{0.54, 0.45}, COMPARATOR_TYPE, AGENT_TYPE);
        JMetal5Agent<PointSolution> a1_2 = AgentUtils.createPointSolutionAgent(new double[]{0.64, 0.40}, COMPARATOR_TYPE, AGENT_TYPE);
        agent1List.add(a1_1);
        agent1List.add(a1_2);
        a1Comparator.setListOfKnownNonDominatedAgents(agent1List);

        agent2 = AgentUtils.createPointSolutionAgent(new double[]{0.52, 0.39}, COMPARATOR_TYPE, AGENT_TYPE);
        JMetal5Agent<PointSolution> a2_1 = AgentUtils.createPointSolutionAgent(new double[]{0.53, 0.55}, COMPARATOR_TYPE, AGENT_TYPE);
        agent2List = new ArrayList<>();
        AreaUnderControlComparator<JMetal5Agent<PointSolution>> a2Comparator = (AreaUnderControlComparator) agent2.getComparator();
        agent2List.add(a2_1);
        a2Comparator.setListOfKnownNonDominatedAgents(agent2List);

        expectedListAfterRemoval = new ArrayList<>();
        expectedListAfterRemoval.add(agent2);

    }


    @Test
    public void agentsAreCorrectlyRemovedFromList()
    {
        System.out.println("\n== Checking if neither agent dominate each other using EmasDominanceComparator. == \n ");
        neitherAgentIsBetterEmasDominance();
        System.out.println("\n== Checking if agents in list of known non dominated agents are in fact non dominated. == \n");
        nonDominatedListIsNonDominated();
        System.out.println("\n== Checking if neither agent dominate each other using AreaUnderControlComparator. == \n");
        neitherAgentIsBetterAreaUnderControl();
        System.out.println("\n== Checking if lists are equal. == \nExpected list1: " + expectedListAfterRemoval
                + "\nActual list2:" + agent1List);

        agent1.getComparator().compare(agent1, agent2);
        assertEquals(expectedListAfterRemoval, agent1List);
    }

    private void neitherAgentIsBetterAreaUnderControl()
    {
        AreaUnderControlComparisonTest.compareAgentsWithResult(agent1, agent2, Constants.NEITHER_IS_BETTER);
    }

    private void neitherAgentIsBetterEmasDominance()
    {
        EmasDominanceComparisonTest.compareAgentWithResult(agent1, agent2, Constants.NEITHER_IS_BETTER);
    }

    private void nonDominatedListIsNonDominated()
    {
        EmasDominanceComparisonTest.listIsNonDominated(agent1List);
    }

    @After
    public void printResult()
    {
        System.out.println("\n == Remove dominated agents from list test has ended. ==");
    }

}
