package org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.areaundercontrolextended;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.areaundercontrol.AreaUnderControlComparisonTest;
import org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.emasdominance.EmasDominanceComparisonTest;
import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.AgentUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5Agent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Checks to see if merge using extended {@link org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator} was as expected.
 * Assumptions that are also checked:
 * 1. A1 and A2 are not dominating mutually ( with {@link org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.AreaUnderControlComparator}.
 * (NOTE: It means that they are also not dominated with {@link org.uma.jmetal.algorithm.multiobjective.lemas.Comparators.EmasDominanceComparator}.
 * 2. List of known non dominated Agents of either agents contain only mutually non dominated agents - meaning each agent in said list is not
 * dominated by either owner of said list and any agent in that list but CAN be dominated by any other agent that is outside of this scope.
 * (eg. A1 has agent A1_1 in his list and A1_1 gets dominated by A2 or any agent in A2s list).
 * 3. Final merge should:..
 * */
public class ListMergeTest {

    private JMetal5Agent<PointSolution> agent1;
    private JMetal5Agent<PointSolution> agent2;
    private ArrayList<JMetal5Agent<PointSolution>> a1_list;
    private ArrayList<JMetal5Agent<PointSolution>> a2_list;
    private ArrayList<JMetal5Agent<PointSolution>> expectedMergedList; /* TODO: Implemented and decide how it should look like. */
    private final String COMPARATOR_TYPE = Constants.AREA_UNDER_CONTROL_COMPARATOR;
    private final String AGENT_TYPE = Constants.BASE_AGENT;

    /**
     * Creates agent with specific genotypes to perform merge on.
     * */
    @Before
    public void setup()
    {
        // A1 = [0.5 , 0.5] with list = [0.58, 0.47] + [0.65, 0.38]  LUB [0.52, 0.47] + [0.65, 0.38]
        // A2 = [0.54, 0.45] with list = [0.61, 0.42] + [0.69, 0.4]
        a1_list = new ArrayList<>();
        a2_list = new ArrayList<>();

        agent1 = AgentUtils.createPointSolutionAgent(new double[]{0.5, 0.5}, COMPARATOR_TYPE, AGENT_TYPE);
        AreaUnderControlComparator<JMetal5Agent<PointSolution>> a1Comparator = (AreaUnderControlComparator) agent1.getComparator();
        JMetal5Agent<PointSolution> a1_1 = AgentUtils.createPointSolutionAgent(new double[]{0.52, 0.47}, COMPARATOR_TYPE, AGENT_TYPE);
        JMetal5Agent<PointSolution> a1_2 = AgentUtils.createPointSolutionAgent(new double[]{0.65, 0.38}, COMPARATOR_TYPE, AGENT_TYPE);
        a1_list.add(a1_1);
        a1_list.add(a1_2);
        a1Comparator.setListOfKnownNonDominatedAgents(a1_list);

        agent2 = AgentUtils.createPointSolutionAgent(new double[]{0.54, 0.45}, COMPARATOR_TYPE, AGENT_TYPE);
        AreaUnderControlComparator<JMetal5Agent<PointSolution>> a2Comparator = (AreaUnderControlComparator) agent2.getComparator();
        JMetal5Agent<PointSolution> a2_1 = AgentUtils.createPointSolutionAgent(new double[]{0.61, 0.42}, COMPARATOR_TYPE, AGENT_TYPE);
        JMetal5Agent<PointSolution> a2_2 = AgentUtils.createPointSolutionAgent(new double[]{0.69, 0.4}, COMPARATOR_TYPE, AGENT_TYPE);
        a2_list.add(a2_1);
        a2_list.add(a2_2);
        a2Comparator.setListOfKnownNonDominatedAgents(a2_list);
    }

    @Test
    public void mergeLists()
    {
        System.out.println("\n== Checking if neither agent dominate each other using EmasDominanceComparator. == \n ");
        neitherAgentIsBetterEmasDominance();
        System.out.println("\n== Checking if neither agent dominate each other using AreaUnderControlComparator. == \n");
        neitherAgentIsBetterAreaUnderControl();
        System.out.println("\n== Checking if neither agent dominate or gets dominated using raw EmasDominanceComparator to their list of known non dominated agents. == \n");
        neitherAgentIsBetterListAreaUnderControl();
        System.out.println("\n== Checking if agents in list of known non dominated agents are in fact non dominated. == \n");
        nonDominatedListIsNonDominated();
        /*TODO: Implemented area under control extended test */
        assertEquals(1, 2);
    }

    private void neitherAgentIsBetterEmasDominance()
    {
        EmasDominanceComparisonTest.compareAgentWithResult(agent1, agent2, Constants.NEITHER_IS_BETTER);
    }

    private void neitherAgentIsBetterListAreaUnderControl()
    {
        AreaUnderControlComparisonTest.compareListsToAgentsWithResult(a1_list, a2_list, agent1, agent2, Constants.NEITHER_IS_BETTER, Constants.NEITHER_IS_BETTER);
    }

    private void neitherAgentIsBetterAreaUnderControl()
    {
        AreaUnderControlComparisonTest.compareAgentsWithResult(agent1, agent2, Constants.NEITHER_IS_BETTER);
    }

    private void nonDominatedListIsNonDominated()
    {
        EmasDominanceComparisonTest.listIsNonDominated(a1_list);
        EmasDominanceComparisonTest.listIsNonDominated(a2_list);
    }

    @After
    public void printResult()
    {
        System.out.println("\n== Merge list test has ended. ==");
    }
}
