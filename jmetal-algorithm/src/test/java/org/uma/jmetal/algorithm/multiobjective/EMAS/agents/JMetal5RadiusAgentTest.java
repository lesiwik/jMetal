package org.uma.jmetal.algorithm.multiobjective.EMAS.agents;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.EMAS.comparators.emasdominance.EmasDominanceComparisonTest;
import org.uma.jmetal.algorithm.multiobjective.EMAS.utils.AgentUtils;
import org.uma.jmetal.algorithm.multiobjective.lemas.Agents.JMetal5RadiusAgent;
import org.uma.jmetal.algorithm.multiobjective.lemas.Utils.Constants;
import org.uma.jmetal.util.point.PointSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class JMetal5RadiusAgentTest {

    private JMetal5RadiusAgent<PointSolution> mainAgent;
    private JMetal5RadiusAgent<PointSolution> outsideRadius;
    private JMetal5RadiusAgent<PointSolution> insideRadius;
    private ArrayList<JMetal5RadiusAgent<PointSolution>> listOfAgents;

    private final String AGENT_TYPE = Constants.RADIUS_AGENT;
    private final String COMPARATOR_TYPE = Constants.EMAS_DOMINANCE_COMPARATOR;
    private final double RADIUS = 0.3;
    private final int NUMBER_OF_AGENTS = 3;
    private final double INITIAL_RESOURCE_LEVEL = 2.0;


    private int[] initialNumberOfMetAgents      = new int[]{ 10, 10, 10};
    private int[] initialNumberOfAgentsInRadius = new int[]{ 4, 6, 2};
    private double[] EXPECTED_INITIAL_RATIOS = new double[]{0.0, 0.0, 0.0};
    private double[] EXPECTED_RATIOS = new double[]{0.0, 0.0, 0.0};

    @Before
    public void setup()
    {
        mainAgent     = (JMetal5RadiusAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{0.5, 3.5}, COMPARATOR_TYPE, AGENT_TYPE);
        outsideRadius = (JMetal5RadiusAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{0.2, 3.6}, COMPARATOR_TYPE, AGENT_TYPE);
        insideRadius  = (JMetal5RadiusAgent<PointSolution>) AgentUtils.createPointSolutionAgent(new double[]{0.4, 3.6}, COMPARATOR_TYPE, AGENT_TYPE);
        listOfAgents = new ArrayList<>();
        listOfAgents.addAll(Arrays.asList(mainAgent, outsideRadius, insideRadius));

        for(int i = 0; i < NUMBER_OF_AGENTS; i++)
        {
            JMetal5RadiusAgent<PointSolution> agent = listOfAgents.get(i);
            agent.setRadius(RADIUS);
            agent.setMetAgents(initialNumberOfMetAgents[i]);
            agent.setMetAgentsInRadius(initialNumberOfAgentsInRadius[i]);
            agent.setResourceLevel(INITIAL_RESOURCE_LEVEL);
            EXPECTED_INITIAL_RATIOS[i] = (double) initialNumberOfAgentsInRadius[i] / initialNumberOfMetAgents[i];
        }
    }


    @Test
    public void neitherAgentIsBetter()
    {
        System.out.println("\n== Checking if neither agent dominate each other using EmasDominanceComparator. == \n ");
        EmasDominanceComparisonTest.compareAgentWithResult(mainAgent, outsideRadius, Constants.NEITHER_IS_BETTER);
        EmasDominanceComparisonTest.compareAgentWithResult(mainAgent, insideRadius, Constants.NEITHER_IS_BETTER);
    }
    /**
     * Correctly returns expected result as well as resources were transferred properly.
     * */
    @Test
    public void doMeetingTest()
    {
        System.out.println("\n== Checking if meeting ratios are correct.   == \n ");
        System.out.println("== (main=0, outside=1, inside=2) ==");
        for(int i = 0; i < NUMBER_OF_AGENTS; i++)
        {
            System.out.println("Comparing agents " + i + " actual ratio -> " + listOfAgents.get(i).getMeetingRatio() + " to expected ratio -> " + EXPECTED_INITIAL_RATIOS[i]);
            assertEquals( EXPECTED_INITIAL_RATIOS[i], listOfAgents.get(i).getMeetingRatio(), 0.001);
        }
        System.out.println("\n== Checking if isAgentInRadius works correctly. == \n");
        System.out.println(radiusString(mainAgent, insideRadius, " Agent is supposed to be inside circle."));
        assertTrue(mainAgent.isAgentInRadius(insideRadius));
        System.out.println(radiusString(mainAgent, outsideRadius, " Agent is supposed to be outside circle."));
        assertFalse(mainAgent.isAgentInRadius(outsideRadius));


        System.out.println("\n== Checking if do meeting works as expected with insideRadius agent. == \n ");
        int doMeetingResult = mainAgent.doMeeting(Collections.singletonList(insideRadius), 1.0);
        assertEquals(Constants.FIRST_IS_BETTER, doMeetingResult);
        System.out.println("\n== Checking if resources were transferred properly. == \n");
        assertEquals( INITIAL_RESOURCE_LEVEL + 1, mainAgent.getResourceLevel(), 0.001);
        assertEquals( INITIAL_RESOURCE_LEVEL - 1, insideRadius.getResourceLevel(), 0.001);
        System.out.println("\n== Checking if meetings were upgraded accordingly. == \n");
        assertEquals(initialNumberOfAgentsInRadius[0] + 1, mainAgent.getMetAgentsInRadius());
        assertEquals(initialNumberOfMetAgents[0] + 1, mainAgent.getMetAgents());
        assertEquals(initialNumberOfAgentsInRadius[2] + 1, insideRadius.getMetAgentsInRadius());
        assertEquals(initialNumberOfMetAgents[2] + 1, insideRadius.getMetAgents());


        System.out.println("\n== Checking if do meeting works as expected with outsideRadius agent. == \n ");
        doMeetingResult = mainAgent.doMeeting(Collections.singletonList(outsideRadius), 1.0);
        assertEquals(Constants.SECOND_IS_BETTER, doMeetingResult);
        System.out.println("\n== Checking if resources were transferred properly. == \n");
        assertEquals( INITIAL_RESOURCE_LEVEL + 1 , outsideRadius.getResourceLevel(), 0.001);
        assertEquals( INITIAL_RESOURCE_LEVEL, mainAgent.getResourceLevel(), 0.001);
        System.out.println("\n== Checking if meetings were upgraded accordingly. == \n");
        assertEquals(initialNumberOfAgentsInRadius[0] + 1, mainAgent.getMetAgentsInRadius());
        assertEquals(initialNumberOfMetAgents[0] + 2, mainAgent.getMetAgents());
        assertEquals(initialNumberOfAgentsInRadius[1], outsideRadius.getMetAgentsInRadius());
        assertEquals(initialNumberOfMetAgents[1] + 1, outsideRadius.getMetAgents());

    }

    private String radiusString(JMetal5RadiusAgent<PointSolution> main, JMetal5RadiusAgent<PointSolution> other,
                                String expected)
    {
        return "Agent " + main + " with radius of " + main.getRadius() + "\n to agent " + other + ". " + expected;
    }

    @After
    public void printResult()
    {

    }
}
